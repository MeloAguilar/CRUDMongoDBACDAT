package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MiConexion {

    private String name;
    private String password;
    private String user;

    private Connection conexion;


    public Connection getConexion() {
        return conexion;
    }

    public MiConexion(String name, String password, String user) {
        this.name = name;
        this.password = password;
        this.user = user;
    }

    /**
     * Método que funciona como opener de conexiones de bases de datos
     * <p>
     * Se encarga de recoger la cadena de conexion y convertirla en un
     * objeto Connection
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection abrirConexion() throws ClassNotFoundException, SQLException {
        //Referencia al driver jdbc de mysql
        Class.forName("com.mysql.cj.jdbc.Driver");
        //Instanciamos la cadena de conexión
        this.conexion = DriverManager.getConnection(name, user, password);
        return conexion;
    }


    /**
     * Método que se encarga de cerrar la conexion de la clase
     */
    public void cerrarConexion() {
        try {

            if (!this.conexion.isClosed()) {
                this.conexion.close();
            }
            ;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
