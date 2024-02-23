package model.storage;

import model.Model;
import model.Recruitment;

import java.util.List;

public interface StorageStrategy {
    List<Recruitment> getRecruitmentList(Model model);
    boolean updateRecruitmentList(List<Recruitment> recruitmentList, Model model);
}
