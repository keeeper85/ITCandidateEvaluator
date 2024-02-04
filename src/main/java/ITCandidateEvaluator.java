import controller.Controller;
import model.Model;
import view.swing.View;

import javax.swing.*;

public class ITCandidateEvaluator {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = controller.getView();

        SwingUtilities.invokeLater(() ->{view.initView();});


    }
}
