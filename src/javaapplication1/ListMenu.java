
package javaapplication1;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;


public class ListMenu<E extends Object> extends JList<E>{
    
    private final DefaultListModel model;
    private int selectedIndex= -1;
    
    public ListMenu(){
        model=new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me) {
               if(SwingUtilities.isLeftMouseButton(me)){
                  int index=locationToIndex(me.getPoint());
                  Object o= model.getElementAt(index);
                  if( o instanceof Model_menu){
                      Model_menu Menu=(Model_menu)o;
                      if(Menu.getType()==Model_menu.MenuType.MENU){
                          selectedIndex =index;
                      }
                  }else{
                      selectedIndex= index;
                  }
                  repaint();
               }
            }
        
            });
        
    }
    
    
    @Override
    public ListCellRenderer<? super E> getCellRenderer() {
    return new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(JList<?> jlist, Object o, int index, boolean selected, boolean focus) {
            Model_menu data;
            if (o instanceof Model_menu) {
                data = (Model_menu) o;
            } else {
                data = new Model_menu("", o + "", Model_menu.MenuType.EMPTY);
            }
            Menuitem item = new Menuitem(data);
item.setSelected(selectedIndex==index);
            return item;
        }

    };
}
        public void addItem(Model_menu data){
            model.addElement(data);
        }
    }
    


