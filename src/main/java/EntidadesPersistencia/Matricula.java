package EntidadesPersistencia;
import java.sql.Timestamp;
import java.time.Instant;

public class Matricula {

    private String _id;
    private int id;
    private String dniProfesor;
    private String dniAlumno;
    private String asignatura;
    private int curso;

    private Timestamp createdAt;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDniProfesor() {
        return dniProfesor;
    }

    public void setDniProfesor(String dniProfesor) {
        this.dniProfesor = dniProfesor;
    }

    public String getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(String dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public Matricula(int id, String dniProfesor, String dniAlumno, String asignatura, int curso) {
        this.id = id;
        this.dniProfesor = dniProfesor;
        this.dniAlumno = dniAlumno;
        this.asignatura = asignatura;
        this.curso = curso;
        createdAt = Timestamp.from(Instant.now());
    }

    public Matricula() {
        this.id = 0;
        this.dniProfesor = "00000000A";
        this.dniAlumno = "00000000A";
        this.asignatura = "asignatura";
        this.curso = 0;
        createdAt = Timestamp.from(Instant.now());
    }

    @Override
    public String toString() {
        return String.format("""
                ========================================
                id = %d
                
                dni Profesor = %s
                
                dni Alumno = %s
                
                asignatura = %s
                
                curso = %d
                ========================================""", id, dniProfesor, dniAlumno, asignatura, curso);

    }
}
