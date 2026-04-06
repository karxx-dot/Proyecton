
package javaapplication1;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;

public class menu extends javax.swing.JPanel {

   
    public menu() {
        initComponents();
        setOpaque(false);
        init();
        listMenu1.setOpaque(false);
    }
private void init(){
listMenu1.addItem(new Model_menu("iconfinder-computer-4341285_120548", "Inicio", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "Clientes", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "simulador", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "prestamos", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "Cobros", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("", " ", Model_menu.MenuType.EMPTY));

listMenu1.addItem(new Model_menu("", "My Data", Model_menu.MenuType.TITLE));
listMenu1.addItem(new Model_menu("", " ", Model_menu.MenuType.EMPTY));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "reportes", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "caja", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "Usuarios", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "Ajustes", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("iconfinder-file-4341289_120551", "", Model_menu.MenuType.MENU));
listMenu1.addItem(new Model_menu("", " ", Model_menu.MenuType.EMPTY));
    
}
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMoving = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        listMenu1 = new javaapplication1.ListMenu<>();

        panelMoving.setOpaque(false);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/javaapplication1/iconfinder-computer-4341285_120548.png"))); // NOI18N
        jLabel1.setText("Aplicacion");

        javax.swing.GroupLayout panelMovingLayout = new javax.swing.GroupLayout(panelMoving);
        panelMoving.setLayout(panelMovingLayout);
        panelMovingLayout.setHorizontalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMovingLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMovingLayout.setVerticalGroup(
            panelMovingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMovingLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelMoving, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMoving, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    @Override
    protected void paintChildren(Graphics grphcs) {
    Graphics2D g2 = (Graphics2D) grphcs;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    GradientPaint g = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046"));
    g2.setPaint(g);
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
    g2.fillRect(getWidth() - 20, 0, getWidth(), getHeight());
    super.paintChildren(grphcs);
}
private int x;
private int y;

public void initMoving(JFrame fram) {
    panelMoving.addMouseListener(new MouseAdapter() {
        
        @Override
        public void mousePressed(MouseEvent me) {
            x = me.getX();
            y = me.getY();
        }
    });

    panelMoving.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent me) {
            fram.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
        }
    });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javaapplication1.ListMenu<String> listMenu1;
    private javax.swing.JPanel panelMoving;
    // End of variables declaration//GEN-END:variables
}
