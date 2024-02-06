package view.swing.stages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerPanel extends JPanel {
    private Timer timer;
    private JLabel timerLabel;
    private JButton pauseButton;

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
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsElapsed++;
                updateTimerLabel();
            }
        });
        timer.start();
    }

    private void updateTimerLabel() {
        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    public int getSecondsElapsed() {
        return secondsElapsed;
    }
}