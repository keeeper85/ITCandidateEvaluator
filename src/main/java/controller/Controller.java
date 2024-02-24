package controller;

import model.Candidate;
import model.Model;
import model.Recruitment;
import view.swing.View;

import javax.swing.*;

/**
 * Controller class is nearly unnecessary however I wanted to keep it here for future development.
 * At first, I thought about moving there Action Listeners of the buttons in View package, but these classes would
 * be very small and this wouldn't bring any visible benefits.
 * The communication from View to Model classes goes through the Data Transfer Object Candidate DTO
 * which instance is factored in here (createTemporaryCandidate method).
 */

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
