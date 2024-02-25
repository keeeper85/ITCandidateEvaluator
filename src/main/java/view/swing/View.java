package view.swing;

import controller.Controller;
import model.Model;
import model.Recruitment;

import javax.swing.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

/**
 *
 */

public class View extends JFrame implements Observer {

    private Model model;
    private Controller controller;
    private JPanel currentPanel;
    private Stack<JPanel> previousPanels = new Stack<>();

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
        Model.logger.info("View has been established.");
    }

    public void setCurrentPanel(JPanel newPanel) {
        previousPanels.add(currentPanel);
        remove(currentPanel);
        currentPanel = newPanel;
        add(newPanel);
        revalidate();
        repaint();
    }

    public void resetPreviousPanels() {
        previousPanels = new Stack<>();
        previousPanels.add(new InitialView(this));
    }

    public void returnToPreviousPanel(){
        remove(currentPanel);
        JPanel previousPanel = previousPanels.pop();
        currentPanel = previousPanel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void startOver(){
        remove(currentPanel);
        currentPanel = new InitialView(this);
        previousPanels.clear();
        add(currentPanel);
        revalidate();
        repaint();
        Model.logger.info("App went back to the initial view. Previous panels reset.");
    }
    public Model getModel() {
        return model;
    }
    public Controller getController() { return controller; }
    @Override
    public void update(Observable o, Object arg) {
        if (currentPanel instanceof RecruitmentsListView){
            RecruitmentsListView recruitmentsListView = (RecruitmentsListView) currentPanel;
            recruitmentsListView.populateRecruitmentList();
        }
        currentPanel.repaint();
        currentPanel.revalidate();
    }
}
