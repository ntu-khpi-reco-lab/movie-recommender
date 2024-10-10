import com.mongodb.client.MongoClient;
import com.mongodb.ConnectionString;
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

    public static MongoCollection<Document> dbConnect(String dbName) {
        logger.info("Подключение к базе данных MongoDB");
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/testDB");
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase("testDB");
        return database.getCollection(database);
    }

    public static void fillDB() {
        Films films = new Films(4); // PAGES = 4
        logger.info("Возвращение всех данных: {} фильмов", films.getAllData().size());

        MongoCollection<Document> collection = dbConnect(DB_NAME);
        List<JSONObject> movieList = films.getAllData();

        logger.info("Вставка {} фильмов в базу данных", movieList.size());

        try {
            for (JSONObject movie : movieList) {
                Document doc = Document.parse(movie.toString());
                collection.insertOne(doc);
            }
            logger.info("Фильмы успешно добавлены в MongoDB");
        } catch (Exception e) {
            logger.error("Ошибка при вставке данных в MongoDB: ", e);
        }
    }
}
