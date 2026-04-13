package javaapplication1.vistas;

import javaapplication1.modelo.Cliente;
import Union.ClienteDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestionClientesUI extends JPanel {

    private JTextField txtCedula, txtNombre, txtTelefono, txtEmail, txtBuscar;
    private JButton btnGuardar, btnEditar, btnEliminar, btnLupa, btnLimpiar, btnNuevo;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    private final ClienteDAO dao = new ClienteDAO();

    private static List<String> clientesGlobales = new ArrayList<>();
    private static List<GestionPrestamosUI> observadores = new ArrayList<>();

    public GestionClientesUI() {
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
        cargarDesdeBD();
    }

    private void inicializarControlador() {
        btnGuardar.addActionListener(e  -> guardarCliente());
        btnEditar.addActionListener(e   -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnLupa.addActionListener(e     -> buscarCliente());
        btnLimpiar.addActionListener(e  -> limpiarCampos());
        btnNuevo.addActionListener(e    -> limpiarCampos());
    }

    private void guardarCliente() {
        String cedula   = getCedula();
        String nombre   = getNombre();
        String telefono = getTelefono();
        String email    = getEmail();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            boolean guardado = dao.guardar(new Cliente(cedula, nombre, telefono, email));
            if (!guardado) {
                JOptionPane.showMessageDialog(this, "⚠️ La cédula ya existe en la base de datos.",
                    "Duplicado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            agregarFilaTabla(new Object[]{cedula, nombre, telefono, email});
            agregarClienteGlobal(nombre);
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Cliente guardado exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarErrorBD("guardar el cliente", ex);
        }
    }

    private void editarCliente() {
        int fila = getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un cliente para editar.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedula   = getCedula();
        String nombre   = getNombre();
        String telefono = getTelefono();
        String email    = getEmail();

        if (cedula.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Por favor, completa todos los campos.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cedulaOriginal = tableModel.getValueAt(fila, 0).toString();

        try {
            boolean ok = dao.actualizar(cedulaOriginal, new Cliente(cedula, nombre, telefono, email));
            if (!ok) {
                JOptionPane.showMessageDialog(this, "⚠️ No se encontró el cliente en la base de datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            actualizarFilaTabla(fila, new Object[]{cedula, nombre, telefono, email});
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "✅ Cliente actualizado exitosamente.",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarErrorBD("editar el cliente", ex);
        }
    }

    private void eliminarCliente() {
        int fila = getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un cliente para eliminar.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que deseas eliminar este cliente?",
            "Confirmación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            String cedulaEliminar  = tableModel.getValueAt(fila, 0).toString();
            String nombreEliminado = tableModel.getValueAt(fila, 1).toString();
            try {
                boolean ok = dao.eliminar(cedulaEliminar);
                if (!ok) {
                    JOptionPane.showMessageDialog(this, "⚠️ No se encontró el cliente en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                eliminarFilaTabla(fila);
                eliminarClienteGlobal(nombreEliminado);
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "✅ Cliente eliminado exitosamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                mostrarErrorBD("eliminar el cliente", ex);
            }
        }
    }

    private void buscarCliente() {
        String texto = getTextoBusqueda();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.",
                "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<Cliente> resultados = dao.buscar(texto);
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ No se encontró ningún cliente.",
                    "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            limpiarTabla();
            for (Cliente c : resultados)
                agregarFilaTabla(new Object[]{c.getCedula(), c.getNombre(), c.getTelefono(), c.getEmail()});
            dataTable.setRowSelectionInterval(0, 0);
            cargarClienteSeleccionado();
            String msg = resultados.size() == 1 ? "✅ Cliente encontrado."
                : "✅ Se encontraron " + resultados.size() + " clientes.";
            JOptionPane.showMessageDialog(this, msg, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            mostrarErrorBD("buscar clientes", ex);
        }
    }

    private void cargarDesdeBD() {
        try {
            List<Cliente> clientes = dao.listarTodos();
            limpiarTabla();
            clientesGlobales.clear();
            for (Cliente c : clientes) {
                agregarFilaTabla(new Object[]{c.getCedula(), c.getNombre(), c.getTelefono(), c.getEmail()});
                clientesGlobales.add(c.getNombre());
            }
            notificarObservadores();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "⚠️ No se pudo conectar a la base de datos.\n\n" + ex.getMessage()
                + "\n\nRevisa usuario/contraseña en ConexionDB.java",
                "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarErrorBD(String accion, SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "❌ Error al " + accion + ":\n" + ex.getMessage(),
            "Error de base de datos", JOptionPane.ERROR_MESSAGE);
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

        c.gridx = 0; c.gridy = 0; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Cédula:"), c);
        c.gridx = 1; c.weightx = 0.2;
        txtCedula = new JTextField(15);
        txtCedula.setToolTipText("Ej: 001-XXXXXXX-X");
        fieldsPanel.add(txtCedula, c);

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Nombre:"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtNombre = new JTextField(15);
        txtNombre.setToolTipText("Ingrese el nombre completo");
        fieldsPanel.add(txtNombre, c);

        c.gridx = 0; c.gridy = 1; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Teléfono:"), c);
        c.gridx = 1; c.weightx = 0.2;
        txtTelefono = new JTextField(15);
        txtTelefono.setToolTipText("Ej: 809-XXX-XXXX");
        fieldsPanel.add(txtTelefono, c);

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Email:"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtEmail = new JTextField(15);
        txtEmail.setToolTipText("Ej: correo@ejemplo.com");
        fieldsPanel.add(txtEmail, c);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setOpaque(false);
        btnGuardar  = crearBoton("💾 Guardar",  new Color(34,  139,  34));
        btnEditar   = crearBoton("✏️ Editar",   new Color(30,  144, 255));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(220,  20,  60));
        btnLimpiar  = crearBoton("Limpiar",     new Color(169, 169, 169));
        btnNuevo    = crearBoton("➕ Nuevo",    new Color(184, 134,  11));
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
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    private JPanel crearTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));
        String[] columns = {"Cédula", "Nombre", "Teléfono", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setRowHeight(25);
        dataTable.getTableHeader().setReorderingAllowed(false);
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarClienteSeleccionado();
        });
        tableContainer.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        return tableContainer;
    }

    private JPanel crearSearchPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Buscar por cédula o nombre:"));
        txtBuscar = new JTextField(20);
        txtBuscar.setToolTipText("Ingrese cédula o nombre");
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
        txtCedula.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtBuscar.setText("");
        dataTable.clearSelection();
    }

    public void cargarClienteSeleccionado() {
        int fila = dataTable.getSelectedRow();
        if (fila != -1) {
            txtCedula.setText((String) tableModel.getValueAt(fila, 0));
            txtNombre.setText((String) tableModel.getValueAt(fila, 1));
            txtTelefono.setText((String) tableModel.getValueAt(fila, 2));
            txtEmail.setText((String) tableModel.getValueAt(fila, 3));
        }
    }

    public void agregarFilaTabla(Object[] fila)            { tableModel.addRow(fila); }
    public void actualizarFilaTabla(int fila, Object[] d)  { for (int i = 0; i < d.length; i++) tableModel.setValueAt(d[i], fila, i); }
    public void eliminarFilaTabla(int fila)                { tableModel.removeRow(fila); }
    public void limpiarTabla()                             { tableModel.setRowCount(0); }

    public String getCedula()           { return txtCedula.getText().trim(); }
    public String getNombre()           { return txtNombre.getText().trim(); }
    public String getTelefono()         { return txtTelefono.getText().trim(); }
    public String getEmail()            { return txtEmail.getText().trim(); }
    public String getTextoBusqueda()    { return txtBuscar.getText().trim(); }
    public int    getFilaSeleccionada() { return dataTable.getSelectedRow(); }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getDataTable()        { return dataTable; }

    public void agregarListenerGuardar(ActionListener l)  { btnGuardar.addActionListener(l); }
    public void agregarListenerEditar(ActionListener l)   { btnEditar.addActionListener(l); }
    public void agregarListenerEliminar(ActionListener l) { btnEliminar.addActionListener(l); }
    public void agregarListenerLimpiar(ActionListener l)  { btnLimpiar.addActionListener(l); }
    public void agregarListenerNuevo(ActionListener l)    { btnNuevo.addActionListener(l); }
    public void agregarListenerBuscar(ActionListener l)   { btnLupa.addActionListener(l); }

    public static void agregarClienteGlobal(String nombre) {
        clientesGlobales.add(nombre);
        notificarObservadores();
    }

    public static void eliminarClienteGlobal(String nombre) {
        clientesGlobales.remove(nombre);
        notificarObservadores();
    }

    public static void agregarObservador(GestionPrestamosUI ui) { observadores.add(ui); }

    private static void notificarObservadores() {
        for (GestionPrestamosUI ui : observadores) ui.actualizarComboClientes();
    }

    public static List<String> obtenerClientesGlobales() { return clientesGlobales; }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); }
        catch (Exception e) { e.printStackTrace(); }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestión de Clientes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GestionClientesUI());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}