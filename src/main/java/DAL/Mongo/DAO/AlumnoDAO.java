package DAL.Mongo.DAO;

import DAL.Mongo.MongoDAL;
import EntidadesPersistencia.Alumno;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static DAL.Mongo.MongoDAL.*;
import static com.mongodb.client.model.Filters.eq;

public class AlumnoDAO {
    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return lista completa de alumnos
     */
    public static List<Alumno> GetALumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var database = cliente.getDatabase(DBNAME);
            var collection = database.getCollection("Alumnos");
            var result = collection.find();
            for (Document doc : result) {
                var alumno = getAlumno(doc, alumnos);
                if (alumno != null)
                    alumnos.add(alumno);
            }


        }
        return alumnos;
    }


    /**
     * Se encaga de recoger un Documento Mongo y generar un objeto Alumno
     *
     * @return un alumno con los datos del documento
     */
    private static Alumno getAlumno(Document result, List<Alumno> alumnos) {
        var alumno = new Alumno();
        alumno.set_id(result.get("_id").toString());
        alumno.setDni((result.get("dni").toString()));
        alumno.setNombre(result.get("nombre").toString());
        alumno.setApellidos(result.get("apellidos").toString());
        String fecha = result.get("fechaNacimiento").toString();
        //Compruebo que la fecha tenga el formato correcto
        if (fecha.contains("/")) {
            String[] fechaS = fecha.split("/");
            if (fechaS[0].length() == 1)
                fechaS[0] = "0" + fechaS[0];
            if (fechaS[1].length() > 0)
                fechaS[1] = "0" + fechaS[1];
            fecha = fechaS[2] + "-" + fechaS[0] + "-" + fechaS[1];
        }
        //Asigno la fecha de nacimiento al alumno
        alumno.setFechaNacimiento(LocalDate.parse(fecha));

        //Compruebo que la fecha de creación tenga el formato correcto
        var fechaCreacion = result.get("createdAt").toString();
        //Compruebo que la fecha tenga el formato correcto y si no, le añado la hora
        Timestamp timestamp = MongoDAL.GetTimestamp(fechaCreacion);
        alumno.setCreatedAt(timestamp);
        //Compruebo si se ha introducido una lista de asignaturas y que no esté vacía
        if (alumnos != null)
            if (alumnos.size() > 0) {
                //Compruebo que el alumno no esté repetido y que sea el más reciente
                for (int i = 0; i < alumnos.size(); i++) {
                    //Si el alumno está repetido y es más antiguo, lo elimino
                    if (alumnos.get(i).getDni().equals(alumno.getDni()) && (alumnos.get(i).getCreatedAt().compareTo(alumno.getCreatedAt()) > 0))
                        alumno = null;
                    else if (alumnos.get(i).getDni().equals(alumno.getDni()) && (alumnos.get(i).getCreatedAt().compareTo(alumno.getCreatedAt()) < 0))
                        alumnos.remove(i);
                }
            }
        return alumno;
    }


    /**
     * Se encarga de recoger un alumno de la base de datos dado su DNI
     *
     * @param idAlumno
     * @return
     */
    public static Alumno GetAlumnoById(String idAlumno) {
        Alumno alumno = new Alumno();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var database = cliente.getDatabase(DBNAME);
            var collection = database.getCollection("Alumnos");
            FindIterable<Document> findresult = collection.find(eq("dni", idAlumno));
            //Ordeno los resultados por fecha de creación y cojo el primero (el más reciente)
            Document result = findresult.sort(new Document("createdAt", -1)).first();
            if (result != null) {
                alumno = getAlumno(result, null);
            }


        }
        return alumno;
    }


    /**
     * Se encarga de insertar un objeto de tipo alumno en la base de datos de MongoDB
     *
     * @param alumno
     */
    public static void insertAlumno(Alumno alumno) {
        try {
            ConnectionString cnst = new ConnectionString(URI);
            MongoClientSettings cls = MongoClientSettings.builder()
                    .applyConnectionString(cnst)
                    .retryWrites(true)
                    .build();
            MongoClient cliente = MongoClients.create(cls);
            MongoDatabase database = cliente.getDatabase(DBNAME);
            MongoCollection<Document> collection = database.getCollection("Alumnos");
            alumno.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            Document doc = new Document()
                    .append("dni", alumno.getDni())
                    .append("nombre", alumno.getNombre())
                    .append("apellidos", alumno.getApellidos())
                    .append("fechaNacimiento", alumno.getFechaNacimiento().toString())
                    .append("createdAt", alumno.getCreatedAt().toString());
            collection.insertOne(doc);
        } catch (Exception e) {
            System.out.println("No se pudo insertar el alumno");
        }
    }




    public static List<Alumno> getEntradasDeAlumno(Alumno alumno) {
        List<Alumno> alumnos = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var db = cliente.getDatabase(DBNAME);
            var collection = db.getCollection("Alumnos");
            var result = collection.find(eq("dni", alumno.getDni()));
            for (Document doc : result) {
                alumnos.add(getAlumno(doc, null));
            }
        }
        return alumnos;
    }


    public static List<Alumno> getAlumnosPorNombre(String nombre) {
        List<Alumno> alumnos = new ArrayList<>();
        ConnectionString cnst = new ConnectionString(URI);
        MongoClientSettings cls = MongoClientSettings.builder()
                .applyConnectionString(cnst)
                .retryWrites(true)
                .build();
        try (MongoClient cliente = MongoClients.create(cls)) {
            var db = cliente.getDatabase(DBNAME);
            var collection = db.getCollection("Alumnos");
            var result = collection.find(eq("nombre", nombre));
            for (Document doc : result) {
                alumnos.add(getAlumno(doc, null));
            }
        }
        return alumnos;
    }
}
