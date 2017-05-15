package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.gui.dialogs.AddComponentDialog;
import com.serhiivasylchenko.gui.dialogs.AddComponentGroupDialog;
import com.serhiivasylchenko.gui.dialogs.AddFieldDialog;
import com.serhiivasylchenko.gui.dialogs.AddSystemDialog;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
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
    private AddFieldDialog addFieldDialog = new AddFieldDialog();

    private static DialogController instance;

    private DialogController() {
    }

    public static DialogController getInstance() {
        if (instance == null) {
            instance = new DialogController();
        }
        return instance;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    void addComponent() {
        addComponent(null, null);
    }

    void addSystem() {
        addSystemDialog.showDialog();
    }

    void addComponentGroup() {
        addComponentGroup(null, null);
    }

    void addComponent(System system, ComponentGroup componentGroup) {
        addComponentDialog.showDialog(system, componentGroup);
    }

    void addComponentGroup(System system, ComponentGroup parentGroup) {
        addComponentGroupDialog.showDialog(system, parentGroup);
    }

    void addFieldToEntity(TechnicalEntity entity) {
        addFieldDialog.showDialog(entity);
    }
}
