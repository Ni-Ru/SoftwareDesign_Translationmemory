package memory;

import static prog.ConsoleReader.readString;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Translator extends User {

	int addedTranslations;
	String[] languages;

	public Translator(String username, String password, String role, int addedTranslations, String[] languages) {
		super(username, password, role);
		this.addedTranslations = addedTranslations;
		this.languages = languages;
	}

	public static void seeOwnTranslations() {
		JSONArray userList = DataHandling.JsonParser("users.json");
		for (int i = 0; i < userList.size(); i++) {
			JSONObject arraySpot = (JSONObject) userList.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			if (username.contentEquals(User.getCurrentUser().username)) {
				Long addedTranslations = (Long) userJson.get("addedTranslations");
				System.out.println("you have added: " + addedTranslations + " Translations");
			}
		}
		Main.menu("translator");
	}

	public static void showWordsMissingTrans() {
		ArrayList<String> untranslated = new ArrayList<String>();
		JSONParser jsonP = new JSONParser();
		// JSONArray declared
		JSONArray data = null;
		try (FileReader reader = new FileReader("words.json")) {
			// Read JSON File
			Object obj = jsonP.parse(reader);
			data = (JSONArray) obj;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < data.size(); i++) {
			JSONObject arraySpot = (JSONObject) data.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("word");
			String word = (String) userJson.get("word");
			Map<String, String> translations = (Map<String, String>) userJson.get("translations");
			double translationNumber = translations.size();
			double langNumber = DataHandling.countData("language.json");
			double percentage = (translationNumber / langNumber) * 100;
			int percentageInt = (int) percentage;
			if (translationNumber < langNumber) {
				System.out.println(word + " (" + percentageInt + " %)");
				untranslated.add(word);
			}
		}

		System.out.println("------------------");
		System.out.println("type one of the Words above to add a translation");
		System.out.println("type 'b' to go back");
		String translate = readString("");
		if (translate.contentEquals("b")) {
			Main.menu("translator");
			return;
		}
		int wordCounter = 0;
		for (String word : untranslated) {
			if (translate.contentEquals(word)) {

				showMissingTrans(word);
				return;
			} else {
				wordCounter++;
			}
			if (wordCounter == untranslated.size()) {
				System.out.println("please choose one of the words shown");
				System.out.println("------------------");
				untranslated.clear();
				showWordsMissingTrans();
			}
		}
	}

	private static void showMissingTrans(String wordToTranslate) {
		ArrayList<String> verifiedLangs = jsonArrayToStringArray();
		ArrayList<String> missingLanguage = new ArrayList<String>();
		JSONArray jsonWords = DataHandling.JsonParser("words.json");
		JSONArray jsonLang = DataHandling.JsonParser("language.json");
		for (int wordCount = 0; wordCount < jsonWords.size(); wordCount++) {
			JSONObject arraySpot = (JSONObject) jsonWords.get(wordCount);
			JSONObject wordJson = (JSONObject) arraySpot.get("word");
			String savedWord = (String) wordJson.get("word");
			Map<String, String> translations = (Map<String, String>) wordJson.get("translations");
			if (savedWord.contentEquals(wordToTranslate)) {
				for (int langCount = 0; langCount < jsonLang.size(); langCount++) {
					JSONObject langArraySpot = (JSONObject) jsonLang.get(langCount);
					JSONObject langJson = (JSONObject) langArraySpot.get("language");
					String langName = (String) langJson.get("name");
					int translationNotHere = 0;
					if (translations.size() == 0) {
						System.out.println(langName + ": (missing)");
						missingLanguage.add(langName);
					} else {
						for (Map.Entry<String, String> entry : translations.entrySet()) {
							String key = entry.getKey();
							if (key.contentEquals(langName)) {

							} else {
								translationNotHere++;
								if (translationNotHere == translations.size()) {
									System.out.println(langName + ": (missing)");
									missingLanguage.add(langName);
								}
							}
						}
					}
				}

			}
		}
		System.out.println("------------------");
		System.out.println("type in which language you want to add (you must be verified for the language to add it)");
		System.out.println("type 'b' to go back");
		String language = readString("");
		if (language.contentEquals("b")) {
			showWordsMissingTrans();
			return;
		}
		int langCounter = 0;
		for (String langName : missingLanguage) {
			if (language.contentEquals(langName)) {
				int verLangCounter = 0;
				for (String lang : verifiedLangs) {
					if (language.contentEquals(lang)) {
						addTranslation(wordToTranslate, langName);
					} else {
						verLangCounter++;
					}
				}
				if (verLangCounter == verifiedLangs.size()) {
					System.out.println("------------------");
					System.out.println("you are not verified for this language!");
					System.out.println("------------------");
					showMissingTrans(wordToTranslate);
				}
				break;

			} else {
				langCounter++;
			}
			if (langCounter == missingLanguage.size()) {
				System.out.println("please type in one of the languages shown");
				System.out.println("------------------");
				missingLanguage.clear();
				showMissingTrans(wordToTranslate);
				break;
			}
		}
	}

	private static void addTranslation(String wordToTranslate, String langName) {
		JSONArray jsonWords = DataHandling.JsonParser("words.json");
		for (int wordCount = 0; wordCount < jsonWords.size(); wordCount++) {
			JSONObject arraySpot = (JSONObject) jsonWords.get(wordCount);
			JSONObject wordJson = (JSONObject) arraySpot.get("word");
			String savedWord = (String) wordJson.get("word");
			Map<String, String> translations = (Map<String, String>) wordJson.get("translations");
			if (wordToTranslate.contentEquals(savedWord)) {
				System.out.println("type translation");
				String finalTranslation = readString("");
				translations.put(langName, finalTranslation);
			}

		}
		try (FileWriter file = new FileWriter("words.json")) {
			// Array is saved in JSON
			file.write(jsonWords.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray jsonUsers = DataHandling.JsonParser("users.json");
		String currentUsername = User.getCurrentUser().username;

		for (int i = 0; i < jsonUsers.size(); i++) {
			JSONObject arraySpot = (JSONObject) jsonUsers.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			if (username.contentEquals(currentUsername)) {
				String addedTranslations = String.valueOf(userJson.get("addedTranslations"));
				System.out.println(addedTranslations);
				int addedTranslationsInt = DataHandling.stringToInt(addedTranslations);
				addedTranslationsInt++;
				userJson.put("addedTranslations", addedTranslationsInt);
			}
		}
		DataHandling.fileWriter("users.json", jsonUsers);
		User.showWord(wordToTranslate);
		Main.menu("translator");
	}

	private static ArrayList<String> jsonArrayToStringArray() {
		ArrayList<String> verifiedLangs = new ArrayList<String>();
		JSONArray jsonUsers = DataHandling.JsonParser("users.json");
		for (int i = 0; i < jsonUsers.size(); i++) {
			JSONObject arraySpot = (JSONObject) jsonUsers.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			if (username.contentEquals(User.getCurrentUser().username)) {
				JSONArray langs = (JSONArray) userJson.get("languages");
				for (int langCount = 0; langCount < langs.size(); langCount++) {
					String language = (String) langs.get(langCount);
					verifiedLangs.add(language);
				}
			}
		}
		return verifiedLangs;
	}
}