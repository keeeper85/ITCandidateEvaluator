package controller;

import model.Candidate;
import model.Model;
import model.Recruitment;
import view.swing.View;

import javax.swing.*;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(model,this);
    }

    public CandidateDTO createTemporaryCandidate(Candidate candidate, Recruitment recruitment){
        return new CandidateDTO(candidate, recruitment);
    }

    public View getView() {
        return view;
    }
}
