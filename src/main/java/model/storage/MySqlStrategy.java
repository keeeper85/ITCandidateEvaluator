package model.storage;

import model.Model;
import model.Recruitment;

import java.util.List;

public class MySqlStrategy extends AbstractStrategy{

    @Override
    public List<Recruitment> getRecruitmentList(Model model) {
        return null;
    }

    @Override
    protected boolean addRecords() {
        return false;
    }

    @Override
    protected boolean replaceRecords() {
        return false;
    }

    @Override
    protected boolean deleteRecords() {
        return false;
    }
}
