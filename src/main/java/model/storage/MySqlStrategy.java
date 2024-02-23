package model.storage;

import model.Model;
import model.Recruitment;

import java.io.*;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class MySqlStrategy extends AbstractStrategy{

    public MySqlStrategy() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/recruitments",
                    "root", "root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Recruitment> getRecruitmentList(Model model) {
        List<Recruitment> recruitmentList = new ArrayList<>();
        Statement statement;
        ResultSet results;

        try {
            statement = connection.createStatement();
            results = statement.executeQuery("SELECT recruitment_object FROM recruitments_list");

            while (results.next()) {
                byte[] serializedObject = results.getBytes("recruitment_object");
                Recruitment recruitment = deserializeObject(serializedObject);
                recruitmentList.add(recruitment);
            }

            results.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return recruitmentList;
    }

    private Recruitment deserializeObject(byte[] serializedObject) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedObject))) {
            return (Recruitment) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error deserializing recruitment object", e);
        }
    }

    @Override
    protected boolean addRecords() {
        String query = "INSERT INTO recruitments_list (recruitment_name, recruitment_object) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (Recruitment recruitment : toAdd) {
                String name = recruitment.getName();
                byte[] serializedObject = serializeObject(recruitment);
                preparedStatement.setString(1, name);
                preparedStatement.setBytes(2, serializedObject);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding records to the database", e);
        }
    }

    private byte[] serializeObject(Recruitment recruitment) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(recruitment);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error serializing recruitment object", e);
        }
    }

    @Override
    protected boolean deleteRecords() {
        List<String> namesForRemoval = getRecruitmentNames(toDelete);

        try {
        String sql = "DELETE FROM recruitments_list WHERE recruitment_name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        for (String name : namesForRemoval) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No record found with name: " + name);
            }
        }
        statement.close();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    }

    @Override
    protected boolean replaceRecords() {
        try {
            connection.setAutoCommit(false);

            boolean deleteSuccess = deleteOldRecords();
            if (!deleteSuccess) {
                connection.rollback();
                return false;
            }

            boolean insertSuccess = insertNewRecords();
            if (!insertSuccess) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean deleteOldRecords() throws SQLException {
        String deleteSql = "DELETE FROM recruitments_list WHERE recruitment_name = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);

        for (Recruitment recruitment : toReplace) {
            deleteStatement.setString(1, recruitment.getName());
            deleteStatement.executeUpdate();
        }

        deleteStatement.close();
        return true;
    }

    private boolean insertNewRecords() throws SQLException {
        String insertSql = "INSERT INTO recruitments_list (recruitment_name, recruitment_object) VALUES (?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertSql);

        for (Recruitment recruitment : toReplace) {
            byte[] serializedObject = serializeObject(recruitment);
            insertStatement.setString(1, recruitment.getName());
            insertStatement.setBytes(2, serializedObject);
            insertStatement.executeUpdate();
        }

        insertStatement.close();
        return true;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
