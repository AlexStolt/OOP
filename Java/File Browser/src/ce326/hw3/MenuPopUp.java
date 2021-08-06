package ce326.hw3;

import javax.swing.*;
import java.util.Hashtable;

public class MenuPopUp extends JPopupMenu {
    public MenuPopUp(){
        for(JMenuItem i : MenuContainer.edit_popup_items){
            add(i);
        }

    }
}
