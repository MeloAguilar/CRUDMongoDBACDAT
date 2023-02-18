package UI;

import DAL.Mongo.DAO.AlumnoDAO;
import DAL.Mongo.DAO.MatriculasDAO;
import DAL.Mongo.DAO.ProfesorDAO;
import DAL.Mongo.MongoDAL;
import EntidadesPersistencia.Alumno;
import EntidadesPersistencia.Matricula;
import EntidadesPersistencia.Profesor;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
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
                            var id = sc.next();
                            if ((AlumnoDAO.GetAlumnoById(id)) != null) {
                                System.out.println(AlumnoDAO.GetAlumnoById(id));
                                salirEle = true;
                            } else {
                                System.out.println("No existe un alumno con ese DNI dentro de la base de datos");
                            }

                        }
                        case 3 -> {
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
            Profesor prof = null;
            if ((prof = ProfesorDAO.GetProfesorById(dni)) != null) {
                List<Matricula> matriculas = MongoDAL.GetMatriculasProfesor(dni);
                for (Matricula mat : matriculas) {
                    System.out.println(mat);
                }
                System.out.println("Desea realizar alguna acción con estas matriculas?");
                System.out.println(menu);
                var eleccion = sc.next();

                switch (eleccion) {
                    case "1" -> editMatricula();
                    case "2" -> eliminarMatricula(sc);
                }
                menuBuscarMatricula(sc);
            }
        }
    }

    public void editMatricula() {

    }


    private void menuBuscarMatricula(Scanner sc) {
        var menu = """
                =========================================
                 
                              Matrículas
                 
                =========================================               
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
            salir = menuEliminar(sc.next());
            System.out.println(menu);

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
     * Se encarga de pedir a la capa DAL que elimine un objeto de la base de datos
     *
     * @param sc
     */
    private void eliminarProfesor(Scanner sc) {
        boolean salir = false;

        while (!salir) {
            try {
                System.out.println("Escribe el dni del profesor que deseas eliminar");
                var id = sc.next();
                var profesor = ProfesorDAO.GetProfesorById(id);
                MongoDAL.deleteObject(profesor);
                salir = true;
            } catch (Exception e) {
                System.out.println("No existe ningun registro con ese DNI");
            }
        }
    }


    /**
     * Se encarga de pedirle a la clase DAL que elimine un registro de la tabla alumnos de la base de datos
     *
     * @param sc
     */
    private void eliminarAlumno(Scanner sc) {
        boolean salir = false;

        while (!salir) {
            try {
                var id = sc.next();
                var alumno = AlumnoDAO.GetAlumnoById(id);
                MongoDAL.deleteObject(alumno);
            } catch (Exception e) {
                System.out.println("No existe ningun Alumno con ese DNI en nuestro registro");
            }
        }
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
                        MongoDAL.insertObject(getDatosProfesorNuevo(sc));
                        System.out.println("Se insertaron los datos con éxito");
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
                        MongoDAL.insertObject(getDatosAlumnoNuevo(sc));
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
        System.out.println("introduzca el nombre de la asignatura");
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


    private String eleccionAsignatura(){

        var r ="PROG|BBDD|DEINT|FOL|EIEMP|ACDAT|PMDMO|PSPRO";
        var real = r.split("|");
        var sc = new Scanner(System.in);
        var asignatura = "0";
        while (!asignatura.matches("[1-9]")){
            asignatura = sc.nextLine();

            System.out.println("Introduzca el nombre de la asignatura");
            System.out.println("1. Programación");
            System.out.println("2. Bases de datos");
            System.out.println("3. Diseño de interfaces");
            System.out.println("4. Empresa");
            System.out.println("5. Formación y orientación laboral");
            System.out.println("6. Empresa e iniciativa emprendedora");
            System.out.println("7. Acceso a datos");
            System.out.println("8. Programación multimedia y dispositivos móviles");
            System.out.println("9. Programación de servicios y procesos");
            switch (asignatura){
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
     * Método que se encarga de recoger por teclado los datos de un nuevo Alumno
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private Alumno getDatosAlumnoNuevo(Scanner sc) {
        sc = new Scanner(System.in);
        Alumno datos = new Alumno();

        System.out.println("introduzca el DNI del Alumno" +
                "(debe ser un número de 8 cifras seguido de una letra mayúscula)");
        var dni = sc.nextLine();
        while (!dni.matches("[0-9]{8}[A-Z]")) {
            System.out.println("El DNI introducido no es válido");
            System.out.println("Introduzca el DNI del profesor"
                    + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
            dni = sc.nextLine();
        }
        datos.setDni(dni);
        System.out.println("introduzca el nombre del Alumno");
        datos.setNombre(sc.nextLine());
        System.out.println("introduzca los apellidos del Alumno");
        datos.setApellidos(sc.nextLine());
        boolean salir = false;
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
        }
        return datos;
    }


    /**
     * Método que se encarga de recoger por teclado los datos de una nueva Matricula
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private Profesor getDatosProfesorNuevo(Scanner sc) {
        sc.nextLine();
        Profesor profesor = new Profesor();
        System.out.println("Introduzca el DNI del profesor"
                + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
        //compruebo que el dni introducido tiene 8 cifras y una letra mayúscula
        var dni = sc.nextLine();
        while (!dni.matches("[0-9]{8}[A-Z]")) {
            System.out.println("El DNI introducido no es válido");
            System.out.println("Introduzca el DNI del profesor"
                    + " (debe ser un número de 8 cifras junto con una letra mayúscula)");
            dni = sc.nextLine();
        }
        try {
            //compruebo que el dni no existe en la base de datos
            var profesorExistente = ProfesorDAO.GetProfesorById(dni);
            if (profesorExistente != null)
                System.out.println("El DNI introducido ya existe en la base de datos");
        } catch (Exception e) {
            profesor.setDni(dni);
        }


        System.out.println("introduzca el nombre del Profesor");
        profesor.setNombre(sc.nextLine());
        System.out.println("introduzca los apellidos del Profesor");
        profesor.setApellidos(sc.nextLine());
        var salir = false;
        //Recojo la fecha de nacimiento del profesor
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

                _datos = getDatosProfesorNuevo(sc);
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
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private Alumno getDatosAlumnoCambiar(Scanner sc) {
        sc = new Scanner(System.in);

        System.out.println("introduce el número de identificación del ALumno");
        var datosAlumno = new Alumno();
        try {
            var id = sc.nextLine();
            System.out.println(AlumnoDAO.GetAlumnoById(id));

            datosAlumno = getDatosAlumnoNuevo(sc);
            datosAlumno.setDni(id);
        } catch (NumberFormatException e) {
            System.out.println("El identificador del ALumno debe ser un numero entero");
        }
        return datosAlumno;
    }


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

}
