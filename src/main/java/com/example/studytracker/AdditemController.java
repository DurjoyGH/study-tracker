package com.example.studytracker;

import java.io.IOException;
import java.sql.SQLException;

import com.example.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;

public class AdditemController {

    @FXML
    private JFXButton backButton;

    @FXML
    private ImageView additemButton;

    @FXML
    private JFXButton myTasksButton;

    DatabaseHandler databaseHandler;

    private static Long userID;

    private boolean ok = false;

    @FXML
    void initialize() {
            additemButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
                additemButton.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("addItemForm.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Add Task!");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
        });

            myTasksButton.setOnAction(event -> {
                Long count;
                databaseHandler = new DatabaseHandler();
                try {
                    count = databaseHandler.getAllTask(getUserID());
                    if(count > 0){
                        ok = true;
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

                if(!ok){
                    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                    informationAlert.setTitle("Opps!");
                    informationAlert.setHeaderText(null);
                    informationAlert.setContentText("No task available!");
                    informationAlert.showAndWait();
                }

                else {
                    myTasksButton.getScene().getWindow().hide();
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
                }
            });

            backButton.setOnAction(event -> {
                backButton.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("login.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Study Tracker - Home");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
            });
    }
    public Long getUserID(){
        return userID;
    }

    public void setUserID(Long userID){
        AdditemController.userID = userID;
    }

}
