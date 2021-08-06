package ce326.hw3;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

public class MenuContainer {
    static ArrayList<JMenuItem> edit_bar_items = edit_items();
    static ArrayList<JMenuItem> edit_popup_items = edit_items();

    public MenuContainer(){}

    //File Menu
    static JMenu file(){
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");

        //On Exit Kill Process
        exit.addActionListener(e -> FileBrowser.window.dispatchEvent(new WindowEvent(FileBrowser.window, WindowEvent.WINDOW_CLOSING)));

        file.add(exit);
        return file;
    }

    //Edit Menu
    static JMenu edit(){
        JMenu edit = new JMenu("Edit");

        for(JMenuItem i : edit_bar_items){
            edit.add(i);
        }

        return edit;
    }

    //View Menu
    static JMenu view(){
        JMenu view = new JMenu("View");

        //Sub-Menu
        JCheckBoxMenuItem search = new JCheckBoxMenuItem("Search");
        JCheckBoxMenuItem hidden = new JCheckBoxMenuItem("Hidden Files/Folders");

        hidden.addItemListener(event -> {
            GlobalFrame.hidden_content = hidden.isSelected();

            GeneralContainerPanel.content.clear();
            GeneralContainerPanel.content.render_labels();
        });


        view.add(search);
        view.add(hidden);

        return view;
    }

    //Edit Sub-Menu
    static ArrayList<JMenuItem> edit_items(){
        ArrayList<JMenuItem> edit_menu_items = new ArrayList<JMenuItem>();

        edit_menu_items.add(new JMenuItem("Cut"));
        edit_menu_items.add(new JMenuItem("Copy"));
        edit_menu_items.add(new JMenuItem("Paste"));
        edit_menu_items.add(new JMenuItem("Rename"));
        edit_menu_items.add(new JMenuItem("Delete"));
        edit_menu_items.add(new JMenuItem("Add to Favourites"));
        edit_menu_items.add(new JMenuItem("Properties"));

        //Event Listeners
        for(JMenuItem i : edit_menu_items){
            i.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    if(i.getText().equals("Cut")){
                        if(GlobalFrame.copy_path != null)
                            GlobalFrame.copy_path = null;

                        //Get Absolute Path of Selected Component
                        GlobalFrame.cut_path = GlobalFrame.path_constructor();

                        //Enable Paste Option
                        enable_paste();
                    }
                    else if(i.getText().equals("Copy")){
                        if(GlobalFrame.cut_path != null)
                            GlobalFrame.cut_path = null;

                        //Get Absolute Path of Selected Component
                        GlobalFrame.copy_path = GlobalFrame.path_constructor();

                        //Enable Paste Option
                        enable_paste();
                    }
                }
            });
        }

        return edit_menu_items;
    }

    //Disable Paste Functionality on Both Menu Bar and Pop-Up Menu
    static void disable_paste(){
        //Menu-Bar
        if(edit_bar_items != null){
            for(JMenuItem i : edit_bar_items){
                if(i.getText() == "Paste"){
                    i.setEnabled(false);
                    break;
                }
            }
        }

        //Pop-Up Menu
        if(edit_popup_items != null){
            for(JMenuItem i : edit_popup_items){
                if(i.getText() == "Paste"){
                    i.setEnabled(false);
                    break;
                }
            }
        }
    }

    //Enable Paste Functionality on Both Menu Bar and Pop-Up Menu
    static void enable_paste(){
        //Menu-Bar
        if(edit_bar_items != null){
            for(JMenuItem i : edit_bar_items){
                if(i.getText() == "Paste"){
                    i.setEnabled(true);
                    break;
                }
            }
        }

        //Pop-Up Menu
        if(edit_popup_items != null){
            for(JMenuItem i : edit_popup_items){
                if(i.getText() == "Paste"){
                    i.setEnabled(true);
                    break;
                }
            }
        }
    }
}
