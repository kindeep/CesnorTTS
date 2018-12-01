package dev.kindeep.mlh18;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RemoveProfanity {
    JsonParser jp = new JsonParser();
    JsonObject o;
    JsonArray ja;

    public String run(String[] sentence) {
        boolean[] offense = new boolean[sentence.length];

        // creates array of booleans (corresponds to words in sentence).
        for (int i = 0; i < sentence.length; i++) {
            boolean result = isOffensive(sentence[i]);
            offense[i] = result;
            System.out.print(result + ", ");
        }
        System.out.println();
        StringBuilder sb = new StringBuilder();

        // builds sentence using censored.
        for (int i = 0; i < sentence.length; i++) {
            if (!offense[i]) {
                sb.append(sentence[i]);
            } else {
                if (sentence[i].endsWith("ing"))
                    sb.append("bleeping");
                else
                    sb.append("bleep");
            }

            sb.append(" ");
        }
        String result = sb.toString();

        return result;
    }

    /*
     * checks if a word is offensive.
     */
    private boolean isOffensive(String word) {
        try {
            Log.e("TheWord", word);
            // builds url.
            StringBuilder sb = new StringBuilder();
            sb.append(new String("https://www.dictionaryapi.com/api/v3/references/collegiate/json/"));
            sb.append(word);
            sb.append(new String("?key=ee7e0255-db39-4b20-895b-91b0138134a0"));
            URL url = new URL(sb.toString());

            // gets results from api.
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            ja = jp.parse(content.toString()).getAsJsonArray();
            o = (JsonObject) ja.get(0);

            JsonElement result = o.get("meta").getAsJsonObject().get("offensive");
            in.close();
            return result.getAsBoolean();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

}
