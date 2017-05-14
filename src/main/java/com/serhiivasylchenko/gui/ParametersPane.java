package com.serhiivasylchenko.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @author Serhii Vasylchenko
 */
public class ParametersPane extends VBox {

    public ParametersPane() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/parametersPane.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error("FXML loading error in class '" + this.getClass().getSimpleName() + "'", e);
            throw new RuntimeException(e);
        }
    }
}
