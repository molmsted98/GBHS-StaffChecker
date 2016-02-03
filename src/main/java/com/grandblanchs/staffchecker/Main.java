package com.grandblanchs.staffchecker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        //Temporary workaround to fix a hang with Windows 10 / Intel processors.
        System.setProperty("glass.accessible.force", "false");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));

        primaryStage.setTitle("Staff Checker");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
