package memory;

import static prog.ConsoleReader.readString;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Admin{
	
	String username;
	String password;
	String role;

	private Admin(String username, String password, String role) {
		this.username = username;
		this.password = password;
		this.role = role;
		// TODO Auto-generated constructor stub
	}
	
	private static Admin adminUser = new Admin("admin", "admin123", "admin");
	
	public static Admin getAdmin() {
		return adminUser;
	}

	public static void addLanguage() {
		System.out.println("Enter the Language you want to add");
		String languageToAdd = readString("");
		int idCounter = DataHandling.countData("language.json");
		Language newLang = new Language(idCounter, languageToAdd);
		JSONObject langJson = Language.languageToJson(newLang);
		DataHandling.saveData(langJson, "language.json");
		System.out.println("language has been added!");
		Main.menu("admin");
	}

	public static void listUsers() {
		JSONArray users = DataHandling.JsonParser("users.json");
		users.forEach(user -> showUser((JSONObject) user));
		System.out.println("------------------");
		System.out.println("type 'm' to go back to menu");
		System.out.println("type the name of a user to change his role to go back to menu");
		System.out.println("type 'l' to manage languages of translators");

		String adminAction = readString("");
		if (adminAction.contentEquals("m")) {
			Main.menu("admin");
			return;
		} else if (adminAction.contentEquals("l")) {
			manageTranslators();
			return;
		} else {
			changeUserRole(adminAction);
		}

	}

	private static void showUser(JSONObject user) {
		JSONObject usercase = (JSONObject) user.get("user");
		String role = (String) usercase.get("role");
		String username = (String) usercase.get("username");
		System.out.println("------------------");
		System.out.println("username: " + username);
		System.out.println("role: " + role);
	}

	private static void changeUserRole(String user) {
		JSONArray userList = DataHandling.JsonParser("users.json");
		int userCounter = 0;
		for (int i = 0; i < userList.size(); i++) {
			JSONObject arraySpot = (JSONObject) userList.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			String role = (String) userJson.get("role");
			if (username.contentEquals(user)) {
				if (role.contentEquals("user")) {
					userJson.put("role", "translator");
					userJson.put("addedTranslations", "0");
					JSONArray languages = new JSONArray();
					userJson.put("languages", languages);
					/*
					 * String[] languages = {}; userJson.put("languages", languages);
					 */
				} else if (role.contentEquals("translator")) {
					userJson.put("role", "user");
					userJson.remove("languages");
					String addedTranslations = (String) userJson.get("addedTranslations");
					int addedTranslationsInt = DataHandling.stringToInt(addedTranslations);
					userJson.remove("addedTranslations");
				}
			} else {
				userCounter++;
			}
			if (userCounter == userList.size()) {
				System.out.println("type in one of the listed usernames");
				listUsers();
			}
		}
		DataHandling.fileWriter("users.json", userList);
		listUsers();
	}

	private static void manageTranslators() {
		ArrayList<String> translators = new ArrayList<String>();
		JSONArray userList = DataHandling.JsonParser("users.json");
		JSONArray jsonLang = DataHandling.JsonParser("language.json");
		int userCounter = 0;
		for (int i = 0; i < userList.size(); i++) {
			JSONObject arraySpot = (JSONObject) userList.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			String role = (String) userJson.get("role");
			if (role.contentEquals("translator")) {
				translators.add(username);
				JSONArray languages = (JSONArray) userJson.get("languages");
				System.out.println("------------------");
				System.out.println(username);
				for (int langCount = 0; langCount < jsonLang.size(); langCount++) {
					JSONObject langArraySpot = (JSONObject) jsonLang.get(langCount);
					JSONObject langJson = (JSONObject) langArraySpot.get("language");
					String langName = (String) langJson.get("name");
					if (languages.size() == 0) {
						System.out.println(langName + ": not verified");
					} else {
						int countVerifiedLangs = 0;
						for (int langUserCount = 0; langUserCount < languages.size(); langUserCount++) {
							String verifiedLang = (String) languages.get(langUserCount);
							if (langName.contentEquals(verifiedLang)) {
								System.out.println(langName + ": verified");
							} else {
								countVerifiedLangs++;
							}

							if (countVerifiedLangs == languages.size()) {
								System.out.println(langName + ": not verified");
							}

						}
					}
				}

			}
		}

		System.out.println("------------------");
		System.out.println("type name of translator to manage his languages");
		System.out.println("type 'b' to go back");
		String translatorToManage = readString("");
		if(translatorToManage.contentEquals("b")) {
			listUsers();
			return;
		}
		int translatorCounter = 0;
		for (String name : translators) {
			if (translatorToManage.contentEquals(name)) {

				manageLanguage(translatorToManage);
				break;

			} else {
				translatorCounter++;
			}
			if (translatorCounter == translators.size()) {
				System.out.println("please type in one of the users shown");
				System.out.println("------------------");
				translators.clear();
				manageTranslators();
				break;
			}
		}

	}

	private static void manageLanguage(String translatorName) {
		ArrayList<String> allLanguages = new ArrayList<String>();
		ArrayList<String> verifiedLanguages = new ArrayList<String>();
		ArrayList<String> unverifiedLanguages = new ArrayList<String>();
		JSONArray userList = DataHandling.JsonParser("users.json");
		JSONArray jsonLang = DataHandling.JsonParser("language.json");
		for (int i = 0; i < userList.size(); i++) {
			JSONObject arraySpot = (JSONObject) userList.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			String role = (String) userJson.get("role");
			JSONArray languages = (JSONArray) userJson.get("languages");
			if (translatorName.contentEquals(username)) {
				System.out.println("------------------");
				System.out.println(username);
				for (int langCount = 0; langCount < jsonLang.size(); langCount++) {
					JSONObject langArraySpot = (JSONObject) jsonLang.get(langCount);
					JSONObject langJson = (JSONObject) langArraySpot.get("language");
					String langName = (String) langJson.get("name");
					allLanguages.add(langName);
					if (languages.size() == 0) {
						System.out.println(langName + ": not verified");
						unverifiedLanguages.add(langName);
					} else {
						int countVerifiedLangs = 0;
						for (int langUserCount = 0; langUserCount < languages.size(); langUserCount++) {
							String verifiedLang = (String) languages.get(langUserCount);
							if (langName.contentEquals(verifiedLang)) {
								System.out.println(langName + ": verified");
								verifiedLanguages.add(langName);
							} else {
								countVerifiedLangs++;
							}

							if (countVerifiedLangs == languages.size()) {
								System.out.println(langName + ": not verified");
								unverifiedLanguages.add(langName);
							}

						}
					}
				}
				System.out.println("------------------");
				System.out.println("type a language to change its status");
				System.out.println("type 'b' to go back");
				String langToChange = readString("");
				if(langToChange.contentEquals("b")) {
					manageTranslators();
					break;
				}
				int langCounter = 0;
				for (String lang : allLanguages) {
					if (langToChange.contentEquals(lang)) {
						for (String unverLang : unverifiedLanguages) {
							if (lang.contentEquals(unverLang)) {
								languages.add(langToChange);
								allLanguages.clear();
								verifiedLanguages.clear();
								unverifiedLanguages.clear();
								DataHandling.fileWriter("users.json", userList);
								manageLanguage(translatorName);
								break;
							}
						}
						for (String verLang : verifiedLanguages) {
							if (lang.contentEquals(verLang)) {
								languages.remove(langToChange);
								allLanguages.clear();
								verifiedLanguages.clear();
								unverifiedLanguages.clear();
								DataHandling.fileWriter("users.json", userList);
								manageLanguage(translatorName);
								break;
							}
						}
						break;

					} else {
						langCounter++;
					}
					if (langCounter == allLanguages.size()) {
						System.out.println("please type in one of the languages shown");
						System.out.println("------------------");
						allLanguages.clear();
						verifiedLanguages.clear();
						unverifiedLanguages.clear();
						manageLanguage(translatorName);
						break;
					}
				}
			}
		}
	}
	
}
