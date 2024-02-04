package controller;

import model.Model;
import view.swing.View;

public class Controller {

    Model model;
    View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(model,this);
    }
}
