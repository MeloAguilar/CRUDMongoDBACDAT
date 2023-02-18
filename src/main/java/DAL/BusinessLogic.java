package DAL;

import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusinessLogic {

    int cantidadProfesores = 0;
    int cantidadMatriculas = 0;
    int cantidadAlumnos = 0;
    public Gestion gestion;

    public BusinessLogic() {

        gestion = new Gestion();
        ResultSet result = gestion.getTable("Alumnos");
        try {
            while (result.next()) {
                cantidadAlumnos++;
            }
        }catch (SQLException we){
            System.out.println("No se pudo acceder");
        }
        result = gestion.getTable("Alumnos");
        try{
            while (result.next()){
                cantidadProfesores++;
            }
        }catch (SQLException we){
            System.out.println("No se pudo acceder");
        }
        result = gestion.getTable("Matriculas");
        try{
            while (result.next()){
                cantidadMatriculas++;
            }
        }catch (SQLException we){
            System.out.println("No se pudo acceder");
        }
    }

    public BusinessLogic(String bbdd, String user, String pass) {
        gestion = new Gestion(bbdd, user, pass);

    }

    /**
     * Método que se encarga de eliminar todas las tablas de la base de datos
     *
     * @return
     */
    public int deleteTables() {
        int filasAfectadas = 0;
        deleteTable("Matriculas");
        filasAfectadas += 1;
        deleteTable("Profesores");
        filasAfectadas += 1;
        deleteTable("Alumnos");
        filasAfectadas += 1;
        return filasAfectadas;
    }

    /**
     * Método que se encarga de insertar un dato dentro de una tabla de la base de datos
     * dado un archivo .sql.
     *
     * <pre>archivo debe ser un archivo .sql con el nombre de la
     * tabla igual a uno que se encuentre en la base de datos a la qu queremos atacar</pre>
     *
     * @param archivo
     * @return
     */
    public int insertArchivoCompleto(File archivo) {
        String linea;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(archivo));

            while ((linea = br.readLine()) != null) {
                gestion.insertString(linea);

            }

            System.out.println("datos insertados correctamente");
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se encontró");
        } catch (IOException e) {
            System.out.println("Errror de entrada o salida de datos");
        } finally {
            if (gestion.getConexion().getConexion() != null){
                gestion.getConexion().cerrarConexion();
            }

        }
        return 0;
    }







    /**
     * Método que se encarga de eliminar una tabla de la base de datos
     * a la q2ue está asociada esta clase
     *
     * @param tabla
     */
    public void deleteTable(String tabla) {

        try {
            gestion.deleteTable(tabla);
        } catch (SQLException e) {
            System.out.println("No se pudo acceder a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("La tabla no existe");
        }
    }

    /**
     * @param nombreTabla
     * @return
     */
    public List listarTabla(String nombreTabla) throws ClassNotFoundException {


        //Genero la cadena de la select
        List lista = null;
        try {
            //Gracias al método de la clase gestion lanzo la peticion y recojo el resultSet
            ResultSet result = gestion.getTable(nombreTabla);


            //Si el nomrbe de la tabla es Profesores
            if (nombreTabla.equals("Profesores")) {
                lista = new ArrayList<Profesor>();
                //Relleno el arrayList de profesores
                while (result.next()) {
                    var profesor = new Profesor(result.getString(1), result.getString(2),result.getString(3), result.getDate(4).toLocalDate(), result.getInt(5));
                    System.out.println(profesor);
                }

                //Si el nombre de la tabla es Alumnos
            } else if (nombreTabla.equals("Alumnos")) {
                lista = new ArrayList<Alumno>();
                //Genero el arrayList de Alumnos
                while (result.next()) {
                    var alumno = new Alumno(result.getString(1), result.getString(2),result.getString(3), result.getDate(4).toLocalDate());
                    System.out.println(alumno);
                }

                //Si el nombre de la tabla es Matriculas
            } else if (nombreTabla.equals("Matriculas")) {
                lista = new ArrayList<Matricula>();
                //Genero el arrayList de Matriculas
                while (result.next()) {
                    var matricula = new Matricula(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5));
                    System.out.println(matricula);
                }
            }
        } catch (SQLException s) {
            System.out.println("No se pudo acceder a la base de datos");
        }

        //Devuelvo la lista
        return lista;
    }




    /**
     * Método que se encarga de modificar un registro de la tabla profesores de la base de datos
     *
     * @param datos
     * @return
     */
    public boolean editAlumno(String[] datos) {
        var exito = false;
        if ((getAlumnoById(Integer.parseInt(datos[0]))) != null) {
            var datosReales = new StringBuilder("Update Alumnos set nombre = ");
            datosReales.append("'");
            datosReales.append(datos[1]);
            datosReales.append("'");
            datosReales.append(", apellidos = ");
            datosReales.append("'");
            datosReales.append(datos[2]);
            datosReales.append("'");
            datosReales.append(", fechaNacimiento = ");
            datosReales.append("'");
            datosReales.append(datos[3]);
            datosReales.append("'");
            datosReales.append(" Where id = ");
            datosReales.append(datos[0]);
            gestion.updateString(String.valueOf(datosReales));
            exito = true;
        } else {
            System.out.println("El registro no se encontró en la base de datos");
        }

        return exito;
    }


    /**
     * Método que se encarga de modificar un registro de la tabla profesores de la base de datos
     *
     * @param datos
     * @return exito
     */
    public boolean editMatricula(String[] datos) {
        var exito = false;

        if ((getMatriculaById(Integer.parseInt(datos[0]))) != null) {
            var datosReales = new StringBuilder("Update Matriculas set idProfesor = ");
            datosReales.append(datos[1]);
            datosReales.append(", idAlumno = ");
            datosReales.append(datos[2]);
            datosReales.append(", asignatura = ");
            datosReales.append("'");
            datosReales.append(datos[3]);
            datosReales.append("'");
            datosReales.append(", curso = ");
            datosReales.append(datos[4]);

            datosReales.append(" Where id = ");
            datosReales.append(datos[0]);
            gestion.updateString(String.valueOf(datosReales));
            exito = true;
        } else {
            System.out.println("El registro no se encontró en la base de datos");
        }


        return exito;
    }


    /**
     * Método que se encarga de modificar un registro de la tabla profesores de la base de datos
     *
     * @param datos
     * @return exito
     */
    public boolean editProfesor(String[] datos) {
        var exito = false;
        try {
            if ((getProfesorById(Integer.parseInt(datos[0]))) != null) {
                var datosReales = new StringBuilder("Update Profesores set nombre = ");
                datosReales.append(datos[1]);
                datosReales.append(" apellidos = ");
                datosReales.append(datos[2]);
                datosReales.append(" fechaNacimiento = ");
                datosReales.append(datos[3]);
                datosReales.append(" antiguedad = ");
                datosReales.append(datos[4]);
                datosReales.append(" Where id = ");
                datosReales.append(datos[0]);
                gestion.updateString(String.valueOf(datosReales));
                exito = true;
            } else {
                System.out.println("El registro no se encontró en la base de datos");
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return exito;
    }


    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Profesor que coincida con los
     * datos que recibe del resultset
     *
     * @param id id correspondiente a un registro de la tabla Profesores
     * @return
     */
    public Profesor getProfesorById(int id) throws ClassNotFoundException {
        Profesor prof = null;
        String nombre;
        String apellidos;
        LocalDate fechaNacimiento;
        int antiguedad;
        ResultSet result;
        //Le pido el resultSet de la query a la clase gestion
        try {



            result = gestion.getSelectResultSet("Select * From ad2223_caguilar.Profesores where id = ? ", id);

            result.next();
            //Establezco los datos que contendra el objeto Profesor
            result.getInt(1);
            nombre = result.getString(2);
            apellidos = result.getString(3);
            fechaNacimiento = result.getDate(4).toLocalDate();
            antiguedad = result.getInt(5);
            //prof = new Profesor(id, nombre, apellidos, fechaNacimiento, antiguedad);
        } catch (SQLException e) {
            System.out.println("No se pudo acceder a la base de datos");
        }
        //Instancio el profesor

        return prof;
    }

    /**
     * Método que se encarga de, dado un ResultSet y un entero,
     * buscar una posicion dentro de la tabla y devuelve
     * un objeto de tipo Profesor que coincida con los
     * datos que recibe del resultset
     *
     * @param id
     * @return alumno
     */
    public Alumno getAlumnoById(int id) {
        Alumno alumno = null;
        String nombre;
        String apellidos;
        LocalDate fechaNacimiento;
        StringBuilder datos = new StringBuilder("Select * From ");
        datos.append(gestion.USER);
        datos.append(".Alumnos Where id=?");
        ResultSet result;
        try {
            result = gestion.getSelectResultSet(String.valueOf(datos), id);
            result.next();
            nombre = result.getString("nombre");
            apellidos = result.getString("apellidos");
            fechaNacimiento = result.getDate("fechaNacimiento").toLocalDate();
            //Volver a modificar para que funcione con el dni y el timestamp en hibernate
            //alumno = new Alumno(id, nombre, apellidos, fechaNacimiento);
        } catch (SQLException e) {
            System.out.println("No se obtuvo ningún registro");
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo obtener informacion de los datos introducidos");
        }

        return alumno;
    }


    /**
     * Método que se encarga de, dado un entero que sea igual al id de una matricula
     * que se encuentre en la base de datos, devuelve un objeto Matricula
     *
     * @param id
     * @return
     */
    public Matricula getMatriculaById(int id) {
        Matricula matricula = null;
        String idProf;
        String idAlumno;
        String asignatura;
        int curso;
        var datos = new StringBuilder("Select * From ");
        datos.append(gestion.USER);
        datos.append(".Matriculas Where id = ?");
        ResultSet result;
        try {
            result = gestion.getSelectResultSet(String.valueOf(datos), id);
            result.next();
            idProf = result.getString("idProfesor");
            idAlumno = result.getString("idAlumno");
            asignatura = result.getString("asignatura");
            curso = result.getInt("curso");
            matricula = new Matricula(id, idProf, idAlumno, asignatura, curso);
        } catch (SQLException e) {
            System.out.println("Columna no válida");
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo obtener informacion de los datos introducidos");
        }

        return matricula;
    }


    /**
     * Método que se encarga de generar las tablas de la base de datos
     *
     * @return
     */
    public int crearTablas() {
        //Este método solo se encarga de llamar al método que genera la tabla para hacer el
        int filasAfectadas = 0;
        gestion.crearTabla("Profesores", new String[]{"id int Primary Key AUTO_INCREMENT", "nombre varChar(50)", "apellidos varChar(50)", "fechaNacimiento Date", "antiguedad int"});
        gestion.crearTabla("Alumnos", new String[]{"id int Primary Key AUTO_INCREMENT", "nombre varChar(50)", "apellidos varChar(50)", "fechaNacimiento Date"});
        gestion.crearTabla("Matriculas", new String[]{"id int Primary Key AUTO_INCREMENT", "idProfesor int", "idAlumno int", "asignatura varChar(50)", "curso int", "Foreign Key (idProfesor) References Profesores(id) On delete Cascade on Update Cascade", "Foreign Key (idAlumno) References Alumnos(id) On Delete Cascade on Update Cascade"});
        return filasAfectadas;
    }


    public int insertProfesor(String[] datos) {
        StringBuilder dat = new StringBuilder("Insert into ad2223_caguilar.Profesores (nombre, apellidos, fechaNacimiento, antiguedad) values (");
        dat.append("'");
        dat.append(datos[0]);
        dat.append("'");
        dat.append(", ");
        dat.append("'");
        dat.append(datos[1]);
        dat.append("'");
        dat.append(", ");
        dat.append("'");
        dat.append(datos[2]);
        dat.append("'");
        dat.append(", ");
        dat.append(datos[3]);

        dat.append(")");
        int filasAfectadas = gestion.insertString(String.valueOf(dat));
        return filasAfectadas;
    }


    public int insertAlumno(String[] datos) {
        StringBuilder dat = new StringBuilder("Insert into ad2223_caguilar.Alumnos (nombre, apellidos, fechaNacimiento) values (");
        dat.append("'");
        dat.append(datos[0]);
        dat.append("'");
        dat.append(", ");
        dat.append("'");
        dat.append(datos[1]);
        dat.append("'");
        dat.append(", ");
        dat.append("'");
        dat.append(datos[2]);
        dat.append("'");
        dat.append(")");
        return gestion.insertString(String.valueOf(dat));
    }


    /**
     * Método que, dado un array de Strings, introduce los datos en la tabla Matriculas de la base de datos
     *
     * @param datos
     */
    public int insertMatricula(String[] datos) {
        var filas = 0;
        try {
            var profesr = getProfesorById(Integer.parseInt(datos[0]));
            var alumno = getAlumnoById(Integer.parseInt(datos[1]));
            if (profesr != null && alumno != null) {
                StringBuilder dat = new StringBuilder("Insert into ad2223_caguilar.Matriculas(idProfesor, idAlumno, asignatura, curso) values (");
                dat.append(datos[0]);
                dat.append(", ");
                dat.append(datos[1]);
                dat.append(", ");
                dat.append("'");
                dat.append(datos[2]);
                dat.append("'");
                dat.append(", ");
                dat.append(datos[3]);

                dat.append(")");
                filas = gestion.insertString(String.valueOf(dat));

            } else {
                System.out.println("idProfesor e idAlumno deben coincidir con un registro de la base de datos.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Debe introducir un numero entero");
        } catch (ClassNotFoundException e) {
            System.out.println("isProfesor e idAlumnos deben ser números enteros");
        }

return filas;
    }


}
