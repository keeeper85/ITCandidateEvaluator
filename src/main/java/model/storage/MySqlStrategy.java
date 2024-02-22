package model.storage;

import model.Recruitment;

import java.util.List;

public class MySqlStrategy implements StorageStrategy{
    @Override
    public List<Recruitment> getRecruitmentList() {
        return null;
    }

    @Override
    public boolean updateRecruitmentList(List<Recruitment> recruitmentList) {

        return true;
    }
}
