import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HourlyView extends View {

    AppManager app;

    JLabel  title = new JLabel("Secondary View");
    JButton sharedButton;

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
        sharedButton.setBounds(60, 800, 220, 30);
        add(sharedButton);
    }
}
