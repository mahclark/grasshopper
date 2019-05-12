package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InitialView extends View {

    private AppManager app;

    private JLabel  title = new JLabel("Initial swing.View");
    private JButton sharedButton;

    public InitialView(AppManager app, JButton button) {
        this.app = app;
        setLayout(null);

        setBackground(Color.BLUE);
        sharedButton = button;
        sharedButton.setBounds(100, 500, 220, 30);
        title.setBounds(50, 50, 100, 30);
        title.setForeground(Color.white);

        add(title);
    }

    @Override
    public void loadView() {
        app.cl.show(app.mainPanel, "initial"); //This is what actually changes the view

        for (ActionListener listener : sharedButton.getActionListeners()) {
            sharedButton.removeActionListener(listener);
        }
        sharedButton.addActionListener(e -> app.segueToSecond());

//        swing.Animator.animate(sharedButton, 100, 500, 1);
        add(sharedButton); //Automatically removes button from previous container
    }
}