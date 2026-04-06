
package javaapplication1;

import javax.swing.Icon;
import javax.swing.ImageIcon;


public class Model_menu {

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

  

    public Model_menu(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }
      public Model_menu() {
    }
    
    private String icon;
    private String name;
    private MenuType type;
    
    public Icon toIcon() {
    // Usamos la ruta donde realmente están tus iconos
    java.net.URL location = getClass().getResource("/javaapplication1/" + icon + ".png");
    
    if (location != null) {
        return new ImageIcon(location);
    } else {
        // Esto evita que el programa se cierre y te avisa qué imagen falta
        System.err.println("Error: No se encontró la imagen en /javaapplication1/" + icon + ".png");
        return null;
    }
}
    public static enum MenuType{
        TITLE,MENU,EMPTY
    }
    
}
