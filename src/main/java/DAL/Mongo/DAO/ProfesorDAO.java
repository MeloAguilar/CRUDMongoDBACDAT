package DAL.Mongo.DAO;

import DAL.Mongo.MongoConnection;
import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Profesor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class ProfesorDAO {







    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de alumnos.
     *
     * @return
     */
    public static List<Profesor> GetProfesores() {
        MongoCollection<Document> collection = new MongoConnection().getConnection().getCollection("Profesores");
        List<Profesor> profesores = new ArrayList<>();
        for(Document doc : collection.find()){
            profesores.add(getProfesor(doc));
        }


        return profesores;
    }
    /**
     * Se encaga de recoger un Documento Mongo y generar un objeto Alumno
     * @return
     */
    private static Profesor getProfesor(Document result){
        Profesor profe = new Profesor();
        profe.set_id(result.get("_id").toString());
        profe.setDni((result.get("dni").toString()));
        profe.setNombre(result.get("nombre").toString());
        profe.setApellidos(result.get("apellidos").toString());
        String fecha = result.get("fechaNacimiento").toString();
        //Compruebo que la fecha tenga el formato correcto
        String[] fechaS = fecha.split("/");
        if(fechaS[0].length() == 1)
            fechaS[0] = "0" + fechaS[0];
        if (fechaS[1].length() == 1)
            fechaS[1] = "0" + fechaS[1];
        fecha = fechaS[2] + "-" + fechaS[0] + "-" + fechaS[1];
        //Asigno la fecha al alumno
        profe.setFechaNacimiento(LocalDate.parse(fecha));

        profe.setAntiguedad(result.getInteger("antiguedad"));
        return profe;
    }
    /**
     * Se encarga de recoger un profesor de la base de datos dado su id
     *
     * @param idProfesor
     * @return
     */
    public static Profesor GetProfesorById(String dniProfesor) {
        MongoCollection<Document> collection = new MongoConnection().getConnection().getCollection("Profesores");

        Document result = collection.find(eq("dni", dniProfesor)).first();
        Profesor profesor = new Profesor();
        try {
            profesor = getProfesor(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return profesor;
    }



    /**
     * Se encarga de insertar un objeto en la base de datos
     *
     * @param object objeto que solo puede ser de tipo Alumno, Profesor o Matricula
     */
    public static void insertProfesor(Profesor profesor){
        try{
            MongoCollection<Document> collection = new MongoConnection().getConnection().getCollection("Profesores");
            Document doc = new Document()
                    .append("dni", profesor.getDni())
                    .append("nombre", profesor.getNombre())
                    .append("apellidos", profesor.getApellidos())
                    .append("fechaNacimiento", profesor.getFechaNacimiento().toString())
                    .append("antiguedad", profesor.getAntiguedad())
                    .append("createdAt", profesor.getCreatedAt().toString());
            collection.insertOne(doc);
        }catch(Exception e){
            System.out.println("No se pudo insertar el profesor");
        }
    }

}
