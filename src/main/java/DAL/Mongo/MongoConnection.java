package DAL.Mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;

import static com.mongodb.client.model.Filters.eq;

public class MongoConnection {

    public static final String URI = "mongodb+srv://yop:Carmelo@cluster0.9aejkfq.mongodb.net/test";
    MongoDatabase database;

    public static final String DBNAME = "Colegio";

    public MongoConnection(){
      // URI = "mongodb+srv://yop:Carmelo@cluster0.9aejkfq.mongodb.net/test";
      // DBNAME = "Colegio";
        database  = null;
    }

    public MongoConnection(String URI, String DBNAME) {
      //  this.URI = URI;
      //  this.DBNAME = DBNAME;
        database  = null;
    }



    public MongoDatabase getConnection(){
        database = null;
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try(MongoClient cliente = MongoClients.create(cls)){
            database = cliente.getDatabase(DBNAME);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return database;
    }



}
