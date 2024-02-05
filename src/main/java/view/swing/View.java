package view.swing;

import controller.Controller;
import model.Model;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

public class View extends JFrame implements Observer {

    private Model model;
    private Controller controller;
    private JPanel currentPanel;

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

        currentPanel = new InitialView(this);
        setCurrentPanel(currentPanel);
    }

    public void setCurrentPanel(JPanel newPanel) {
        remove(currentPanel);
        currentPanel = newPanel;
        add(newPanel);
        revalidate();
        repaint();
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
