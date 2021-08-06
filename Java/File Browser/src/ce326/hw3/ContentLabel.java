package ce326.hw3;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
                            //No Components are Selected
                            GlobalFrame.selected_label = null;
                            setBackground(GlobalFrame.background);

                            //Disable Edit in Menu Bar
                            MenuBar.disable_edit();

                            //Change Directory
                            GlobalFrame.path = GlobalFrame.path + "/" + getText();

                            //Re-Render DOM
                            GlobalFrame.general.clear();
                            GlobalFrame.general.render();
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
}
