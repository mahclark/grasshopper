import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InitialView extends View {

    AppManager app;

    JLabel  title = new JLabel("Initial View");
    JButton sharedButton;

    public InitialView(AppManager app, JButton button) {
        this.app = app;
        setLayout(null);

        setBackground(Color.BLUE);
        sharedButton = button;
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

        sharedButton.setBounds(100, 500, 220, 30);
        add(sharedButton); //Automatically removes button from previous container
    }
}