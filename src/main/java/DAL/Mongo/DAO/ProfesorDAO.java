package DAL.Mongo.DAO;

import DAL.Mongo.MongoDAL;
import EntidadesPersistencia.Profesor;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static DAL.Mongo.MongoDAL.*;
import static com.mongodb.client.model.Filters.eq;

public class ProfesorDAO {


    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Profesor> GetProfesores() {
        List<Profesor> profesores = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var database = cliente.getDatabase(DBNAME);
            var collection = database.getCollection("Profesores");
            var result = collection.find();
            for (Document doc : result) {
                var profesor = getProfesor(doc, profesores);
                if (profesor != null)
                    profesores.add(profesor);
            }
        }
        return profesores;
    }


    /**
     * Se encaga de recoger un Documento Mongo y generar un objeto Alumno
     *
     * @return
     */
    private static Profesor getProfesor(Document result, List<Profesor> profesores) {
        Profesor profe = new Profesor();
        profe.set_id(result.get("_id").toString());
        profe.setDni((result.get("dni").toString()));
        profe.setNombre(result.get("nombre").toString());
        profe.setApellidos(result.get("apellidos").toString());
        String fecha = result.get("fechaNacimiento").toString();
        //Compruebo que la fecha tenga el formato correcto
        if(fecha.contains("/")) {
            String[] fechaS = fecha.split("/");
            if (fechaS[0].length() == 1)
                fechaS[0] = "0" + fechaS[0];
            if (fechaS[1].length() == 1)
                fechaS[1] = "0" + fechaS[1];
            fecha = fechaS[2] + "-" + fechaS[0] + "-" + fechaS[1];
        }
        //Asigno la fecha de nascimento al profesor
        profe.setFechaNacimiento(LocalDate.parse(fecha));

        //Compruebo si el profesor ya existe en la lista
        if (profesores != null) {
            for (Profesor p : profesores) {
                //Si el profesor ya existe en la lista, compruebo que el que se quiere añadir es más reciente
                if (p.getDni().equals(profe.getDni()) && p.getCreatedAt().compareTo(profe.getCreatedAt()) > 0)
                    return null;
                else if (p.getDni().equals(profe.getDni()) && p.getCreatedAt().compareTo(profe.getCreatedAt()) < 0) {
                    profesores.remove(p);
                    break;
                }
            }
        }
        profe.setCreatedAt(MongoDAL.GetTimestamp(result.get("createdAt").toString()));
        profe.setAntiguedad(result.getInteger("antiguedad"));
        return profe;
    }





    /**
     * Se encarga de recoger un profesor de la base de datos dado su id
     *
     * @param dniProfesor
     * @return
     */
    public static Profesor GetProfesorById(String dniProfesor) {
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        Profesor profesor = null;
        try (MongoClient cliente = MongoClients.create(cls)) {
            MongoDatabase db = cliente.getDatabase(DBNAME);
            MongoCollection<Document> collection = db.getCollection("Profesores");
            FindIterable<Document> findresult = collection.find(eq("dni", dniProfesor));

            Document result = findresult.sort(new Document("createdAt", -1)).first();
            try {
                profesor = getProfesor(result, null);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return profesor;
    }

    public static List<Profesor> getEntradasDeProfesor(Profesor profesor) {
        List<Profesor> profesores = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var db = cliente.getDatabase(DBNAME);
            var collection = db.getCollection("Profesores");
            var result = collection.find(eq("dni", profesor.getDni()));
            for (Document doc : result) {
                profesores.add(getProfesor(doc, null));
            }
        }
        profesores.sort(Comparator.comparing(Profesor::getCreatedAt));
        return profesores;
    }
    /**
     * Se encarga de insertar un objeto en la base de datos
     *
     * @param profesor objeto a modificar
     */
    public static void insertProfesor(Profesor profesor) {
        try {
            ConnectionString cnst = new ConnectionString(URI);
            MongoClientSettings cls = MongoClientSettings.builder()
                    .applyConnectionString(cnst)
                    .retryWrites(true)
                    .build();
            MongoClient cliente = MongoClients.create(cls);
            MongoDatabase db = cliente.getDatabase(DBNAME);
            MongoCollection<Document> collection = db.getCollection("Profesores");
            profesor.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            Document doc = new Document()
                    .append("dni", profesor.getDni())
                    .append("nombre", profesor.getNombre())
                    .append("apellidos", profesor.getApellidos())
                    .append("fechaNacimiento", profesor.getFechaNacimiento().toString())
                    .append("antiguedad", profesor.getAntiguedad())
                    .append("createdAt", profesor.getCreatedAt().toString());
            collection.insertOne(doc);
        } catch (Exception e) {
            System.out.println("No se pudo insertar el profesor");
        }
    }


}
