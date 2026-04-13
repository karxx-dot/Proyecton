package Union;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javaapplication1.modelo.Cuota;

public class CuotaDAO {

    public boolean guardar(int idPrestamo, Cuota c) throws SQLException {
    String sql = "INSERT INTO detalles_cuotas (idprestamos, nro_cuotas, fecha_pago, dia_pago, " +
                 "capital, interes, cuota_total, saldo_restante, estado_cuota) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, idPrestamo);
        ps.setInt(2, c.getNumero());
        ps.setDate(3, Date.valueOf(c.getFecha()));
        ps.setString(4, c.getFecha().getDayOfWeek().toString()); // ← dia_pago
        ps.setDouble(5, c.getCapital());
        ps.setDouble(6, c.getInteres());
        ps.setDouble(7, c.getMontoTotal());
        ps.setDouble(8, c.getSaldoPendiente());
        ps.setString(9, "Pendiente");
        return ps.executeUpdate() > 0;
    }
}

    public boolean actualizar(int idDetalle, String estado) throws SQLException {
        String sql = "UPDATE detalles_cuotas SET estado_cuota=? WHERE idDetalles=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idDetalle);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idDetalle) throws SQLException {
        String sql = "DELETE FROM detalles_cuotas WHERE idDetalles=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idDetalle);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Object[]> listarPorPrestamo(int idPrestamo) throws SQLException {
        String sql = "SELECT d.idDetalles, p.idprestamos, d.nro_cuotas, d.fecha_pago, " +
                     "d.capital, d.interes, d.cuota_total, d.saldo_restante, d.estado_cuota " +
                     "FROM detalles_cuotas d " +
                     "JOIN prestamos p ON d.idprestamos = p.idprestamos " +
                     "WHERE d.idprestamos=? ORDER BY d.nro_cuotas";
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getInt("idDetalles"),
                        rs.getInt("idprestamos"),
                        rs.getInt("nro_cuotas"),
                        rs.getDate("fecha_pago").toLocalDate(),
                        rs.getDouble("capital"),
                        rs.getDouble("interes"),
                        rs.getDouble("cuota_total"),
                        rs.getDouble("saldo_restante"),
                        rs.getString("estado_cuota")
                    });
                }
            }
        }
        return lista;
    }

    public List<Object[]> listarTodos() throws SQLException {
        String sql = "SELECT d.idDetalles, d.idprestamos, d.nro_cuotas, d.fecha_pago, " +
                     "d.capital, d.interes, d.cuota_total, d.saldo_restante, d.estado_cuota " +
                     "FROM detalles_cuotas d ORDER BY d.idprestamos, d.nro_cuotas";
        List<Object[]> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("idDetalles"),
                    rs.getInt("idprestamos"),
                    rs.getInt("nro_cuotas"),
                    rs.getDate("fecha_pago").toLocalDate(),
                    rs.getDouble("capital"),
                    rs.getDouble("interes"),
                    rs.getDouble("cuota_total"),
                    rs.getDouble("saldo_restante"),
                    rs.getString("estado_cuota")
                });
            }
        }
        return lista;
    }
}