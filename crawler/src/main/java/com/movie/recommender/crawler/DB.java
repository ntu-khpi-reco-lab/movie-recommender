package com.movie.recommender.crawler;

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
    private static final String MONGO_CLIENT_PATH = "mongodb://localhost:27017/";
    private static final String CLIENT = "mydatabase";
    private static final String DB_NAME = "films";
    private static final Logger logger = LoggerFactory.getLogger(DB.class);

    public static void dbConnect(String dbName) {

    }

    public static void fillDB() {
    }
}
