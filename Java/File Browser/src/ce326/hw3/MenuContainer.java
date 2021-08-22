package ce326.hw3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

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

        //Enable Search Bar
        search.addItemListener(event -> {
            GeneralContainerPanel.search_activated = search.isSelected();

            GlobalFrame.general.clear();
            GlobalFrame.general.render();
        });


        //Show-Hide Files
        hidden.addItemListener(event -> {
            GeneralContainerPanel.hidden_content = hidden.isSelected();

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
                    else if(i.getText().equals("Paste")){
                        //Paste Directory or File and Disable Paste Option
                        if(GlobalFrame.paste_path != null) {

                            File source;
                            File destination;

                            //Set Source File to Copy or Cut
                            if(GlobalFrame.copy_path != null)
                                source = new File(GlobalFrame.copy_path.toString());
                            else
                                source = new File(GlobalFrame.cut_path.toString());

                            //Set Destination File to Paste
                            destination = new File(GlobalFrame.paste_path.toString() + source.getPath().substring(source.getPath().lastIndexOf('/')));

                            //Recursive Paste Directory or Files
                            try {
                                FileUtils.copy_file_or_directory(source, destination);

                                GlobalFrame.general.clear();
                                GlobalFrame.general.render();

                                GlobalFrame.copy_path = null;
                                GlobalFrame.cut_path = null;
                                GlobalFrame.paste_path = null;
                                disable_paste();

                            } catch (Exception exception){}
                        }
                    }
                    else if(i.getText().equals("Rename")){
                        JDialog dialog = new JDialog(FileBrowser.window, "Rename Directory or File", true);
                        JLabel title = new JLabel("Rename File/Directory");
                        TextField form = new TextField();
                        JButton confirm = new JButton("Confirm");

                        form.setText(GlobalFrame.selected_label.getText());
                        form.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                super.mouseClicked(e);
                                form.setText("");
                            }
                        });


                        confirm.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                File from = new File(ContentLabel.label_path());
                                File to = new File(String.format("%s/%s", GlobalFrame.path, form.getText()));

                                from.renameTo(to); //Rename File or Directory
                                dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));

                                //Clear DOM to Re-Render
                                GlobalFrame.general.clear();

                                //Re-Render DOM
                                GlobalFrame.general.render();
                            }
                        });


                        //Layout
                        dialog.setLayout(new GridLayout(3, 1));
                        dialog.add(title);
                        dialog.add(form);
                        dialog.add(confirm);

                        dialog.setSize(new Dimension(350, 150));
                        dialog.setVisible(true);
                    }
                    else if(i.getText().equals("Properties")){
                        JDialog dialog = new JDialog(FileBrowser.window, "Properties", true); //Title of Window
                        JLabel title = new JLabel(String.format("Title: %s", GlobalFrame.selected_label.getText())); //Name of File or Directory
                        JLabel path = new JLabel(String.format("Path: %s", GlobalFrame.selected_label.label_path()));

                        File selected_label = new File(GlobalFrame.selected_label.label_path());
                        JLabel size = new JLabel(String.format("Size: %d Bytes", file_size(selected_label)));

                        JButton close = new JButton("OK");

                        //Close Modal Window
                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                            }
                        });

                        //Layout
                        dialog.setLayout(new GridLayout(4, 1));
                        dialog.add(title);
                        dialog.add(path);
                        dialog.add(size);
                        dialog.add(close);

                        dialog.setSize(new Dimension(350, 150));
                        dialog.setVisible(true);
                    }
                    else if(i.getText().equals("Add to Favourites")){
                        try {
                            //Append Entry to Favourites
                            File xmlFile = new File(GlobalFrame.configuration);

                            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(xmlFile);
                            Element documentElement = document.getDocumentElement();
                            Element textNode = document.createElement("directory");
                            //textNode.setTextContent(ContentLabel.selectedLabel.getText());

                            textNode.setAttribute("name", GlobalFrame.selected_label.getText());
                            textNode.setAttribute("path", GlobalFrame.selected_label.label_path());

                            documentElement.appendChild(textNode);
                            document.replaceChild(documentElement, documentElement);
                            Transformer tFormer = TransformerFactory.newInstance().newTransformer();
                            tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
                            Source source = new DOMSource(document);
                            Result result = new StreamResult(xmlFile);
                            tFormer.transform(source, result);

                            //Refresh Favourites Panel
                            GlobalFrame.favourites.refresh();

                        } catch (Exception exception) {
                            System.out.println(exception);
                        }
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
        GlobalFrame.paste_enabled = false;
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
        GlobalFrame.paste_enabled = true;
    }

    //Method to Disable Menu Buttons
    static void enable_all(){
        for(JMenuItem i : edit_popup_items){
            if(GlobalFrame.copy_path != null || GlobalFrame.cut_path != null){
                if(i.getText().equals("Paste")){
                    continue;
                }
            }
            i.setEnabled(true);
        }
    }

    //Get File or Directory Size in Bytes
    static long file_size(File file_or_directory){
        long length = 0;
        if(file_or_directory.isDirectory()) {
            for (File file : file_or_directory.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += file_size(file);
            }
        }
        //Directory if a File
        else {
            length = file_or_directory.length();
        }
        return length;
    }

    //Functions that Recursively Deletes Files and Directories
    static void delete_file_or_directory(File file_or_directory){

    }

    //A Function Used to Enable or Disable Paste Menu Option Based on the Destination
    static void paste_option(String destination){
        //Different Source and Destination Path
        if (GlobalFrame.copy_path != null) {
            if (!GlobalFrame.copy_path.toString().equals(destination)) {
                GlobalFrame.paste_path = new StringBuilder(destination);
                MenuContainer.enable_paste();
            }
            else {
                MenuContainer.disable_paste();
            }
        } else if (GlobalFrame.cut_path != null) {
            if (!GlobalFrame.cut_path.toString().equals(destination)) {
                GlobalFrame.paste_path = new StringBuilder(destination);
                MenuContainer.enable_paste();
            }
            else {
                MenuContainer.disable_paste();
            }
        }
        else {
            MenuContainer.disable_paste();
        }
    }
}
