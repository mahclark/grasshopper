import javax.swing.*;
import java.awt.*;

public class SecondaryView extends View {

    AppManager app;

    JButton initialButton = new JButton("Go to 1");

    public SecondaryView(AppManager app) {
        this.app = app;
        setLayout(null);

        setBackground(Color.GREEN);
        initialButton.addActionListener(e -> app.segueToInitial());
        initialButton.setBounds(60, 800, 220, 30);
        add(initialButton);
    }
}