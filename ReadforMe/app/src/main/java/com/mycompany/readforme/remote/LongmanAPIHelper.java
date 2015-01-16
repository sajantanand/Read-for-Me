package com.mycompany.readforme.remote;

import android.util.Log;

import com.google.gson.JsonArray;
import com.mycompany.readforme.Settings;

/**
 * Created by @sajantanand on 1/16/2015.
 */

public class LongmanAPIHelper {


    private String headword;
    private boolean found = false;
    private com.google.gson.JsonArray multipleDef = new JsonArray();
		
	public String getDictionaryEntry(String headword) throws Exception {
		this.headword = headword;
        String URL_PREFIX =
                "https://api.pearson.com/v2/dictionaries/ldoce5/entries?headword=";

		String url = URL_PREFIX + headword + Settings.API_KEY;
		HTTPSCall call = new HTTPSCall(url);
		Log.i(Settings.LOG_TAG, url);
		return (parseDictionaryEntry(call.doRemoteCall()));
	}
	
	private String parseDictionaryEntry (String entryJSONAsString) {
        Integer i;
        String headWordObject;
        String comparison;
        com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
        com.google.gson.JsonObject jsonObject = jsonParser.parse(entryJSONAsString).getAsJsonObject();

        com.google.gson.JsonArray jsonArray = jsonObject.get("results").getAsJsonArray();

        if (jsonArray.size() == 0) {
            return "No definition is available. Array size is zero.";
        }

        comparison = '"' + headword + '"';


        for (i = 0; i <= (jsonArray.size() - 1); i++) {
            jsonObject = jsonArray.get(i).getAsJsonObject();
            headWordObject = jsonObject.get("headword").toString();


            if (headWordObject.equalsIgnoreCase(comparison)) {
                found = true;
                multipleDef.add(jsonObject);
            }
        }
        if (!found) {
            return "No definition is available. No match found for headword.";
        }


        jsonObject = multipleDef.get(0).getAsJsonObject();


        jsonArray = jsonObject.get("senses").getAsJsonArray();
        jsonObject = jsonArray.get(0).getAsJsonObject();
        jsonArray = jsonObject.get("definition").getAsJsonArray();
        return (headword + ": " + jsonArray.get(0).getAsString());

    }

    public String getAlternateDefinition(short counter)
    {
        com.google.gson.JsonArray jsonArray;

        if (multipleDef.size() <= 1)
        {
            return "No alternate definitions are available.";
        }
        int iteration = counter % multipleDef.size();

        com.google.gson.JsonObject jsonObject = multipleDef.get(iteration).getAsJsonObject();
        jsonArray = jsonObject.get("senses").getAsJsonArray();
        jsonObject = jsonArray.get(0).getAsJsonObject();
        jsonArray = jsonObject.get("definition").getAsJsonArray();
        return (headword + ": " + jsonArray.get(0).getAsString());

    }
}
