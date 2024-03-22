package com.example.studytracker;

import com.example.database.DatabaseHandler;
import com.example.model.Task;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ListController {

    @FXML
    private JFXButton backButton;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private JFXListView<Task> listView;

    @FXML
    private JFXButton taskButton;

    @FXML
    private TextField taskTextField;

    @FXML
    private ImageView reloadButton;

    private DatabaseHandler databaseHandler;

    @FXML
    private Button searchButton;

    @FXML
    void initialize() throws SQLException {

        ObservableList<Task> list = FXCollections.observableArrayList();
        AdditemController additemController = new AdditemController();
        databaseHandler = new DatabaseHandler();
        ResultSet resultSet = databaseHandler.getTaskByUser(additemController.getUserID());

        while (resultSet.next()){
            Task task = new Task();
            task.setTaskID(resultSet.getLong("TaskID"));
            task.setTask(resultSet.getString("Task"));
            task.setCreatedDate(resultSet.getDate("CreatedDate").toLocalDate());
            task.setDescription(resultSet.getString("Description"));
            list.add(task);
        }

        listView.setItems(list);
        listView.setCellFactory(CellController -> new CellController());

        taskButton.setOnAction(event -> {
            try {
                addNewTask();
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setTitle("Successful!");
                informationAlert.setHeaderText(null);
                informationAlert.setContentText("Task added successfully!");
                informationAlert.show();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        reloadButton.setOnMouseClicked(event ->{
            try {
                reloadList();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        backButton.setOnAction(event -> {
            backButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("additem.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Add Task");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        });

        searchButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("searchTask.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Your Tasks!");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        });
    }

    public void reloadList() throws SQLException, ClassNotFoundException {

        ObservableList<Task> reloadTasks = FXCollections.observableArrayList();
        AdditemController additemController = new AdditemController();
        databaseHandler = new DatabaseHandler();
        ResultSet resultSet = databaseHandler.getTaskByUser(additemController.getUserID());

        while (resultSet.next()) {
            Task task = new Task();
            task.setTaskID(resultSet.getLong("TaskID"));
            task.setTask(resultSet.getString("Task"));
            task.setCreatedDate(resultSet.getDate("CreatedDate").toLocalDate());
            task.setDescription(resultSet.getString("Description"));

            reloadTasks.addAll(task);

        }

        listView.setItems(reloadTasks);
        listView.setCellFactory(CellController -> new CellController());

    }

    public void addNewTask() throws SQLException, ClassNotFoundException {
            if(!taskTextField.getText().isEmpty() || !descriptionTextField.getText().isEmpty()){
                Task myNewTask = getTask();
                databaseHandler.insertTask(myNewTask);
                taskTextField.setText("");
                descriptionTextField.setText("");

                initialize();
            }
    }

    private Task getTask() {
        Task myNewTask = new Task();
        AdditemController additemController = new AdditemController();

        LocalDate currentDate = LocalDate.now();
        myNewTask.setUserID(additemController.getUserID());
        myNewTask.setTask(taskTextField.getText().trim());
        myNewTask.setCreatedDate(currentDate);
        myNewTask.setDescription(descriptionTextField.getText().trim());
        return myNewTask;
    }

}
