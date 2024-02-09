package view.swing.stages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerPanel extends JPanel {
    private Timer timer;
    private JLabel timerLabel;
    private JButton pauseButton;
    private final int TIMER_DELAY = 1000;
    private final int SECONDS_IN_HOUR = 3600;
    private final int MINUTES_IN_HOUR = 60;

    private int secondsElapsed;

    public TimerPanel() {
        setLayout(new FlowLayout());

        timerLabel = new JLabel("00:00:00");
        add(timerLabel);

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    pauseButton.setText("Resume");
                } else {
                    timer.start();
                    pauseButton.setText("Pause");
                }
            }
        });
        add(pauseButton);
        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                updateTimerLabel();
            }
        });
        timer.start();
    }

    private void updateTimerLabel() {
        int hours = secondsElapsed / SECONDS_IN_HOUR;
        int minutes = (secondsElapsed % SECONDS_IN_HOUR) / MINUTES_IN_HOUR;
        int seconds = secondsElapsed % MINUTES_IN_HOUR;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }

    public void setSecondsElapsed(int secondsElapsed) {
        this.secondsElapsed = secondsElapsed;
    }
}