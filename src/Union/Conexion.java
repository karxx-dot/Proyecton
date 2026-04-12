package Union;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/usertare";
    private static final String USER = "root";
    private static final String PASSWORD = "Koorui24";

    public static Connection getConexion() {
    Connection con = null;
    try {
        // Asegúrate de que la URL, usuario y clave sean correctos
        String url = "jdbc:mysql://localhost:3306/tu_base_de_datos"; 
        con = DriverManager.getConnection(url, "root", "tu_password");
    } catch (SQLException e) {
        // ESTO TE DIRÁ POR QUÉ "CON" ES NULL
        System.err.println("¡ERROR DE CONEXIÓN!: " + e.getMessage());
    }
    return con;
}
}
