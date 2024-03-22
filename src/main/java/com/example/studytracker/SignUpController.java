package com.example.studytracker;

import com.example.database.DatabaseHandler;
import com.example.model.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private JFXCheckBox signUpCheckBoxMale;

    @FXML
    private JFXCheckBox signUpCheckBoxFemale;

    @FXML
    private TextField signUpFirstName;

    @FXML
    private TextField signUpInstitution;

    @FXML
    private TextField signUpLastName;

    @FXML
    private PasswordField signUpPassword;

    @FXML
    private TextField signUpUsername;

    private boolean isError = false;

    @FXML
    void initialize() {

        signUpFirstName.setOnKeyPressed(event -> handleEnterKey(event, signUpLastName));
        signUpLastName.setOnKeyPressed(event -> handleEnterKey(event, signUpInstitution));
        signUpInstitution.setOnKeyPressed(event -> handleEnterKey(event, signUpUsername));
        signUpUsername.setOnKeyPressed(event -> handleEnterKey(event, signUpPassword));
        signUpPassword.setOnKeyPressed(event -> handleEnterKey(event, signUpButton));

        signUpButton.setOnAction(event -> {
            try {
                if (areFieldsEmpty()) {
                    Alert warningAlert = new Alert(Alert.AlertType.WARNING);
                    warningAlert.setTitle("Warning!");
                    warningAlert.setHeaderText(null);
                    warningAlert.setContentText("Please fill up every required information!");
                    warningAlert.showAndWait();
                    return;
                }

                createUser();

                if(!isError) {
                    signUpButton.getScene().getWindow().hide();
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
                }

            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
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

    private void createUser() throws SQLException, ClassNotFoundException {
        DatabaseHandler databaseHandler = new DatabaseHandler();

        String firstName = signUpFirstName.getText().trim();
        String lastName = signUpLastName.getText().trim();
        String institution = signUpInstitution.getText().trim();
        String gender;

        if(signUpCheckBoxMale.isSelected()){
            gender = "Male";
        } else{
            gender = "Female";
        }

        String userName = signUpUsername.getText().trim();
        String password = signUpPassword.getText().trim();

        if (databaseHandler.isUsernameExists(userName)) {
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("Warning!");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Username already exists. Please choose a different username.");
            warningAlert.showAndWait();
            isError = true;
            return;
        }
        else{
            isError = false;
        }

        User user = new User(firstName, lastName, institution, gender, userName, password);

        databaseHandler.signUp(user);
    }

    private boolean areFieldsEmpty() {
        return signUpFirstName.getText().trim().isEmpty() ||
                signUpLastName.getText().trim().isEmpty() ||
                signUpInstitution.getText().trim().isEmpty() ||
                signUpUsername.getText().trim().isEmpty() ||
                signUpPassword.getText().trim().isEmpty() ||
                (!signUpCheckBoxMale.isSelected() && !signUpCheckBoxFemale.isSelected());
    }

    private void handleEnterKey(KeyEvent event, Node nextField) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            nextField.requestFocus();
        }
    }

}
