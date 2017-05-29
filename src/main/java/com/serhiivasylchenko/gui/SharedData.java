package com.serhiivasylchenko.gui;

import com.serhiivasylchenko.persistence.TechnicalEntity;

/**
 * @author Serhii Vasylchenko
 */
public class SharedData {

    private TechnicalEntity selectedEntity;

    private static SharedData instance;

    private SharedData() {
    }

    public static SharedData getInstanse() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public TechnicalEntity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(TechnicalEntity selectedEntity) {
        this.selectedEntity = selectedEntity;
    }
}
