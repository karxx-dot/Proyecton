package Union;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.modelo.Prestamo;

public class PrestamoDAO {

    public boolean guardar(int idCliente, Prestamo p) throws SQLException {
        String sql = "INSERT INTO prestamos (idClientes, monto, tasa, plazo, fecha_inicio) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ps.setDouble(2, p.getMonto());
            ps.setDouble(3, p.getTasaInteresAnual());
            ps.setInt(4, p.getNumeroCuotas());
            ps.setDate(5, Date.valueOf(p.getFechaInicio()));
            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizar(int idPrestamo, Prestamo p) throws SQLException {
        String sql = "UPDATE prestamos SET monto=?, tasa=?, plazo=?, fecha_inicio=? WHERE idprestamos=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, p.getMonto());
            ps.setDouble(2, p.getTasaInteresAnual());
            ps.setInt(3, p.getNumeroCuotas());
            ps.setDate(4, Date.valueOf(p.getFechaInicio()));
            ps.setInt(5, idPrestamo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idPrestamo) throws SQLException {
        String sql = "DELETE FROM prestamos WHERE idprestamos=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT p.idprestamos, c.nombre, p.monto, p.tasa, p.plazo, p.fecha_inicio " +
                     "FROM prestamos p " +
                     "JOIN clientes c ON p.idClientes = c.idClientes " +
                     "ORDER BY p.idprestamos";
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("idprestamos"),
                    rs.getString("nombre"),
                    rs.getDouble("monto"),
                    rs.getDouble("tasa"),
                    rs.getInt("plazo"),
                    rs.getDate("fecha_inicio").toLocalDate()
                });
            }
        }
        return lista;
    }

    public int obtenerIdPorFila(int idprestamos) throws SQLException {
        return idprestamos;
    }

    public int obtenerIdClientePorNombre(String nombre) throws SQLException {
        String sql = "SELECT idClientes FROM clientes WHERE nombre=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("idClientes");
            }
        }
        return -1;
    }

    public List<Object[]> buscar(String texto) throws SQLException {
        String sql = "SELECT p.idprestamos, c.nombre, p.monto, p.tasa, p.plazo, p.fecha_inicio " +
                     "FROM prestamos p " +
                     "JOIN clientes c ON p.idClientes = c.idClientes " +
                     "WHERE c.nombre LIKE ? OR p.monto LIKE ? " +
                     "ORDER BY p.idprestamos";
        List<Object[]> lista = new ArrayList<>();
        String patron = "%" + texto + "%";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, patron);
            ps.setString(2, patron);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getInt("idprestamos"),
                        rs.getString("nombre"),
                        rs.getDouble("monto"),
                        rs.getDouble("tasa"),
                        rs.getInt("plazo"),
                        rs.getDate("fecha_inicio").toLocalDate()
                    });
                }
            }
        }
        return lista;
    }
}