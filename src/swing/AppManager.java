package swing;

import javax.swing.*;
import java.awt.*;

public class AppManager extends JFrame {
    JPanel mainPanel = new JPanel();
    View initialView;
    View secondaryView;
    CardLayout cl = new CardLayout();

    JButton switchButton = new JButton("Shared Button");

    public AppManager(String title) {
        super(title);
        mainPanel.setLayout(cl);
        setSize(562,1000); // 0.75 scaled resolution of iPhone 8
        setResizable(false);

        initialView = new InitialView(this, switchButton);
        secondaryView = new HourlyView(this, switchButton); //is there a better way to pass shared view components?

        mainPanel.add(initialView, "initial");
        mainPanel.add(secondaryView, "secondary");
        initialView.loadView();

        add(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void segueToSecond() {
        secondaryView.loadView();
    }
    public void segueToInitial() {
        initialView.loadView();
    }

    public static void main(String[] args) {
        AppManager app = new AppManager("Grasshopper");
        app.setVisible(true);
    }
}