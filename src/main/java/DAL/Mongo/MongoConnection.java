package DAL.Mongo;

import com.mongodb.client.*;
import org.bson.Document;

import javax.print.Doc;

import static com.mongodb.client.model.Filters.eq;

public class MongoConnection {

    String uri;
    MongoDatabase database;

    String dbname;

    public MongoConnection(){
        uri = "mongodb+srv://yop:Carmelo@cluster0.9aejkfq.mongodb.net/test";
        dbname = "Colegio";
    }

    public MongoConnection(String uri, String dbname) {
        this.uri = uri;
        this.dbname = dbname;
        database  = null;
    }



    public void getConnection(){
        try(MongoClient cliente = MongoClients.create(uri)){
            database = cliente.getDatabase(dbname);
        }
    }



}
