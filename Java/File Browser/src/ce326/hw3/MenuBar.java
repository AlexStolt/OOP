package ce326.hw3;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    static JMenu file, edit, view;
    public MenuBar(){
        file = MenuContainer.file();
        edit = MenuContainer.edit();
        view = MenuContainer.view();

        //Disable Edit Option on Initialization
        disable_edit();

        add(file);
        add(edit);
        add(view);
    }

    //Method to Enable Edit Menu
    static void enable_edit(){
        edit.setEnabled(true);
    }

    //Method to Disable Edit Menu
    static void disable_edit(){
        edit.setEnabled(false);
    }
}
