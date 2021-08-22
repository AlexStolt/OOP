package ce326.hw3;

import javax.swing.*;
import java.awt.*;

public class GeneralContainerPanel extends JPanel {
    static boolean search_activated = false;
    static boolean hidden_content = false;
    static SearchBarPanel searchbar;
    static BreadcrumbPanel breadcrumb;
    static ContentPanel content;

    public GeneralContainerPanel(){
        render();
    }

    void render(){
        //Set Layout Manager
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        //Global Constraints
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        //Create and Render Sub-Panels
        searchbar = new SearchBarPanel();
        breadcrumb = new BreadcrumbPanel();
        content = new ContentPanel();

        //Two Grid Rows and One Column
        if(!search_activated){
            //Grid[0] = BreadCrumb
            constraints.gridy = 0;
            constraints.weighty = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;

            add(breadcrumb, constraints);

            //Grid[1] = Content
            constraints.gridy = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
            add(content.render_panel(), constraints);
        }
        //Three Grid Rows and One Column
        else {
            //Grid[0] = Search
            constraints.gridy = 0;
            constraints.weighty = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(searchbar, constraints);

            //Grid[1] = BreadCrumb
            constraints.gridy = 1;
            constraints.weighty = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(breadcrumb, constraints);

            //Grid[2] = Content
            constraints.gridy = 2;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.BOTH;
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
