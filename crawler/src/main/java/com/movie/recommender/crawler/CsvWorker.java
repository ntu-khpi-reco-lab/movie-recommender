package com.movie.recommender.crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvWorker {

    public String csvLoader(String filePath, String input) {
        try {
            // Parsing the input JSON using Gson
            JsonArray docs = JsonParser.parseString(input).getAsJsonArray();

            // Convert the JsonArray to a CSV string using CDL (org.json)
            String csvString = CDL.toString(new JSONArray(docs.toString()));
            File file = new File(filePath);
            FileUtils.writeStringToFile(file, csvString, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "good";
    }

    public JsonArray csvReader(String filePath) {
        JsonArray jsonArray = new JsonArray();
        try {
            // Read the CSV file into a list of lines
            File file = new File(filePath);
            List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);

            // Get headers from the first line
            String[] headers = lines.get(0).split(",");

            // Loop through each row (starting from the second line)
            for (int i = 1; i < lines.size(); i++) {
                String[] values = lines.get(i).split(",");
                JsonObject jsonObject = new JsonObject();

                // Add each value with the corresponding header as key
                for (int j = 0; j < headers.length; j++) {
                    jsonObject.addProperty(headers[j], values[j]);
                }

                // Add the JsonObject to the JsonArray
                jsonArray.add(jsonObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
