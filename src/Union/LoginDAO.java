package Union;

import java.sql.*;
import javaapplication1.modelo.Usuario;

public class LoginDAO {

    // Método principal para validar el acceso
    public boolean validar(String user, String pass, String tipo) throws SQLException {
        // Consulta exacta para los tres campos del formulario
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND password = ? AND tipo_usuario = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, tipo);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Si hay resultado, las credenciales son válidas
            }
        }
    }

    // Método por si quieres usar el botón "REGISTRARSE"
    public boolean registrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuarios (usuario, password, tipo_usuario) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getTipo());
            
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // El usuario ya existe
        }
    }
}