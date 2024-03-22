package com.example.studytracker;

import com.example.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SearchTaskController {

    @FXML
    private JFXButton searchButton;

    @FXML
    private DatePicker selectDate;

    public static LocalDate localDate;

    DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    void initialize() {
        searchButton.setOnAction(event -> {
            try {
                localDate = selectDate.getValue();
                setSelectedDate(localDate);
                AdditemController additemController = new AdditemController();
                ResultSet resultSet = databaseHandler.getTasksByDate(localDate, additemController.getUserID());
                if(resultSet.next()){
                    searchButton.getScene().getWindow().hide();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("searchResult.fxml"));
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
                    stage.showAndWait();
                } else{
                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Opps!");
                    informationAlert.setHeaderText(null);
                    informationAlert.setContentText("No task available on this date!");
                    informationAlert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public LocalDate getSelectedDate() {
        return localDate;
    }

    public void setSelectedDate(LocalDate localDate){
        SearchTaskController.localDate = localDate;
    }


}
