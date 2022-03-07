package com.example.pathfinder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class main extends Application {

    //Start method
    @Override
    public void start(Stage stage) throws IOException {
        //Initialize stage elements and setup UI
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Path Finder");
        stage.setScene(scene);
        stage.show();

    }

    //Launch the main method
    public static void main(String[] args) {
        launch();
    }
}