package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class ContentPanel extends JPanel {
    ContentPanel self = this;
    public ContentPanel(){
        setBackground(GlobalFrame.background);
        render_labels();
    }

    JScrollPane render_panel(){
        //Scroll Pane
        JScrollPane scroll = new JScrollPane(this);

        //Only Vertical Scroll
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scroll;
    }

    void render_labels(){
        ImageIcon icon = null;

        //Get Directories
        File[] directories = GlobalFrame.get_directories(GlobalFrame.path, GeneralContainerPanel.hidden_content);

        //Get Files
        File[] files = GlobalFrame.get_files(GlobalFrame.path,  GeneralContainerPanel.hidden_content);

        GlobalFrame.sort(directories);

        //Get Directory Icon
        if(directories.length > 0){
            icon = new ImageIcon("src/icons/folder.png");
        }

        //Set Dimensions for Scroll Pane
        dimensions(directories.length, files.length);

        //JLabels for Directories
        for(File directory : directories){
            add(new ContentLabel(directory, icon, true));
        }

        //JLabels for Files
        for(File file : files){
            switch (file_extension(file)) {
                case "audio" -> icon = new ImageIcon("src/icons/audio.png");
                case "bmp" -> icon = new ImageIcon("src/icons/bmp.png");
                case "doc" -> icon = new ImageIcon("src/icons/doc.png");
                case "docx" -> icon = new ImageIcon("src/icons/docx.png");
                case "giff" -> icon = new ImageIcon("src/icons/giff.png");
                case "gz" -> icon = new ImageIcon("src/icons/gz.png");
                case "htm" -> icon = new ImageIcon("src/icons/htm.png");
                case "html" -> icon = new ImageIcon("src/icons/html.png");
                case "image" -> icon = new ImageIcon("src/icons/image.png");
                case "jpeg" -> icon = new ImageIcon("src/icons/jpeg.png");
                case "jpg" -> icon = new ImageIcon("src/icons/jpg.png");
                case "mp3" -> icon = new ImageIcon("src/icons/mp3.png");
                case "ods" -> icon = new ImageIcon("src/icons/ods.png");
                case "odt" -> icon = new ImageIcon("src/icons/odt.png");
                case "ogg" -> icon = new ImageIcon("src/icons/ogg.png");
                case "pdf" -> icon = new ImageIcon("src/icons/pdf.png");
                case "png" -> icon = new ImageIcon("src/icons/png.png");
                case "tar" -> icon = new ImageIcon("src/icons/tar.png");
                case "tgz" -> icon = new ImageIcon("src/icons/tgz.png");
                case "txt" -> icon = new ImageIcon("src/icons/txt.png");
                case "video" -> icon = new ImageIcon("src/icons/video.png");
                case "wav" -> icon = new ImageIcon("src/icons/wav.png");
                case "xlsx" -> icon = new ImageIcon("src/icons/xlsx.png");
                case "xlx" -> icon = new ImageIcon("src/icons/xlx.png");
                case "xml" -> icon = new ImageIcon("src/icons/xml.png");
                case "zip" -> icon = new ImageIcon("src/icons/zip.png");
                default -> icon = new ImageIcon("src/icons/question.png");
            }

            //Create a Label and Render on "DOM"
            add(new ContentLabel(file, icon, false));
        }

        //Layout Manager
        setLayout(new FlowLayout(FlowLayout.LEADING, GlobalFrame.content_horizontal_gap, GlobalFrame.content_vertical_gap));

        //Calculate Content Height to Update JScrollPane Dimensions
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                self.dimensions(directories.length, files.length);
            }
        });

        //Mouse Listener to Disable Selected Label Outline
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);
                if(GlobalFrame.selected_label != null){
                    //Reset Background
                    GlobalFrame.selected_label.setBackground(GlobalFrame.background);

                    //Disable Edit
                    MenuBar.disable_edit();

                    //No Components are Selected
                    GlobalFrame.selected_label = null;
                }

                //Right Click
                if(event.getButton() == MouseEvent.BUTTON3){
                    //Handle Paste Option (Disable or Enable)
                    MenuContainer.paste_option(GlobalFrame.path);

                    //Pop-Up Menu
                    MenuPopUp popup = new MenuPopUp();
                    add(popup);
                    popup.show(self, event.getX(), event.getY());
                }
            }
        });

        repaint();
        revalidate();
    }

    //Return the Type of the File (.png, .jpeg etc)
    private String file_extension(File file){
        String name = file.getName();
        int index = name.lastIndexOf(".");
        return index < 0 ? "" : name.substring(index + 1);
    }

    //A Method to Calculate the Dimensions for the Scroll Pane
    private void dimensions(int directories, int files){
        //cols = panel_width / (element_width + element_margin)
        int columns = getSize().width / (GlobalFrame.content_label.width + GlobalFrame.content_horizontal_gap);

        //rows = ceil(total_components / columns)
        int rows = (int) Math.ceil((double) (directories + files) / (double) columns);

        //height = rows + total_margin
        int height = (rows * (GlobalFrame.content_label.height + GlobalFrame.content_vertical_gap));
        setPreferredSize(new Dimension(getWidth() - 1, height + GlobalFrame.content_vertical_gap));
    }

    void clear(){
        for (Component component : getComponents()){
            remove(component);
        }

        //Refresh DOM
        revalidate();
        repaint();
    }
}
