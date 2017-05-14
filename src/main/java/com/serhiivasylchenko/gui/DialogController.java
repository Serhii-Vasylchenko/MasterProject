package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.gui.dialogs.AddComponentDialog;
import com.serhiivasylchenko.gui.dialogs.AddComponentGroupDialog;
import com.serhiivasylchenko.gui.dialogs.AddSystemDialog;
import javafx.fxml.Initializable;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Serhii Vasylchenko
 */

public class DialogController implements Initializable {

    private final Logger LOGGER = Logger.getLogger(DialogController.class);

    private AddComponentDialog addComponentDialog = new AddComponentDialog();
    private AddSystemDialog addSystemDialog = new AddSystemDialog();
    private AddComponentGroupDialog addComponentGroupDialog = new AddComponentGroupDialog();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void addComponent() {
        addComponentDialog.showDialog();
    }

    void addSystem() {
        addSystemDialog.showDialog();
    }

    void addComponentGroup() {
        addComponentGroupDialog.showDialog();
    }
}
