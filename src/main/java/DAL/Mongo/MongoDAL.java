package DAL.Mongo;

import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDAL {


    /**
     * Método que se encarga d recoger un objeto mongoCollection y devolverlo para su uso
     *
     * @param collectionName
     * @return coleccion de todos los registros del Documento no relacional
     */
    public static MongoCollection<Document> GetCollection(String collectionName) {
        MongoConnection cnn = new MongoConnection();
        cnn.getConnection();
        return cnn.database.getCollection(collectionName);
    }


    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Alumno> GetALumnos() {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Alumnos");
        List<Alumno> alumnos = new ArrayList<>();
        MongoCursor<Document> result = collection.find().iterator();
        try {
            while (result.hasNext()) {
                String json = result.next().toJson();
                Alumno alumno = mapper.readValue(json, Alumno.class);
                alumnos.add(alumno);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            result.close();
        }

        return alumnos;
    }


    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Profesor> GetProfesores() {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Profesores");
        List<Profesor> profesores = new ArrayList<>();
        MongoCursor<Document> result = collection.find().iterator();
        try {
            while (result.hasNext()) {
                String json = result.next().toJson();
                Profesor profesor = mapper.readValue(json, Profesor.class);
                profesores.add(profesor);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            result.close();
        }

        return profesores;
    }


    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Matricula> GetMatriculasALumno(int idAlumno) {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Matriculas");
        List<Matricula> matriculas = new ArrayList<>();
        MongoCursor<Document> result = collection.find(eq("id", idAlumno)).iterator();
        try {
            while (result.hasNext()) {
                String json = result.next().toJson();
                Matricula matricula = mapper.readValue(json, Matricula.class);
                matriculas.add(matricula);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            result.close();
        }

        return matriculas;
    }

    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Matricula> GetMatriculasProfesor(int idProfesor) {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Matriculas");
        List<Matricula> matriculas = new ArrayList<>();
        MongoCursor<Document> result = collection.find(eq("idProfesor", idProfesor)).iterator();
        try {
            while (result.hasNext()) {
                String json = result.next().toJson();
                Matricula matricula = mapper.readValue(json, Matricula.class);
                matriculas.add(matricula);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            result.close();
        }

        return matriculas;
    }


    /**
     * Se encarga de recoger un alumno de la base de datos dado su id
     * @param idAlumno
     * @return
     */
    public static Alumno GetAlumnoById(int idAlumno) {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Alumnos");
        Alumno alumno = new Alumno();
        Document result = collection.find(eq("id", idAlumno)).first();
        try {
            if (result != null) {
                alumno = mapper.readValue(result.toJson(), Alumno.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return alumno;
    }


    /**
     * Se encarga de recoger un profesor de la base de datos dado su id
     * @param idAlumno
     * @return
     */
    public static Profesor GetProfesorById(int idProfesor){
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Profesores");

        Document result = collection.find(eq("id", idProfesor)).first();
        Profesor profesor = new Profesor();
        try {
            if (result != null) {
                profesor = mapper.readValue(result.toJson(), Profesor.class);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return profesor;
    }




    public void insertObject(Object object){

    }

}
