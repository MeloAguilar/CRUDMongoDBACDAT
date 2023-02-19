package DAL.Mongo;

import DAL.Mongo.DAO.AlumnoDAO;
import DAL.Mongo.DAO.MatriculasDAO;
import DAL.Mongo.DAO.ProfesorDAO;
import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.DeleteOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.Timestamp;

import static com.mongodb.client.model.Filters.eq;

public class MongoDAL {

    public static final String URI = "mongodb+srv://yop:Carmelo@cluster0.9aejkfq.mongodb.net/test";


    public static final String DBNAME = "Colegio";


    /**
     * Método que sirve para comprobar que un string es una fecha tipo timestamp
     *
     * @param fecha
     * @return
     */
    public static Timestamp GetTimestamp(String fecha) {
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
            MatriculasDAO.insertMatricula((Matricula) object);
        }
    }


    public static void editAlumno() {

    }


    /**
     * Se encarga de editar un objeto en la base de datos
     *
     * @param object objeto que solo puede ser de tipo Alumno, Profesor o Matricula
     */
    public static void editObject(Object object) {
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient cliente = MongoClients.create(cls);
        MongoDatabase db = cliente.getDatabase(DBNAME);
        if (object instanceof Alumno) {
            Alumno alumno = (Alumno) object;
            var collection = db.getCollection("Alumnos");
            try {
                //editar alumno
                AlumnoDAO.insertAlumno(alumno);
                System.out.println("nuevo registro editado del alumno con dni: " + alumno.getDni());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        //si el objeto es instancia de Profesor
        else if (object instanceof Profesor) {
            Profesor profesor = (Profesor) object;
            var collection = db.getCollection("Profesores");
            try {
                //editar profesor
                ProfesorDAO.insertProfesor(profesor);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        //si el objeto es instancia de Matricula
        else if (object instanceof Matricula) {
            Matricula matricula = (Matricula) object;
            var collection = db.getCollection("Matriculas");
            try {
                //editar matricula
                MatriculasDAO.insertMatricula(matricula);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static boolean deleteByString(String collectionName, String _id) {
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient cliente = MongoClients.create(cls);
        MongoDatabase db = cliente.getDatabase(DBNAME);
        var collection = db.getCollection(collectionName);
        try {
            var result = collection.deleteOne(eq("_id", new ObjectId(_id)));
            if (result.getDeletedCount() > 0) {
                System.out.println("Se ha eliminado el registro con id: " + _id);
                return true;
            } else {
                System.out.println("No se ha encontrado el registro con id: " + _id);
                return false;

            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Se encarga de eliminar un objeto en la base de datos
     *
     * @param object objeto que solo puede ser de tipo Alumno, Profesor o Matricula
     */
    public static void deleteObject(Object object) {
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        MongoClient cliente = MongoClients.create(cls);
        MongoDatabase db = cliente.getDatabase(DBNAME);
        if (object instanceof Alumno) {
            Alumno alumno = (Alumno) object;
            var collection = db.getCollection("Alumnos");
            //eliminar alumno
            collection.deleteOne(eq("dni", alumno.getDni()), (DeleteOptions) eq("createdAt", alumno.getCreatedAt()));

        }
        //si el objeto es instancia de Profesor
        else if (object instanceof Profesor) {
            Profesor profesor = (Profesor) object;
            var collection = db.getCollection("Profesores");
            //eliminar profesor
            collection.deleteOne(eq("dni", profesor.getDni()), (DeleteOptions) eq("createdAt", profesor.getCreatedAt()));
        }
        //si el objeto es instancia de Matricula
        else if (object instanceof Matricula) {
            Matricula matricula = (Matricula) object;
            var collection = db.getCollection("Matriculas");
            //eliminar matricula
            collection.deleteOne(eq("dni", matricula.getId()), (DeleteOptions) eq("createdAt", matricula.getCreatedAt()));
        }

    }
}
