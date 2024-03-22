module com.example.studytracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires java.sql;


    opens com.example.studytracker to javafx.fxml;
    exports com.example.studytracker;
}