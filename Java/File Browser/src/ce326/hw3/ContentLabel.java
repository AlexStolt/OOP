package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ContentLabel extends JLabel {
    private final boolean directory;
    private final ContentLabel self = this;

    public ContentLabel(File file, ImageIcon icon, boolean directory){
        this.directory = directory;

        setIcon(icon); //Set Directory Icon
        setText(file.getName());

        setHorizontalTextPosition(JLabel.CENTER); //Horizontal Center Text
        setVerticalTextPosition(JLabel.BOTTOM); //Vertical Bottom Text
        setHorizontalAlignment(JLabel.CENTER); //Horizontal Alignment of Text and Image
        setVerticalAlignment(JLabel.CENTER);  //Vertical Alignment of Text and Image

        //Set Label Sizing
        setPreferredSize(GlobalFrame.content_label);

        //General
        setOpaque(true);
        setBackground(GlobalFrame.background);

        //Tooltip Text
        setToolTipText(getText());

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent event) {
                //Left Click
                if(event.getButton() == MouseEvent.BUTTON1){


                    if(event.getClickCount() < 2){
                        //Another Element is Already Selected
                        if(GlobalFrame.selected_label != null && GlobalFrame.selected_label != self){
                            GlobalFrame.selected_label.setBackground(GlobalFrame.background);
                        }

                        //Set Component as Selected
                        GlobalFrame.selected_label = self;

                        setBackground(GlobalFrame.selected_background);

                        //Enable Edit in Menu Bar
                        MenuBar.enable_edit();
                    }
                    else {
                        if (!self.directory){
                            //Open File
                        }
                        else {
                            File directory = new File(label_path());
                            //No Permissions
                            if (!directory.canRead()) {
                                //Modal Window
                                JDialog dialog = new JDialog(FileBrowser.window, "Permission Error", true); //Title of Window
                                JLabel title = new JLabel(String.format("Title: %s", GlobalFrame.selected_label.getText())); //Name of File or Directory
                                JLabel path = new JLabel(String.format("Path: %s", GlobalFrame.selected_label.label_path()));
                                JLabel error = new JLabel(String.format("[Error]: No Read Permission"));
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
                                dialog.add(error);
                                dialog.add(close);

                                dialog.setSize(new Dimension(350, 150));
                                dialog.setVisible(true);

                            }
                            //Change Directory
                            else {
                                //Change Directory if Permissions
                                GlobalFrame.path = label_path();

                                //No Label is Selected
                                GlobalFrame.selected_label = null;
                                //No Components are Selected
                                setBackground(GlobalFrame.background);
                                //Disable Edit in Menu Bar
                                MenuBar.disable_edit();

                                //Re-Render DOM
                                GlobalFrame.general.clear();
                                GlobalFrame.general.render();
                            }
                        }
                    }
                }
                //Right Click
                else if(event.getButton() == MouseEvent.BUTTON3){
                    //Another Element is Already Selected
                    if(GlobalFrame.selected_label != null && GlobalFrame.selected_label != self){
                        GlobalFrame.selected_label.setBackground(GlobalFrame.background);
                    }

                    //Set Component as Selected
                    GlobalFrame.selected_label = self;
                    setBackground(GlobalFrame.selected_background);

                    //Enable Edit in Menu Bar
                    MenuBar.enable_edit();

                    File selected = new File(label_path());
                    if(!selected.isDirectory()){
                        MenuContainer.disable_paste();
                    }
                    else {
                        //Handle Paste Option (Disable or Enable)
                        MenuContainer.paste_option(label_path());
                    }


                    //Pop-Up Menu
                    MenuPopUp popup = new MenuPopUp();
                    add(popup);
                    popup.show(self, event.getX(), event.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent event) {

            }

            @Override
            public void mouseReleased(MouseEvent event) {}

            @Override
            public void mouseEntered(MouseEvent event) {
                if(GlobalFrame.selected_label != self)
                    setBackground(GlobalFrame.hover_background);
            }

            @Override
            public void mouseExited(MouseEvent event) {
                if(GlobalFrame.selected_label != self)
                    setBackground(GlobalFrame.background);
            }
        });
    }

    public static String label_path(){
        return String.format("%s/%s", GlobalFrame.path, GlobalFrame.selected_label.getText());
    }

}
