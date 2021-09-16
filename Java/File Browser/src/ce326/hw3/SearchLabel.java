package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;



public class SearchLabel extends JLabel {
    public SearchLabel(File file, ImageIcon icon){
        setIcon(icon); //Set Directory Icon
        setText(file.getPath());

        setHorizontalTextPosition(JLabel.RIGHT); //Horizontal Center Text
        setVerticalTextPosition(JLabel.CENTER); //Vertical Bottom Text
        setHorizontalAlignment(JLabel.CENTER); //Horizontal Alignment of Text and Image
        setVerticalAlignment(JLabel.CENTER);  //Vertical Alignment of Text and Image

       addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               super.mouseClicked(e);

               //Move to Directory
               if(file.isDirectory()){
                   GlobalFrame.path = file.getPath();
               }
               //File is Executable
               else if(file.canExecute()){
                   //Run without Parameters
                   try {
                       Runtime.getRuntime().exec(file.getPath(), null, file.getParentFile());
                   } catch (Exception exception){
                       System.out.printf("[Error]: Executing %s%n", file.getName());
                   }
               }
               //File Opened with its Default Program
               else if(!file.isDirectory()){
                   try {
                       Desktop.getDesktop().open(file);
                   }
                   catch (Exception exception){
                       System.out.printf("[Error]: Opening %s%n", file.getName());
                   }
               }

               //Refresh DOM
               GlobalFrame.general.clear();
               GlobalFrame.general.render();
           }
       });
    }
}
