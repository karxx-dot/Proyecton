package javaapplication1.vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GestionSimuladorUI extends JPanel {

    private JTextField txtMonto;
    private JTextField txtCuotas;
    private JSpinner spnTasa;
    private JComboBox<String> cmbFormaPago;
    private JCheckBox chkSabado;
    private JCheckBox chkDomingo;
    private JFormattedTextField txtFecha;
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    public GestionSimuladorUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(242, 242, 242));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 650)); // ← NUEVO
        setPreferredSize(new Dimension(0, 650));               // ← NUEVO

        JPanel norte = crearPanelSuperior();
        norte.setPreferredSize(new Dimension(0, 200));

        add(norte, BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(242, 242, 242));

        JPanel panelInputs = new JPanel(new GridLayout(3, 4, 10, 8));
        panelInputs.setBackground(Color.WHITE);
        panelInputs.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Fila 1
        panelInputs.add(crearLabel("Monto Requerido"));
        txtMonto = new JTextField("5000");
        panelInputs.add(txtMonto);
        panelInputs.add(crearLabel("Nro de Cuotas"));
        txtCuotas = new JTextField("10");
        panelInputs.add(txtCuotas);

        // Fila 2
        panelInputs.add(crearLabel("Tasa de Interés %"));
        spnTasa = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 100.0, 0.5));
        panelInputs.add(spnTasa);
        panelInputs.add(crearLabel("Forma de Pago"));
        cmbFormaPago = new JComboBox<>(new String[]{"MENSUAL", "QUINCENAL", "SEMANAL"});
        panelInputs.add(cmbFormaPago);

        // Fila 3
        chkSabado = new JCheckBox("Pagos Sábado");
        chkSabado.setBackground(Color.WHITE);
        panelInputs.add(chkSabado);
        chkDomingo = new JCheckBox("Pagos Domingo");
        chkDomingo.setBackground(Color.WHITE);
        panelInputs.add(chkDomingo);
        panelInputs.add(crearLabel("Fecha"));
        txtFecha = new JFormattedTextField();
        txtFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        panelInputs.add(txtFecha);

        // Panel botones
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 5, 5));
        panelBotones.setBackground(new Color(242, 242, 242));
        panelBotones.add(crearBoton("PRÉSTAMO FRANCÉS",   () -> calcularFrances()));
        panelBotones.add(crearBoton("PRÉSTAMO ALEMÁN",    () -> calcularAleman()));
        panelBotones.add(crearBoton("PRÉSTAMO AMERICANO", () -> calcularAmericano()));
        panelBotones.add(crearBoton("PRÉSTAMO MANUAL",    () -> calcularManual()));
        panelBotones.add(crearBoton("PRÉSTAMO SIMPLE",    () -> calcularSimple()));
        panelBotones.add(new JLabel());

        panel.add(panelInputs, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.EAST);

        return panel;
    }

    private JScrollPane crearTabla() {
        modeloTabla = new DefaultTableModel(
            new String[]{"Nro", "Fecha", "Capital", "Interés", "Cuota", "Saldo"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getTableHeader().setBackground(new Color(230, 126, 34));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setGridColor(new Color(200, 200, 200));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);   // ← NUEVO
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // ← NUEVO

        return scroll;
    }

    private void calcularFrances() {
        try {
            modeloTabla.setRowCount(0);
            double monto = Double.parseDouble(txtMonto.getText());
            int cuotas = Integer.parseInt(txtCuotas.getText());
            double tasa = (double) spnTasa.getValue() / 100;
            double tasaPeriodo = ajustarTasa(tasa);

            double cuota;
            if (tasaPeriodo == 0) {
                cuota = monto / cuotas;
            } else {
                cuota = monto * tasaPeriodo * Math.pow(1 + tasaPeriodo, cuotas)
                        / (Math.pow(1 + tasaPeriodo, cuotas) - 1);
            }

            double saldo = monto;
            LocalDate fecha = LocalDate.now();

            for (int i = 1; i <= cuotas; i++) {
                fecha = siguienteFecha(fecha);
                double interes = saldo * tasaPeriodo;
                double capital = cuota - interes;
                saldo -= capital;
                modeloTabla.addRow(new Object[]{
                    i,
                    fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    String.format("%.2f", capital),
                    String.format("%.2f", interes),
                    String.format("%.2f", cuota),
                    String.format("%.2f", Math.max(saldo, 0))
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verifique los datos ingresados");
        }
    }

    private void calcularAleman() {
        try {
            modeloTabla.setRowCount(0);
            double monto = Double.parseDouble(txtMonto.getText());
            int cuotas = Integer.parseInt(txtCuotas.getText());
            double tasa = (double) spnTasa.getValue() / 100;
            double tasaPeriodo = ajustarTasa(tasa);
            double capital = monto / cuotas;
            double saldo = monto;
            LocalDate fecha = LocalDate.now();

            for (int i = 1; i <= cuotas; i++) {
                fecha = siguienteFecha(fecha);
                double interes = saldo * tasaPeriodo;
                double cuota = capital + interes;
                saldo -= capital;
                modeloTabla.addRow(new Object[]{
                    i,
                    fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    String.format("%.2f", capital),
                    String.format("%.2f", interes),
                    String.format("%.2f", cuota),
                    String.format("%.2f", Math.max(saldo, 0))
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verifique los datos ingresados");
        }
    }

    private void calcularAmericano() {
        try {
            modeloTabla.setRowCount(0);
            double monto = Double.parseDouble(txtMonto.getText());
            int cuotas = Integer.parseInt(txtCuotas.getText());
            double tasa = (double) spnTasa.getValue() / 100;
            double tasaPeriodo = ajustarTasa(tasa);
            LocalDate fecha = LocalDate.now();

            for (int i = 1; i <= cuotas; i++) {
                fecha = siguienteFecha(fecha);
                double interes = monto * tasaPeriodo;
                double capital = (i == cuotas) ? monto : 0;
                double cuota = interes + capital;
                double saldo = (i == cuotas) ? 0 : monto;
                modeloTabla.addRow(new Object[]{
                    i,
                    fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    String.format("%.2f", capital),
                    String.format("%.2f", interes),
                    String.format("%.2f", cuota),
                    String.format("%.2f", saldo)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verifique los datos ingresados");
        }
    }

    private void calcularSimple() {
        try {
            modeloTabla.setRowCount(0);
            double monto = Double.parseDouble(txtMonto.getText());
            int cuotas = Integer.parseInt(txtCuotas.getText());
            double tasa = (double) spnTasa.getValue() / 100;
            double tasaPeriodo = ajustarTasa(tasa);
            double interesFijo = monto * tasaPeriodo;
            double capital = monto / cuotas;
            double cuota = capital + interesFijo;
            double saldo = monto;
            LocalDate fecha = LocalDate.now();

            for (int i = 1; i <= cuotas; i++) {
                fecha = siguienteFecha(fecha);
                saldo -= capital;
                modeloTabla.addRow(new Object[]{
                    i,
                    fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    String.format("%.2f", capital),
                    String.format("%.2f", interesFijo),
                    String.format("%.2f", cuota),
                    String.format("%.2f", Math.max(saldo, 0))
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verifique los datos ingresados");
        }
    }

    private void calcularManual() {
        JOptionPane.showMessageDialog(this, "Modo manual: ingrese las cuotas manualmente");
    }

    private double ajustarTasa(double tasaAnual) {
        String forma = (String) cmbFormaPago.getSelectedItem();
        switch (forma) {
            case "QUINCENAL": return tasaAnual / 24;
            case "SEMANAL":   return tasaAnual / 52;
            default:          return tasaAnual / 12;
        }
    }

    private LocalDate siguienteFecha(LocalDate fecha) {
        boolean sabado = chkSabado.isSelected();
        boolean domingo = chkDomingo.isSelected();
        String forma = (String) cmbFormaPago.getSelectedItem();

        LocalDate siguiente;
        switch (forma) {
            case "QUINCENAL": siguiente = fecha.plusDays(15); break;
            case "SEMANAL":   siguiente = fecha.plusWeeks(1); break;
            default:          siguiente = fecha.plusMonths(1);
        }

        if (!sabado && siguiente.getDayOfWeek().getValue() == 6)
            siguiente = siguiente.plusDays(2);
        if (!domingo && siguiente.getDayOfWeek().getValue() == 7)
            siguiente = siguiente.plusDays(1);

        return siguiente;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    private JButton crearBoton(String texto, Runnable accion) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(230, 126, 34));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> accion.run());
        return btn;
    }
}