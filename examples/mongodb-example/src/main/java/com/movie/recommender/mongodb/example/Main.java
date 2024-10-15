package com.movie.recommender.mongodb.example;

import com.google.gson.Gson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;





public class Main {
    public static void main(String[] args) {
        // Create a Gson instance to handle JSON serialization and deserialization
        Gson gson = new Gson();

        // Connect to MongoDB using the connection string (includes user credentials and database authentication
        try (MongoClient mongoClient = MongoClients.create("mongodb://user:pass@localhost:27017/?authSource=admin")) {

            System.out.println("Connected to MongoDB!");

            // Get the 'test' database (if it doesn't exist, MongoDB will create it when we insert a document)
            MongoDatabase database = mongoClient.getDatabase("test");
            System.out.println("Database: " + database.getName());

            // Get the 'testCollection' collection (MongoDB will create it if it doesn't exist yet)
            MongoCollection<Document> collection = database.getCollection("testCollection");

            // Create a new Person object (our POJO that we want to store in MongoDB)
            Person person = new Person("John", 30, "New York");

            // Convert the Person object to a MongoDB Document using Gson (JSON to BSON conversion)
            Document document = Document.parse(gson.toJson(person));

            // Insert the document into the 'testCollection' collection
            collection.insertOne(document);
            System.out.println("Document inserted successfully!");

            // Retrieve the first document where the name field equals 'John'
            Document myDoc = collection.find(new Document("name", "John")).first();

            // If a document is found, deserialize it back to a Person object using Gson
            if (myDoc != null) {
                Person foundPerson = gson.fromJson(myDoc.toJson(), Person.class);
                System.out.println(foundPerson);
            } else {
                // If no document is found, print a message
                System.out.println("No document found!");
            }
        }
    }
}
