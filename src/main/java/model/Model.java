package model;

import java.util.HashMap;
import java.util.Observable;

public class Model extends Observable {

    public Recruitment createNewRecruitment(String name, Presets presets){
        return new Recruitment(this, name, presets);
    }
}
