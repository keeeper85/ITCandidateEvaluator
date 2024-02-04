package view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class RecruitmentView extends JPanel {

    private View view;
    private Dimension buttonSize = new Dimension(500, 100);
    private Font buttonFont = new Font(Font.DIALOG, Font.PLAIN, 26);
    private ArrayList<JButton> buttons = new ArrayList<>();

    public RecruitmentView(View view) {
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
            button.setPreferredSize(buttonSize);
            button.setFont(buttonFont);
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
        buttons.add(startNew);
        return startNew;
    }

    private JButton createOpenButton(){
        JButton openRecruitment = new JButton("Open an existing recruitment process");
        buttons.add(openRecruitment);
        return openRecruitment;
    }

    private JButton createExitButton(){
        JButton exit = new JButton("Exit");
        exit.addActionListener((e -> {System.exit(0);}));
        buttons.add(exit);
        return exit;
    }


}
