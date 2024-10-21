package com.movie.recommender.crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DB {

    private static final String CONNECTION_PATH = "mongodb://localhost:27017";
    private static final String DB_NAME = "films";
    private static final Logger logger = LoggerFactory.getLogger(DB.class);

    private MongoClient mongoClient = null;
    private MongoDatabase database = null;

    // Constructor to connect to MongoDB and get the database
    public DB() {
        try {
            connect();
        } catch (Exception e) {
            logger.error("Error connecting to MongoDB", e);
        }
    }

    // Connect to MongoDB and initialize the database connection
    private void connect() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_PATH);
            database = mongoClient.getDatabase(DB_NAME);
            logger.info("Connected to MongoDB and database: " + DB_NAME);
        }
    }

    // Get the MongoCollection (will create it if it doesn't exist)
    public MongoCollection<Document> getCollection(String collectionName) {
        if (database != null) {
            return database.getCollection(collectionName);
        } else {
            throw new IllegalStateException("Database connection is not initialized.");
        }
    }

    // Insert a single document into the specified collection
    public void insertDocument(String collectionName, String jsString) {
        try {
            MongoCollection<Document> collection = getCollection(collectionName);
            Document document = Document.parse(jsString);
            collection.insertOne(document);
            logger.info("Document inserted successfully into collection: " + collectionName);
        } catch (Exception e) {
            logger.error("Error inserting document into MongoDB", e);
        }
    }

    public void insertJsonListToCollection(String collectionName, JsonArray jsonList) {
        for (JsonElement jsonElement : jsonList) {
            insertDocument(collectionName, jsonElement.toString());
        }
    }

    // Close the MongoClient when done
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            logger.info("MongoDB connection closed.");
        }
    }


}
