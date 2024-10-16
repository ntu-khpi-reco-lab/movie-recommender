package com.movie.recommender.crawler;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DB {
    private static String CONNECTION_PATH = "mongodb://localhost:27017" ;
    private static final String CLIENT = "mydatabase";
    private static final String DB_NAME = "films";
    private static final Logger logger = LoggerFactory.getLogger(DB.class);
    public MongoCollection<Document> collection = null;
    public void  dbConnect(String dbName) {
        // Connect to MongoDB using the connection string (includes user credentials and database authentication)
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_PATH)) {
            System.out.println("Connected to MongoDB!");

            // Get the 'test' database (if it doesn't exist, MongoDB will create it when we insert a document)
            MongoDatabase database = mongoClient.getDatabase(dbName);
            System.out.println("Database: " + database.getName());

            // Get the 'testCollection' collection (MongoDB will create it if it doesn't exist yet)
            this.collection = database.getCollection("testCollection");

        }
    }

    public static void fillDB(MongoCollection<Document> collection, JSONObject jsonObject ) {

        // Convert the Person object to a MongoDB Document using Gson (JSON to BSON conversion)
        Document document = Document.parse(String.valueOf(jsonObject));

        // Insert the document into the 'testCollection' collection
        collection.insertOne(document);
        System.out.println("Document inserted successfully!");

        // Retrieve the first document where the name field equals 'John'
        Document myDoc = collection.find(new Document("name", "John")).first();

        // If a document is found, deserialize it back to a Person object using Gson
        if (myDoc != null) {

        } else {
            // If no document is found, print a message
            System.out.println("No document found!");
        }
    }



}