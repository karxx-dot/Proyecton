package javaapplication1.controladores;

import javaapplication1.vistas.GestionCuotasUI;
import javax.swing.*;

public class CuotaControlador {
    private GestionCuotasUI vista;

    public CuotaControlador(GestionCuotasUI vista) {
        this.vista = vista;
        inicializarListeners();
    }

    private void inicializarListeners() {
        vista.agregarListenerGuardar(e -> guardarCuota());
        vista.agregarListenerEditar(e -> editarCuota());
        vista.agregarListenerEliminar(e -> eliminarCuota());
        vista.agregarListenerBuscar(e -> buscarCuota());
        vista.agregarListenerLimpiar(e -> vista.limpiarCampos());
        vista.agregarListenerNuevo(e -> vista.limpiarCampos());
    }

    private void guardarCuota() {
        String prestamo = vista.getPrestamo();
        String numeroCuota = vista.getNumeroCuota();
        String monto = vista.getMonto();
        String fechaPago = vista.getFechaPago();
        String estado = vista.getEstado();

        if (prestamo.isEmpty() || numeroCuota.isEmpty() || monto.isEmpty() || 
            fechaPago.isEmpty() || estado.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(monto);
            Integer.parseInt(numeroCuota);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Monto y Número de Cuota deben ser números.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fila = {prestamo, numeroCuota, monto, fechaPago, estado};
        vista.agregarFilaTabla(fila);
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Cuota guardada exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarCuota() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona una cuota para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String prestamo = vista.getPrestamo();
        String numeroCuota = vista.getNumeroCuota();
        String monto = vista.getMonto();
        String fechaPago = vista.getFechaPago();
        String estado = vista.getEstado();

        if (prestamo.isEmpty() || numeroCuota.isEmpty() || monto.isEmpty() || 
            fechaPago.isEmpty() || estado.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(monto);
            Integer.parseInt(numeroCuota);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "⚠️ Monto y Número de Cuota deben ser números.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fila = {prestamo, numeroCuota, monto, fechaPago, estado};
        vista.actualizarFilaTabla(filaSeleccionada, fila);
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Cuota actualizada exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCuota() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona una cuota para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Estás seguro de que deseas eliminar esta cuota?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            vista.eliminarFilaTabla(filaSeleccionada);
            vista.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "✅ Cuota eliminada exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarCuota() {
        String textoBusqueda = vista.getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < vista.getTableModel().getRowCount(); i++) {
            String prestamo = vista.getTableModel().getValueAt(i, 0).toString().toLowerCase();
            String numeroCuota = vista.getTableModel().getValueAt(i, 1).toString().toLowerCase();
            String estado = vista.getTableModel().getValueAt(i, 4).toString().toLowerCase();

            if (prestamo.contains(textoBusqueda) || numeroCuota.contains(textoBusqueda) || 
                estado.contains(textoBusqueda)) {
                vista.getDataTable().setRowSelectionInterval(i, i);
                vista.cargarCuotaSeleccionada();
                JOptionPane.showMessageDialog(vista, "✅ Cuota encontrada.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(vista, "❌ No se encontró ninguna cuota.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }
}