package com.example.studytracker;

import com.example.database.DatabaseHandler;
import com.example.model.Task;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddItemFormController {

    @FXML
    private JFXButton backButton;

    private DatabaseHandler databaseHandler;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private JFXButton saveTaskButton;

    @FXML
    private TextField taskTextField;

    @FXML
    private JFXButton taskButton;


    @FXML
    void initialize() {

        databaseHandler = new DatabaseHandler();

        saveTaskButton.setOnAction(event -> {
            try {
                Task task = new Task();
                LocalDate currentDate = LocalDate.now();
                String taskText = taskTextField.getText().trim();
                String descriptionText = descriptionTextField.getText().trim();

                AdditemController additemController = new AdditemController();

                if(!taskText.isEmpty() || !descriptionText.isEmpty()){
                    task.setUserID(additemController.getUserID());
                    task.setCreatedDate(currentDate);
                    task.setDescription(descriptionText);
                    task.setTask(taskText);
                    databaseHandler.insertTask(task);

                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Successful!");
                    informationAlert.setHeaderText(null);
                    informationAlert.setContentText("Task added successfully!");
                    informationAlert.showAndWait();

                    taskButton.setVisible(true);
                    Long taskNumber = databaseHandler.getAllTask(additemController.getUserID());
                    taskButton.setText("Tasks: "+"("+taskNumber+")");
                    taskTextField.setText("");
                    descriptionTextField.setText("");
                    taskButton.setOnAction(event1 -> {
                        taskButton.getScene().getWindow().hide();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("list.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        Parent root = loader.getRoot();
                        Stage stage = new Stage();
                        stage.setTitle("Your Task List");
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();
                    });
                }

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
    }

}
