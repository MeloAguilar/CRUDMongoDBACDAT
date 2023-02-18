package DAL.Mongo;

import DAL.Mongo.DAO.AlumnoDAO;
import DAL.Mongo.DAO.ProfesorDAO;
import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.DeleteOptions;
import org.bson.Document;

import java.sql.Timestamp;
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
        MongoDatabase db = cnn.getConnection();
        return db.getCollection(collectionName);
    }


    /**
     * Método que sirve para comprobar que un string es una fecha tipo timestamp
     * @param fecha
     * @return
     */
    public static Timestamp GetTimestamp(String fecha){
        Timestamp timestamp = null;
        try {
            timestamp = Timestamp.valueOf(fecha);
        } catch (Exception e) {
            fecha += " 00:00:00";
            timestamp = Timestamp.valueOf(fecha);
        }

        return timestamp;
    }









    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de matrículas.
     *
     * @return
     */
    public static Matricula GetMatricula(String idAlumno, String idProfesor) {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Matriculas");
        Matricula matricula = null;
        MongoCursor<Document> result = collection.find(eq("id", idAlumno)).iterator();
        try {
            while (result.hasNext()) {
                String json = result.next().toJson();
                matricula = mapper.readValue(json, Matricula.class);
                if (matricula.getDniProfesor() == idProfesor) {
                    break;
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            result.close();
        }

        return matricula;
    }



    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Matricula> GetMatriculasALumno(String idAlumno) {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Matriculas");
        List<Matricula> matriculas = new ArrayList<>();
        MongoCursor<Document> result = collection.find(eq("dniAlumno", idAlumno)).iterator();
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
    public static List<Matricula> GetMatriculasProfesor(String idProfesor) {
        ObjectMapper mapper = new ObjectMapper();
        MongoCollection<Document> collection = GetCollection("Matriculas");
        List<Matricula> matriculas = new ArrayList<>();
        MongoCursor<Document> result = collection.find(eq("dniProfesor", idProfesor)).iterator();
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
     * Se encarga de insertar un objeto en la base de datos
     *
     * @param object objeto que solo puede ser de tipo Alumno, Profesor o Matricula
     */
    public static void insertObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        if (object instanceof Alumno) {
           AlumnoDAO.insertAlumno((Alumno) object);

        }
        //si el objeto es instancia de Profesor
        else if (object instanceof Profesor) {
           ProfesorDAO.insertProfesor((Profesor) object);
        }
        //si el objeto es instancia de Matricula
        else if (object instanceof Matricula) {
            Matricula matricula = (Matricula) object;
            var collection = GetCollection("Matriculas");
            try {
                //insertar matricula
                collection.insertOne(Document.parse(mapper.writeValueAsString(matricula)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private void insertMatricula(Matricula matricula){
        try{
            MongoClient mongoClient = MongoClients.create(new MongoConnection().URI);
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
     * Se encarga de editar un objeto en la base de datos
     *
     * @param object objeto que solo puede ser de tipo Alumno, Profesor o Matricula
     */
    public static void editObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        if (object instanceof Alumno) {
            Alumno alumno = (Alumno) object;
            var collection = GetCollection("Alumnos");
            try {
                //editar alumno
                collection.replaceOne(eq("id", alumno.getDni()), Document.parse(mapper.writeValueAsString(alumno)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        //si el objeto es instancia de Profesor
        else if (object instanceof Profesor) {
            Profesor profesor = (Profesor) object;
            var collection = GetCollection("Profesores");
            try {
                //editar profesor
                collection.replaceOne(eq("id", profesor.getDni()), Document.parse(mapper.writeValueAsString(profesor)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        //si el objeto es instancia de Matricula
        else if (object instanceof Matricula) {
            Matricula matricula = (Matricula) object;
            var collection = GetCollection("Matriculas");
            try {
                //editar matricula
                collection.replaceOne(eq("id", matricula.getId()), Document.parse(mapper.writeValueAsString(matricula)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Se encarga de eliminar un objeto en la base de datos
     *
     * @param object objeto que solo puede ser de tipo Alumno, Profesor o Matricula
     */
    public static void deleteObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        if (object instanceof Alumno) {
            Alumno alumno = (Alumno) object;
            var collection = GetCollection("Alumnos");
            //eliminar alumno
            collection.deleteOne(eq("id", alumno.getDni()), (DeleteOptions) eq("createdAt", alumno.getCreatedAt()));

        }
        //si el objeto es instancia de Profesor
        else if (object instanceof Profesor) {
            Profesor profesor = (Profesor) object;
            var collection = GetCollection("Profesores");
            //eliminar profesor
            collection.deleteOne(eq("id", profesor.getDni()), (DeleteOptions) eq("createdAt", profesor.getCreatedAt()));
        }
        //si el objeto es instancia de Matricula
        else if (object instanceof Matricula) {
            Matricula matricula = (Matricula) object;
            var collection = GetCollection("Matriculas");
            //eliminar matricula
            collection.deleteOne(eq("id", matricula.getId()), (DeleteOptions) eq("createdAt", matricula.getCreatedAt()));
        }

    }
}
