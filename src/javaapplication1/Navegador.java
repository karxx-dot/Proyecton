package javaapplication1;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Navegador {
    private static Navegador instancia;
    private CardLayout cardLayout;
    private JPanel panelContenido;

    // ← Guardar referencia al scroll del simulador
    private JScrollPane scrollSimulador;

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
        javaapplication1.vistas.GestionClientesUI  clientes  = new javaapplication1.vistas.GestionClientesUI();
        javaapplication1.vistas.GestionPrestamosUI prestamos = new javaapplication1.vistas.GestionPrestamosUI();
        javaapplication1.vistas.GestionCuotasUI    cobros    = new javaapplication1.vistas.GestionCuotasUI();
        javaapplication1.vistas.GestionUsuariosUI  usuarios  = new javaapplication1.vistas.GestionUsuariosUI();
        javaapplication1.vistas.GestionSimuladorUI simulador = new javaapplication1.vistas.GestionSimuladorUI();

        javaapplication1.vistas.GestionClientesUI.agregarObservador(prestamos);

        // ← Guardar el scroll del simulador para resetearlo después
        scrollSimulador = wrapScroll(simulador);

        panelContenido.add(inicio,    "Inicio");
        panelContenido.add(clientes,  "Clientes");
        panelContenido.add(prestamos, "prestamos");
        panelContenido.add(cobros,    "Cobros");
        panelContenido.add(usuarios,  "Usuarios");
        panelContenido.add(simulador, "simulador");

        cardLayout.show(panelContenido, "Inicio");
    }

    private JScrollPane wrapScroll(JPanel vista) {
        JScrollPane scroll = new JScrollPane(vista);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        return scroll;
    }

    public void irA(String pantalla) {
        cardLayout.show(panelContenido, pantalla);

        // ← Resetear scroll del simulador al entrar
        if (pantalla.equals("simulador")) {
            scrollSimulador.getVerticalScrollBar().setValue(0);
        }

        for (java.awt.Component comp : panelContenido.getComponents()) {
            java.awt.Component vista = comp;
            if (comp instanceof JScrollPane) {
                vista = ((JScrollPane) comp).getViewport().getView();
            }

            if (pantalla.equals("Clientes") && vista instanceof javaapplication1.vistas.GestionClientesUI) {
                ((javaapplication1.vistas.GestionClientesUI) vista).cargarDesdeBD();
                break;
            }
            if (pantalla.equals("prestamos") && vista instanceof javaapplication1.vistas.GestionPrestamosUI) {
                ((javaapplication1.vistas.GestionPrestamosUI) vista).actualizarComboClientes();
                break;
            }
            if (pantalla.equals("Cobros") && vista instanceof javaapplication1.vistas.GestionCuotasUI) {
                ((javaapplication1.vistas.GestionCuotasUI) vista).actualizarComboPrestamos();
                ((javaapplication1.vistas.GestionCuotasUI) vista).cargarDesdeBD();
                 break;
            }
        }
    }
}