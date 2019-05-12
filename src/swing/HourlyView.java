package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HourlyView extends View {

    private AppManager app;

    private JLabel  title = new JLabel("Secondary swing.View");
    private JButton sharedButton;

    public HourlyView(AppManager app, JButton button) {
        this.app = app;
        setLayout(null);

        setBackground(Color.GREEN);
        sharedButton = button;
        title.setBounds(50, 50, 100, 30);

        add(title);
    }

    @Override
    public void loadView() {
        app.cl.show(app.mainPanel, "secondary");
        for (ActionListener listener : sharedButton.getActionListeners()) {
            sharedButton.removeActionListener(listener);
        }
        sharedButton.addActionListener(e -> app.segueToInitial());
//        swing.Animator.animate(sharedButton, 60, 200, 1);
        add(sharedButton);
    }
}
