package Union;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String HOST     = "localhost";
    private static final String PUERTO   = "3306";
    private static final String BD       = "usertare";
    private static final String USUARIO  = "root"; 
    private static final String PASSWORD = "Koorui24"; 

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BD
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    // Cada llamada abre una conexión nueva fresca
    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado.", e);
        }
    }
}
