package com.jepretblur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load Login.fxml dari folder resources
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jepretblur/view/Login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Jepret Blur System - Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}