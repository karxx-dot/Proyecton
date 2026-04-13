package javaapplication1.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GestionClientesUI extends JPanel {

    private JTextField txtCedula, txtNombre, txtTelefono, txtEmail, txtBuscar;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLupa, btnLimpiar, btnNuevo;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    // Variables estáticas para compartir clientes globalmente
    private static List<String> clientesGlobales = new ArrayList<>();
    private static List<GestionPrestamosUI> observadores = new ArrayList<>();

    public GestionClientesUI() {

        setSize(1000, 650);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Aplicar Look & Feel Nimbus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(crearFormPanel(), BorderLayout.NORTH);
        add(crearTablePanel(), BorderLayout.CENTER);
        add(crearSearchPanel(), BorderLayout.SOUTH);
        
        // Inicializar controlador
        inicializarControlador();
    }

    private void inicializarControlador() {
        btnGuardar.addActionListener(e -> guardarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLupa.addActionListener(e -> buscarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnNuevo.addActionListener(e -> limpiarCampos());
    }

    private void guardarCliente() {
        String cedula = getCedula();
        String nombre = getNombre();
        String telefono = getTelefono();
        String email = getEmail();

        // Validaciones
        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si la cédula ya existe
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(cedula)) {
                JOptionPane.showMessageDialog(this, "⚠️ La cédula ya existe.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Agregar fila a la tabla
        Object[] fila = {cedula, nombre, telefono, email};
        agregarFilaTabla(fila);
        
        // ✅ NOTIFICAR A GESTIÓN DE PRÉSTAMOS
        agregarClienteGlobal(nombre);
        
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Cliente guardado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarCliente() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un cliente para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedula = getCedula();
        String nombre = getNombre();
        String telefono = getTelefono();
        String email = getEmail();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[] fila = {cedula, nombre, telefono, email};
        actualizarFilaTabla(filaSeleccionada, fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Cliente actualizado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarCliente() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un cliente para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas eliminar este cliente?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String nombreEliminado = (String) tableModel.getValueAt(filaSeleccionada, 1);
            eliminarFilaTabla(filaSeleccionada);
            
            // ✅ NOTIFICAR A GESTIÓN DE PRÉSTAMOS
            eliminarClienteGlobal(nombreEliminado);
            
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Cliente eliminado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarCliente() {
        String textoBusqueda = getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String cedula = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String nombreTabla = tableModel.getValueAt(i, 1).toString().toLowerCase();

            if (cedula.contains(textoBusqueda) || nombreTabla.contains(textoBusqueda)) {
                dataTable.setRowSelectionInterval(i, i);
                cargarClienteSeleccionado();
                JOptionPane.showMessageDialog(this, "✅ Cliente encontrado.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "❌ No se encontró ningún cliente.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel crearFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createTitledBorder("Formulario de Cliente"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Cédula
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Cédula:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtCedula = new JTextField(15);
        txtCedula.setToolTipText("Ej: 001-XXXXXXX-X");
        fieldsPanel.add(txtCedula, c);

        // Nombre
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Nombre:"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtNombre = new JTextField(15);
        txtNombre.setToolTipText("Ingrese el nombre completo");
        fieldsPanel.add(txtNombre, c);

        // Teléfono
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Teléfono:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtTelefono = new JTextField(15);
        txtTelefono.setToolTipText("Ej: 809-XXX-XXXX");
        fieldsPanel.add(txtTelefono, c);

        // Email
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Email:"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtEmail = new JTextField(15);
        txtEmail.setToolTipText("Ej: correo@ejemplo.com");
        fieldsPanel.add(txtEmail, c);

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
        tableContainer.setBorder(BorderFactory.createTitledBorder("Lista de Clientes"));
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));

        String[] columns = {"Cédula", "Nombre", "Teléfono", "Email"};
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
                cargarClienteSeleccionado();
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

        searchPanel.add(new JLabel("Buscar por cédula o nombre:"));
        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Ingrese cédula o nombre");
        searchPanel.add(txtBuscar);

        btnLupa = crearBoton("🔍 Buscar", new Color(70, 130, 180));
        searchPanel.add(btnLupa);

        searchContainer.add(searchPanel, BorderLayout.WEST);

        return searchContainer;
    }

    // ==================== MÉTODOS PÚBLICOS ====================

    public void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtBuscar.setText("");
        dataTable.clearSelection();
    }

    public void cargarClienteSeleccionado() {
        int filaSeleccionada = dataTable.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtCedula.setText((String) tableModel.getValueAt(filaSeleccionada, 0));
            txtNombre.setText((String) tableModel.getValueAt(filaSeleccionada, 1));
            txtTelefono.setText((String) tableModel.getValueAt(filaSeleccionada, 2));
            txtEmail.setText((String) tableModel.getValueAt(filaSeleccionada, 3));
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

    // ==================== GETTERS ====================

    public String getCedula() {
        return txtCedula.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getTelefono() {
        return txtTelefono.getText().trim();
    }

    public String getEmail() {
        return txtEmail.getText().trim();
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
            GestionClientesUI vista = new GestionClientesUI();
            vista.setVisible(true);
        });
    }
}