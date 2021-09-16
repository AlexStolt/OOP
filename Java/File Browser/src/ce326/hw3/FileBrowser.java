package ce326.hw3;

public class FileBrowser {
    static GlobalFrame window;
    static boolean os_type_windows = false;
    static boolean os_type_linux = false;
    public static void main(String[] args) {

        //Set OS the File Browser is Running (Used to Define Path Separators)
        String system = System.getProperty("os.name");
        if(system.contains("Windows"))
            os_type_windows = true;
        else
            os_type_linux = true;


        //Start GUI Application
        javax.swing.SwingUtilities.invokeLater(() -> window = new GlobalFrame("File Browser", 800, 600));
    }
}
