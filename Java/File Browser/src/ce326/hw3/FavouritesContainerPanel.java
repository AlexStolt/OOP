package ce326.hw3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FavouritesContainerPanel extends JPanel {
    FavouritesContainerPanel self = this;
    public FavouritesContainerPanel(){
        //Calculate Content Height to Update JScrollPane Dimensions
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                int rows = self.getHeight() / (FavouritesLabel.height + FavouritesLabel.margin);
                setLayout(new GridLayout(rows, 1));
                repaint();
                revalidate();
            }
        });

        //setBorder(BorderFactory.createMatteBorder(0,0,0, 1, new Color(0x8D8D8D)));
        try {
            render();
        } catch (Exception exception){
            System.out.println(exception);
        }

    }

    void render() throws IOException, SAXException, ParserConfigurationException {
        //Render Basic XML Input
        add(new FavouritesLabel("Home", System.getProperty("user.home")));




        File xmlFile = new File(GlobalFrame.configuration);
        if(!xmlFile.exists()){
            //Create File

            xmlFile.getParentFile().mkdirs();
            xmlFile.createNewFile();

            FileWriter writer = new FileWriter(xmlFile);
            writer.write("""
                    <?xml version="1.0" encoding="UTF-8"?>
                    <favourites>
                    </favourites>""");
            writer.close();

            return;
        }
        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        //an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile);
        doc.getDocumentElement().normalize();


        NodeList nodes = doc.getElementsByTagName("directory");
        // nodeList is not iterable, so we are using for loop
        for (int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
//                System.out.println(element.getAttributes().getNamedItem("name").getTextContent());
//                System.out.println(element.getAttributes().getNamedItem("path").getTextContent());

                add(new FavouritesLabel(element.getAttributes().getNamedItem("name").getTextContent(), element.getAttributes().getNamedItem("path").getTextContent()));
            }
        }
    }

    void refresh(){
        //Clean Components
        for (Component component : getComponents()) {
            remove(component);
        }

        try {
            render();
        } catch (Exception exception){
            System.out.println(exception);
        }

        //Refresh DOM
        revalidate();
        repaint();
    }

}
