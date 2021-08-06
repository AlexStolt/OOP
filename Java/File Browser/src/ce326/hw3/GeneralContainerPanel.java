package ce326.hw3;

import javax.swing.*;
import java.awt.*;

public class GeneralContainerPanel extends JPanel {
    static boolean search_activated;
    static BreadcrumbPanel breadcrumb = null;
    static ContentPanel content = null;
    public GeneralContainerPanel(){
        search_activated = false;

        render();
    }

    void render(){

        //Set Layout Manager
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Global Constraints
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        //Two Grid Rows and One Column
        if(!search_activated){
            //Grid[0] = BreadCrumb
            constraints.gridy = 0;
            constraints.weighty = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            breadcrumb = new BreadcrumbPanel();
            add(breadcrumb, constraints);

            //Grid[1] = Content
            constraints.gridy = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            content = new ContentPanel();
            add(content.render_panel(), constraints);
        }
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
