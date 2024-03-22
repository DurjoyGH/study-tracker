package com.example.studytracker;

import com.example.animations.Shaker;
import com.example.database.DatabaseHandler;
import com.example.model.User;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Modality;

public class LoginController {

    @FXML
    private JFXButton aboutButton;

    @FXML
    private JFXButton loginLoginButton;

    @FXML
    private PasswordField loginPassword;

    @FXML
    private JFXButton loginSignUpButton;

    @FXML
    private TextField loginUsername;

    private DatabaseHandler databaseHandler;

    private Long userID;

    private boolean ok = false;

    private boolean isAboutWindowOpen = false;

    private Stage aboutStage;

    @FXML
    void initialize() {

        loginUsername.setOnKeyPressed(event -> handleEnterKey(event, loginPassword));


        databaseHandler = new DatabaseHandler();

        loginLoginButton.setOnAction(event -> {
            String loginName = loginUsername.getText().trim();
            String loginPass = loginPassword.getText().trim();

            if(loginName.isEmpty() || loginPass.isEmpty()){
                Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                warningAlert.setTitle("Warning!");
                warningAlert.setHeaderText(null);
                warningAlert.setContentText("Please fill up every required information!");
                warningAlert.showAndWait();
                return;
            }

            User user = new User();
            user.setUserName(loginName);
            user.setPassword(loginPass);

            try {
                databaseHandler.getUser(user);
                ResultSet userRow = databaseHandler.getUser(user);

                while (userRow.next()){
                    ok = true;
                    userID = userRow.getLong("UserID");
                }

                if(ok){
                    showAddItemScreen();
                } else{
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error!");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Wrong username or password!");
                    errorAlert.showAndWait();

                    Shaker userNameShaker = new Shaker(loginUsername);
                    Shaker passwordShaker = new Shaker(loginPassword);
                    userNameShaker.shake();
                    passwordShaker.shake();
                }
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        loginSignUpButton.setOnAction(event -> {
            loginSignUpButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("signup.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        });

        aboutButton.setOnAction(event -> {
            if (!isAboutWindowOpen) {
                isAboutWindowOpen = true;

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("about.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Parent root = loader.getRoot();
                aboutStage = new Stage();
                aboutStage.initModality(Modality.APPLICATION_MODAL);
                aboutStage.setTitle("About Us");
                aboutStage.setScene(new Scene(root));
                aboutStage.setResizable(false);
                aboutStage.setOnCloseRequest(closeEvent -> isAboutWindowOpen = false);
                aboutStage.showAndWait();
            } else {
                aboutStage.toFront();
            }
        });
    }

    private void showAddItemScreen(){
        loginSignUpButton.getScene().getWindow().hide();
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

        AdditemController additemController = loader.getController();
        additemController.setUserID(userID);

        stage.showAndWait();

    }

    private void handleEnterKey(KeyEvent event, Node nextField) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            nextField.requestFocus();
        }
    }

}
