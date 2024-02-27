package model.storage;

import model.Model;
import model.Recruitment;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * AbstractStrategy implements StorageStrategy interface which enlists methods necessary for AS descendants.
 * Methods implemented here have the purpose to prepare lists (List<Recruitment>): toAdd, toDelete and toReplace.
 * Descendants of this class will use these lists to modify the database they use.
 * Some class which is often used by the descendants is getRecruitmentNames(List<Recruitment> recruitmentList) List<Recruitment> to List<String> with names only.
 */

public abstract class AbstractStrategy implements StorageStrategy {
    protected List<Recruitment> toAdd = new ArrayList<>();
    protected List<Recruitment> toDelete = new ArrayList<>();
    protected List<Recruitment> toReplace = new ArrayList<>();
    protected Connection connection;


    @Override
    public abstract List<Recruitment> getRecruitmentList(Model model);

    @Override
    public boolean updateRecruitmentList(List<Recruitment> recruitmentList, Model model) {
        List<Recruitment> freshRecruitments = recruitmentList;
        List<Recruitment> oldRecruitments = getRecruitmentList(model);

        addNew(freshRecruitments,oldRecruitments);
        replaceModified(freshRecruitments);
        deleteOld(freshRecruitments,oldRecruitments);

        return updateList();
    }

    private void addNew(List<Recruitment> freshRecruitments, List<Recruitment> oldRecruitments) {
        List<String> namesToAdd = new ArrayList<>();

        for (Recruitment freshRecruitment : freshRecruitments) {
            String freshName = freshRecruitment.getName();
            List<String> oldNames = getRecruitmentNames(oldRecruitments);
            if (!oldNames.contains(freshName)) namesToAdd.add(freshName);
        }

        toAdd.addAll(getRecruitmentsFromNames(namesToAdd, freshRecruitments));
    }

    private void replaceModified(List<Recruitment> freshRecruitments) {
        for (Recruitment freshRecruitment : freshRecruitments) {
            if (freshRecruitment.isModified()) toReplace.add(freshRecruitment);
        }
    }

    private void deleteOld(List<Recruitment> freshRecruitments, List<Recruitment> oldRecruitments) {
        List<String> namesToDelete = new ArrayList<>();

        for (Recruitment oldRecruitment : oldRecruitments) {
            String oldName = oldRecruitment.getName();
            List<String> freshNames = getRecruitmentNames(freshRecruitments);
            if (!freshNames.contains(oldName)) namesToDelete.add(oldName);
        }

        toDelete.addAll(getRecruitmentsFromNames(namesToDelete, oldRecruitments));
    }

    protected List<String> getRecruitmentNames(List<Recruitment> recruitmentList){
        List<String> recruitmentNames = new ArrayList<>();
        for (Recruitment recruitment : recruitmentList) {
            recruitmentNames.add(recruitment.getName());
        }

        return recruitmentNames;
    }

    private List<Recruitment> getRecruitmentsFromNames(List<String> names, List<Recruitment> recruitments){
        List<Recruitment> recruitmentList = new ArrayList<>();

        for (Recruitment recruitment : recruitments) {
            String recruitmentName = recruitment.getName();
            for (String name : names) {
                if (name.equals(recruitmentName)) recruitmentList.add(recruitment);
            }
        }

        return recruitmentList;
    }

    private boolean updateList() {
        boolean isAdded = true;
        boolean isReplaced = true;
        boolean isDeleted = true;

        if (toAdd.size() > 0) isAdded = addRecords();
        if (toReplace.size() > 0) isReplaced = replaceRecords();
        if (toDelete.size() > 0) isDeleted = deleteRecords();

        Model.logger.info("Recruitment list has been updated.");
        cleanLists();

        return (isAdded && isReplaced && isDeleted);
    }
    private void cleanLists() {
        toAdd.clear();
        toReplace.clear();
        toDelete.clear();
    }

    public List<Recruitment> getToAdd() {
        return toAdd;
    }

    public List<Recruitment> getToDelete() {
        return toDelete;
    }

    public List<Recruitment> getToReplace() {
        return toReplace;
    }

    protected abstract boolean addRecords();

    protected abstract boolean replaceRecords();

    protected abstract boolean deleteRecords();

}
