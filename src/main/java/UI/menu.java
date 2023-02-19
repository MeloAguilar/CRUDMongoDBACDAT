package UI;

import DAL.Mongo.DAO.AlumnoDAO;
import DAL.Mongo.DAO.MatriculasDAO;
import DAL.Mongo.DAO.ProfesorDAO;
import DAL.Mongo.MongoDAL;
import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

public class menu {

    //Método que lanza el inicio del menú
    public void iniciarMenu(Scanner sc) {

        start(sc);
    }

    private static int seleccionarOpcion(Scanner sc) {
        int eleccion = 0;
        String opcion = sc.next();

        try {
            eleccion = Integer.parseInt(opcion);
        } catch (NumberFormatException e) {
            System.out.println("No se pudo registrar esa opcion");
        }

        return eleccion;
    }

    /**
     * Método que se encarga de iniciar el programa.
     * Muestra ciertas opciones, pide al usuario
     * una eleccion entre ellas y llama al método necesario para
     * proseguir el menu.
     *
     * @param sc
     */
    public void start(Scanner sc) {
        var menu = """
                =========================================
                                
                1 = insertar registro
                                
                2 = modificar registro
                                
                3 = eliminar registro
                                
                4 = listar registros de tabla
                                
                5 = obtener registro por número de identificación
                                
                0 = Salir
                                
                ========================================""";

        var salir = false;
        while (!salir) {
            System.out.println(menu);
            var eleccion = seleccionarOpcion(sc);
            switch (eleccion) {
                case 1 -> {
                    menuInsertarRegistro(sc);
                }
                case 2 -> {
                    menuModificarRegistro(sc);
                }
                case 3 -> {
                    eliminarRegistro(sc);
                }
                case 4 -> {
                    menuListarRegistrosDeTabla(sc);
                }
                case 5 -> {
                    menuObtenerRegistroPorNumeroDeIdentificacion(sc);
                }
                case 0 -> {
                    salir = true;
                }

            }
        }
    }


