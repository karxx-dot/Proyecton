
package Union;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.modelo.Cliente;

public class ClienteDAO {

    public boolean guardar(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes (cedula, nombre, telefono, email) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCedula());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }
    }

    public boolean actualizar(String cedulaOriginal, Cliente c) throws SQLException {
        String sql = "UPDATE clientes SET cedula=?, nombre=?, telefono=?, email=? WHERE cedula=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCedula());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.setString(4, c.getEmail());
            ps.setString(5, cedulaOriginal);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String cedula) throws SQLException {
        String sql = "DELETE FROM clientes WHERE cedula = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT idClientes, cedula, nombre, telefono, email FROM clientes ORDER BY nombre";
        List<Cliente> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Cliente> buscar(String texto) throws SQLException {
        String sql = "SELECT idClientes, cedula, nombre, telefono, email FROM clientes " +
                     "WHERE cedula LIKE ? OR nombre LIKE ? ORDER BY nombre";
        List<Cliente> lista = new ArrayList<>();
        String patron = "%" + texto + "%";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("idClientes"),
            rs.getString("cedula"),
            rs.getString("nombre"),
            rs.getString("telefono"),
            rs.getString("email")
        );
    }
}