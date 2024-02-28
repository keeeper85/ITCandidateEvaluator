package view.swing.stages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimerPanelTest {

    @Test
    void getSecondsElapsed() {
        int secondsElapsedBefore = 0;
        TimerPanel timerPanel = new TimerPanel(secondsElapsedBefore);

        assertEquals(secondsElapsedBefore, timerPanel.getSecondsElapsed());

        int secondsElapsedAfter = 10;
        timerPanel = new TimerPanel(secondsElapsedAfter);

        assertEquals(secondsElapsedAfter, timerPanel.getSecondsElapsed());
    }
}