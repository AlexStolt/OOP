package ce326.hw3;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Arrays;

public class GlobalFrame extends JFrame {
    private final String title;
    static int width;
    static int height;
    static FavouritesContainerPanel favourites;
    static GeneralContainerPanel general;
    static String path = System.getProperty("user.home");
    static String configuration = String.format(String.format("%s/.java-file-browser/properties.xml", GlobalFrame.path));
    static StringBuilder copy_path = null;
    static StringBuilder cut_path = null;
    static StringBuilder paste_path = null;
    static boolean paste_enabled = false; //Menu Option is Enabled (if(copy_path || cut_path) -> true)

    //A List of Values to Easily Customize UX
    static Dimension breadcrumb = new Dimension(0, 60); //Stretches Horizontally
    static Dimension content_label = new Dimension(100, 100);
    static int content_vertical_gap = 10;
    static int content_horizontal_gap = 10;
    static Color background = new Color(234, 234, 234);
    static Color hover_background = new Color(206, 206, 206);
    static Color selected_background = new Color(160, 160, 160);
    static ContentLabel selected_label = null;
    static EmptyBorder breadcrumb_padding = new EmptyBorder(10, 10, 10, 10);

    public GlobalFrame(String title, int width, int height){
        this.title = title;
        GlobalFrame.width = width;
        GlobalFrame.height = height;

        //Render Menu Bar
        menu();

        //Render Components (Grid Layout)
        components();

        //Render Basic Window
        render();

        //Disable Paste on Edit Menu
        MenuContainer.disable_paste();

        //Event Listener to Update Width and Height
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                super.componentResized(event);

                //Update Width and Height
                GlobalFrame.width = (int) getContentPane().getSize().getWidth();
                GlobalFrame.height = (int) getContentPane().getSize().getHeight();
            }
        });
    }

    //Render Window on Screen
    private void render(){
        setTitle(title);
        setSize(new Dimension(width, height));
        //setMinimumSize(new Dimension(width - 100, height - 50));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void menu(){
        MenuBar bar = new MenuBar();
        setJMenuBar(bar);
    }

    private void components(){
        //Layout Manager (Grid Bag)
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Both Objects are Oriented West (Left) and can be Scaled Vertically
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weighty = 1;

        //Favourites Container Component Should NOT Scale Horizontally and Should Stretch Vertically
        constraints.weightx = 0;
        constraints.fill = GridBagConstraints.VERTICAL;
        favourites = new FavouritesContainerPanel();
        add(favourites, constraints);

        //General Container Component Should Scale and Stretch Both Horizontally and Vertically
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        general = new GeneralContainerPanel();
        add(general, constraints);
    }

    //List Directories on Path
    //Hidden: True -> Return Hidden Directories (ex path: ".../.../.<DIR>/")
    //Hidden: False -> Return Visible Directories (ex path: ".../.../<DIR>/")
    static File[] get_directories(String path, boolean hidden){
        if(!hidden) {
            return new File(path).listFiles(file -> file.isDirectory() && !file.isHidden());
        }
        else {
            return new File(path).listFiles(file -> file.isDirectory());
        }
    }

    //List Files on Path
    //Hidden: True -> Return Hidden Files (ex path: ".../.../.<FILE>/")
    //Hidden: False -> Return Visible Files (ex path: ".../.../<FILE>/")
    static File[] get_files(String path, boolean hidden){
        if(!hidden) {
            return new File(path).listFiles(file -> !file.isDirectory() && !file.isHidden());
        }
        else {
            return new File(path).listFiles(file -> !file.isDirectory());
        }

    }

    //Sort a File Array (Should be Called Twice if Hidden Files Exists)
    static void sort(File[] files){
        Arrays.sort(files);
    }

    //Create Path for Selected Label
    static StringBuilder path_constructor(){
        StringBuilder path = null;
        if(GlobalFrame.selected_label != null){
            path = new StringBuilder(GlobalFrame.path);
            path.append("/");
            path.append(GlobalFrame.selected_label.getText());
        }

        return path;
    }
}
