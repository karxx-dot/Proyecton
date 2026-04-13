package javaapplication1.vistas;

import Union.CuotaDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GestionCuotasUI extends javax.swing.JPanel {

    private JComboBox<String> cbPrestamo;
    private JTextField txtMonto, txtCuotas, txtTasa, txtFechaInicio, txtEstado, txtBuscar;
    private JButton btnCalcular, btnGuardar, btnEliminar, btnLimpiar, btnNuevo, btnLupa;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    private final CuotaDAO dao = new CuotaDAO();
    private List<Integer> idsDetalles = new ArrayList<>();
    private static List<GestionCuotasUI> observadores = new ArrayList<>();

    public GestionCuotasUI() {
    setLayout(new BorderLayout());
    setBorder(new EmptyBorder(15, 15, 15, 15));
    setBackground(Color.WHITE);

    add(crearFormPanel(),   BorderLayout.NORTH);
    add(crearTablePanel(),  BorderLayout.CENTER);
    add(crearSearchPanel(), BorderLayout.SOUTH);

    inicializarControlador();
    GestionCuotasUI.agregarObservador(this);
    actualizarComboPrestamos();
}

    private void inicializarControlador() {
        btnCalcular.addActionListener(e -> calcularCuotas());
        btnGuardar.addActionListener(e  -> guardarCuotas());
        btnEliminar.addActionListener(e -> eliminarCuota());
        btnLupa.addActionListener(e     -> buscarCuota());
        btnLimpiar.addActionListener(e  -> limpiarCampos());
        btnNuevo.addActionListener(e    -> limpiarCampos());
    }

    // ==================== CÁLCULOS ====================

    private void calcularCuotas() {
        String tipo = getPrestamo();
        if (tipo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona un tipo de préstamo.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double monto  = Double.parseDouble(txtMonto.getText().trim());
            int    cuotas = Integer.parseInt(txtCuotas.getText().trim());
            double tasa   = Double.parseDouble(txtTasa.getText().trim());
            LocalDate fecha = LocalDate.parse(txtFechaInicio.getText().trim(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            limpiarTabla();

            switch (tipo) {
                case "PRÉSTAMO FRANCÉS":   calcularFrances(monto, cuotas, tasa, fecha);   break;
                case "PRÉSTAMO ALEMÁN":    calcularAleman(monto, cuotas, tasa, fecha);    break;
                case "PRÉSTAMO AMERICANO": calcularAmericano(monto, cuotas, tasa, fecha); break;
                case "PRÉSTAMO SIMPLE":    calcularSimple(monto, cuotas, tasa, fecha);    break;
                case "PRÉSTAMO MANUAL":
                    JOptionPane.showMessageDialog(this,
                        "Modo manual: agrega las cuotas una por una con '➕ Nuevo'.",
                        "Manual", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Monto, Cuotas y Tasa deben ser números.\nFecha: dd/MM/yyyy",
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "⚠️ Verifica los datos ingresados.\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calcularFrances(double monto, int cuotas, double tasaAnual, LocalDate fecha) {
        double tasaMensual = tasaAnual / 100.0 / 12.0;
        double cuota = tasaMensual == 0 ? monto / cuotas :
            monto * tasaMensual * Math.pow(1 + tasaMensual, cuotas)
            / (Math.pow(1 + tasaMensual, cuotas) - 1);
        double saldo = monto;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 1; i <= cuotas; i++) {
            fecha = fecha.plusMonths(1);
            double interes = saldo * tasaMensual;
            double capital = cuota - interes;
            saldo = Math.max(saldo - capital, 0);
            tableModel.addRow(new Object[]{
                i,
                fecha.format(fmt),
                String.format("%.2f", capital),
                String.format("%.2f", interes),
                String.format("%.2f", cuota),
                String.format("%.2f", saldo)
            });
        }
    }

    private void calcularAleman(double monto, int cuotas, double tasaAnual, LocalDate fecha) {
        double tasaMensual = tasaAnual / 100.0 / 12.0;
        double capital = monto / cuotas;
        double saldo = monto;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 1; i <= cuotas; i++) {
            fecha = fecha.plusMonths(1);
            double interes = saldo * tasaMensual;
            double cuota = capital + interes;
            saldo = Math.max(saldo - capital, 0);
            tableModel.addRow(new Object[]{
                i,
                fecha.format(fmt),
                String.format("%.2f", capital),
                String.format("%.2f", interes),
                String.format("%.2f", cuota),
                String.format("%.2f", saldo)
            });
        }
    }

    private void calcularAmericano(double monto, int cuotas, double tasaAnual, LocalDate fecha) {
        double tasaMensual = tasaAnual / 100.0 / 12.0;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 1; i <= cuotas; i++) {
            fecha = fecha.plusMonths(1);
            double interes = monto * tasaMensual;
            double capital = (i == cuotas) ? monto : 0;
            double cuota   = interes + capital;
            double saldo   = (i == cuotas) ? 0 : monto;
            tableModel.addRow(new Object[]{
                i,
                fecha.format(fmt),
                String.format("%.2f", capital),
                String.format("%.2f", interes),
                String.format("%.2f", cuota),
                String.format("%.2f", saldo)
            });
        }
    }

    private void calcularSimple(double monto, int cuotas, double tasaAnual, LocalDate fecha) {
        double tasaMensual  = tasaAnual / 100.0 / 12.0;
        double interesFijo  = monto * tasaMensual;
        double capital      = monto / cuotas;
        double cuota        = capital + interesFijo;
        double saldo        = monto;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 1; i <= cuotas; i++) {
            fecha = fecha.plusMonths(1);
            saldo = Math.max(saldo - capital, 0);
            tableModel.addRow(new Object[]{
                i,
                fecha.format(fmt),
                String.format("%.2f", capital),
                String.format("%.2f", interesFijo),
                String.format("%.2f", cuota),
                String.format("%.2f", saldo)
            });
        }
    }

    // ==================== GUARDAR EN BD ====================

    private void guardarCuotas() {
    if (tableModel.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this,
            "⚠️ Primero calcula las cuotas antes de guardar.",
            "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        Union.PrestamoDAO prestamoDAO = new Union.PrestamoDAO();
        List<Object[]> prestamos = prestamoDAO.listarTodos();
        
        System.out.println("Préstamos en BD: " + prestamos.size()); // ← DIAGNÓSTICO

        if (prestamos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "⚠️ No hay préstamos registrados en la BD.\nGuarda un préstamo primero.",
                "Sin préstamos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] opciones = new String[prestamos.size()];
        int[] ids = new int[prestamos.size()];
        for (int i = 0; i < prestamos.size(); i++) {
            ids[i] = (int) prestamos.get(i)[0];
            opciones[i] = "Préstamo #" + prestamos.get(i)[0] + " - " + prestamos.get(i)[1]
                        + " - $" + prestamos.get(i)[2];
            System.out.println("Opción: " + opciones[i]); // ← DIAGNÓSTICO
        }

        String seleccion = (String) JOptionPane.showInputDialog(
            this,
            "Selecciona el préstamo al que pertenecen estas cuotas:",
            "Seleccionar Préstamo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (seleccion == null) return;

        int idPrestamo = -1;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) {
                idPrestamo = ids[i];
                break;
            }
        }

        System.out.println("idPrestamo seleccionado: " + idPrestamo); // ← DIAGNÓSTICO

        if (idPrestamo == -1) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int guardadas = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int       nro     = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
                LocalDate fecha   = LocalDate.parse(tableModel.getValueAt(i, 1).toString(), fmt);
                double capital = Double.parseDouble(tableModel.getValueAt(i, 2).toString().replace(",", "."));
                double interes = Double.parseDouble(tableModel.getValueAt(i, 3).toString().replace(",", "."));
                double cuota   = Double.parseDouble(tableModel.getValueAt(i, 4).toString().replace(",", "."));
                double saldo   = Double.parseDouble(tableModel.getValueAt(i, 5).toString().replace(",", "."));

                System.out.println("Guardando fila " + i + ": nro=" + nro + " fecha=" + fecha); // ← DIAGNÓSTICO

                javaapplication1.modelo.Cuota c = new javaapplication1.modelo.Cuota(
                    nro, fecha, capital, interes, cuota, saldo);
                boolean ok = dao.guardar(idPrestamo, c);
                System.out.println("  → guardado: " + ok); // ← DIAGNÓSTICO
                if (ok) guardadas++;

            } catch (Exception ex) {
                System.out.println("ERROR en fila " + i + ": " + ex.getMessage()); // ← DIAGNÓSTICO
                ex.printStackTrace();
            }
        }

        JOptionPane.showMessageDialog(this,
            "✅ " + guardadas + " cuotas guardadas correctamente.",
            "Éxito", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
            "❌ Error BD:\n" + ex.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    private void eliminarCuota() {
        int fila = getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "⚠️ Selecciona una cuota para eliminar.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar esta cuota?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                if (!idsDetalles.isEmpty() && fila < idsDetalles.size()) {
                    dao.eliminar(idsDetalles.get(fila));
                    idsDetalles.remove(fila);
                }
                tableModel.removeRow(fila);
                limpiarCampos();
                JOptionPane.showMessageDialog(this, "✅ Cuota eliminada.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error BD:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarCuota() {
    String texto = txtBuscar.getText().trim().toLowerCase();
    if (texto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "⚠️ Ingresa un texto para buscar.",
                "Validación", JOptionPane.WARNING_MESSAGE);
        return;
    }
    for (int i = 0; i < tableModel.getRowCount(); i++) {
        for (int j = 0; j < tableModel.getColumnCount(); j++) {  // ← recorre todas las columnas
            if (tableModel.getValueAt(i, j).toString().toLowerCase().contains(texto)) {
                dataTable.setRowSelectionInterval(i, i);
                dataTable.scrollRectToVisible(dataTable.getCellRect(i, 0, true)); // ← hace scroll hasta la fila
                JOptionPane.showMessageDialog(this, "✅ Cuota encontrada en fila " + (i+1),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }
    JOptionPane.showMessageDialog(this, "❌ No se encontró ninguna cuota.",
            "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
}

    // ==================== UI ====================

    private JPanel crearFormPanel() {
    JPanel formContainer = new JPanel(new BorderLayout());
    formContainer.setBackground(Color.WHITE);
    formContainer.setBorder(BorderFactory.createTitledBorder("Formulario de Cuota"));
    formContainer.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 220)); // ← AGREGAR
    formContainer.setPreferredSize(new java.awt.Dimension(0, 220));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setOpaque(false);
        fieldsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0 - Tipo préstamo y Monto
        c.gridx = 0; c.gridy = 0; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Tipo Préstamo:"), c);
        c.gridx = 1; c.weightx = 0.2;
        cbPrestamo = new JComboBox<>();
        fieldsPanel.add(cbPrestamo, c);

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Monto:"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtMonto = new JTextField("5000.00", 15);
        fieldsPanel.add(txtMonto, c);

        // Fila 1 - Cuotas y Tasa
        c.gridx = 0; c.gridy = 1; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Nro. Cuotas:"), c);
        c.gridx = 1; c.weightx = 0.2;
        txtCuotas = new JTextField("12", 15);
        fieldsPanel.add(txtCuotas, c);

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Tasa Anual (%):"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtTasa = new JTextField("5.5", 15);
        fieldsPanel.add(txtTasa, c);

        // Fila 2 - Fecha y Estado
        c.gridx = 0; c.gridy = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Fecha Inicio:"), c);
        c.gridx = 1; c.weightx = 0.2;
        txtFechaInicio = new JTextField(
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 15);
        fieldsPanel.add(txtFechaInicio, c);

        c.gridx = 2; c.weightx = 0.1;
        fieldsPanel.add(new JLabel("Estado:"), c);
        c.gridx = 3; c.weightx = 0.2;
        txtEstado = new JTextField("Pendiente", 15);
        fieldsPanel.add(txtEstado, c);

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        buttonPanel.setOpaque(false);
        btnCalcular = crearBoton("🧮 Calcular", new Color(70, 130, 180));
        btnGuardar  = crearBoton("💾 Guardar BD", new Color(34, 139, 34));
        btnEliminar = crearBoton("🗑️ Eliminar",  new Color(220, 20, 60));
        btnLimpiar  = crearBoton("Limpiar",       new Color(169, 169, 169));
        btnNuevo    = crearBoton("➕ Nuevo",      new Color(184, 134, 11));
        buttonPanel.add(btnCalcular);
        buttonPanel.add(btnGuardar);
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
        boton.setPreferredSize(new Dimension(130, 35));
        return boton;
    }

    private JPanel crearTablePanel() {
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Columnas con Capital e Interés visibles
        String[] columns = {"Nro", "Fecha Pago", "Capital", "Interés", "Cuota Total", "Saldo"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setRowHeight(25);
        dataTable.getTableHeader().setReorderingAllowed(false);
        dataTable.getTableHeader().setBackground(new Color(70, 130, 180));
        dataTable.getTableHeader().setForeground(Color.WHITE);
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarCuotaSeleccionada();
        });
        tableContainer.add(new JScrollPane(dataTable), BorderLayout.CENTER);
        return tableContainer;
    }

    private JPanel crearSearchPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setOpaque(false);
        searchPanel.add(new JLabel("Buscar cuota:"));
        txtBuscar = new JTextField(20);
        searchPanel.add(txtBuscar);
        btnLupa = crearBoton("🔍 Buscar", new Color(70, 130, 180));
        searchPanel.add(btnLupa);
        JButton btnTodos = crearBoton("🔄 Limpiar", new Color(100, 100, 100));
        btnTodos.addActionListener(e -> limpiarCampos());
        searchPanel.add(btnTodos);
        container.add(searchPanel, BorderLayout.WEST);
        return container;
    }

    // ==================== MÉTODOS PÚBLICOS ====================

    public void limpiarCampos() {
        cbPrestamo.setSelectedIndex(0);
        txtMonto.setText("5000.00");
        txtCuotas.setText("12");
        txtTasa.setText("5.5");
        txtFechaInicio.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtEstado.setText("Pendiente");
        txtBuscar.setText("");
        limpiarTabla();
        dataTable.clearSelection();
        idsDetalles.clear();
    }

    public void cargarCuotaSeleccionada() {
        int fila = dataTable.getSelectedRow();
        if (fila != -1) {
            txtCuotas.setText(tableModel.getValueAt(fila, 0).toString());
            txtFechaInicio.setText(tableModel.getValueAt(fila, 1).toString());
            txtMonto.setText(tableModel.getValueAt(fila, 4).toString());
        }
    }

    public void agregarFilaTabla(Object[] fila)           { tableModel.addRow(fila); }
    public void actualizarFilaTabla(int fila, Object[] d) { for (int i = 0; i < d.length; i++) tableModel.setValueAt(d[i], fila, i); }
    public void eliminarFilaTabla(int fila)               { tableModel.removeRow(fila); }
    public void limpiarTabla()                            { tableModel.setRowCount(0); }

    public void actualizarComboPrestamos() {
        cbPrestamo.removeAllItems();
        cbPrestamo.addItem("PRÉSTAMO FRANCÉS");
        cbPrestamo.addItem("PRÉSTAMO ALEMÁN");
        cbPrestamo.addItem("PRÉSTAMO AMERICANO");
        cbPrestamo.addItem("PRÉSTAMO SIMPLE");
        cbPrestamo.addItem("PRÉSTAMO MANUAL");
    }

    public void cargarDesdeBD() {
        // Método mantenido por compatibilidad con Navegador.java
    }

    public String getPrestamo()      { Object o = cbPrestamo.getSelectedItem(); return o == null ? "" : o.toString(); }
    public String getNumeroCuota()   { return txtCuotas.getText().trim(); }
    public String getMonto()         { return txtMonto.getText().trim(); }
    public String getFechaPago()     { return txtFechaInicio.getText().trim(); }
    public String getEstado()        { return txtEstado.getText().trim(); }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }
    public int    getFilaSeleccionada() { return dataTable.getSelectedRow(); }
    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getDataTable()     { return dataTable; }

    public void agregarListenerGuardar(ActionListener l)  { btnGuardar.addActionListener(l); }
    public void agregarListenerEditar(ActionListener l)   { /* editar reemplazado por calcular */ }
    public void agregarListenerEliminar(ActionListener l) { btnEliminar.addActionListener(l); }
    public void agregarListenerLimpiar(ActionListener l)  { btnLimpiar.addActionListener(l); }
    public void agregarListenerNuevo(ActionListener l)    { btnNuevo.addActionListener(l); }
    public void agregarListenerBuscar(ActionListener l)   { btnLupa.addActionListener(l); }

    public static void agregarPrestamoGlobal(String p)       { }
    public static void eliminarPrestamoGlobal(String p)      { }
    public static void agregarObservador(GestionCuotasUI ui) { observadores.add(ui); }
    public static List<String> obtenerPrestamosGlobales()    { return new ArrayList<>(); }
}