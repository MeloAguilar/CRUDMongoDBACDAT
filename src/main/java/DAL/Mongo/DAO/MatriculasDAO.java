package DAL.Mongo.DAO;

import DAL.Mongo.MongoDAL;
import EntidadesPersistencia.Matricula;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.DeleteOptions;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.Instant;
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
                var matricula = getMatricula(doc, matriculas);
                if (matricula != null)
                    matriculas.add(matricula);
            }
        }catch (Exception e){
            e.printStackTrace();
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


        if (matriculas != null)
            if (matriculas.size() > 0) {
                //Compruebo que el alumno no esté repetido y que sea el más reciente
                for (int i = 0; i < matriculas.size(); i++) {
                    //Si el alumno está repetido y es más antiguo, lo elimino
                    if (matriculas.get(i).getId()==(matricula.getId()) && (matriculas.get(i).getCreatedAt().compareTo(matricula.getCreatedAt()) > 0))
                        matricula = null;
                    else if (matriculas.get(i).getId()==(matricula.getId()) && (matriculas.get(i).getCreatedAt().compareTo(matricula.getCreatedAt()) < 0))
                        matriculas.remove(i);
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


    /**
     * Se encarga de recoger un documento json dado el id de una matricula
     * y generar un objeto matricula.
     *
     * <pre>
     * id debe coincidir con la key id de la tabla collection matriculas
     * id debe ser un entero positivo.
     * </pre>
     * <post>
     *     Si no se encuentra la matricula con el id dado, se devuelve null
     * </post>
     * @param id
     * @return
     */
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


    /**
     * Se encarga de insertar una matricula en la base de datos
     * @param matricula
     */
    public static void insertMatricula(Matricula matricula){
        try{
            MongoClient mongoClient = MongoClients.create(URI);
            MongoDatabase database = mongoClient.getDatabase("Colegio");
            MongoCollection<Document> collection = database.getCollection("Matriculas");
            var idMat = GetMatriculas().size();
            Document doc = new Document()
                    .append("id", (idMat + 1))
                    .append("dniAlumno", matricula.getDniAlumno())
                    .append("dniProfesor", matricula.getDniProfesor())
                    .append("asignatura", matricula.getAsignatura())
                    .append("curso", matricula.getCurso())
                    .append("createdAt", matricula.getCreatedAt().toString());
            collection.insertOne(doc);
        }catch(Exception e){
            System.out.println("Error al insertar la matricula");
        }
    }


    /**
     *Se encarga de editar una matricula. Esto no significa que se llame al método updateOne,
     * ya que nosotros controlamos las entradas mediante un Timestamp. Esto significa que
     * si se llama a este método, se insertará un nuevo documento con el mismo id que el
     * anterior, pero con un timestamp más reciente y un id de Mongo diferente
     *
     *<pre>matricula no puede estar vacío ni ser null</pre>
     * <post>introducirá un nuevo registro en la base de datos con un id igual a uno ya existente en esta</post>
     * @param matricula
     */
    public static void editMatricula(Matricula matricula){
        try{
            MongoClient mongoClient = MongoClients.create(URI);
            MongoDatabase database = mongoClient.getDatabase("Colegio");
            MongoCollection<Document> collection = database.getCollection("Matriculas");
            Document doc = new Document()
                    .append("id", matricula.getId())
                    .append("dniAlumno", matricula.getDniAlumno())
                    .append("dniProfesor", matricula.getDniProfesor())
                    .append("asignatura", matricula.getAsignatura())
                    .append("curso", matricula.getCurso())
                    .append("createdAt", Timestamp.from(Instant.now()).toString());
            collection.insertOne(doc);
        }catch(Exception e){
            System.out.println("Error al editar la matricula");
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
    public static List<Matricula> GetMatriculasAlumno(String idAlumno) {
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

    public static List<Matricula> GetOcurenciasMatricula(int id){
        List<Matricula> matriculas = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(cls);
        MongoDatabase database = mongoClient.getDatabase("Colegio");

        MongoCollection<Document> collection = database.getCollection("Matriculas");
        MongoCursor<Document> result = collection.find(eq("id", id)).iterator();


        while (result.hasNext()) {
            matriculas.add(getMatricula(result.next(), null));
        }



        return matriculas;
    }
}
