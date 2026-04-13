
package javaapplication1;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;


public class Form_Home extends javax.swing.JPanel {

  
    public Form_Home() {
        initComponents();
        card1.setData(new Model_Card(new ImageIcon(getClass().getResource("")), "Cantidad a devolver", "80.000$", "texto"));
card2.setData(new Model_Card(new ImageIcon(getClass().getResource("")), "Texto", "texto", "texto"));
card3.setData(new Model_Card(new ImageIcon(getClass().getResource("")), "texto", "texto", "texto"));
    //AGREGAR LAS TABLAS 
    
    spTable.setVerticalScrollBar(new JScrollBar());
    spTable.getVerticalScrollBar(). setBackground(Color.WHITE);
    spTable.getViewport().setBackground(Color.white);
    JPanel p = new JPanel();
    p.setBackground(Color.WHITE);
    spTable.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p);
    
table.addRow(new Object[]{"Mike Bhand", "mikebhand@gmail.com", "cliente", "25 Apr,2018", StatusType.Pendiente});
table.addRow(new Object[]{"Andrew Strauss", "andrewstrauss@gmail.com", "cliente", "25 Apr,2018", StatusType.aprobado});
table.addRow(new Object[]{"Ross Kopelman", "rosskopelman@gmail.com", "cliente", "25 Apr,2018", StatusType.aprobado});
table.addRow(new Object[]{"Mike Hussy", "mikehussy@gmail.com", "cliente", "25 Apr,2018", StatusType.rechazado});
table.addRow(new Object[]{"Kevin Pietersen", "kevinpietersen@gmail.com", "Admin", "25 Apr,2018", StatusType.Pendiente});
table.addRow(new Object[]{"Andrew Strauss", "andrewstrauss@gmail.com", "cliente", "25 Apr,2018", StatusType.aprobado});
table.addRow(new Object[]{"Ross Kopelman", "rosskopelman@gmail.com", "cliente", "25 Apr,2018", StatusType.aprobado});
table.addRow(new Object[]{"Mike Hussy", "mikehussy@gmail.com", "Admin", "25 Apr,2018", StatusType.rechazado});
table.addRow(new Object[]{"Kevin Pietersen", "kevinpietersen@gmail.com", "Admin", "25 Apr,2018", StatusType.Pendiente});
table.addRow(new Object[]{"Andrew Strauss", "andrewstrauss@gmail.com", "cliente", "25 Apr,2018", StatusType.aprobado});
table.addRow(new Object[]{"Ross Kopelman", "rosskopelman@gmail.com", "cliente", "25 Apr,2018", StatusType.aprobado});
table.addRow(new Object[]{"Mike Hussy", "mikehussy@gmail.com", "empleado", "25 Apr,2018", StatusType.rechazado});
table.addRow(new Object[]{"Kevin Pietersen", "kevinpietersen@gmail.com", "cliente", "25 Apr,2018", StatusType.rechazado});
    
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        card1 = new javaapplication1.Card();
        card2 = new javaapplication1.Card();
        card3 = new javaapplication1.Card();
        panelBorder1 = new javaapplication1.PanelBorder();
        jLabel1 = new javax.swing.JLabel();
        spTable = new javax.swing.JScrollPane();
        table = new javaapplication1.Table();

        panel.setLayout(new java.awt.GridLayout(1, 0, 10, 0));
        panel.add(card1);
        panel.add(card2);
        panel.add(card3);

        panelBorder1.setBackground(new java.awt.Color(255, 255, 255));
        panelBorder1.setPreferredSize(new java.awt.Dimension(740, 780));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(127, 127, 127));
        jLabel1.setText("tabla de usuarios");

        spTable.setPreferredSize(new java.awt.Dimension(740, 700));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "nombre", "Email", "tipo", "fecha", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        spTable.setViewportView(table);

        javax.swing.GroupLayout panelBorder1Layout = new javax.swing.GroupLayout(panelBorder1);
        panelBorder1.setLayout(panelBorder1Layout);
        panelBorder1Layout.setHorizontalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(575, Short.MAX_VALUE))
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spTable, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBorder1Layout.setVerticalGroup(
            panelBorder1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBorder1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spTable, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelBorder1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(panelBorder1, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(423, 423, 423))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javaapplication1.Card card1;
    private javaapplication1.Card card2;
    private javaapplication1.Card card3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panel;
    private javaapplication1.PanelBorder panelBorder1;
    private javax.swing.JScrollPane spTable;
    private javaapplication1.Table table;
    // End of variables declaration//GEN-END:variables
}
