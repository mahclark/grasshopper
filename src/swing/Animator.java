package swing;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

public class Animator {

    private final static int fps = 60;

    public static void animate(JComponent component, int xDest, int yDest, double duration) {

        Timer timer = new Timer();

        final int xInitial = component.getX();
        final int yInitial = component.getY();

        class ScheduleTask extends TimerTask {
            private int frame = 0;

            @Override
            public void run() {
                frame++;

                double x = xInitial + (xDest - xInitial)*frame/(duration*fps);
                double y = xInitial + (yDest - yInitial)*frame/(duration*fps);

                component.setLocation((int) x, (int) y);

                if (frame >= duration * fps) {
                    timer.cancel();
                    component.setLocation(xDest, yDest);
                }
            }
        }

        timer.scheduleAtFixedRate(new ScheduleTask(), 0, 1000 / fps);
    }
}
