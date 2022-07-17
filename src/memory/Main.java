package memory;

import static prog.ConsoleReader.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
	
	public static void main(String[] args) {

		LoginRegister.loginScreen();
	}
	
	public static void menu(String role) {
		System.out.println("------------------");
		System.out.println("Translationmemory");
		System.out.println("type 's' to search for a word");
		System.out.println("type 'n' to see the number of saved words");
		if(role.contentEquals("user") || role.contentEquals("translator")) {
			System.out.println("type 'a' to see how many words you already added");
		}
		if(role.contentEquals("admin")) {
			System.out.println("type 'l' to add language");	
			System.out.println("type 'u' to manage users");	
		}
		if(role.contentEquals("translator")){
			System.out.println("type 't' to see words with missing translations");
			System.out.println("type 'tn' to see number of translations you added");
		}
		System.out.println("type 'b' to go back to loginscreen");
		System.out.println("type 'x' to exit Translationmemory");
		String action = readString("");
		System.out.println("------------------");
		
		switch (action) {
		case "s":
			User.searchWords();
			System.out.println("------------------");
			break;
		case "n":
			User.seeAllWords();
			break;
		case "b":
			LoginRegister.loginScreen();
			break;
		case "a":
			if(role.contentEquals("user") || role.contentEquals("translator")) {
				User.ownWordsNumber();
			}else {
				System.out.println("Error! Please type one of the letters that are shown.");
				System.out.println("------------------");
				String currentRole = User.getCurrentUser().role;
				menu(currentRole);
			}
			break;
		case "l":
			if(role.contentEquals("admin")) {
				Admin.addLanguage();
			}else {
				System.out.println("Error! Please type one of the letters that are shown.");
				System.out.println("------------------");
				String currentRole = User.getCurrentUser().role;
				menu(currentRole);
			}
			break;
		case "t":
			if(role.contentEquals("translator")) {
				Translator.showWordsMissingTrans();
			}else {
				System.out.println("Error! Please type one of the letters that are shown.");
				System.out.println("------------------");
				String currentRole = User.getCurrentUser().role;
				menu(currentRole);
			}
			break;
		case "tn":
			if(role.contentEquals("translator")) {
				Translator.seeOwnTranslations();
			}else {
				System.out.println("Error! Please type one of the letters that are shown.");
				System.out.println("------------------");
				String currentRole = User.getCurrentUser().role;
				menu(currentRole);
			}
			break;
		case "x":
			System.exit(0);
			break;
		case "u":
			if(role.contentEquals("admin")) {
				Admin.listUsers();
			}else {
				System.out.println("Error! Please type one of the letters that are shown.");
				System.out.println("------------------");
				String currentRole = User.getCurrentUser().role;
				menu(currentRole);
			}
			break;
		default:
			System.out.println("Error! Please type one of the letters that are shown.");
			System.out.println("------------------");
			String currentRole = User.getCurrentUser().role;
			menu(currentRole);
			break;
		}
	}

	
}
