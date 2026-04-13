package javaapplication1.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GestionCuotasUI extends javax.swing.JPanel {

    private JComboBox<String> cbPrestamo;
    private JTextField txtNumeroCuota, txtMonto, txtFechaPago, txtEstado, txtBuscar;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLupa, btnLimpiar, btnNuevo;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    // Variables estáticas para compartir préstamos
    private static List<String> prestamosGlobales = new ArrayList<>();
    private static List<GestionCuotasUI> observadores = new ArrayList<>();

    public GestionCuotasUI() {
        
        setSize(1100, 700);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(crearFormPanel(), BorderLayout.NORTH);
        add(crearTablePanel(), BorderLayout.CENTER);
        add(crearSearchPanel(), BorderLayout.SOUTH);

        inicializarControlador();
        
        // Registrar como observador
        GestionCuotasUI.agregarObservador(this);
        actualizarComboPrestamos();
    }

    private void inicializarControlador() {
        btnGuardar.addActionListener(e -> guardarCuota());
        btnEditar.addActionListener(e -> editarCuota());
        btnEliminar.addActionListener(e -> eliminarCuota());
        btnLupa.addActionListener(e -> buscarCuota());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnNuevo.addActionListener(e -> limpiarCampos());
    }

    private void guardarCuota() {
        String prestamo = getPrestamo();
        String numeroCuota = getNumeroCuota();
        String monto = getMonto();
        String fechaPago = getFechaPago();
        String estado = getEstado();

        // Validaciones
        if (prestamo.isEmpty() || numeroCuota.isEmpty() || monto.isEmpty() || 
            fechaPago.isEmpty() || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que monto sea número
        try {
            Double.parseDouble(monto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ El monto debe ser un número.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar que número de cuota sea número
        try {
            Integer.parseInt(numeroCuota);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ El número de cuota debe ser un número.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar fila a la tabla
        Object[] fila = {prestamo, numeroCuota, monto, fechaPago, estado};
        agregarFilaTabla(fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Cuota guardada exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarCuota() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una cuota para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String prestamo = getPrestamo();
        String numeroCuota = getNumeroCuota();
        String monto = getMonto();
        String fechaPago = getFechaPago();
        String estado = getEstado();

        if (prestamo.isEmpty() || numeroCuota.isEmpty() || monto.isEmpty() || 
            fechaPago.isEmpty() || estado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(monto);
            Integer.parseInt(numeroCuota);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Monto y Número de Cuota deben ser números.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fila = {prestamo, numeroCuota, monto, fechaPago, estado};
        actualizarFilaTabla(filaSeleccionada, fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Cuota actualizada exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCuota() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una cuota para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas eliminar esta cuota?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarFilaTabla(filaSeleccionada);
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Cuota eliminada exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarCuota() {
        String textoBusqueda = getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String prestamo = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String numeroCuota = tableModel.getValueAt(i, 1).toString().toLowerCase();
            String estado = tableModel.getValueAt(i, 4).toString().toLowerCase();

            if (prestamo.contains(textoBusqueda) || numeroCuota.contains(textoBusqueda) || 
                estado.contains(textoBusqueda)) {
                dataTable.setRowSelectionInterval(i, i);
                cargarCuotaSeleccionada();
                JOptionPane.showMessageDialog(this, "✅ Cuota encontrada.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "❌ No se encontró ninguna cuota.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel crearFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createTitledBorder("Formulario de Cuota"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Préstamo - ComboBox VACÍO
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Préstamo:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        cbPrestamo = new JComboBox<>();
        cbPrestamo.setToolTipText("Seleccione un préstamo");
        fieldsPanel.add(cbPrestamo, c);

        // Número de Cuota
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Número de Cuota:"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtNumeroCuota = new JTextField(15);
        txtNumeroCuota.setToolTipText("Ej: 1, 2, 3...");
        fieldsPanel.add(txtNumeroCuota, c);

        // Monto
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Monto:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtMonto = new JTextField(15);
        txtMonto.setToolTipText("Ej: 500.00");
        fieldsPanel.add(txtMonto, c);

        // Fecha de Pago
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Fecha de Pago:"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtFechaPago = new JTextField(15);
        txtFechaPago.setToolTipText("Ej: 10/05/2026");
        fieldsPanel.add(txtFechaPago, c);

        // Estado
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Estado:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtEstado = new JTextField(15);
        txtEstado.setToolTipText("Ej: Pagada, Pendiente, Atrasada");
        fieldsPanel.add(txtEstado, c);

        // Botones CRUD
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setOpaque(false);

        btnGuardar = crearBoton("💾 Guardar", new Color(34, 139, 34));
        btnEditar = crearBoton("✏️ Editar", new Color(30, 144, 255));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(220, 20, 60));
        btnLimpiar = crearBoton("Limpiar", new Color(169, 169, 169));
        btnNuevo = crearBoton("➕ Nuevo", new Color(184, 134, 11));

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnNuevo);

        formContainer.add(fieldsPanel, BorderLayout.CENTER);
        formContainer.add(buttonPanel, BorderLayout.SOUTH);

        return formContainer;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 11));
        boton.setPreferredSize(new Dimension(120, 35));
        return boton;
    }

    private JPanel crearTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createTitledBorder("Lista de Cuotas"));
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));

        String[] columns = {"Préstamo", "Número de Cuota", "Monto", "Fecha de Pago", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setRowHeight(25);
        dataTable.getTableHeader().setReorderingAllowed(false);

        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCuotaSeleccionada();
            }
        });

        JScrollPane scrollPane = new JScrollPane(dataTable);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        return tableContainer;
    }

    private JPanel crearSearchPanel() {
        JPanel searchContainer = new JPanel(new BorderLayout());
        searchContainer.setBorder(BorderFactory.createTitledBorder("Búsqueda"));
        searchContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);

        searchPanel.add(new JLabel("Buscar cuota (préstamo, número o estado):"));
        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Ingrese datos de búsqueda");
        searchPanel.add(txtBuscar);

        btnLupa = crearBoton("🔍 Buscar", new Color(70, 130, 180));
        searchPanel.add(btnLupa);

        searchContainer.add(searchPanel, BorderLayout.WEST);

        return searchContainer;
    }

    // ==================== MÉTODOS PÚBLICOS ====================

    public void limpiarCampos() {
        cbPrestamo.setSelectedIndex(-1);
        txtNumeroCuota.setText("");
        txtMonto.setText("");
        txtFechaPago.setText("");
        txtEstado.setText("");
        txtBuscar.setText("");
        dataTable.clearSelection();
    }

    public void cargarCuotaSeleccionada() {
        int filaSeleccionada = dataTable.getSelectedRow();
        if (filaSeleccionada != -1) {
            cbPrestamo.setSelectedItem((String) tableModel.getValueAt(filaSeleccionada, 0));
            txtNumeroCuota.setText((String) tableModel.getValueAt(filaSeleccionada, 1));
            txtMonto.setText((String) tableModel.getValueAt(filaSeleccionada, 2));
            txtFechaPago.setText((String) tableModel.getValueAt(filaSeleccionada, 3));
            txtEstado.setText((String) tableModel.getValueAt(filaSeleccionada, 4));
        }
    }

    public void agregarFilaTabla(Object[] fila) {
        tableModel.addRow(fila);
    }

    public void actualizarFilaTabla(int fila, Object[] datos) {
        for (int i = 0; i < datos.length; i++) {
            tableModel.setValueAt(datos[i], fila, i);
        }
    }

    public void eliminarFilaTabla(int fila) {
        tableModel.removeRow(fila);
    }

    public void limpiarTabla() {
        tableModel.setRowCount(0);
    }

    public void actualizarComboPrestamos() {
        cbPrestamo.removeAllItems();
        for (String prestamo : prestamosGlobales) {
            cbPrestamo.addItem(prestamo);
        }
    }

    // ==================== GETTERS ====================

    public String getPrestamo() {
        Object prestamo = cbPrestamo.getSelectedItem();
        return (prestamo == null) ? "" : prestamo.toString();
    }

    public String getNumeroCuota() {
        return txtNumeroCuota.getText().trim();
    }

    public String getMonto() {
        return txtMonto.getText().trim();
    }

    public String getFechaPago() {
        return txtFechaPago.getText().trim();
    }

    public String getEstado() {
        return txtEstado.getText().trim();
    }

    public String getTextoBusqueda() {
        return txtBuscar.getText().trim();
    }

    public int getFilaSeleccionada() {
        return dataTable.getSelectedRow();
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getDataTable() {
        return dataTable;
    }

    // ==================== SETTERS DE LISTENERS ====================

    public void agregarListenerGuardar(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }

    public void agregarListenerEditar(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }

    public void agregarListenerEliminar(ActionListener listener) {
        btnEliminar.addActionListener(listener);
    }

    public void agregarListenerLimpiar(ActionListener listener) {
        btnLimpiar.addActionListener(listener);
    }

    public void agregarListenerNuevo(ActionListener listener) {
        btnNuevo.addActionListener(listener);
    }

    public void agregarListenerBuscar(ActionListener listener) {
        btnLupa.addActionListener(listener);
    }

    // ==================== MÉTODOS ESTÁTICOS PARA COMUNICACIÓN ====================

    public static void agregarPrestamoGlobal(String descripcionPrestamo) {
        prestamosGlobales.add(descripcionPrestamo);
        notificarObservadores();
    }

    public static void eliminarPrestamoGlobal(String descripcionPrestamo) {
        prestamosGlobales.remove(descripcionPrestamo);
        notificarObservadores();
    }

    public static void agregarObservador(GestionCuotasUI ui) {
        observadores.add(ui);
    }

    private static void notificarObservadores() {
        for (GestionCuotasUI ui : observadores) {
            ui.actualizarComboPrestamos();
        }
    }

    public static List<String> obtenerPrestamosGlobales() {
        return prestamosGlobales;
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            GestionCuotasUI vista = new GestionCuotasUI();
            vista.setVisible(true);
        });
    }
}