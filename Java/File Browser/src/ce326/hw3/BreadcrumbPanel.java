package ce326.hw3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BreadcrumbPanel extends JPanel {
    public BreadcrumbPanel(){
        String [] path = GlobalFrame.path.split("/");

        for(int i = 0; i < path.length; i++){
            if(path[i] != "") {
                //Add Directory
                add(new PathLink(path[i]));
                if (i >= path.length - 1) {
                    break;
                }

                //Add ">"
                add(new PathLink());
            }
        }

        setPreferredSize(GlobalFrame.breadcrumb);
        setLayout(new FlowLayout(FlowLayout.LEADING));
        setBorder(BorderFactory.createMatteBorder(0, 1,0, 0, new Color(0x000000)));
    }

    //Each Individual Path
    class PathLink extends JLabel {
        StringBuilder path = new StringBuilder();

        public PathLink(String directory) {
            //Keep Current Path
            path.append(GlobalFrame.path.split(directory)[0] + directory); //Path = /.../.../directory/
            setBorder(GlobalFrame.breadcrumb_padding);
            setText(directory);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent event) {
                    super.mouseClicked(event);

                    //Update when Path Changes
                    if(!GlobalFrame.path.equals(path())) {
                        //Update Path
                        GlobalFrame.path = path();

                        //Clear DOM to Re-Render
                        GlobalFrame.general.clear();

                        //Re-Render DOM
                        GlobalFrame.general.render();
                    }
                }
                @Override
                public void mouseEntered(MouseEvent event){
                    setForeground(new Color(0xFF5F48));
                }
                @Override
                public void mouseExited(MouseEvent event){
                    setForeground(new Color(000));
                }
            });
        }
        public PathLink(){
            //Keep Current Path
            path = null;
            setBorder(GlobalFrame.breadcrumb_padding);
            setText(">");
        }

        String path(){
            return path.toString();
        }
    }
}
