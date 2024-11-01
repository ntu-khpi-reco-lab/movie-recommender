package com.movie.recommender.crawler.dataimport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.movie.recommender.common.model.movie.MovieDetails;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class MongoDBService {
    private MongoClient mongoClient = null;
    private MongoDatabase database = null;
    private MongoCollection<Document> collection = null;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MongoDBService() {
        String mongoClientPath = System.getenv("MONGO_CLIENT_PATH");
        String databaseName = System.getenv("MONGO_DATABASE");
        String collectionName = System.getenv("MONGO_COLLECTION");

        if (mongoClientPath == null || databaseName == null || collectionName == null) {
            log.error("Required environment variables are not set: {}{}{}",
                    mongoClientPath == null ? "MONGO_CLIENT_PATH " : "",
                    databaseName == null ? "MONGO_DATABASE " : "",
                    collectionName == null ? "MONGO_COLLECTION" : "");
            return;
        }
        mongoClient = MongoClients.create(mongoClientPath);
        database = mongoClient.getDatabase(databaseName);
        collection = database.getCollection(collectionName);
        log.info("Connected to MongoDB database: {}", database.getName());
    }

    public void insertMovies(List<MovieDetails> movieDetailsList) {
        log.info("Inserting movie data into MongoDB...");

        // Convert each MovieDetails object to a MongoDB Document using Jackson
        for (MovieDetails movie : movieDetailsList) {
            try {
                // Convert MovieDetails object to JSON string and then parse to Document
                String jsonString = objectMapper.writeValueAsString(movie);
                Document document = Document.parse(jsonString);
                collection.insertOne(document);
            } catch (Exception e) {
                log.error("Error while inserting movie: {}", movie.getTitle(), e);
            }
        }

        log.info("Movie data insertion into MongoDB completed.");
    }

    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            log.info("MongoDB connection closed.");
        }
    }
}
