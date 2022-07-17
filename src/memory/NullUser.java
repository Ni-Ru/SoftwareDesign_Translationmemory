package memory;

import static prog.ConsoleReader.readString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class NullUser extends User {

	private static String role = "guest";
	private static String password = "null";
	private static String username = "guest";

	public NullUser() {
		super(username, password, role);
	}
	
	public static NullUser guest() {
		NullUser guest = new NullUser();
		return guest;
	}
	
	public static void guestFailedSearching() {
		System.out.println("You need to be logged in if you want to add a word");
		System.out.println("type 'b' to go back to the loginscreen");
		System.out.println("type 'm' to go back to the menu");
		System.out.println("type 's' to search for another word");
		String actionGuest = readString("");
		switch (actionGuest) {
		case "b":
			LoginRegister.loginScreen();
			break;
		case "m":
			Main.menu("guest");
			break;
		case "s":
			searchWords();
			break;
		default:
			System.out.println("Error! Please type one of the letters that are shown.");
			System.out.println("------------------");
			guestFailedSearching();
			break;
		}
	}
	
}
