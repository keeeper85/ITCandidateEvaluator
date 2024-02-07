package view.swing;

import java.awt.*;

public class ViewConstants {
    public static final String APP_NAME = "ITCandidateEvaluator";
    public static final int WINDOW_WIDTH_PIXELS = 1280;
    public static final int WINDOW_HEIGHT_PIXELS = 720;
    public static final Font FONT_LARGE = new Font(Font.DIALOG, Font.PLAIN, 26);
    public static final Font FONT_STAGE_INFO = new Font(Font.DIALOG, Font.PLAIN, 16);
    public static final String[] SLIDER_VALUE_DESCRIPTION = {"Unacceptable", "Poor", "Not too good", "Average", "Quite good", "Impressive", "Amazing!"};
    public static final String PRESETS_HOWTO = "Adjust sliders accordingly to their importance.\n" +
            "Higher slider value represents higher multiplier for its item score.\n" +
            "If a slider is set to '0' - its item will not occur during the process.\n" +
            "You can choose one of default presets or create your own - adjust sliders and press 'Save Presets' button.\n" +
            "Press 'Start' button when you're ready, set the name for the process and start the evaluation.\n";

    public static final String RESUME_STAGE_INFO =
            "<html>This stage's purpose is to evaluate candidate's background - whether it's beneficial for the applied position.<br>" +
            "( It should be performed before the actual interview. )<br>" +
            "<br>" +
            "<br>" +
            "At first go through the candidate's resume (and application letter if attached). Consider the following:<br>" +
            "- does it look neat and well prepared?<br>" +
            "- are there spelling or grammar errors?<br>" +
            "- does it contain all the necessary information?<br>" +
            "- is it too long or too short?<br>" +
            "- is it a default template or is it customized for the offer/company?<br>" +
            "- does it look appealing for you as a recruiter?<br>" +
            "<br>" +
            "<br>" +
            "Then you can check candidate's social media and attached links for website profiles or blogs.<br>" +
            "- does the candidate's profile look interesting? Are you excited for the interview or the exact opposite?<br>" +
            "- did you find any disturbing information about the candidate?<br>" +
            "- do you think this candidate would fit well to the team?<br>" +
            "<br></html>";
    public static final String LANGUAGE_STAGE_INFO = "language stage";
    public static final String EXPERIENCE_STAGE_INFO = "experience stage";
    public static final String PROJECTS_STAGE_INFO = "projects stage";
    public static final String LIVE_CODING_STAGE_INFO = "live coding stage";
    public static final String QUESTIONS_STAGE_INFO = "questions stage";
    public static final String SALARY_STAGE_INFO = "salary stage";
    public static final String SOFT_SKILLS_STAGE_INFO = "soft skills stage";

}
