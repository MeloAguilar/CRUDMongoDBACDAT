package DAL.Mongo.DAO;

import DAL.Mongo.MongoDAL;
import EntidadesPersistencia.Matricula;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static DAL.Mongo.MongoDAL.*;
import static com.mongodb.client.model.Filters.eq;

public class MatriculasDAO {

    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de Matriculas.
     *
     * @return
     */
    public static List<Matricula> GetMatriculas() {

        List<Matricula> matriculas = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var database = cliente.getDatabase(DBNAME);
            var collection = database.getCollection("Matriculas");
            var result = collection.find();
            for (Document doc : result) {
                var matricula = getMatricula(doc, null);
                if (matricula != null)
                    matriculas.add(matricula);
            }
        }

        return matriculas;
    }


    /**
     * Se encaga de recoger un Documento Mongo y generar un objeto Alumno
     * @return
     */
    private static Matricula getMatricula(Document result, List<Matricula> matriculas) {
        var matricula = new Matricula();
        matricula.set_id(result.get("_id").toString());
        matricula.setId(result.getInteger("id"));
        matricula.setDniProfesor((result.getString("dniProfesor")));
        matricula.setDniAlumno(result.get("dniAlumno").toString());
        matricula.setAsignatura(result.get("asignatura").toString());
        matricula.setCurso(result.getInteger("curso"));
        matricula.setCreatedAt(MongoDAL.GetTimestamp(result.get("createdAt").toString()));

        if(matriculas != null){
            for (Matricula matricula1 : matriculas) {
                if((matricula1.getId() == matricula.getId()) && Comparator.comparing(Matricula::getCreatedAt).reversed().compare(matricula1, matricula) < 0){
                    return null;
                }
                else if (matricula1.getDniAlumno().equals(matricula.getDniAlumno()) && Comparator.comparing(Matricula::getCreatedAt).reversed().compare(matricula1, matricula) < 0){
                    matriculas.remove(matricula1);
                }
            }
        }
        return matricula;
    }
    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de matrículas.
     *
     * @return
     */
    public static List<Matricula> GetMatriculasProfesorYAlumno(String idAlumno, String idProfesor) {

        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        List<Matricula> matriculas = new ArrayList<>();
        try (MongoClient cliente = MongoClients.create(cls)) {
            MongoDatabase db = cliente.getDatabase(DBNAME);
            MongoCollection<Document> collection = db.getCollection("Matriculas");
            FindIterable<Document> result = collection.find(eq("dniAlumno", idAlumno));
            try{
                for (Document document : result) {
                  if(document.getString("dniProfesor").equals(idProfesor)){

                      matriculas.add(getMatricula(document, null));
                  }

                }
            }catch (Exception e){
                System.out.println("No se ha encontrado la matrícula");
            }
        }


        return matriculas;
    }


    public static Matricula GetMatriculaById(int id) {
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(cls);
        MongoDatabase database = mongoClient.getDatabase("Colegio");
        MongoCollection<Document> collection = database.getCollection("Matriculas");
        Document result = collection.find(eq("id", id)).first();
        Matricula matricula = getMatricula(result, null);
        return matricula;
    }


    public static void insertMatricula(Matricula matricula){
        try{
            MongoClient mongoClient = MongoClients.create(URI);
            MongoDatabase database = mongoClient.getDatabase("Colegio");
            MongoCollection<Document> collection = database.getCollection("Matriculas");
            Document doc = new Document()
                    .append("id", matricula.getId())
                    .append("dniAlumno", matricula.getDniAlumno())
                    .append("dniProfesor", matricula.getDniProfesor())
                    .append("createdAt", matricula.getCreatedAt().toString());
            collection.insertOne(doc);
        }catch(Exception e){
            System.out.println("Error al insertar la matricula");
        }
    }




    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Matricula> GetMatriculasProfesor(String idProfesor) {
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(cls);
        MongoDatabase database = mongoClient.getDatabase("Colegio");
MongoCollection<Document> collection = database.getCollection("Matriculas");
        List<Matricula> matriculas = new ArrayList<>();
        FindIterable<Document> result = collection.find(eq("dniProfesor", idProfesor));
        for (Document document : result) {
            matriculas.add(getMatricula(document, null));
        }

        return matriculas;
    }



    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Matricula> GetMatriculasALumno(String idAlumno) {
        List<Matricula> matriculas = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(cls);
        MongoDatabase database = mongoClient.getDatabase("Colegio");
        MongoCollection<Document> collection = database.getCollection("Matriculas");
        MongoCursor<Document> result = collection.find(eq("dniAlumno", idAlumno)).iterator();

        while (result.hasNext()) {
            matriculas.add(getMatricula(result.next(), null));
        }

        return matriculas;
    }
}
