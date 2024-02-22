package model.storage;

import model.Recruitment;

import java.util.List;

public interface StorageStrategy {
    List<Recruitment> getRecruitmentList();
    boolean updateRecruitmentList(List<Recruitment> recruitmentList);
}
