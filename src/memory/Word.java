package memory;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class Word {
	
	public static boolean searchedWordExists = false;
	int id;
	String word;
	Map<String, String> translations;

	public Word(int id, String word, Map<String, String> translations) {
		this.id = id;
		this.word = word;
		this.translations = translations;
	}

	public static JSONObject wordToJson(Word newWord) {
		JSONObject newWordJson = new JSONObject();
		newWordJson.put("id", newWord.id);
		newWordJson.put("word", newWord.word);
		newWordJson.put("translations", newWord.translations);
		
		JSONObject newWordJsonObject = new JSONObject();
		newWordJsonObject.put("word", newWordJson);
		
		return newWordJsonObject;
	}

}
