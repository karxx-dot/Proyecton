package javaapplication1.controladores;

import javaapplication1.vistas.GestionUsuariosUI;
import javax.swing.*;

public class UsuarioControlador {
    private GestionUsuariosUI vista;

    public UsuarioControlador(GestionUsuariosUI vista) {
        this.vista = vista;
        inicializarListeners();
    }

    private void inicializarListeners() {
        vista.agregarListenerGuardar(e -> guardarUsuario());
        vista.agregarListenerEditar(e -> editarUsuario());
        vista.agregarListenerEliminar(e -> eliminarUsuario());
        vista.agregarListenerBuscar(e -> buscarUsuario());
        vista.agregarListenerLimpiar(e -> vista.limpiarCampos());
        vista.agregarListenerNuevo(e -> vista.limpiarCampos());
    }

    private void guardarUsuario() {
        String usuario = vista.getUsuario();
        String password = vista.getPassword();
        String rol = vista.getRol();

        if (usuario.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < vista.getTableModel().getRowCount(); i++) {
            if (vista.getTableModel().getValueAt(i, 0).toString().equals(usuario)) {
                JOptionPane.showMessageDialog(vista, "⚠️ El usuario ya existe.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Object[] fila = {usuario, password, rol};
        vista.agregarFilaTabla(fila);
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Usuario guardado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarUsuario() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona un usuario para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String usuario = vista.getUsuario();
        String password = vista.getPassword();
        String rol = vista.getRol();

        if (usuario.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[] fila = {usuario, password, rol};
        vista.actualizarFilaTabla(filaSeleccionada, fila);
        vista.limpiarCampos();
        JOptionPane.showMessageDialog(vista, "✅ Usuario actualizado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarUsuario() {
        int filaSeleccionada = vista.getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "⚠️ Selecciona un usuario para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(vista, 
                "¿Estás seguro de que deseas eliminar este usuario?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            vista.eliminarFilaTabla(filaSeleccionada);
            vista.limpiarCampos();
            JOptionPane.showMessageDialog(vista, "✅ Usuario eliminado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarUsuario() {
        String textoBusqueda = vista.getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < vista.getTableModel().getRowCount(); i++) {
            String usuario = vista.getTableModel().getValueAt(i, 0).toString().toLowerCase();
            String rol = vista.getTableModel().getValueAt(i, 2).toString().toLowerCase();

            if (usuario.contains(textoBusqueda) || rol.contains(textoBusqueda)) {
                vista.getDataTable().setRowSelectionInterval(i, i);
                JOptionPane.showMessageDialog(vista, "✅ Usuario encontrado.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(vista, "❌ No se encontró ningún usuario.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }
}