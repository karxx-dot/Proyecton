
package javaapplication1;

import javax.swing.Icon;


public class Model_Card {

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Model_Card(Icon icon, String Title, String values, String description) {
        this.icon = icon;
        this.Title = Title;
        this.values = values;
        this.description = description;
    }

    public Model_Card() {
    }
    
    

    private Icon icon;
    private String Title;
    private String values;
    private String description;
}
