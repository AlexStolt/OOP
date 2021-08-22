package ce326.hw3;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FavouritesLabel extends JLabel {
    private String title;
    private String path;
    static int width = 100;
    static int height = 22;
    static int margin = 40;
    public FavouritesLabel(String title, String path){
        this.path = path;
        setText(title);

        //Alignment
        setHorizontalTextPosition(JLabel.CENTER); //Horizontal Center Text
        setVerticalTextPosition(JLabel.BOTTOM); //Vertical Bottom Text
        setHorizontalAlignment(JLabel.CENTER); //Horizontal Alignment of Text and Image
        setVerticalAlignment(JLabel.CENTER);  //Vertical Alignment of Text and Image

        //Set Dimensions
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        //Set Border
        Border border = BorderFactory.createMatteBorder(margin, margin, margin, margin, new Color(0x2B2B2B));
        setBorder(BorderFactory.createEmptyBorder(margin, 0, margin, 0));

        //Listener to Change Directory
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GlobalFrame.path = path;

                //Clear DOM to Re-Render
                GlobalFrame.general.clear();

                //Re-Render DOM
                GlobalFrame.general.render();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(new Color(0xFF5F48));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(new Color(000));
            }
        });

    }

    String get_path(){
        return path;
    }
}
