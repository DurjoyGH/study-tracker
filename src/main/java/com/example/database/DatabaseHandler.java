package com.example.database;

import com.example.model.Task;
import com.example.model.User;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseHandler extends Configs {
    Connection dbConnection;
    public Connection getDbConnection() throws SQLException {
        String connectionString = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName + "?" + "sslmode=verify-full";
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPassword);
        return dbConnection;
    }

    public void signUp(User user) throws SQLException {
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                         Const.USER_FIRSTNAME + "," + Const.USER_LASTNAME + "," + Const.USER_INSTITUTION + "," +
                         Const.USER_GENDER + "," + Const.USER_USERNAME + "," + Const.USER_PASSWORD + ")"
                         + "VALUES(?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

        preparedStatement.setString(1, user.getFirstName());
        preparedStatement.setString(2, user.getLastName());
        preparedStatement.setString(3, user.getInstitution());
        preparedStatement.setString(4, user.getGender());
        preparedStatement.setString(5, user.getUserName());
        preparedStatement.setString(6, user.getPassword());

        preparedStatement.executeUpdate();
    }

    public ResultSet getUser(User user) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;

        if(!user.getUserName().isEmpty() || !user.getPassword().isEmpty()){
            String query = "SELECT * FROM " + Const.USER_TABLE +
                           " WHERE " + Const.USER_USERNAME + " =?" +
                           " AND " + Const.USER_PASSWORD + " =?";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getUserName());
            preparedStatement.setString(2, user.getPassword());

            resultSet = preparedStatement.executeQuery();
        } else{
            System.out.println("Please enter everything correctly!");
        }
        return resultSet;
    }

    public ResultSet getTaskByUser(Long userID) throws SQLException {
        ResultSet resultTask = null;
        String query = "SELECT * FROM " + Const.TASK_TABLE +
                " WHERE " + Const.USER_ID + " =?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setLong(1, userID);
        resultTask = preparedStatement.executeQuery();
        return  resultTask;
    }

    public void insertTask(Task task) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO " + Const.TASK_TABLE + "(" +
                Const.USER_ID + "," + Const.TASK_TASK + "," + Const.TASK_DATE + "," + Const.TASK_DESCRIPTION + ")"
                + "VALUES(?, ?, ?, ?)";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

        preparedStatement.setLong(1,task.getUserID());
        preparedStatement.setString(2, task.getTask());
        preparedStatement.setObject(3, task.getCreatedDate());
        preparedStatement.setString(4, task.getDescription());

        preparedStatement.executeUpdate();
    }

    public void deleteTask(Long userID, Long taskID) throws SQLException, ClassNotFoundException {
        String query = "DELETE FROM " + Const.TASK_TABLE + " WHERE " +
                        Const.USER_ID + " =?" + " AND " + Const.TASK_ID + " =?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setLong(1, userID);
        preparedStatement.setLong(2, taskID);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void updateTask(Timestamp createdDate, String description, String task, Long taskID) throws SQLException, ClassNotFoundException {

        String query = "UPDATE tasks SET CreatedDate = ?, Description = ?, Task = ? WHERE TaskID = ?";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setTimestamp(1,createdDate);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, task);
        preparedStatement.setLong(4, taskID);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Long getAllTask(Long userID) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM " + Const.TASK_TABLE +
                " WHERE " + Const.USER_ID + " =?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setLong(1, userID);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            return resultSet.getLong(1);
        }
        return resultSet.getLong(1);
    }

    public boolean isUsernameExists(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username=?";
        try (PreparedStatement preparedStatement = getDbConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public ResultSet getTasksByDate(LocalDate date, Long UserID) throws SQLException {
        ResultSet resultTask = null;
        String query = "SELECT * FROM " + Const.TASK_TABLE +
                " WHERE " + Const.TASK_DATE + " =?" + " AND " + Const.USER_ID + " =?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
        preparedStatement.setObject(1, date);
        preparedStatement.setLong(2, UserID);
        resultTask = preparedStatement.executeQuery();
        return resultTask;
    }

}