    /**
     * Método que se encarga de mostrar el menu para obtener un registro de una tabla.
     * Se muestran las opciones y el usuario deberá elegir introduciendo un número entre los
     * que representan a las tablas de la base de datos.
     * Después se pedirá que introduzca un nuevo entero que representará el campo id
     * de la tabla que se haya elegido en inicio del menu
     *
     * @param sc
     */
    private void menuObtenerRegistroPorNumeroDeIdentificacion(Scanner sc) {
        var menu = """
                =========================================
                                
                0 = salir
                                
                1 = buscar Profesor
                                
                2 = buscar Alumno
                                
                3 = buscar Matrícula
                                
                ========================================""";
        var salir = false;
        while (!salir) {

            System.out.println(menu);
            var eleccion = seleccionarOpcion(sc);

            boolean salirEle = false;
            while (!salirEle) {
                try {
                    switch (eleccion) {
                        case 1 -> {
                            System.out.println("Introduzca el DNI del profesor que desea buscar");
                            var dni = sc.next();
                            var prof = new Profesor();
                            if ((prof = ProfesorDAO.GetProfesorById(dni)) != null) {
                                System.out.println(prof);
                                salirEle = true;
                            } else {
                                System.out.println("No existe un profesor con ese DNI dentro de la base de datos");
                            }

                        }
                        case 2 -> {
                            System.out.println("Introduzca el DNI del alumno que desea buscar");
                            var id = sc.next();
                            if ((AlumnoDAO.GetAlumnoById(id)) != null) {
                                System.out.println(AlumnoDAO.GetAlumnoById(id));
                                salirEle = true;
                            } else {
                                System.out.println("No existe un alumno con ese DNI dentro de la base de datos");
                            }

                        }
                        case 3 -> {
                            System.out.println("Introduzca el id de la matrícula que desea buscar");
                            menuBuscarMatricula(sc);
                            var id = getEnteroMenuBuscarPorId(sc, eleccion);
                            if ((MatriculasDAO.GetMatriculaById(id)) != null) {
                                System.out.println(MatriculasDAO.GetMatriculaById(id));
                            }
                            salirEle = true;
                        }
                        case 0 -> {
                            salir = true;
                            salirEle = true;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Debe introducir un número");
                }


            }
        }
    }


    public void editMatricula(Matricula matricula, Scanner sc) {
        sc = new Scanner(System.in);
        System.out.println("Introduzca el nuevo dni del alumno");
        var id = sc.next();
        var alumno =new Alumno();
        while (!id.matches("[0-9]{8}[A-Z]")) {
            System.out.println("El DNI introducido no es válido");
            System.out.println("Introduzca el DNI del alumno"
                    + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
            id = sc.next();
            alumno = AlumnoDAO.GetAlumnoById(id);
            if(alumno.getDni().equals("00000000A")){
                System.out.println("No existe un alumno con ese DNI");
                id = "";
            }
        }
        alumno.setDni(id);
        var profesor =new Profesor();
        while (!id.matches("[0-9]{8}[A-Z]")) {
            System.out.println("El DNI introducido no es válido");
            System.out.println("Introduzca el DNI del alumno"
                    + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
            id = sc.next();
            profesor = ProfesorDAO.GetProfesorById(id);
            if(profesor.getDni().equals("00000000A")){
                System.out.println("No existe un profesor con ese DNI");
                id = "";
            }
        }
        profesor.setDni(id);
        matricula.setAsignatura(eleccionAsignatura());

        var salida = false;
        do{
            System.out.println("Introduzca el curso");
            matricula.setCurso(sc.nextInt());
            salida = (matricula.getCurso() >= 1) && (matricula.getCurso() <= 2);
        }while(!salida);

        System.out.println("Matricula editada correctamente");
    }


    private void eliminarRegistro(Scanner sc) {
        var menu = """
                =========================================
                                
                0 = salir
                                
                1 = eliminar Profesor
                                
                2 = eliminar Alumno
                                
                3 = eliminar Matrícula
                                
                ========================================""";
        var salir = false;
        while (!salir) {
            System.out.println(menu);
            salir = menuEliminar(sc.next());
        }
    }

    private boolean menuEliminar(String eleccion) {
        var salir = false;
        Scanner sc = new Scanner(System.in);
        try {
            switch (eleccion) {
                case "1" -> eliminarProfesor(sc);
                case "2" -> eliminarAlumno(sc);
                case "3" -> eliminarMatricula(sc);
                case "0" -> salir = true;
            }


        } catch (NumberFormatException e) {
            System.out.println("Debe introducir un número");

        }
        return salir;

    }

    /**
     * Método que se encarga de, dado un entero correspondiente a una de las tres tablas que se encuentran
     * en la base de datos, pide otro entero que representará el identificador de un registro que se
     * encuentre en esta.
     *
     * @param sc
     * @param eleccion
     * @return
     */
    private int getEnteroMenuBuscarPorId(Scanner sc, int eleccion) {
        var nombreTabla = "";
        var salir = false;
        while (!salir) {


            switch (eleccion) {
                case 1 -> {
                    nombreTabla = "el Profesor";
                }
                case 2 -> {
                    nombreTabla = "el Alumno";

                }
                case 3 -> {
                    nombreTabla = "la Matricula";
                }
            }
            if (eleccion >= 1 && eleccion <= 3) {
                salir = true;
            }
        }
        System.out.println("Escriba el numero de identificacion de " + nombreTabla);
        var id = seleccionarOpcion(sc);

        return id;
    }


    /**
     * Método que se encarga de pedir al usuario un número entero, haciendo que represente una de las tres
     * tablas de la base de datos
     *
     * @param sc
     */
    private void menuListarRegistrosDeTabla(Scanner sc) {
        var menu = format("""
                =========================================
                                
                0 = salir
                                
                1 = listar Profesores
                                
                2 = listar Alumnos
                                
                3 = listar Matrículas
                                
                ========================================""");

        var salir = false;

        while (!salir) {
            System.out.println(menu);
            var eleccion = seleccionarOpcion(sc);
            if (eleccion >= 0 && eleccion <= 3) {
                try {
                    switch (eleccion) {
                        case 1 -> {
                            var profesores = ProfesorDAO.GetProfesores();
                            for (Profesor prof : profesores) {
                                System.out.println(prof);
                            }
                        }
                        case 2 -> {
                            List<Alumno> alumnos = AlumnoDAO.GetALumnos();
                            for (Alumno alumno : alumnos) {
                                System.out.println(alumno);
                            }

                        }
                        case 3 -> {
                            var matriculas = MatriculasDAO.GetMatriculas();
                            for (Matricula mat :
                                    matriculas) {
                                System.out.println(mat);
                            }

                        }
                        case 0 -> {
                            salir = true;
                        }
                    }
                } catch (Exception f) {
                    System.out.println("No se pudieron recoger los datos");
                }

            } else {
                System.out.println("Debe introducir un número correspondiente a una de las elecciones posibles");
            }

        }

    }


    /**
     * Método que dados 3 Strings forma una fecha con formato yyyy-MM-dd
     *
     * @param year
     * @param month
     * @param day
     * @return una cadena que representa la fecha
     * @throws NumberFormatException
     */
    private String formarFecha(String year, String month, String day) throws NumberFormatException {
        String fecha = "";
        int _year = Integer.parseInt(year);
        if (_year >= 1960 && _year <= Date.from(Instant.ofEpochSecond(System.currentTimeMillis())).getYear()) {
            fecha += year + "-";
        }
        try {
            MonthDay.of(Month.of(Integer.parseInt(month)), Integer.parseInt(day));
            fecha += month + "-" + day;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("La fecha no es correcta");
        }
        ;

        return fecha;
    }

    private void menuModificarRegistro(Scanner sc) {
        var menu = format("""
                =========================================
                      
                0 = salir
                          
                1 = modifiar Profesor
                                
                2 = modificar Alumno
                                
                3 = modificar Matrícula
                                
                ========================================""");
        var salir = false;
        while (!salir) {
            System.out.println(menu);

            var eleccion = seleccionarOpcion(sc);
            switch (eleccion) {
                case 1 -> {
                    MongoDAL.editObject(getDatosProfesorCambiar(sc));
                }
                case 2 -> {
                    MongoDAL.editObject(getDatosAlumnoCambiar(sc));
                }
                case 3 -> {
                    MongoDAL.editObject(getDatosMatriculaCambiar(sc));
                }
                case 0 -> {
                    salir = true;
                }

            }
        }
    }


    private void menuInsertarRegistro(Scanner sc) {
        var menu = format("""
                =========================================
                                
                1 = insertar Profesor
                                
                2 = insertar Alumno
                                
                3 = insertar Matrícula
                                
                0 = Salir
                                
                ========================================""");

        var id = 0;
        var salir = false;
        while (!salir) {
            System.out.println(menu);
            var eleccion = seleccionarOpcion(sc);
            switch (eleccion) {
                case 1 -> {
                    try {
                        var profesor = getDatosProfesorNuevo(sc, null);
                        if (profesor != null) {
                            MongoDAL.insertObject(profesor);
                            System.out.println("Se insertaron los datos con éxito");
                        } else
                            throw new Exception();
                    } catch (Exception e) {
                        System.out.println("No se pudieron insertar los datos");
                    }

                    //filas = bl.insertProfesor(getDatosProfesorNuevo(sc));
                    //if(filas > 0){
                    //    System.out.println("Se insertaron los datos con éxito");
                    //    try {
                    //        bl.cantidadProfesores++;
                    //        id += bl.cantidadProfesores;
                    //        var professor = bl.getProfesorById(id);
                    //        System.out.println(professor);
                    //    } catch (ClassNotFoundException e) {
                    //        System.out.println("No se encontró el registro que buscaba");
                    //    }
                    //}
                }
                case 2 -> {
                    try {
                        MongoDAL.insertObject(getDatosAlumnoNuevo(sc, null));
                        System.out.println("Se insertaron los datos con éxito");
                    } catch (Exception e) {
                        System.out.println("No se pudieron insertar los datos");
                    }
                }

                //{
                //var datos = new String[3];
                //var lista = (getDatosAlumnoNuevo(sc));
                //for (int i = 0 ; i < lista.size(); i++) {
                //    datos[i] = lista.get(i);
                //}
                //filas = bl.insertAlumno(datos);
                //if(filas > 0){
                //    System.out.println("Se insertaron los datos con éxito");
//
                //    var alumno = bl.getAlumnoById(++bl.cantidadAlumnos);
                //    System.out.println(alumno);
//
                //}
                //}
                case 3 -> {
                    try {
                        MongoDAL.insertObject(getDatosMatriculaNuevo(sc));
                        System.out.println("Se insertaron los datos con éxito");
                    } catch (Exception e) {
                        System.out.println("No se pudieron insertar los datos");
                    }
                }
                //{
                //    filas = bl.insertMatricula(getDatosMatriculaNuevo(sc));
                //    if(filas > 0){
                //        System.out.println("Se insertaron los datos con éxito");
                //        try {
                //            bl.cantidadMatriculas++;
                //            id = bl.cantidadMatriculas;
                //            var matricula = bl.getProfesorById(id);
                //            System.out.println(matricula);
                //        } catch (ClassNotFoundException e) {
                //            System.out.println("No se encontró el registro que buscaba");
                //        }
                //    }
                //}
                case 0 -> {
                    salir = true;
                }

            }
        }

    }


/**
 * Region Utilidades
 */


/**
 * End Region Utilidades
 */
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Region Alumnos
     */


    /**
     * Método que se encarga de pedirle a la clase DAL que obtenga todos los registros de la tabla alumnos de la base de datos
     * según el nombre introducido por el usuario
     * @param sc
     */
    private void getAlumnosPorNombre(Scanner sc) {
        sc = new Scanner(System.in);
        System.out.println("Introduzca el nombre del alumno");
        var nombre = sc.nextLine();
        var alumnos = AlumnoDAO.getAlumnosPorNombre(nombre);
        if (alumnos != null) {
            for (var alumno : alumnos) {
                System.out.println(alumno);
            }
        } else {
            System.out.println("No se encontraron alumnos con ese nombre");
        }
    }

    /**
     * Se encarga de pedirle a la clase DAL que elimine un registro de la tabla alumnos de la base de datos
     *
     * <pre>se debe introducir un objeto Scanner instanciado para la recoleccion de datos</pre>
     * <post>se elimina el registro de la tabla alumnos de la base de datos</post>
     *
     * @param sc
     */
    private boolean eliminarAlumno(Scanner sc) {
        sc = new Scanner(System.in);
        boolean salir = false;
        boolean eliminado = false;
        while (!salir) {
            try {
                System.out.println("Introduzca el DNI del alumno que desea eliminar");
                var id = sc.next();
                while (!id.matches("[0-9]{8}[A-Z]")) {
                    System.out.println("El DNI introducido no es válido");
                    System.out.println("Introduzca el DNI del alumno"
                            + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
                    id = sc.next();
                }

                var alumno = AlumnoDAO.GetAlumnoById(id);
                if (alumno.getDni().equals("00000000A")) {
                    System.out.println("El alumno no existe en nuestro registro");
                    return false;
                }
                List<Alumno> alumnos = AlumnoDAO.getEntradasDeAlumno(alumno);
                var cantidad = 0;
                System.out.println("Desea eliminar un registro en específico o todos los datos de nuestro registro?");
                System.out.println("1. Eliminar un registro en específico");
                System.out.println("2. Eliminar todos los registros");
                System.out.println("0. Cancelar");
                var opcion = sc.nextInt();
                switch (opcion) {
                    //Eliminar registro específico
                    case 1 -> {
                        //Si hay más de un registro para el alumno
                        if (alumnos.size() > 0) {
                            for (Alumno a : alumnos) {
                                cantidad++;
                                System.out.println(cantidad + " para eliminar este registro \n" + a);

                            }
                            System.out.println("escriba el numero indicado para eliminar el registro correspondiente o 0 para cancelar");
                            try {
                                //Recojo el id del registro que se desea eliminar
                                var idRegistro = sc.nextInt();
                                //Compruebo que la entrada sea válida
                                if (idRegistro > (alumnos.size() - 1) || idRegistro < 0)
                                    System.out.println("No existe ningun registro con ese numero");
                                else if (idRegistro == 0) {
                                    System.out.println("cancelando eliminacion");
                                    salir = true;
                                    break;
                                    //Si la entrada es válida, elimino el registro
                                } else {
                                    eliminado = MongoDAL.deleteByString("Alumnos", alumnos.get((idRegistro - 1)).get_id());
                                    salir = true;
                                }
                                //Si la entrada no es válida, cancelo la eliminación
                            } catch (Exception e) {
                                eliminado = false;
                                System.out.println("No existe ningun registro con ese numero");
                            }

                            //Si no hay más de un registro para el alumno
                        } else {
                            boolean salir2 = false;
                            while (!salir2) {
                                //Pregunto si se desea eliminar el registro
                                System.out.println("Está seguro de que desea eliminar el alumno seleccionado?");
                                System.out.println("1. Si");
                                System.out.println("2. No");
                                var opcion2 = sc.nextInt();
                                switch (opcion2) {
                                    case 1 -> {
                                        MongoDAL.deleteByString("Alumnos", alumno.get_id());
                                        eliminado = true;
                                        salir2 = true;
                                        salir = true;
                                    }
                                    case 2 -> {
                                        System.out.println("cancelando eliminacion");
                                        salir2 = true;
                                        salir = true;
                                    }
                                    default -> System.out.println("Opcion no valida");
                                }
                            }
                        }
                    }
                    case 2 -> {

                        var salir2 = false;
                        alumnos = AlumnoDAO.getEntradasDeAlumno(alumno);
                        for (Alumno a : alumnos) {
                            System.out.println(a);
                        }
                        while (!salir2) {
                            System.out.println("Está seguro de que desea eliminar todos los registros relacionados con el alumno seleccionado?");
                            System.out.println("Recuerde que si elimina todos los registros, el alumno desaparecerá de nuestro registro");
                            System.out.println("1. Si");
                            System.out.println("2. No");
                            var opcion2 = sc.nextInt();
                            switch (opcion2) {
                                case 1 -> {
                                    //Elimino todos los registros relacionados con el alumno
                                    for (Alumno a : alumnos) {
                                        MongoDAL.deleteObject(a);
                                    }
                                    eliminado = true;
                                    salir2 = true;
                                    salir = true;
                                }
                                //Si no se desea eliminar el registro, cancelo la eliminación
                                case 2 -> {
                                    salir2 = true;
                                    salir = true;
                                    System.out.println("Cancelando eliminacion");
                                }
                            }
                        }
                    }
                    case 0 -> {
                        System.out.println("Cancelando eliminacion");
                        salir = true;
                    }

                }

            } catch (Exception e) {
                eliminado = false;
                System.out.println("No existe ningun Alumno con ese DNI en nuestro registro");
            }
        }
        return eliminado;
    }


    /**
     * Método que se encarga de recoger por teclado los datos de un nuevo Alumno
     *
     * <pre>se debe introducir un objeto Scanner instanciado para la recoleccion de datos</pre>
     * <post>se devuelve un objeto Alumno con los datos introducidos por teclado</post>
     *
     * @param sc
     * @return
     */
    private Alumno getDatosAlumnoNuevo(Scanner sc, Alumno alumnoSupp) {
        sc = new Scanner(System.in);
        Alumno datos = alumnoSupp;
        var dni = "";
        if (datos == null) {
            datos = new Alumno();
            System.out.println("introduzca el DNI del Alumno");
            //volveremos a pedir el dni del alumno mientras no se introduzca un parámetro válido
            dni = sc.nextLine();
            while (!dni.matches("[0-9]{8}[A-Z]")) {
                System.out.println("El DNI introducido no es válido");
                System.out.println("Introduzca el DNI del alumno"
                        + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
                dni = sc.nextLine();
            }

        } else {
            dni = alumnoSupp.getDni();
        }


        try {
            var alumnoExistente = AlumnoDAO.GetAlumnoById(dni);
            if (alumnoExistente != null) {
                var entrads = AlumnoDAO.getEntradasDeAlumno(alumnoExistente);
                for (var entrada : entrads
                ) {
                    System.out.println(entrada);
                }
                System.out.println("El DNI introducido ya existe en la base de datos");
                System.out.println("Esto generará un nuevo registro en la base de datos, \nlo que significa que " +
                        "se añadirá una nueva entrada a la tabla de alumnos con el mismo DNI \n" +
                        "pero diferente id de MongoDB, \npor lo que se tomará como una edición de" +
                        "los datos del alumno.");
                System.out.println("Está seguro de que desea continuar? (S/N)");
                if (sc.nextLine().equalsIgnoreCase("S"))
                    datos.setDni(dni);
                else
                    return null;
            }
            //Si no existe el alumno, pero se envió uno sin DNI, se añadirá uno nuevo
        } catch (Exception e) {
            datos.setDni(dni);
        }

        //volveremos a pedir el nombre del alumno mientras no se introduzca un parámetro válido
        var nombre = "";
        while (nombre.equals("") || nombre.matches("[0-9]+")) {
            System.out.println("introduzca el nombre del Alumno");
            nombre = sc.nextLine();
        }
        var apellidos = "";
        while (apellidos.equals("") || apellidos.matches("[0-9]+")) {
            System.out.println("introduzca los apellidos del Alumno");
            apellidos = sc.nextLine();
        }
        boolean salir = false;
        //Esto se repite en la clase Profesor así que lo mejor debió haber sido hacer
        //una clase Persona de la que heredaran ambos
        //Este comentario es para futuras versiones.
        while (!salir) {
            try {
                System.out.println("introduzca el año de nacimiento del alumno");
                var year = sc.nextLine();
                System.out.println("introduzca el mes de nacimiento del alumno");
                var month = sc.nextLine();
                System.out.println("introduzca el día de nacimiento del alumno");
                var day = sc.nextLine();
                datos.setFechaNacimiento(LocalDate.parse(formarFecha(year, month, day)));
                salir = true;
            } catch (Exception numberFormatException) {
                System.out.println("Debe introducir una fecha válida");
            }

            datos.setNombre(nombre);
            datos.setApellidos(apellidos);
        }
        return datos;
    }


    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * <pre>se debe introducir un objeto Scanner instanciado para la recoleccion de datos</pre>
     * <post>se devuelve un objeto Alumno con los datos introducidos por teclado</post>
     *
     * @param sc
     * @return
     */
    private Alumno getDatosAlumnoCambiar(Scanner sc) {
        sc = new Scanner(System.in);
        boolean salir = false;
        Alumno datosAlumno = null;
        while (!salir) {
            System.out.println("introduce el DNI del ALumno");
            try {
                var id = sc.nextLine();
                var supp = AlumnoDAO.GetAlumnoById(id);
                List<Alumno> lista = AlumnoDAO.getEntradasDeAlumno(supp);
                lista.sort(Comparator.comparing(Alumno::getCreatedAt));
                datosAlumno = lista.get(lista.size() - 1);
                datosAlumno = getDatosAlumnoNuevo(sc, supp);
                if (datosAlumno == null)
                    return null;
                datosAlumno.setDni(supp.getDni());
                datosAlumno.setCreatedAt(Timestamp.from(Instant.now()));
                salir = true;
            } catch (Exception e) {
                System.out.println("El DNI del alumno debe tener 8 números y una letra mayúscula");
            }
        }

        return datosAlumno;
    }


    /**
     * End Region Alumnos
     */

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Region Profesores
     */


    /**
     * Se encarga de pedir a la capa DAL que elimine un objeto de la base de datos
     *
     * @param sc
     */
    private void eliminarProfesor(Scanner sc) {
        boolean salir = false;
        var eliminado = false;
        while (!salir) {
            try {
                System.out.println("Introduzca el DNI del profesor que desea eliminar");
                var id = sc.next();
                while (!id.matches("[0-9]{8}[A-Z]")) {
                    System.out.println("El DNI introducido no es válido");
                    System.out.println("Introduzca el DNI del alumno"
                            + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
                    id = sc.next();
                }
                var profesor = ProfesorDAO.GetProfesorById(id);
                if (profesor.getDni().equals("00000000A")) {
                    System.out.println("No se puede eliminar el profesor por defecto");
                    return;
                }
                var profesores = ProfesorDAO.getEntradasDeProfesor(profesor);
                var cantidad = 0;
                System.out.println("Desea eliminar un registro en específico o todos los datos de nuestro registro?");
                System.out.println("1. Eliminar un registro en específico");
                System.out.println("2. Eliminar todos los registros");
                System.out.println("0. Cancelar");
                var eleccion = sc.nextInt();
                switch (eleccion) {
                    case 1:
                        if (profesores.size() > 0) {
                            for (Profesor p : profesores) {
                                cantidad++;
                                System.out.println(cantidad + " para elimunar este registro \n" + p);
                            }
                            System.out.println("escriba el numero indicado para eliminar el registro correspondiente o 0 para cancelar");
                            try {
                                var eleccionRegistro = sc.nextInt();
                                if (eleccionRegistro == 0) {
                                    System.out.println("Operación cancelada");
                                    return;
                                } else if (eleccionRegistro < 0 || eleccionRegistro > profesores.size() - 1) {
                                    System.out.println("El número introducido no es válido");
                                    return;
                                } else {
                                    MongoDAL.deleteByString("Profesores", profesores.get(eleccionRegistro - 1).get_id());
                                    salir = true;
                                }
                            } catch (Exception e) {
                                System.out.println("El número introducido no es válido");
                                return;
                            }
                        } else {
                            boolean salir2 = false;
                            while (!salir2) {
                                System.out.println("No hay registros de este profesor, ¿desea eliminarlo de todos modos?");
                                System.out.println("1. Sí");
                                System.out.println("2. No");
                                var eleccion2 = sc.nextInt();
                                switch (eleccion2) {
                                    case 1:
                                        MongoDAL.deleteByString("Profesores", profesor.get_id());
                                        salir2 = true;
                                        salir = true;
                                        break;
                                    case 2:
                                        System.out.println("Operación cancelada");
                                        salir2 = true;
                                        salir = true;
                                        break;
                                    default:
                                        System.out.println("El número introducido no es válido");
                                        break;
                                }
                            }
                        }

                        break;
                    case 2:

                        var salir2 = false;
                        profesores = ProfesorDAO.getEntradasDeProfesor(profesor);
                        for (Profesor p : profesores) {
                            System.out.println(p);
                        }
                        while(!salir2){
                            System.out.println("Está seguro de que desea eliminar todos los registros relacionados con el alumno seleccionado?");
                            System.out.println("Recuerde que si elimina todos los registros, el alumno desaparecerá de nuestro registro");
                            System.out.println("1. Si");
                            System.out.println("2. No");
                            var opcion2 = sc.nextInt();
                            switch (opcion2){
                                case 1:
                                    for (Profesor p : profesores) {
                                        MongoDAL.deleteByString("Profesores", p.get_id());
                                    }
                                    salir2 = true;
                                    salir = true;
                                    break;
                                case 2:
                                    System.out.println("Operación cancelada");
                                    salir2 = true;
                                    salir = true;
                                    break;
                                default:
                                    System.out.println("El número introducido no es válido");
                                    break;
                            }
                        }
                }

            } catch (Exception e) {
                System.out.println("El DNI introducido no existe en la base de datos");
            }
        }

    }


    /**
     * Método que se encarga de recoger por teclado los datos de un nuevo Profesor
     * En caso de que este ya exista en la base de datos, se informará al usuario
     * y se pedirá que acepte o no la modificación de los datos de dicho profesor.
     * <p>
     * Esto quiere decir que, si en la opción 1 del menú principal se elige la opción
     * de añadir un nuevo profesor, y este ya existe en la base de datos, se le preguntará
     * al usuario si desea modificar los datos del profesor existente.
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private Profesor getDatosProfesorNuevo(Scanner sc, Profesor profesorSupp) {
        sc = new Scanner(System.in);
        var dni = "";
        Profesor profesor = null;
        profesor = profesorSupp;
        if (profesorSupp == null) {
            profesor = new Profesor();
            System.out.println("Introduzca el DNI del profesor"
                    + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
            //compruebo que el dni introducido tiene 8 cifras y una letra mayúscula
            dni = sc.nextLine();
            while (!dni.matches("[0-9]{8}[A-Z]")) {
                System.out.println("El DNI introducido no es válido");
                System.out.println("Introduzca el DNI del profesor"
                        + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
                dni = sc.nextLine();
            }

        }

        try {
            //compruebo que el dni no existe en la base de datos, si es así, muestro las entradas del profesor
            //y pregunto si desea añadir una entrada editada de dicho profesor.
            var profesorExistente = ProfesorDAO.GetProfesorById(dni);
            if (profesorExistente != null) {
                System.out.println(ProfesorDAO.getEntradasDeProfesor(profesorExistente));
                System.out.println("El DNI introducido ya existe en la base de datos");
                System.out.println("Esto generará un nuevo registro en la base de datos, \n lo que significa que " +
                        "se añadirá una nueva entrada a la tabla de profesores con el mismo DNI \n" +
                        "pero diferente id de MongoDB,\npor lo que se tomará como una edición de\" +\n" +
                        "\"los datos del Profesor.");
                System.out.println("Está seguro de que desea continuar? (S/N)");
                if (sc.nextLine().toUpperCase().equals("S"))
                    profesor.setDni(dni);
                else
                    return null;
            }
        } catch (Exception e) {
            profesor.setDni(dni);
        }

        //Recojo el nombre del profesor y los apellidos
        var nombre = "";
        while (nombre.equals("") || nombre.matches("[0-9]+")) {
            System.out.println("introduzca el nombre del Profesor");
            nombre = sc.nextLine();
        }
        var apellidos = "";
        while (apellidos.equals("") || apellidos.matches("[0-9]+")) {
            System.out.println("introduzca los apellidos del Profesor");
            apellidos = sc.nextLine();
        }
        var salir = false;
        //Mientras que la fecha de nacimiento no sea válida, se seguirá pidiendo al usuario
        while (!salir) {
            try {
                //Recojo el año de nacimiento del profesor
                System.out.println("introduzca el año de nacimiento del profesor");
                var year = sc.nextLine();
                //Recojo el mes de nacimiento del profesor
                System.out.println("introduzca el mes de nacimiento del profesor");
                var month = sc.nextLine();
                //Recojo el día de nacimiento del profesor
                System.out.println("introduzca el día de nacimiento del profesor");
                var day = sc.nextLine();
                //Formateo la fecha y la asigno al profesor
                profesor.setFechaNacimiento(LocalDate.parse(formarFecha(year, month, day)));

                System.out.println("introduzca los años de antigüedad en la docencia");
                //Compruebo que la antigüedad sea un número entero
                //y mayor que 0
                boolean isNumeroBien = false;
                while (!isNumeroBien) {
                    var antiguedad = Integer.parseInt(sc.nextLine());
                    if (antiguedad < 0) {
                        System.out.println("La antigüedad debe ser mayor que 0");
                    } else {
                        profesor.setAntiguedad(antiguedad);
                        isNumeroBien = true;
                    }
                }

                salir = true;
            } catch (Exception e) {
                System.out.println("Algo salió mal, vuelva a introducir la fecha..");
            }
            profesor.setNombre(nombre);
            profesor.setApellidos(apellidos);
        }
        return profesor;
    }


    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private Profesor getDatosProfesorCambiar(Scanner sc) {
        Profesor _datos = new Profesor();
        boolean salir = false;
        //volveremos a pedir el id del profesor mientras no se introduzca un numero entero
        while (!salir) {
            try {
                System.out.println("introduce el DNI del Profesor");
                var id = sc.next();
                var supp = ProfesorDAO.GetProfesorById(id);//Comprobamos que el profesor existe
                List<Profesor> lista = ProfesorDAO.getEntradasDeProfesor(supp);
                lista.sort(Comparator.comparing(Profesor::getCreatedAt));
                //obtenemos el registro creado mas recientemente
                _datos = lista.get(lista.size() - 1);
                //obtenemos los datos del profesor

                _datos = getDatosProfesorNuevo(sc, _datos);
                if (_datos == null)
                    return null;
                _datos.setDni(supp.getDni());
                _datos.setCreatedAt(Timestamp.from(Instant.now()));
                salir = true;
            } catch (NumberFormatException e) {
                System.out.println("El id del profesor debe ser un numero entero");
            }
        }
        return _datos;
    }


    /**
     * End region Profesores
     */

//////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Region Matriculas
     */


    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private Matricula getDatosMatriculaCambiar(Scanner sc) {
        Matricula _datos = new Matricula();

        System.out.println("introduce el número de identificación de la Matricula");
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(MatriculasDAO.GetMatriculaById(id));
            var datosMatricula = getDatosMatriculaNuevo(sc);
            _datos.setDniAlumno(datosMatricula.getDniAlumno());
            _datos.setDniProfesor(datosMatricula.getDniProfesor());
            _datos.setCurso(datosMatricula.getCurso());
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return _datos;
    }


    /**
     * Se encarga de pedir a la capa DAL que elimine un registro de la tabla mat4riculas de la base de datos.
     *
     * @param sc
     */
    private void eliminarMatricula(Scanner sc) {
        boolean salir = false;

        while (!salir) {
            try {
                var id = Integer.parseInt(sc.next());
                var matricula = MatriculasDAO.GetMatriculaById(id);
                MongoDAL.deleteObject(matricula);
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número");
            }
        }
    }


    private void buscarMatriculaDNIProfesor(Scanner sc) {
        var menu = """
                =========================================
                                
                0 = salir
                                
                1 = Editar Matricula
                                
                2 = Eliminar matrícula
                                
                ========================================""";
        boolean salir = false;
        while (!salir) {

            System.out.println("Escribe el DNI del profesor sobre el que quiere rescatar llas matriculas.");
            var dni = sc.next();
            var contador = 0;
            Profesor prof = null;
            if ((prof = ProfesorDAO.GetProfesorById(dni)) != null) {
                List<Matricula> matriculas = MatriculasDAO.GetMatriculasProfesor(dni);
                for (Matricula mat : matriculas) {
                    contador++;
                    System.out.println(contador + " para eliminar este registro\n" +mat);
                }

                System.out.println("escriba un número para elegir una matricula o 0 para salir");
                var eleccion = sc.next();
                System.out.println("Desea realizar alguna acción con esta matricula?");
                System.out.println("1 = Editar Matricula");
                System.out.println("2 = Eliminar matrícula");
                System.out.println("0 = Salir");
                System.out.println(menu);
                var eleccion2 = sc.next();
                var salir3 = false;
                while (!salir3) {
                    switch (eleccion2) {
                        case "1" -> editMatricula(matriculas.get(Integer.parseInt(eleccion) - 1), sc);
                        case "2" -> eliminarMatricula(sc);
                        case "0" -> salir3 = true;
                    }
                }
                menuBuscarMatricula(sc);
            }
        }
    }


    private void menuBuscarMatricula(Scanner sc) {
        var menu = """
                =========================================
                 
                              Matrículas
                 
                =========================================\s
                             
                0 = salir
                                
                1 = Buscar por DNI de Profesor
                                
                2 = Buscar por DNI de Alumno
                                
                3 = Buscar por ID de Matrícula
                                
                ========================================""";


        var salir = false;
        while (!salir) {
            System.out.println(menu);
            var eleccion = seleccionarOpcion(sc);
            switch (eleccion) {
                case 1 -> buscarMatriculaDNIProfesor(sc);
            }
        }
    }


    /**
     * Se encargan de recoger la entrada por teclado del usuario y convertirla en un String correspondiente a las
     * asignaturas que se imparten en el centro.
     *
     * @return
     */
    private String eleccionAsignatura() {

        var r = "PROG|BBDD|DEINT|FOL|EIEMP|ACDAT|PMDMO|PSPRO";
        var real = r.split("|");
        var sc = new Scanner(System.in);
        var asignatura = "0";
        while (!asignatura.matches("[1-9]")) {
            asignatura = sc.nextLine();
            var menu = """
                    =========================================
                     
                      Introduzca el digito correspondiente 
                                a la asignatura
                     
                    =========================================               
                             
                    1 = Programación
                                    
                    2 = Bases de datos
                                    
                    3 = Diseño de Interfaces
                                    
                    4 = Formación y orientación laboral
                                    
                    5 = Empresa e iniciativa emprendedora
                                    
                    6 = Acceso a datos
                                    
                    7 = Programación multimedia y dispositivos móviles
                                    
                    8 = Programación de servicios y procesos
                                    
                    ========================================""";
            System.out.println(menu);
            switch (asignatura) {
                case "1" -> asignatura = real[0];
                case "2" -> asignatura = real[1];
                case "3" -> asignatura = real[2];
                case "4" -> asignatura = real[3];
                case "5" -> asignatura = real[4];
                case "6" -> asignatura = real[5];
                case "7" -> asignatura = real[6];
                case "8" -> asignatura = real[7];
                case "9" -> asignatura = real[8];
                default -> {
                    System.out.println("La asignatura introducida no es válida");
                    asignatura = "0";
                }
            }
        }
        return asignatura;
    }


    /**
     * Método que se encarga de recoger por teclado los datos de un nuevo Profesor
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private Matricula getDatosMatriculaNuevo(Scanner sc) {
        sc = new Scanner(System.in);
        Matricula datos = new Matricula();

        boolean salir = false;
        //volveremos a pedir el dni del profesor mientras no se introduzca un
        while (!salir) {
            try {
                System.out.println("introduzca el DNI del Profesor que imparte la asignatura");
                var dni = sc.nextLine();
                while (!dni.matches("[0-9]{8}[A-Z]")) {
                    System.out.println("El DNI introducido no es válido");
                    System.out.println("Introduzca el DNI del profesor"
                            + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
                    dni = sc.nextLine();
                }

                ProfesorDAO.GetProfesorById(dni);//Comprobamos que el profesor existe
                datos.setDniProfesor(dni);
                salir = true;
            } catch (Exception e) {
                salir = false;
                System.out.println("El profesor no existe en nuestro registro");
            }
        }
        salir = false;
        System.out.println("introduzca el DNI del Alumno");
        //volveremos a pedir el dni del alumno mientras no se introduzca un parámetro válido
        while (!salir) {
            try {
                var dni = sc.nextLine();
                while (!dni.matches("[0-9]{8}[A-Z]")) {
                    System.out.println("El DNI introducido no es válido");
                    System.out.println("Introduzca el DNI del alumno"
                            + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
                    dni = sc.nextLine();
                }
                AlumnoDAO.GetAlumnoById(dni);//Comprobamos que el alumno existe

                datos.setDniAlumno(dni);
                salir = true;
            } catch (Exception e) {
                salir = false;
                System.out.println("El alumno no existe en nuestro registro");
            }
        }
        datos.setAsignatura(eleccionAsignatura());
        System.out.println("introduzca el numero del curso");
        salir = false;
        //volveremos a pedir el curso mientras no se introduzca un numero entero
        while (!salir) {
            try {
                var curso = Integer.parseInt(sc.next());
                datos.setCurso(curso);
                salir = true;
            } catch (NumberFormatException e) {
                System.out.println("El curso debe ser un numero entero");
            }
        }

        return datos;
    }


    /**
     * End Region Matriculas
     */


}
