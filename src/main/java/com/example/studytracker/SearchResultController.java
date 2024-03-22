package com.example.studytracker;
import com.example.database.DatabaseHandler;
import com.example.model.Task;
import com.jfoenix.controls.JFXListView;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class SearchResultController {

    @FXML
    private Label dateShowLabel;

    @FXML
    private JFXListView<Task> listView;

    @FXML
    void initialize() throws SQLException {
        ObservableList<Task> list = FXCollections.observableArrayList();
        SearchTaskController searchTaskController = new SearchTaskController();
        AdditemController additemController = new AdditemController();
        DatabaseHandler databaseHandler = new DatabaseHandler();
        ResultSet resultSet = databaseHandler.getTasksByDate(searchTaskController.getSelectedDate(), additemController.getUserID());

        dateShowLabel.setText(searchTaskController.getSelectedDate().toString());

        while (resultSet.next()){
            Task task = new Task();
            task.setTaskID(resultSet.getLong("TaskID"));
            task.setTask(resultSet.getString("Task"));
            task.setCreatedDate(resultSet.getDate("CreatedDate").toLocalDate());
            task.setDescription(resultSet.getString("Description"));
            list.addAll(task);
        }

        listView.setItems(list);
        listView.setCellFactory(CellController -> new CellController());
    }

}
