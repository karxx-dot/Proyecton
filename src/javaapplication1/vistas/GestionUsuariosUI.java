package javaapplication1.vistas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class GestionUsuariosUI extends JFrame {

    private JTextField txtUsuario, txtRol, txtBuscar;
    private JPasswordField txtPassword;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLupa, btnLimpiar, btnNuevo;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public GestionUsuariosUI() {
        setTitle("Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        // Aplicar Look & Feel Nimbus
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        add(crearFormPanel(), BorderLayout.NORTH);
        add(crearTablePanel(), BorderLayout.CENTER);
        add(crearSearchPanel(), BorderLayout.SOUTH);
        
        // Inicializar controlador
        inicializarControlador();
    }

    private void inicializarControlador() {
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLupa.addActionListener(e -> buscarUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnNuevo.addActionListener(e -> limpiarCampos());
    }

    private void guardarUsuario() {
        String usuario = getUsuario();
        String password = getPassword();
        String rol = getRol();

        // Validaciones
        if (usuario.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar si el usuario ya existe
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equals(usuario)) {
                JOptionPane.showMessageDialog(this, "⚠️ El usuario ya existe.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Agregar fila a la tabla
        Object[] fila = {usuario, password, rol};
        agregarFilaTabla(fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Usuario guardado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void editarUsuario() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un usuario para editar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String usuario = getUsuario();
        String password = getPassword();
        String rol = getRol();

        if (usuario.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object[] fila = {usuario, password, rol};
        actualizarFilaTabla(filaSeleccionada, fila);
        limpiarCampos();
        JOptionPane.showMessageDialog(this, "✅ Usuario actualizado exitosamente.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarUsuario() {
        int filaSeleccionada = getFilaSeleccionada();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un usuario para eliminar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas eliminar este usuario?", 
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            eliminarFilaTabla(filaSeleccionada);
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Usuario eliminado exitosamente.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscarUsuario() {
        String textoBusqueda = getTextoBusqueda().toLowerCase();

        if (textoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.", 
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String usuario = tableModel.getValueAt(i, 0).toString().toLowerCase();
            String rol = tableModel.getValueAt(i, 2).toString().toLowerCase();

            if (usuario.contains(textoBusqueda) || rol.contains(textoBusqueda)) {
                dataTable.setRowSelectionInterval(i, i);
                cargarUsuarioSeleccionado();
                JOptionPane.showMessageDialog(this, "✅ Usuario encontrado.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "❌ No se encontró ningún usuario.", 
                "No encontrado", JOptionPane.INFORMATION_MESSAGE);
    }

    private JPanel crearFormPanel() {
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createTitledBorder("Formulario de Usuario"));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Usuario
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Usuario:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtUsuario = new JTextField(15);
        txtUsuario.setToolTipText("Ingrese el nombre de usuario");
        fieldsPanel.add(txtUsuario, c);

        // Contraseña
        c.gridx = 2;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Contraseña:"), c);
        c.gridx = 3;
        c.weightx = 0.2;
        txtPassword = new JPasswordField(15);
        txtPassword.setToolTipText("Ingrese la contraseña");
        fieldsPanel.add(txtPassword, c);

        // Rol
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Rol:"), c);
        c.gridx = 1;
        c.weightx = 0.2;
        txtRol = new JTextField(15);
        txtRol.setToolTipText("Ej: Admin, Usuario, Moderador");
        fieldsPanel.add(txtRol, c);

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
        tableContainer.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));

        String[] columns = {"Usuario", "Contraseña", "Rol"};
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
                cargarUsuarioSeleccionado();
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

        searchPanel.add(new JLabel("Buscar por usuario o rol:"));
        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Ingrese usuario o rol");
        searchPanel.add(txtBuscar);

        btnLupa = crearBoton("🔍 Buscar", new Color(70, 130, 180));
        searchPanel.add(btnLupa);

        searchContainer.add(searchPanel, BorderLayout.WEST);

        return searchContainer;
    }

    // ==================== MÉTODOS PÚBLICOS ====================

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtPassword.setText("");
        txtRol.setText("");
        txtBuscar.setText("");
        dataTable.clearSelection();
    }

    public void cargarUsuarioSeleccionado() {
        int filaSeleccionada = dataTable.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtUsuario.setText((String) tableModel.getValueAt(filaSeleccionada, 0));
            txtPassword.setText((String) tableModel.getValueAt(filaSeleccionada, 1));
            txtRol.setText((String) tableModel.getValueAt(filaSeleccionada, 2));
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

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword()).trim();
    }

    public String getRol() {
        return txtRol.getText().trim();
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

    // ==================== MAIN ====================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            GestionUsuariosUI vista = new GestionUsuariosUI();
            vista.setVisible(true);
        });
    }
}