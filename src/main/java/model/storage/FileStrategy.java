package model.storage;

import model.Recruitment;

import java.util.ArrayList;
import java.util.List;

public class FileStrategy implements StorageStrategy{
    @Override
    public List<Recruitment> getRecruitmentList() {
        List<Recruitment> recruitmentList = new ArrayList<>();

        return recruitmentList;
    }

    @Override
    public boolean updateRecruitmentList(List<Recruitment> recruitmentList) {

        return true;
    }
}
