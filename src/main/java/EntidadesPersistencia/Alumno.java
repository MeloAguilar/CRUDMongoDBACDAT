package EntidadesPersistencia;

import java.time.LocalDate;

public class Alumno {
    private int id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;


    public Alumno(int id, String nombre, String apellidos, LocalDate fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Alumno() {
        this.id = 0;
        this.nombre = "";
        this.apellidos = "";
        this.fechaNacimiento = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                id = %d
                
                nombre = %s
                
                apellidos = %s
                
                fechaNacimiento = %s
                ========================================""", id, nombre, apellidos, fechaNacimiento.toString());

    }



}
