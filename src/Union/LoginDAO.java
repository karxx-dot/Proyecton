package Union;

import java.sql.*;
import javaapplication1.modelo.Usuario;

public class LoginDAO {

    public boolean validar(String user, String pass, String tipo) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE nombreUsuario=? AND password=? AND rol=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean registrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nombreReal, nombreUsuario, password, rol) VALUES (?,?,?,?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombreReal());
            ps.setString(2, u.getNombreUsuario());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }
    }
}