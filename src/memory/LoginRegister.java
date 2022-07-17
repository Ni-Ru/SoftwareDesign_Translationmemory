package memory;

import static prog.ConsoleReader.readString;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LoginRegister {
	
	//public static Admin adminUser = Admin.getAdmin();

	private static boolean existingUsername = false;
	
	public static void loginScreen() {
		User.logout();
		System.out.println("Translationmemory");
		System.out.println("type 'e' to continue as guest");
		System.out.println("type 'l' to login");
		System.out.println("type 'r' to register");
		System.out.println("type 'x' to exit Translationmemory");
		String action = readString("");
		System.out.println("------------------");

		switch (action) {
		case "e":
			System.out.println("Welcome Guest!");
			System.out.println("------------------");
			Main.menu("guest");
			break;
		case "l":
			LoginRegister.login();
			break;
		case "r":
			LoginRegister.register();
			LoginRegister.login();
			break;
		case "x":
			System.exit(0);
			break;
		default:
			System.out.println("Error! Please type one of the letters that are shown.");
			System.out.println("------------------");
			loginScreen();
			new Main();
			break;
		}

	}

	public static void register() {
		System.out.println("---registration---");
		String username = readString("username");
		DataHandling.checkDoubleCase("users.json", username, "user", "username", "this username is already taken");
		String password = readString("password");
		String role = "user";

		
		// JSON Object created
		User newRegUser = new User(username, password,role);
		JSONObject newUserJson = User.userToJson(newRegUser);
		DataHandling.saveData(newUserJson, "users.json");
		System.out.println("------------------");
	}
	
	private static void login() {
		System.out.println("---login---");

		String username = readString("username");
		String password = readString("password");
		
		Admin admin = Admin.getAdmin();
		User adminUser = new User(admin.username, admin.password, admin.role);
		
		if(username.contentEquals(admin.username)) {
			if(password.contentEquals(admin.password)) {
				User.login(adminUser);
				Main.menu("admin");
				return;
			}else {
				System.out.println("wrong password");
				login();
				return;
			}
		}

		JSONParser jsonP = new JSONParser();
		// JSONArray declared
		try (FileReader reader = new FileReader("users.json")) {
			// Read JSON File
			Object obj = jsonP.parse(reader);
			JSONArray userList = (JSONArray) obj;

			// Iterate over user array
			userList.forEach(user -> parseUser((JSONObject) user, username, password));
			
			if (existingUsername == false) {
				System.out.println("sorry, could not find an account with this username");
				System.out.println("------------------");
				loginScreen();
			}

		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseUser(JSONObject user, String loginUsername, String loginPassword) {
		JSONObject savedUser = (JSONObject) user.get("user");
		// get username and password
		String username = (String) savedUser.get("username");
		String password = (String) savedUser.get("password");
		String role = (String) savedUser.get("role");
		int addedWords = DataHandling.stringToInt(String.valueOf(savedUser.get("addedWords")));

		int addedTranslations = 0;
		String[] languages = {};
		if (role == "translator") {
			addedTranslations = DataHandling.stringToInt(String.valueOf(savedUser.get("addedTranslations")));
			languages = (String[]) savedUser.get("languages");
		}
		
		// check if username typed in Login matches with any usernames in the JSON
		if (loginUsername.contentEquals(username)) {
			existingUsername = true;
			if (loginPassword.contentEquals(password)) {
				System.out.println("logged in!");

				if (role.contentEquals("user") || role.contentEquals("translator")) {
					User current = new User(username, password, role);
					User.login(current);
					Main.menu(current.role);
				} 
			} else {
				System.out.println("oops! seems like you typed in the wrong password");
				System.out.println("------------------");
				loginScreen();
			}
		}
	}
}
