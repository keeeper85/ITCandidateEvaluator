package model.storage;

import model.Recruitment;

import java.util.List;

public class MySqlStrategy extends AbstractStrategy{

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
