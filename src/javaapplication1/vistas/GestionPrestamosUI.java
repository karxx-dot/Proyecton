package javaapplication1.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class GestionPrestamosUI extends JFrame {

    private JComboBox<String> cbCliente;
    private JTextField txtMonto, txtTasa, txtPlazo, txtBuscar;
    private JFormattedTextField txtFechaInicio;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLupa, btnLimpiar, btnNuevo;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    
    // Variable estática para compartir clientes globalmente
    private static List<String> clientesGlobales = new ArrayList<>();
    private static List<GestionPrestamosUI> observadores = new ArrayList<>();

    public GestionPrestamosUI() {
        setTitle("Gestión de Préstamos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

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
        GestionPrestamosUI.agregarObservador(this);
        actualizarComboClientes();
    }

    private void inicializarControlador() {
        btnGuardar.addActionListener(e -> guardarPrestamo());
        btnEditar.addActionListener(e -> editarPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());
        btnLupa.addActionListener(e -> buscarPrestamo());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnNuevo.addActionListener(e -> limpiarCampos());
    }

    private void guardarPrestamo() {
        String cliente = getCliente();
        String monto = getMonto();
        String tasa = getTasa();
        String plazo = getPlazo();
        String fechaInicio = getFechaInicio();

        // Validaciones
        if (cliente.isEmpty() || monto.isEmpty() || tasa.isEmpty() || plazo.isEmpty() || fechaInicio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que monto, tasa y plazo sean números
        try {
            Double.parseDouble(monto);
            Double.parseDouble(tasa);
            Integer.parseInt(plazo);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Monto, Tasa y Plazo deben ser números.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Agregar fila a la tabla
        Object[] fila = {cliente, monto, tasa + "%", plazo, fechaInicio};
        agregarFilaTabla(fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Préstamo guardado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarPrestamo() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un préstamo para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cliente = getCliente();
        String monto = getMonto();
        String tasa = getTasa();
        String plazo = getPlazo();
        String fechaInicio = getFechaInicio();

        if (cliente.isEmpty() || monto.isEmpty() || tasa.isEmpty() || plazo.isEmpty() || fechaInicio.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Double.parseDouble(monto);
            Double.parseDouble(tasa);
            Integer.parseInt(plazo);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Monto, Tasa y Plazo deben ser números.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object[] fila = {cliente, monto, tasa + "%", plazo, fechaInicio};
        actualizarFilaTabla(filaSeleccionada, fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Préstamo actualizado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarPrestamo() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un préstamo para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas eliminar este préstamo?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarFilaTabla(filaSeleccionada);
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Préstamo eliminado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarPrestamo() {
        String textoBusqueda = getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String cliente = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String monto = tableModel.getValueAt(i, 1).toString().toLowerCase();

            if (cliente.contains(textoBusqueda) || monto.contains(textoBusqueda)) {
                dataTable.setRowSelectionInterval(i, i);
                cargarPrestamoSeleccionado();
                JOptionPane.showMessageDialog(this, "✅ Préstamo encontrado.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "❌ No se encontró ningún préstamo.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel crearFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createTitledBorder("Formulario de Préstamo"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Cliente - ComboBox VACÍO
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Cliente:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        cbCliente = new JComboBox<>();
        cbCliente.setToolTipText("Seleccione un cliente");
        fieldsPanel.add(cbCliente, c);

        // Monto
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Monto:"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtMonto = new JTextField(15);
        txtMonto.setToolTipText("Ej: 5000.00");
        fieldsPanel.add(txtMonto, c);

        // Tasa
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Tasa (%):"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtTasa = new JTextField(15);
        txtTasa.setToolTipText("Ej: 5.5");
        fieldsPanel.add(txtTasa, c);

        // Plazo
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Plazo (meses):"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtPlazo = new JTextField(15);
        txtPlazo.setToolTipText("Ej: 12");
        fieldsPanel.add(txtPlazo, c);

        // Fecha Inicio
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Fecha Inicio:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        try {
            txtFechaInicio = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            txtFechaInicio.setToolTipText("Ej: 10/04/2026");
        } catch (Exception ex) {
            txtFechaInicio = new JFormattedTextField();
        }
        fieldsPanel.add(txtFechaInicio, c);

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
        tableContainer.setBorder(BorderFactory.createTitledBorder("Lista de Préstamos"));
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));

        String[] columns = {"Cliente", "Monto", "Tasa", "Plazo (meses)", "Fecha Inicio"};
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

        // Listener para cargar datos cuando se selecciona una fila
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarPrestamoSeleccionado();
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

        searchPanel.add(new JLabel("Buscar préstamo (cliente o monto):"));
        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Ingrese cliente o monto");
        searchPanel.add(txtBuscar);

        btnLupa = crearBoton("🔍 Buscar", new Color(70, 130, 180));
        searchPanel.add(btnLupa);

        searchContainer.add(searchPanel, BorderLayout.WEST);

        return searchContainer;
    }

    // ==================== MÉTODOS PÚBLICOS ====================

    public void limpiarCampos() {
        cbCliente.setSelectedIndex(-1);
        txtMonto.setText("");
        txtTasa.setText("");
        txtPlazo.setText("");
        txtFechaInicio.setText("");
        txtBuscar.setText("");
        dataTable.clearSelection();
    }

    public void cargarPrestamoSeleccionado() {
        int filaSeleccionada = dataTable.getSelectedRow();
        if (filaSeleccionada != -1) {
            cbCliente.setSelectedItem((String) tableModel.getValueAt(filaSeleccionada, 0));
            txtMonto.setText((String) tableModel.getValueAt(filaSeleccionada, 1));
            String tasa = (String) tableModel.getValueAt(filaSeleccionada, 2);
            txtTasa.setText(tasa.replace("%", ""));
            txtPlazo.setText((String) tableModel.getValueAt(filaSeleccionada, 3));
            txtFechaInicio.setText((String) tableModel.getValueAt(filaSeleccionada, 4));
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

    public void actualizarComboClientes() {
        cbCliente.removeAllItems();
        for (String cliente : clientesGlobales) {
            cbCliente.addItem(cliente);
        }
    }

    // ==================== GETTERS ====================

    public String getCliente() {
        Object cliente = cbCliente.getSelectedItem();
        return (cliente == null) ? "" : cliente.toString();
    }

    public String getMonto() {
        return txtMonto.getText().trim();
    }

    public String getTasa() {
        return txtTasa.getText().trim();
    }

    public String getPlazo() {
        return txtPlazo.getText().trim();
    }

    public String getFechaInicio() {
        return txtFechaInicio.getText().trim();
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

    public static void agregarClienteGlobal(String nombreCompleto) {
        clientesGlobales.add(nombreCompleto);
        notificarObservadores();
    }

    public static void eliminarClienteGlobal(String nombreCompleto) {
        clientesGlobales.remove(nombreCompleto);
        notificarObservadores();
    }

    public static void agregarObservador(GestionPrestamosUI ui) {
        observadores.add(ui);
    }

    private static void notificarObservadores() {
        for (GestionPrestamosUI ui : observadores) {
            ui.actualizarComboClientes();
        }
    }

    public static List<String> obtenerClientesGlobales() {
        return clientesGlobales;
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            GestionPrestamosUI vista = new GestionPrestamosUI();
            vista.setVisible(true);
        });
    }
}