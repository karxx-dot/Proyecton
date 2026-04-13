package Union;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion{

    private static final String HOST     = "localhost";
    private static final String PUERTO   = "3306";
    private static final String BD       = "usertare"; 
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "Koorui24";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BD
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static Connection instancia = null;

    private Conexion() {}

    public static Connection getConexion() throws SQLException {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "Driver MySQL no encontrado. Agrega mysql-connector-j al classpath.", e);
        }
        return instancia;
    }

    public static void cerrar() {
        if (instancia != null) {
            try {
                if (!instancia.isClosed()) instancia.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                instancia = null;
            }
        }
    }
}
