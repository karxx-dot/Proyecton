package javaapplication1.controladores;

import Union.PrestamoDAO;
import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.List;
import javaapplication1.modelo.Prestamo;
import javaapplication1.vistas.GestionPrestamosUI;
import javax.swing.*;

public class PrestamoControlador {
    private GestionPrestamosUI vista;

    public PrestamoControlador(GestionPrestamosUI vista) {
    this.vista = vista;
    inicializarListeners();
    vista.actualizarComboClientes();
    cargarTablaDesdeDB();
}

    private void inicializarListeners() {
        vista.agregarListenerGuardar(e -> guardarPrestamo());
        vista.agregarListenerEditar(e -> editarPrestamo());
        vista.agregarListenerEliminar(e -> eliminarPrestamo());
        vista.agregarListenerBuscar(e -> buscarPrestamo());
        vista.agregarListenerLimpiar(e -> vista.limpiarCampos());
        vista.agregarListenerNuevo(e -> vista.limpiarCampos());
    }

    private void guardarPrestamo() {
    String nombreCliente = vista.getCliente();
    String monto = vista.getMonto();
    String tasa = vista.getTasa();
    String plazo = vista.getPlazo();
    String fechaInicio = vista.getFechaInicio();

    if (nombreCliente.isEmpty() || monto.isEmpty() || tasa.isEmpty() 
        || plazo.isEmpty() || fechaInicio.isEmpty()) {
        JOptionPane.showMessageDialog(vista, "⚠️ Completa todos los campos.",
                "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        double montoD = Double.parseDouble(monto);
        double tasaD  = Double.parseDouble(tasa);
        int    plazoI = Integer.parseInt(plazo);

        // Obtener idCliente desde BD
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        int idCliente = prestamoDAO.obtenerIdClientePorNombre(nombreCliente);
        if (idCliente == -1) {
            JOptionPane.showMessageDialog(vista, "❌ Cliente no encontrado en BD.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear y guardar préstamo
        java.time.LocalDate fecha = java.time.LocalDate.parse(fechaInicio,
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Prestamo p = new Prestamo(montoD, plazoI, tasaD, fecha);
        prestamoDAO.guardar(idCliente, p);

        // Refrescar tabla
        vista.limpiarTabla();
        cargarTablaDesdeDB();

        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Préstamo guardado.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(vista, "⚠️ Monto, Tasa y Plazo deben ser números.",
                "Error", JOptionPane.ERROR_MESSAGE);
    } catch (HeadlessException | SQLException ex) {
        JOptionPane.showMessageDialog(vista, "❌ Error: " + ex.getMessage(),
                "Error BD", JOptionPane.ERROR_MESSAGE);
    }
}

    private void editarPrestamo() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona un préstamo para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cliente = vista.getCliente();
        String monto = vista.getMonto();
        String tasa = vista.getTasa();
        String plazo = vista.getPlazo();
        String fechaInicio = vista.getFechaInicio();

        if (cliente.isEmpty() || monto.isEmpty() || tasa.isEmpty() || plazo.isEmpty() || fechaInicio.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(monto);
            Double.parseDouble(tasa);
            Integer.parseInt(plazo);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Monto, Tasa y Plazo deben ser números.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fila = {cliente, monto, tasa + "%", plazo, fechaInicio};
        vista.actualizarFilaTabla(filaSeleccionada, fila);
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Préstamo actualizado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarPrestamo() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona un préstamo para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Estás seguro de que deseas eliminar este préstamo?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String cliente = (String) vista.getTableModel().getValueAt(filaSeleccionada, 0);
            vista.eliminarFilaTabla(filaSeleccionada);
            
            javaapplication1.vistas.GestionCuotasUI.eliminarPrestamoGlobal(cliente);
            
            vista.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "✅ Préstamo eliminado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarPrestamo() {
        String textoBusqueda = vista.getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < vista.getTableModel().getRowCount(); i++) {
            String cliente = vista.getTableModel().getValueAt(i, 0).toString().toLowerCase();
            String monto = vista.getTableModel().getValueAt(i, 1).toString().toLowerCase();

            if (cliente.contains(textoBusqueda) || monto.contains(textoBusqueda)) {
                vista.getDataTable().setRowSelectionInterval(i, i);
                vista.cargarPrestamoSeleccionado();
                JOptionPane.showMessageDialog(vista, "✅ Préstamo encontrado.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(vista, "❌ No se encontró ningún préstamo.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }
    public void cargarTablaDesdeDB() {
    try {
        PrestamoDAO dao = new PrestamoDAO();
        List<Object[]> lista = dao.listarTodos();
        vista.limpiarTabla();
        for (Object[] fila : lista) {
    
            vista.agregarFilaTabla(new Object[]{
                fila[1],         
                fila[2],      
                fila[3] + "%",   
                fila[4],  
                fila[5].toString() 
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(vista, "Error cargando préstamos: " + e.getMessage(),
                "Error BD", JOptionPane.ERROR_MESSAGE);
    }
}
}