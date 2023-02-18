package DAL.Mongo.DAO;

import DAL.Mongo.MongoConnection;
import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MatriculasDAO {

    /**
     * Se encarga de recoger un documento json dado el nombre de una collection
     * y generar una lista de Matriculas.
     *
     * @return
     */
    public static List<Matricula> GetMatriculas() {
        var collection = new MongoConnection().getConnection().getCollection("Matriculas");
        List<Matricula> matriculas = new ArrayList<>();
        for(Document doc : collection.find()){
            matriculas.add(getMatricula(doc));
        }

        return matriculas;
    }


    /**
     * Se encaga de recoger un Documento Mongo y generar un objeto Alumno
     * @return
     */
    private static Matricula getMatricula(Document result){
        var matricula = new Matricula();
        matricula.set_id(result.get("_id").toString());
        matricula.setDniProfesor((result.getString("dniProfesor")));
        matricula.setDniAlumno(result.get("dniAlumno").toString());
        matricula.setAsignatura(result.get("asignatura").toString());
        matricula.setCurso(result.getInteger("curso"));
        matricula.setCreatedAt(Timestamp.from(result.getDate("createdAt").toInstant()));

        return matricula;
    }


    public static Matricula GetMatriculaById(int id) {
        MongoCollection<Document> collection = new MongoConnection().getConnection().getCollection("Matriculas");
        Document result = collection.find(eq("_id", id)).first();
        Matricula matricula = getMatricula(result);
        return matricula;
    }
}
