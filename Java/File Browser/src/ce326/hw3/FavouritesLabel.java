package ce326.hw3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class FavouritesLabel extends JLabel {
    private String title;
    private String path;
    static int width = 100;
    static int height = 22;
    static int margin = 40;
    FavouritesLabel self = this;
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
            public void mouseClicked(MouseEvent event) {
                //Left Click
                if(event.getButton() == MouseEvent.BUTTON1) {
                    GlobalFrame.path = path;

                    //Clear DOM to Re-Render
                    GlobalFrame.general.clear();

                    //Re-Render DOM
                    GlobalFrame.general.render();
                }
                //Right Click
                else if(event.getButton() == MouseEvent.BUTTON3){
                    //Pop-Up Menu to Delete
                    JPopupMenu popup = new JPopupMenu();
                    JMenuItem delete = new JMenuItem("Delete");

                    //Delete XML Entry
                    delete.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            File xml_file = new File(GlobalFrame.configuration);
                            try {
                                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                                Document document = documentBuilder.parse(xml_file);
                                NodeList directories = document.getElementsByTagName("directory");
                                for (int i = 0; i < directories.getLength(); i++) {
                                    Element directory = (Element) directories.item(i);
                                    if(directory.getAttribute("name").equals(title)){
                                        directory.getParentNode().removeChild(directories.item(i));

                                        //Save XML File
                                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                                        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                                        Source source = new DOMSource(document);
                                        Result result = new StreamResult(xml_file);
                                        transformer.transform(source, result);

                                        //Refresh Favourites
                                        GlobalFrame.favourites.refresh();

                                        break;
                                    }
                                }
                                //saveXMLContent(document, xmlFile);
                            } catch (Exception exception) {
                                System.err.println(exception.getMessage());
                            }
                        }
                    });

                    popup.add(delete);
                    add(popup);
                    popup.show(self, event.getX(), event.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent event) {

            }

            @Override
            public void mouseReleased(MouseEvent event) {

            }

            @Override
            public void mouseEntered(MouseEvent event) {
                setForeground(new Color(0xFF5F48));
            }

            @Override
            public void mouseExited(MouseEvent event) {
                setForeground(new Color(000));
            }
        });

    }

    String get_path(){
        return path;
    }
}
