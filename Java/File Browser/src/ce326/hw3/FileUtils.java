package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
    public static void copy_file_or_directory(File source, File destination) throws IOException {
        if (source.isDirectory())
            copy_directory(source, destination);
        else {
            ensure_parent_directory(destination);
            copy_file(source, destination);
        }
    }

    private static void copy_directory(File source, File destination) throws IOException {
        File[] content = source.listFiles();

        //Create Path
        if (!destination.exists())
            destination.mkdirs();
        else {
            //File Already Exists
            //Modal Window
            JDialog dialog = new JDialog(FileBrowser.window, "Replace Directory", true); //Title of Window
            JLabel title = new JLabel("Directory Already Exists, Would You Like to Replace It?"); //Name of File or Directory
            JButton accept = new JButton("Accept");
            JButton decline = new JButton("Decline");

            //Replace File
            accept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Delete File and Retry
                    destination.delete();
                    destination.mkdirs();
                    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                }
            });

            //Close Modal Window
            decline.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                }
            });

            //Layout
            dialog.setLayout(new GridLayout(3, 1));
            dialog.add(title);
            dialog.add(accept);
            dialog.add(decline);

            dialog.setSize(new Dimension(350, 150));
            dialog.setVisible(true);
        }
        //Recursive Copy Directories or Files
        if (content != null) {
            for (File file : content) {
                File created = new File(destination.getAbsolutePath() + File.separator + file.getName());
                if (file.isDirectory())
                    copy_directory(file, created);
                else
                    copy_file(file, created);
            }
        }
    }

    private static void copy_file(File source, File destination) throws IOException {
        try {
            Files.copy(source.toPath(), destination.toPath());
        }
        catch (Exception exception){
            //File Already Exists
            //Modal Window
            JDialog dialog = new JDialog(FileBrowser.window, "Replace File", true); //Title of Window
            JLabel title = new JLabel("File Already Exists, Would You Like to Replace It?"); //Name of File or Directory
            JButton accept = new JButton("Accept");
            JButton decline = new JButton("Decline");

            //Replace File
            accept.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Delete File and Retry
                    destination.delete();
                    try {
                        Files.copy(source.toPath(), destination.toPath());
                    } catch (Exception ex){}
                    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                }
            });

            //Close Modal Window
            decline.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
                }
            });

            //Layout
            dialog.setLayout(new GridLayout(3, 1));
            dialog.add(title);
            dialog.add(accept);
            dialog.add(decline);

            dialog.setSize(new Dimension(350, 150));
            dialog.setVisible(true);
        }
    }

    private static void ensure_parent_directory(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists())
            parent.mkdirs();
    }

    static void deleteFolder(File file){
        for (File subFile : file.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }

}
