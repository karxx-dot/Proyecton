package javaapplication1.vistas;

import Union.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javaapplication1.modelo.Usuario;

public class GestionUsuariosUI extends javax.swing.JPanel {

    private JTextField txtNombreReal, txtUsuario, txtRol, txtBuscar;
    private JPasswordField txtPassword;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLupa, btnLimpiar, btnNuevo;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    // ── NUEVO ───────────────────────────────────────────────
    private final UsuarioDAO dao = new UsuarioDAO();
    private List<Integer> idsUsuarios = new java.util.ArrayList<>();
    // ───────────────────────────────────────────────────────

    public GestionUsuariosUI() {
        setSize(1000, 650);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 15, 15, 15));

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(crearFormPanel(),   BorderLayout.NORTH);
        add(crearTablePanel(),  BorderLayout.CENTER);
        add(crearSearchPanel(), BorderLayout.SOUTH);

        inicializarControlador();
        cargarDesdeBD(); // ← NUEVO
    }

    private void inicializarControlador() {
        btnGuardar.addActionListener(e  -> guardarUsuario());
        btnEditar.addActionListener(e   -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        btnLupa.addActionListener(e     -> buscarUsuario());
        btnLimpiar.addActionListener(e  -> limpiarCampos());
        btnNuevo.addActionListener(e    -> limpiarCampos());
    }

    // ── NUEVO ───────────────────────────────────────────────
    public void cargarDesdeBD() {
        try {
            List<Object[]> lista = dao.listarTodos();
            limpiarTabla();
            idsUsuarios.clear();
            for (Object[] row : lista) {
                idsUsuarios.add((Integer) row[0]);
                tableModel.addRow(new Object[]{
                    row[2], // nombreUsuario
                    row[3], // password
                    row[4]  // rol
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Error al cargar usuarios:\n" + ex.getMessage(),
                "Error BD", JOptionPane.ERROR_MESSAGE);
        }
    }
    // ───────────────────────────────────────────────────────

    private void guardarUsuario() {
        String nombreReal = getNombreReal();
        String usuario    = getUsuario();
        String password   = getPassword();
        String rol        = getRol();

        if (nombreReal.isEmpty() || usuario.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // ── NUEVO ───────────────────────────────────────
            Usuario u = new Usuario(nombreReal, usuario, password, rol);
            boolean ok = dao.guardar(u);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "⚠️ El usuario ya existe.",
                        "Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cargarDesdeBD();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Usuario guardado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // ───────────────────────────────────────────────
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error BD:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ " + ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editarUsuario() {
        int fila = getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un usuario para editar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreReal = getNombreReal();
        String usuario    = getUsuario();
        String password   = getPassword();
        String rol        = getRol();

        if (nombreReal.isEmpty() || usuario.isEmpty() || password.isEmpty() || rol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // ── NUEVO ───────────────────────────────────────
            int idUsuario = idsUsuarios.get(fila);
            Usuario u = new Usuario(nombreReal, usuario, password, rol);
            boolean ok = dao.actualizar(idUsuario, u);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                    "⚠️ No se encontró el usuario en la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            cargarDesdeBD();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Usuario actualizado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // ───────────────────────────────────────────────
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error BD:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        int fila = getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un usuario para eliminar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar este usuario?",
                "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                // ── NUEVO ───────────────────────────────────
                int idUsuario = idsUsuarios.get(fila);
                boolean ok = dao.eliminar(idUsuario);
                if (!ok) {
                    JOptionPane.showMessageDialog(this,
                        "⚠️ No se encontró el usuario en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                cargarDesdeBD();
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "✅ Usuario eliminado exitosamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                // ───────────────────────────────────────────
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error BD:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarUsuario() {
        String texto = getTextoBusqueda();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            // ── NUEVO ───────────────────────────────────────
            List<Object[]> resultados = dao.buscar(texto);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ No se encontró ningún usuario.",
                        "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            limpiarTabla();
            idsUsuarios.clear();
            for (Object[] row : resultados) {
                idsUsuarios.add((Integer) row[0]);
                tableModel.addRow(new Object[]{ row[2], row[3], row[4] });
            }
            dataTable.setRowSelectionInterval(0, 0);
            cargarUsuarioSeleccionado();
            // ───────────────────────────────────────────────
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error BD:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        // ── NUEVO: campo Nombre Real ─────────────────────
        c.gridx = 0; c.gridy = 0; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Nombre Real:"), c);
        c.gridx = 1; c.weightx = 0.2;
        txtNombreReal = new JTextField(15);
        fieldsPanel.add(txtNombreReal, c);
        // ─────────────────────────────────────────────────

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Usuario:"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtUsuario = new JTextField(15);
        fieldsPanel.add(txtUsuario, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Contraseña:"), c);
        c.gridx = 1; c.weightx = 0.2;
        txtPassword = new JPasswordField(15);
        fieldsPanel.add(txtPassword, c);

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Rol:"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtRol = new JTextField(15);
        fieldsPanel.add(txtRol, c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setOpaque(false);
        btnGuardar  = crearBoton("💾 Guardar",  new Color(34, 139, 34));
        btnEditar   = crearBoton("✏️ Editar",   new Color(30, 144, 255));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(220, 20, 60));
        btnLimpiar  = crearBoton("Limpiar",     new Color(169, 169, 169));
        btnNuevo    = crearBoton("➕ Nuevo",    new Color(184, 134, 11));
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
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));
        String[] columns = {"Usuario", "Contraseña", "Rol"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setRowHeight(25);
        dataTable.getTableHeader().setReorderingAllowed(false);
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarUsuarioSeleccionado();
        });
        tableContainer.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        return tableContainer;
    }

    private JPanel crearSearchPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Buscar por usuario o rol:"));
        txtBuscar = new JTextField(20);
        searchPanel.add(txtBuscar);
        btnLupa = crearBoton("🔍 Buscar", new Color(70, 130, 180));
        searchPanel.add(btnLupa);
        JButton btnTodos = crearBoton("🔄 Todos", new Color(100, 100, 100));
        btnTodos.addActionListener(e -> cargarDesdeBD());
        searchPanel.add(btnTodos);
        container.add(searchPanel, BorderLayout.WEST);
        return container;
    }

    public void limpiarCampos() {
        txtNombreReal.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
        txtRol.setText("");
        txtBuscar.setText("");
        dataTable.clearSelection();
    }

    public void cargarUsuarioSeleccionado() {
        int fila = dataTable.getSelectedRow();
        if (fila != -1) {
            // nombreReal no está en tabla, lo dejamos vacío
            txtNombreReal.setText("");
            txtUsuario.setText(tableModel.getValueAt(fila, 0).toString());
            txtPassword.setText(tableModel.getValueAt(fila, 1).toString());
            txtRol.setText(tableModel.getValueAt(fila, 2).toString());
        }
    }

    public void agregarFilaTabla(Object[] fila)           { tableModel.addRow(fila); }
    public void actualizarFilaTabla(int fila, Object[] d) { for (int i = 0; i < d.length; i++) tableModel.setValueAt(d[i], fila, i); }
    public void eliminarFilaTabla(int fila)               { tableModel.removeRow(fila); }
    public void limpiarTabla()                            { tableModel.setRowCount(0); }

    public String getNombreReal()     { return txtNombreReal.getText().trim(); }
    public String getUsuario()        { return txtUsuario.getText().trim(); }
    public String getPassword()       { return new String(txtPassword.getPassword()).trim(); }
    public String getRol()            { return txtRol.getText().trim(); }
    public String getTextoBusqueda()  { return txtBuscar.getText().trim(); }
    public int    getFilaSeleccionada(){ return dataTable.getSelectedRow(); }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getDataTable()      { return dataTable; }

    public void agregarListenerGuardar(ActionListener l)  { btnGuardar.addActionListener(l); }
    public void agregarListenerEditar(ActionListener l)   { btnEditar.addActionListener(l); }
    public void agregarListenerEliminar(ActionListener l) { btnEliminar.addActionListener(l); }
    public void agregarListenerLimpiar(ActionListener l)  { btnLimpiar.addActionListener(l); }
    public void agregarListenerNuevo(ActionListener l)    { btnNuevo.addActionListener(l); }
    public void agregarListenerBuscar(ActionListener l)   { btnLupa.addActionListener(l); }
}