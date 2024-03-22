package com.example.studytracker;

import java.io.IOException;
import java.sql.SQLException;
import com.example.database.DatabaseHandler;
import com.example.model.Task;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CellController extends JFXListCell<Task> {

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView deleteButton;

    @FXML
    private Label descriptionLabel;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ImageView updateButton;

    @FXML
    private Label taskLabel;

    private FXMLLoader fxmlLoader;

    private DatabaseHandler databaseHandler;

    @FXML
    void initialize() {

    }

    @Override
    public void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
            setText(null);
            setGraphic(null);
        } else{
            if(fxmlLoader == null){
                fxmlLoader = new FXMLLoader(getClass().getResource("cell.fxml"));
                fxmlLoader.setController(this);
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Long taskID = item.getTaskID();

            updateButton.setOnMouseClicked(event -> {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("update.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setTitle("Update");
                stage.setScene(new Scene(root));
                stage.setResizable(false);

                UpdateController updateController = loader.getController();
                updateController.setTaskField(item.getTask());
                updateController.setUpdateDescriptionField(item.getDescription());
                updateController.setTaskID(taskID);

                stage.show();
            });

            taskLabel.setText(item.getTask());
            dateLabel.setText(item.getCreatedDate().toString());
            descriptionLabel.setText(item.getDescription());

            deleteButton.setOnMouseClicked(event -> {
                databaseHandler = new DatabaseHandler();
                AdditemController additemController = new AdditemController();
                try {
                    databaseHandler.deleteTask(additemController.getUserID(), taskID);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                getListView().getItems().remove(getItem());
            });

            setText(null);
            setGraphic(rootPane);
        }
    }
}
