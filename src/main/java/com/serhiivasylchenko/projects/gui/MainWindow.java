package com.serhiivasylchenko.projects.gui;

/**
 * @author Serhii Vasylchenko
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/mainWindow.fxml"));
            primaryStage.setTitle("Master Project");
            primaryStage.setScene(new Scene(root, 1200, 800));
            primaryStage.show();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }

    }

    public void runAnalysis(ActionEvent actionEvent) {

    }
}
