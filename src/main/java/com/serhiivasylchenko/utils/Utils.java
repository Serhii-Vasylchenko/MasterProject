package com.serhiivasylchenko.utils;

import com.serhiivasylchenko.core.PersistenceBean;
import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.persistence.learning.Learner;
import javafx.scene.control.Alert;

import java.util.Collections;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public class Utils {

    private static final PersistenceBean persistenceBean = PersistenceBean.getInstance();

    public static void notImplemented() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Soon!");
        alert.setHeaderText(null);
        alert.setContentText("Not implemented yet. Stay tuned!");

        alert.showAndWait();
    }

    public static System getSystem(TechnicalEntity entity) {
        System system = null;

        if (entity instanceof System) {
            system = (System) entity;
        } else if (entity instanceof Component) {
            system = ((Component) entity).getSystem();
        } else if (entity instanceof ComponentGroup) {
            system = ((ComponentGroup) entity).getSystem();
        }

        return system;
    }

    public static List<Learner> getLearners(System system) {
        if (system != null) {
            List<Learner> learners = persistenceBean.find(Learner.class, Learner.NQ_BY_SYSTEM_ORDERED,
                    new Parameters().add("system", system));
            if (learners != null && !learners.isEmpty()) {
                return learners;
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }
}
