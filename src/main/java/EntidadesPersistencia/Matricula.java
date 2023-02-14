package EntidadesPersistencia;

public class Matricula {
    private int id;
    private int idProfesor;
    private int idAlumno;
    private String asignatura;
    private int curso;


    public Matricula(int id, int idProfesor, int idAlumno, String asignatura, int curso) {
        this.id = id;
        this.idProfesor = idProfesor;
        this.idAlumno = idAlumno;
        this.asignatura = asignatura;
        this.curso = curso;
    }

    public Matricula() {
        this.id = 0;
        this.idProfesor = 0;
        this.idAlumno = 0;
        this.asignatura = "asignatura";
        this.curso = 0;
    }

    @Override
    public String toString() {
        return String.format("""
                ========================================
                id = %d
                
                idProfesor = %d
                
                idAlumno = %d
                
                asignatura = %s
                
                curso = %d
                ========================================""", id, idProfesor, idAlumno, asignatura, curso);

    }
}
