package ce326.hw3;

import javax.swing.*;
import java.util.Hashtable;

public class MenuPopUp extends JPopupMenu {
    public MenuPopUp(){
        for(JMenuItem i : MenuContainer.edit_popup_items){
            if(i.getText().equals("Paste")){
                i.setEnabled(GlobalFrame.paste_enabled);
            }
            else {
                //Right Click on Label
                if(GlobalFrame.selected_label != null) {
                    i.setEnabled(true);
                }
                //Right Click on Panel
                else {
                    i.setEnabled(false);
                }
            }
            add(i);
        }
    }
}
