module com.example.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pathfinder to javafx.fxml;
    exports com.example.pathfinder;
}