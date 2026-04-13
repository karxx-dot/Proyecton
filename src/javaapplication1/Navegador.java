package javaapplication1;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class Navegador {

    private static Navegador instancia;
    private CardLayout cardLayout;
    private JPanel panelContenido;

    private Navegador() {}

    public static Navegador getInstance() {
        if (instancia == null) {
            instancia = new Navegador();
        }
        return instancia;
    }

    public void init(JPanel panel, CardLayout card) {
        this.panelContenido = panel;
        this.cardLayout = card;

        Form_Home inicio = new Form_Home();
        javaapplication1.vistas.GestionClientesUI clientes = new javaapplication1.vistas.GestionClientesUI();
        javaapplication1.vistas.GestionPrestamosUI prestamos = new javaapplication1.vistas.GestionPrestamosUI();
        javaapplication1.vistas.GestionCuotasUI cobros = new javaapplication1.vistas.GestionCuotasUI();
        javaapplication1.vistas.GestionUsuariosUI usuarios = new javaapplication1.vistas.GestionUsuariosUI();
        javaapplication1.vistas.GestionSimuladorUI Simulador = new javaapplication1.vistas.GestionSimuladorUI();
        
        panelContenido.add(inicio,    "Inicio");
        panelContenido.add(clientes,  "Clientes");
        panelContenido.add(prestamos, "prestamos");
        panelContenido.add(cobros,    "Cobros");
        panelContenido.add(usuarios,  "Usuarios");
        panelContenido.add(Simulador, "simulador");
        cardLayout.show(panelContenido, "Inicio");
    }

    // ← ESTO FALTABA
    public void irA(String pantalla) {
    cardLayout.show(panelContenido, pantalla);
    
    // Recargar datos al navegar a Clientes
    if (pantalla.equals("Clientes")) {
        for (java.awt.Component comp : panelContenido.getComponents()) {
            if (comp instanceof javaapplication1.vistas.GestionClientesUI) {
                ((javaapplication1.vistas.GestionClientesUI) comp).cargarDesdeBD();
                break;
            }
        }
    }
}
}
