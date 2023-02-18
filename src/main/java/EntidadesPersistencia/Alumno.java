package EntidadesPersistencia;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

public class Alumno {

    public String _id;
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;

    private Timestamp createdAt;


    public Alumno(String dni, String nombre, String apellidos, LocalDate fechaNacimiento) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        createdAt = Timestamp.from(Instant.now());
    }


    public Alumno() {
        this.dni = "00000000A";
        this.nombre = "";
        this.apellidos = "";
        this.fechaNacimiento = LocalDate.now();
        createdAt = Timestamp.from(Instant.now());
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
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

    public void setFechaNacimiento(String value) {
        try{
            this.fechaNacimiento = LocalDate.from(LocalDate.parse(value));
        }catch(Exception e){
            this.fechaNacimiento = null;
            System.out.println("Fallo en el formato de la fecha");

        }
    }
    public void setFechaNacimiento(LocalDate value) {
        this.fechaNacimiento = value;
    }
    @Override
    public String toString() {
        return String.format("""
                ========================================
                DNI = %s
                
                nombre = %s
                
                apellidos = %s
                
                fechaNacimiento = %s
                ========================================""", dni, nombre, apellidos, fechaNacimiento.toString());

    }



}
