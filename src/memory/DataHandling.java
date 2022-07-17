package memory;

import static prog.ConsoleReader.readString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataHandling {

	public static void saveData(JSONObject newJsonData, String fileName) {

		File jsonFile = new File(fileName);
		// check if JSONFile exists
		if (jsonFile.exists()) {
			JSONParser jsonP = new JSONParser();
			// JSONArray declared
			JSONArray data = null;
			try (FileReader reader = new FileReader(fileName)) {
				// Read JSON File
				Object obj = jsonP.parse(reader);
				data = (JSONArray) obj;
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}

			// data gets added in JSONArray
			data.add(newJsonData);

			try (FileWriter file = new FileWriter(fileName)) {
				// Array is saved in JSON
				file.write(data.toJSONString());
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// if JSON doesnt exist new Array is created
			JSONArray dataList = new JSONArray();
			dataList.add(newJsonData);

			try (FileWriter file = new FileWriter(fileName)) {
				file.write(dataList.toJSONString());
				file.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// check for double TODO
	public static void checkDoubleCase(String fileName, String newString, String instanceType, String stringKey,String message) {
		boolean doubleInstance = false;
		JSONParser jsonP = new JSONParser();
		// JSONArray declared
		JSONArray data = null;
		try (FileReader reader = new FileReader(fileName)) {
			// Read JSON File
			Object obj = jsonP.parse(reader);
			data = (JSONArray) obj;
			data.forEach(dataObject -> checkDoubles((JSONObject) dataObject, newString, instanceType, stringKey, message));
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}

	private static void checkDoubles(JSONObject instance, String newString, String instanceType, String stringKey,
			String message) {
		JSONObject savedInstance = (JSONObject) instance.get(instanceType);
		String savedString = (String) savedInstance.get(stringKey);
		if (newString.contentEquals(savedString)) {
			System.out.println(message);
			switch (instanceType) {
			case "word":
				Word.searchedWordExists = true;
				break;
			case "user":
				System.out.println("this username is already taken!");
				LoginRegister.register();
				break;
			}
		}
	}

	static int countData(String fileName) {
		File jsonFile = new File(fileName);
		int dataNumber = 0;
		// check if JSONFile exists
		if (jsonFile.exists()) {
			JSONParser jsonP = new JSONParser();
			// JSONArray declared
			JSONArray data = null;
			try (FileReader reader = new FileReader(fileName)) {
				// Read JSON File
				Object obj = jsonP.parse(reader);
				data = (JSONArray) obj;
			} catch (FileNotFoundException e) {

			} catch (IOException e) {
				e.printStackTrace();
			} catch (org.json.simple.parser.ParseException e) {
				e.printStackTrace();
			}

			// data gets added in JSONArray
			dataNumber = data.size();
		}

		return dataNumber;
	}

	public static int countTranslations() {
		
		int fullyTranslated = 0;

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

		// data gets added in JSONArray
		for (int i = 0; i < data.size(); i++) {
			JSONObject arraySpot = (JSONObject) data.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("word");
			Map<String, String> translations = (Map<String, String>) userJson.get("translations");
			int translationNumber = translations.size();
			int langNumber = DataHandling.countData("language.json");
			if(translationNumber == langNumber) {
				fullyTranslated++;
			}
		}
		return fullyTranslated;
	}

	public static void increaseAddedWords() {
		JSONParser jsonP = new JSONParser();
		// JSONArray declared
		JSONArray data = null;
		try (FileReader reader = new FileReader("users.json")) {
			// Read JSON File
			Object obj = jsonP.parse(reader);
			data = (JSONArray) obj;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		// data gets added in JSONArray
		for (int i = 0; i < data.size(); i++) {
			JSONObject arraySpot = (JSONObject) data.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			long addedWordsLong = (long) userJson.get("addedWords");
			int addedWords = (int) addedWordsLong;
			if (username.contentEquals(User.getCurrentUser().username)) {
				addedWords++;
				userJson.put("addedWords", addedWords);
			}
		}
		try (FileWriter file = new FileWriter("users.json")) {
			// Array is saved in JSON
			file.write(data.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static int stringToInt(String stringToConvert) {
		int foo;
		try {
			foo = Integer.parseInt(stringToConvert);
		} catch (NumberFormatException e) {
			foo = 0;
		}
		return foo;
	}
	
	public static void showTranslations(String searchedWord) {
		JSONArray jsonWords = JsonParser("words.json");
		JSONArray jsonLang = JsonParser("language.json");
	

		for (int wordCount = 0; wordCount < jsonWords.size(); wordCount++) {
			JSONObject arraySpot = (JSONObject) jsonWords.get(wordCount);
			JSONObject wordJson = (JSONObject) arraySpot.get("word");
			String savedWord = (String) wordJson.get("word");
			Map<String, String> translations = (Map<String, String>) wordJson.get("translations");
			if(savedWord.contentEquals(searchedWord)) {
				for (int langCount = 0; langCount < jsonLang.size(); langCount++) {
					JSONObject langArraySpot = (JSONObject) jsonLang.get(langCount);
					JSONObject langJson = (JSONObject) langArraySpot.get("language");
					String langName = (String) langJson.get("name");
					int translationNotHere = 0;
					if (translations.size()==0) {
						System.out.println(langName + ": (none)");
					}else {
						for (Map.Entry<String, String> entry : translations.entrySet()){
							String key = entry.getKey();
							String value = entry.getValue();
							if(key.contentEquals(langName)) {
								System.out.println(langName + ": " + value);
							}else {
								translationNotHere ++;
								if(translationNotHere == translations.size()) {
									System.out.println(langName + ": (none)");
								}
							}
						}	
					}
				}	
			}
			
		}
	}
	
	public static JSONArray JsonParser(String fileName) {
		JSONParser jsonP = new JSONParser();
		// JSONArray declared
		JSONArray data = null;
		try (FileReader reader = new FileReader(fileName)) {
			// Read JSON File
			Object obj = jsonP.parse(reader);
			data = (JSONArray) obj;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	public static void fileWriter(String filename, JSONArray data) {
		try (FileWriter file = new FileWriter(filename)) {
			// Array is saved in JSON
			file.write(data.toJSONString());
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
