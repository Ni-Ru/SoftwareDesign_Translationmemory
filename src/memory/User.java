package memory;

import static prog.ConsoleReader.readString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class User {
	
	private static User currentUser = null;

	String username;
	String password;
	String role;
	int addedWords;

	public User(String username, String password,String role) {
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	public static User getCurrentUser() {
		if (currentUser == null) {
			NullUser guest = new NullUser();
			return guest;
		}else {
			return currentUser;
		}
	}
	
	public static void logout() {
		currentUser = null;
	}
	
	public static void login(User loginUser) {
		currentUser = loginUser;
	}
	
	public static JSONObject userToJson(User newUser) {
		JSONObject newUserJson = new JSONObject();
		newUserJson.put("username", newUser.username);
		newUserJson.put("password", newUser.password);
		newUserJson.put("addedWords", newUser.addedWords);
		newUserJson.put("role", newUser.role);

		JSONObject newUserJsonObject = new JSONObject();
		newUserJsonObject.put("user", newUserJson);
 
		return newUserJsonObject;
	}
	
	public static void searchWords() {
		Word.searchedWordExists = false;
		System.out.println("search a Word:");
		String word = readString("");
		DataHandling.checkDoubleCase("words.json", word, "word","word" ,"the Word you searched for was found!");
		if(Word.searchedWordExists == true) {
			showWord(word);
			Word.searchedWordExists = false;
			System.out.println("------------------");
			menuOrSearch();
			
		}else {
			System.out.println("this word is not saved in the Translationmemory yet!");
			if(User.getCurrentUser().role=="guest") {
					NullUser.guestFailedSearching();
				}else {
					User.userFailedSearching(word);
				}
			}
		}
	
	public static void menuOrSearch() {
		System.out.println("type 'm' to go back to the menu");
		System.out.println("type 's' to search again");
		String action = readString("");
		switch (action) {
		case "m":
			String role = User.getCurrentUser().role;
			Main.menu(role);
			break;
		case "s":
			searchWords();
			break;
		default:
			System.out.println("Error! Please type one of the letters that are shown.");
			System.out.println("------------------");
			menuOrSearch();
			break;
		}
	}
	
	
	public static void showWord(String word) {
		System.out.println("English: " + word);
		DataHandling.showTranslations(word);
	}

	public static void seeAllWords() {
		int wordNumber = DataHandling.countData("words.json");
		int fullyTranslated = DataHandling.countTranslations();
		System.out.println(wordNumber + " words saved in this Translationmemory");
		System.out.println(fullyTranslated + " of them are fully translated");
		System.out.println("------------------");
		String role = User.getCurrentUser().role;
		Main.menu(role);
		
	}
	
	public static void userFailedSearching(String word) {
		System.out.println("you can add the word if you want");
		System.out.println("type '+' to add the word to the Translationmemory");
		System.out.println("type 'b' to go back to the menu");
		System.out.println("type 's' to search another word");
		String actionUser = readString("");
		switch (actionUser) {
		case "+":
			addWord(word);
			afterWordAdded();
			break;
		case "b":
			Main.menu(User.currentUser.role);
			break;
		case "s":
			User.searchWords();
			break;
		default:
			System.out.println("Error! Please type one of the letters that are shown.");
			System.out.println("------------------");
			userFailedSearching(word);
			break;
		}
	}
	
	public static void afterWordAdded() {
		System.out.println("word has been added!");
		System.out.println("type 'b' to go back to the menu");
		System.out.println("type 's' to search another word");
		String actionUser = readString("");
		switch (actionUser) {
		case "b":
			Main.menu(User.currentUser.role);
			break;
		case "s":
			User.searchWords();
			break;
		default:
			System.out.println("Error! Please type one of the letters that are shown.");
			System.out.println("------------------");
			afterWordAdded();
			break;
		}
	}

	public static void addWord(String addedWord) {
		int idCounter = DataHandling.countData("words.json");
		Map<String, String> translations = new HashMap<String, String>();
		Word newWord = new Word(idCounter, addedWord, translations);
		JSONObject wordJson = Word.wordToJson(newWord);
		DataHandling.saveData(wordJson, "words.json");
		DataHandling.increaseAddedWords();
	}
	
	public static void ownWordsNumber(){
		JSONArray userList = DataHandling.JsonParser("users.json");
		for (int i = 0; i < userList.size(); i++) {
			JSONObject arraySpot = (JSONObject) userList.get(i);
			JSONObject userJson = (JSONObject) arraySpot.get("user");
			String username = (String) userJson.get("username");
			Long addedWords = (Long) userJson.get("addedWords");
			if(username.contentEquals(User.currentUser.username)) {
				System.out.println("you have submitted: " + addedWords + " words");
			}
		}
		Main.menu(User.currentUser.role);
	}

}
