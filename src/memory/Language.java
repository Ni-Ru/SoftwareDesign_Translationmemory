package memory;

import org.json.simple.JSONObject;

public class Language {

	int id;
	String name;
	
	public Language(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static JSONObject languageToJson(Language newLang) {
		JSONObject newLangJson = new JSONObject();
		newLangJson.put("id", newLang.id);
		newLangJson.put("name", newLang.name);
		
		JSONObject newLangJsonObject = new JSONObject();
		newLangJsonObject.put("language", newLangJson);
		
		return newLangJsonObject;
	}
	
	

}
