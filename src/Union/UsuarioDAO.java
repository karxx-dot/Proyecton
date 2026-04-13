package Union;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.modelo.Usuario;

public class UsuarioDAO {

    public boolean guardar(Usuario u) throws SQLException {
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

    public boolean actualizar(int id, Usuario u) throws SQLException {
        String sql = "UPDATE usuario SET nombreReal=?, nombreUsuario=?, password=?, rol=? WHERE idUsuario=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombreReal());
            ps.setString(2, u.getNombreUsuario());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRol());
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE idUsuario=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT idUsuario, nombreReal, nombreUsuario, password, rol FROM usuario ORDER BY nombreUsuario";
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("idUsuario"),
                    rs.getString("nombreReal"),
                    rs.getString("nombreUsuario"),
                    rs.getString("password"),
                    rs.getString("rol")
                });
            }
        }
        return lista;
    }

    public boolean validarLogin(String nombreUsuario, String password) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE nombreUsuario=? AND password=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<Object[]> buscar(String texto) throws SQLException {
        String sql = "SELECT idUsuario, nombreReal, nombreUsuario, password, rol FROM usuario " +
                     "WHERE nombreUsuario LIKE ? OR rol LIKE ? ORDER BY nombreUsuario";
        List<Object[]> lista = new ArrayList<>();
        String patron = "%" + texto + "%";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getInt("idUsuario"),
                        rs.getString("nombreReal"),
                        rs.getString("nombreUsuario"),
                        rs.getString("password"),
                        rs.getString("rol")
                    });
                }
            }
        }
        return lista;
    }
}