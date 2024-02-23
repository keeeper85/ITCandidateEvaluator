import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import model.*;
import model.storage.FileStrategy;
import model.storage.MySqlStrategy;
import model.storage.StorageStrategy;
import view.swing.View;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class ITCandidateEvaluator {
    public static void main(String[] args) {
        Model model = new Model(chooseStorage());
        Controller controller = new Controller(model);
        View view = controller.getView();
        model.addObserver(view);

        SwingUtilities.invokeLater(() ->{view.initView();});

    }

    public static StorageStrategy chooseStorage() {

        String[] options = {"Use local files", "Use MySQL database"};
        int result = JOptionPane.showOptionDialog(
                null,
                "Where do you want to keep your recruitment records?",
                "Choose storage:",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        switch (result) {
            case 0:
                return new FileStrategy();
            case 1:
                return new MySqlStrategy();
            default:
                JOptionPane.showMessageDialog(
                        null,
                        "No storage option selected. Exiting the application.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
        }
        return null;
    }
}
