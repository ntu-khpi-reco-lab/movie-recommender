package com.movie.recommender.crawler;


import com.google.gson.JsonArray;
import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class CsvWorker {

    public String csvLoader(String filePath, String input){
        String jsonString;
        JSONObject jsonObject;

        // Try block to check for exceptions
        try {

            JSONArray docs = new JSONArray(input);

            // Step 3: Fetching the JSON Array test
            // from the JSON Object


            // Step 4: Create a new CSV file using
            //  the package java.io.File
            File file = new File(filePath);

            // Step 5: Produce a comma delimited text from
            // the JSONArray of JSONObjects
            // and write the string to the newly created CSV
            // file

            String csvString = CDL.toString(docs);
            FileUtils.writeStringToFile(file, csvString);
        }

        // Catch block to handle exceptions
        catch (Exception e) {

            // Display exceptions on console with line
            // number using printStackTrace() method
            e.printStackTrace();
        }

        return "good";
    }
}