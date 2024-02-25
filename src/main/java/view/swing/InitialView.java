package view.swing;

import model.Model;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * It's the first thing the user sees (just after selecting storage).
 * From here the user can move onto the Presets view to create a new Recruitment process or to the Recruitment list view to see open processes.
 * Exit button shuts down the app and close the database connection (if previously established).
 */

public class InitialView extends JPanel {

    private View view;
    private final Dimension BUTTON_SIZE = new Dimension(500, 100);
    private ArrayList<JButton> buttons = new ArrayList<>();

    public InitialView(View view) {
        this.view = view;
        setLayout();
        addButtons();
    }

    private void setLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addButtons() {

        JPanel buttonPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        buttonPanel1.add(createStartNewButton());
        buttonPanel2.add(createOpenButton());
        buttonPanel3.add(createExitButton());

        for (JButton button : buttons) {
            button.setPreferredSize(BUTTON_SIZE);
            button.setFont(ViewConstants.FONT_LARGE);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        add(Box.createVerticalGlue());
        add(buttonPanel1);
        add(buttonPanel2);
        add(buttonPanel3);
        add(Box.createVerticalGlue());
    }

    private JButton createStartNewButton(){
        JButton startNew = new JButton("Start new recruitment process");
        startNew.addActionListener(e -> {view.setCurrentPanel(new PresetsView(view));});
        buttons.add(startNew);
        return startNew;
    }

    private JButton createOpenButton(){
        JButton openRecruitment = new JButton("Open an existing recruitment process");
        openRecruitment.addActionListener(e -> {view.setCurrentPanel(new RecruitmentsListView(view));});
        buttons.add(openRecruitment);
        return openRecruitment;
    }

    private JButton createExitButton(){
        JButton exit = new JButton("Exit");
        exit.addActionListener((e -> {
            view.getModel().closeDatabaseConnection();
            Model.logger.info("Exit button clicked. App is closing");
            System.exit(0);}));
        buttons.add(exit);
        return exit;
    }


}
