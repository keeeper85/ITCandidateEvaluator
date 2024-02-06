package view.swing.stages;

import view.swing.View;

import javax.swing.*;
import java.util.HashMap;

public class LiveCodingView extends JPanel implements Collectable{

    private View view;

    public LiveCodingView(View view) {
        this.view = view;
    }

    @Override
    public HashMap<String, String> collectData() {
        return null;
    }
}
