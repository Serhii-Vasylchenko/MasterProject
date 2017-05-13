package com.serhiivasylchenko.gui;

/**
 * @author Serhii Vasylchenko
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
        MainController mainController = new MainController();
        mainLoader.setController(mainController);

        BorderPane root = mainLoader.load();

        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(900);
        primaryStage.setWidth(1400);
        primaryStage.setTitle("Master Project");
        primaryStage.show();
    }
}
