package javaapplication1.controladores;

import javaapplication1.vistas.GestionClientesUI;
import javax.swing.*;

public class ClienteControlador {
    private GestionClientesUI vista;

    public ClienteControlador(GestionClientesUI vista) {
        this.vista = vista;
        inicializarListeners();
    }

    private void inicializarListeners() {
        vista.agregarListenerGuardar(e -> guardarCliente());
        vista.agregarListenerEditar(e -> editarCliente());
        vista.agregarListenerEliminar(e -> eliminarCliente());
        vista.agregarListenerBuscar(e -> buscarCliente());
        vista.agregarListenerLimpiar(e -> vista.limpiarCampos());
        vista.agregarListenerNuevo(e -> vista.limpiarCampos());
    }

    private void guardarCliente() {
        String cedula = vista.getCedula();
        String nombre = vista.getNombre();
        String telefono = vista.getTelefono();
        String email = vista.getEmail();

        // Validaciones
        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si la cédula ya existe
        for (int i = 0; i < vista.getTableModel().getRowCount(); i++) {
            if (vista.getTableModel().getValueAt(i, 0).toString().equals(cedula)) {
                JOptionPane.showMessageDialog(vista, "⚠️ La cédula ya existe.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Agregar fila a la tabla
        Object[] fila = {cedula, nombre, telefono, email};
        vista.agregarFilaTabla(fila);
        
        
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Cliente guardado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarCliente() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona un cliente para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedula = vista.getCedula();
        String nombre = vista.getNombre();
        String telefono = vista.getTelefono();
        String email = vista.getEmail();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[] fila = {cedula, nombre, telefono, email};
        vista.actualizarFilaTabla(filaSeleccionada, fila);
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Cliente actualizado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCliente() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona un cliente para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Estás seguro de que deseas eliminar este cliente?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String nombreEliminado = (String) vista.getTableModel().getValueAt(filaSeleccionada, 1);
            vista.eliminarFilaTabla(filaSeleccionada);
            
            vista.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "✅ Cliente eliminado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarCliente() {
        String textoBusqueda = vista.getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < vista.getTableModel().getRowCount(); i++) {
            String cedula = vista.getTableModel().getValueAt(i, 0).toString().toLowerCase();
            String nombre = vista.getTableModel().getValueAt(i, 1).toString().toLowerCase();

            if (cedula.contains(textoBusqueda) || nombre.contains(textoBusqueda)) {
                vista.getDataTable().setRowSelectionInterval(i, i);
                vista.cargarClienteSeleccionado();
                JOptionPane.showMessageDialog(vista, "✅ Cliente encontrado.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(vista, "❌ No se encontró ningún cliente.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }
}