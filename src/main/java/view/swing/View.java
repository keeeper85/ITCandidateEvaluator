package view.swing;

import controller.Controller;
import model.Model;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {

    Model model;
    Controller controller;

    public View(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
