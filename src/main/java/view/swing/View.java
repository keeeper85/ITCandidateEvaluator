package view.swing;

import controller.Controller;
import model.Model;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class View extends JFrame implements Observer {

    Model model;
    Controller controller;

    public View(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    public void initView(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(ViewConstants.WINDOW_WIDTH_PIXELS, ViewConstants.WINDOW_HEIGHT_PIXELS);
        setLocationRelativeTo(null);
        setTitle(ViewConstants.APP_NAME);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setVisible(true);
        setResizable(false);

        drawRecruitmentPanel();
    }

    private void drawRecruitmentPanel() {
        RecruitmentView recruitmentView = new RecruitmentView(this);
        add(recruitmentView);
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
