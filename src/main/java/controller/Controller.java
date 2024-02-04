package controller;

import model.Model;
import view.swing.View;

import javax.swing.*;

public class Controller {

    Model model;
    View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(model,this);
    }

    public View getView() {
        return view;
    }
}
