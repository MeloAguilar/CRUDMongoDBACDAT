package EntidadesPersistencia;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

public class Profesor {


    //Atributos
    private String _id;
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private int antiguedad;

    private Timestamp createdAt;
    //End Atributos

    //Getters y Setters


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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    //End Getters y Setters
    //Constructores
    public Profesor(String _id, String _nombre, String _apellidos, LocalDate _fechaNac, int _antiguedad){
        this.dni = _id;
        this.nombre = _nombre;
        this.apellidos = _apellidos;
        this.fechaNacimiento = _fechaNac;
        this.antiguedad = _antiguedad;
        this.createdAt = Timestamp.from(Instant.now());
    }

    public Profesor(String nombre, String apellidos, LocalDate fechaNacimiento, int antiguedad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.antiguedad = antiguedad;
        this.createdAt = Timestamp.from(Instant.now());
    }

    public Profesor() {
        this.dni = "00000000A";
        this.nombre = "";
        this.apellidos = "";
        this.fechaNacimiento = LocalDate.now();
        this.antiguedad = 0;
        this.createdAt = Timestamp.from(Instant.now());
    }
//End Constructores


    @Override
    public String toString() {
        return String.format("""
                ========================================
                DNI = %s
                
                nombre = %s
                
                apellidos = %s
                
                fechaNacimiento = %s
                
                antigüedad = %d
                
                entrada creada = %s
                ========================================""", dni, nombre, apellidos, fechaNacimiento, antiguedad, createdAt);
    }
}
