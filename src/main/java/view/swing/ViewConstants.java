package view.swing;

import java.awt.*;

public class ViewConstants {
    public static final String APP_NAME = "ITCandidateEvaluator";
    public static final int WINDOW_WIDTH_PIXELS = 1280;
    public static final int WINDOW_HEIGHT_PIXELS = 720;
    public static final Font BUTTON_FONT_LARGE = new Font(Font.DIALOG, Font.PLAIN, 26);
    public static final String PRESETS_HOWTO = "Adjust sliders accordingly to their importance.\n" +
            "Higher slider value represents higher multiplier for its item score.\n" +
            "If a slider is set to '0' - its item will not occur during the process.\n" +
            "You can choose one of default presets or create your own - adjust sliders and press 'Save Presets' button.\n" +
            "Press 'Start' button when you're ready, set the name for the process and start the evaluation.\n";

}
