package model.storage;

import model.Model;
import model.Presets;
import model.Recruitment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mockito;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MySqlStrategyTest {

    MySqlStrategy mySqlStrategy;
    private Connection connection;

    @BeforeEach
    public void setUp(){
        mySqlStrategy = new MySqlStrategy();
    }

    @Test
    public void testConnection() throws SQLException {
        connection = mySqlStrategy.getConnection();

        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void closeConnection() throws SQLException {
        connection = mySqlStrategy.getConnection();
        mySqlStrategy.closeConnection();

        assertTrue(connection.isClosed());
    }

    @Test
    public void getRecruitmentList(){
        Model model = new Model(mySqlStrategy);
        int listSize0 = mySqlStrategy.getRecruitmentList(model).size();

        assertEquals(0, listSize0);

        model.startNewRecruitment("testRecruitment", "testPresets", new HashMap<>());
        int listSize1 = mySqlStrategy.getRecruitmentList(model).size();

        assertEquals(1, listSize1);

        Recruitment recruitment = model.getOpenRecruitmentProcesses().get(0);
        model.deleteExistingRecruitment(recruitment);
        int listSize2 = mySqlStrategy.getRecruitmentList(model).size();

        assertEquals(0, listSize2);
    }

}