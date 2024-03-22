package com.example.studytracker;

import com.example.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import java.sql.SQLException;
import java.util.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class UpdateController {

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField taskTextField;

    @FXML
    private JFXButton updateButton;

    private DatabaseHandler databaseHandler;

    private Long taskID;

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    public Long getTaskID() {
        return taskID;
    }

    public void setTaskField(String task) {
        this.taskTextField.setText(task);
    }

    public String getTask() {
        return this.taskTextField.getText().trim();
    }

    public void setUpdateDescriptionField(String description) {
        this.descriptionTextField.setText(description);
    }

    public String getDescription() {
        return this.descriptionTextField.getText().trim();
    }


    @FXML
    void initialize() {
        updateButton.setOnAction(event -> {
            Calendar calendar = Calendar.getInstance();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTimeInMillis());

            Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
            informationAlert.setTitle("Confirmed!");
            informationAlert.setHeaderText(null);
            informationAlert.setContentText("Updated Successfully! Reload to show the change!");
            informationAlert.showAndWait();

            databaseHandler = new DatabaseHandler();
            try {
                databaseHandler.updateTask(timestamp, getDescription(), getTask(), getTaskID());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
