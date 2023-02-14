package UI;

import DAL.BusinessLogic;

import java.time.Instant;
import java.time.Month;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

public class menu {
    private BusinessLogic bl;

    //Método que lanza el inicio del menú
    public void iniciarMenu(Scanner sc) {
        bl = new BusinessLogic();
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
            try {


                switch (eleccion) {
                    case 1 -> {
                        var id = getEnteroMenuBuscarPorId(sc, eleccion);
                       if(( bl.getProfesorById(id)) != null) {
                            System.out.println(bl.getProfesorById(id));
                        }


                    }
                    case 2 -> {
                        var id = getEnteroMenuBuscarPorId(sc, eleccion);
                        if(( bl.getAlumnoById(id)) != null) {
                            System.out.println(bl.getAlumnoById(id));
                        }

                    }
                    case 3 -> {
                        var id = getEnteroMenuBuscarPorId(sc, eleccion);
                        if(( bl.getMatriculaById(id)) != null) {
                            System.out.println(bl.getMatriculaById(id));
                        }
                    }
                    case 0 -> {
                        salir = true;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe introducir un número");
            } catch (ClassNotFoundException s) {
                System.out.println("No se pudo obtener ningun dato");
            }

        }

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
                            bl.listarTabla("Profesores");
                        }
                        case 2 -> {
                            bl.listarTabla("Alumnos");
                        }
                        case 3 -> {
                            bl.listarTabla("Matriculas");

                        }
                        case 0 -> {
                            salir = true;
                        }
                    }
                } catch (ClassNotFoundException f) {
                    System.out.println("No se pudieron recoger los datos");
                }

            } else {
                System.out.println("Debe introducir un número correspondiente a una de las elecciones posibles");
            }

        }

    }

    private void eliminarRegistro(Scanner sc) {
        System.out.println("inserta el nombre de la tabla en la que se encuentra el registro que desea eliminar");
        var nombreTabla = sc.next();

        if (bl.gestion.testIfExists(nombreTabla)) {
            try {
                System.out.println("inserta el número de identificacion del registro que desea eliminar");
                int id = Integer.parseInt(sc.next());
                bl.gestion.delete(nombreTabla, id);
                System.out.println("Se eliminó el registro con número de identificación "+id);
            } catch (NumberFormatException e) {
                System.out.println("El identificador debe ser un numero entero, por lo ue no se eliminó ningun registro de la base de datos");
            }

        }

    }

    private String formarFecha(String year, String month, String day) throws NumberFormatException{
        String fecha = "";
        int _year = Integer.parseInt(year);
        if(_year >= 1960 && _year <= Date.from(Instant.ofEpochSecond(System.currentTimeMillis())).getYear()){
            fecha+=year+"-";
        }
       try{
           MonthDay.of(Month.of(Integer.parseInt(month)), Integer.parseInt(day));
           fecha+=month+"-"+day;
       }
       catch(ArrayIndexOutOfBoundsException e){
           System.out.println("La fecha no es correcta");
       };

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
        while(!salir) {
            System.out.println(menu);

            var eleccion = seleccionarOpcion(sc);
            switch (eleccion) {
                case 1 -> {
                    if (bl.editProfesor(getDatosProfesorCambiar(sc))) {
                        System.out.println("Se modificó el Profesor con éxito");
                    }
                    ;
                }
                case 2 -> {
                    if (bl.editAlumno(getDatosAlumnoCambiar(sc))) {
                        System.out.println("Se modificó el registro del Alumno");
                    }
                }
                case 3 -> {
                    if (bl.editMatricula(getDatosMatriculaCambiar(sc))) {
                        System.out.println("Se modificó la matrícula con éxito");
                    }
                    ;
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
        while(!salir){
            System.out.println(menu);
            var filas = 0;
            var eleccion = seleccionarOpcion(sc);
            switch (eleccion) {
                case 1 -> {
                    MongoDAL.
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

                    var datos = new String[3];
                    var lista = (getDatosAlumnoNuevo(sc));
                    for (int i = 0 ; i < lista.size(); i++) {
                        datos[i] = lista.get(i);
                    }
                    filas = bl.insertAlumno(datos);
                    if(filas > 0){
                        System.out.println("Se insertaron los datos con éxito");

                        var alumno = bl.getAlumnoById(++bl.cantidadAlumnos);
                        System.out.println(alumno);

                    }
                }
                case 3 -> {
                    filas = bl.insertMatricula(getDatosMatriculaNuevo(sc));
                    if(filas > 0){
                        System.out.println("Se insertaron los datos con éxito");
                        try {
                            bl.cantidadMatriculas++;
                            id = bl.cantidadMatriculas;
                            var matricula = bl.getProfesorById(id);
                            System.out.println(matricula);
                        } catch (ClassNotFoundException e) {
                            System.out.println("No se encontró el registro que buscaba");
                        }
                    }
                }
                case 0 -> {
                    salir =true;
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
    private String[] getDatosMatriculaNuevo(Scanner sc) {
        sc = new Scanner(System.in);
        String[] datos = new String[4];
        System.out.println("introduzca el id del Profesor que imparte la asignatura");
        datos[0] = sc.nextLine();
        System.out.println("introduzca el id del Alumno");
        datos[1] = sc.nextLine();
        System.out.println("introduzca el nombre de la asignatura");
        datos[2] = sc.nextLine();
        System.out.println("introduzca el numero del curso");
        datos[3] = sc.nextLine();

        return datos;
    }


    /**
     * Método que se encarga de recoger por teclado los datos de un nuevo Alumno
     *
     * <pre></pre>
     *
     * @param sc
     * @return
     */
    private List<String> getDatosAlumnoNuevo(Scanner sc) {
        sc = new Scanner(System.in);
        List<String> datos = new ArrayList<>();

        System.out.println("introduzca el nombre del Alumno");
        datos.add(sc.nextLine());
        System.out.println("introduzca los apellidos del Alumno");
        datos.add(sc.nextLine());
        try {
            System.out.println("introduzca el año de nacimiento del alumno");
            var year = sc.nextLine();
            System.out.println("introduzca el mes de nacimiento del alumno");
            var month = sc.nextLine();
            System.out.println("introduzca el día de nacimiento del alumno");
            var day = sc.nextLine();
            datos.add(formarFecha(year, month, day));
        }catch(Exception e){
            System.out.println("Debe introducir una fecha válida");
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
    private String[] getDatosProfesorNuevo(Scanner sc) {
        sc.nextLine();
        String[] datos = new String[4];
        System.out.println("introduzca el nombre del Profesor");
        datos[0] = sc.nextLine();
        System.out.println("introduzca los apellidos del Profesor");
        datos[1] = sc.nextLine();
        var salir = false;
        while(!salir) {
            try {
                System.out.println("introduzca el año de nacimiento del profesor");
                var year = sc.nextLine();
                System.out.println("introduzca el mes de nacimiento del profesor");
                var month = sc.nextLine();
                System.out.println("introduzca el día de nacimiento del profesor");
                var day = sc.nextLine();
                datos[2] = formarFecha(year, month, day);

                System.out.println("introduzca los años de antigüedad en la docencia");
                datos[3] = sc.nextLine();
                salir = true;
            } catch (Exception e) {
                System.out.println("Algo salió mal, vuelva a introducir la fecha..");
            }

        }
        return datos;
    }


    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private String[] getDatosProfesorCambiar(Scanner sc) {
        List<String> _datos = new ArrayList<>();

        System.out.println("introduce el número de identificación del Profesor");
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(bl.getProfesorById(id));
            var datosProfesor = getDatosProfesorNuevo(sc);
            for (String dato : datosProfesor) {
                _datos.add(dato);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return (String[]) _datos.toArray();
    }

    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private String[] getDatosAlumnoCambiar(Scanner sc) {
        sc = new Scanner(System.in);
        String[] datos = new  String[4];

        System.out.println("introduce el número de identificación del ALumno");
        var datosAlumno = new String[4];
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(bl.getAlumnoById(id));

            var lista = new ArrayList<String>();
            lista.add(String.valueOf(id));
            lista.addAll(getDatosAlumnoNuevo(sc));


            for (int i  = 0 ; i < lista.size();i++) {
                datosAlumno[i] = lista.get(i);
            }
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return datosAlumno;
    }

    /**
     * Método que se encarga de recoger el identificador del Profesor
     *
     * @param sc
     * @return
     */
    private String[] getDatosMatriculaCambiar(Scanner sc) {
        List<String> _datos = new ArrayList<>();

        System.out.println("introduce el número de identificación del Profesor");
        try {
            var id = Integer.parseInt(sc.nextLine());
            System.out.println(bl.getMatriculaById(id));
            var datosMatricula = getDatosMatriculaNuevo(sc);
            for (String dato : datosMatricula) {
                _datos.add(dato);
            }
        } catch (NumberFormatException e) {
            System.out.println("El identificador del Profesor debe ser un numero entero");
        }
        return (String[]) _datos.toArray();
    }

}
