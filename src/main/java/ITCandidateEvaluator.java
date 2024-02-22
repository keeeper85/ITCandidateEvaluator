import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import model.*;
import model.storage.FileStrategy;
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
        Model model = new Model(new FileStrategy());
        Controller controller = new Controller(model);
        View view = controller.getView();
        model.addObserver(view);

        SwingUtilities.invokeLater(() ->{view.initView();});

    }

    public static void alterString(String string){
        String altered = string.replace("\n","\n\n");
        System.out.println(altered);
    }
}
