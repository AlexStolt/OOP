package ce326.hw3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SearchBarPanel extends JPanel {
    public SearchBarPanel(){
        setLayout(new GridLayout(1,1));
        Form form = new Form();
        Button search = new Button();

        add(form);
        add(search);
    }



    class Form extends JTextField {
        public Form(){
            super("Recursive Search");
            setFont(new Font("Arial", Font.PLAIN, 15));

            addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    setText("");
                }
            });


        }
    }

    class Button extends JButton {
        public Button(){
            super("Search");
            setFont(new Font("Arial", Font.PLAIN, 15));
            setPreferredSize(new Dimension(20,25));
        }
    }
}
