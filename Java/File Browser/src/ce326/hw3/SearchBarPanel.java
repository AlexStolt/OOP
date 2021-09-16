package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("SuspiciousListRemoveInLoop")
public class SearchBarPanel extends JPanel {
    static ArrayList<File> files_found;
    static boolean search = false;
    public SearchBarPanel(){
        setLayout(new GridLayout(1,1));
        Form form = new Form();
        Button search = new Button();

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(!form.getText().isEmpty()){
                    File root = new File(GlobalFrame.path);

                    //Search for File with Text (Name) and Append Paths to "files_found" List
                    files_found = new ArrayList<>();
                    String[] words = form.getText().split(" "); //Word and Filter
                    String text = words[0]; //Word
                    recursive_search(root, text);


                    //Input Error
                    if(words.length > 2){
                        System.out.println("[Error]: Search Syntax");
                        return;
                    }

                    //Handle Filters
                    if(words.length == 2){
                        try {
                            //Filter
                            String filter = words[1].split("type:")[1];
                            if(filter.equals("dir")){
                                for (int i = 0; i < files_found.size(); i++) {
                                    if(!files_found.get(i).isDirectory()){
                                        files_found.remove(i);
                                    }
                                }
                            }
                            else {
                                for (int i = 0; i < files_found.size(); i++) {
                                    String extension = extension_of_string(files_found.get(i).getName());
                                    if (!extension.equals(filter)) {
                                        files_found.remove(i);
                                    }
                                }
                            }
                        } catch (Exception exception){
                            System.out.println("[Error]: Search Syntax");
                            return;
                        }
                    }

                    SearchBarPanel.search = true; //Render Labels and Paths
                    GlobalFrame.general.clear();
                    GlobalFrame.general.render();
                    SearchBarPanel.search = false; //Reset Rendering
                }
            }
        });


        add(form);
        add(search);
    }
    private String extension_of_string(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1);
        }
        return extension;
    }

    private void recursive_search(File root, String text) {


        if (root.isDirectory()) {
            for (File file : Objects.requireNonNull(root.listFiles())) {
                //Add File to List
                Pattern pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    files_found.add(file);
                }

                //Recursive Call
                if (file.isDirectory()) {
                    recursive_search(file, text);
                }

            }
        }

    }


    //Text Field
    static class Form extends JTextField {
        public Form(){
            super("Recursive Search");
            setFont(new Font("Arial", Font.PLAIN, 15));

            addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    if(getText().equals("Recursive Search"))
                        setText(""); //Reset Text
                }
            });
        }

    }

    //Search Button
    static class Button extends JButton {
        public Button(){
            super("Search");
            setFont(new Font("Arial", Font.PLAIN, 15));
            setPreferredSize(new Dimension(20,25));
        }
    }
}
