package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;

/**
 * @author Serhii Vasylchenko
 */
public class Validator {

    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    public Validator() {
    }

    /**
     * Each component should have unique name per system
     *
     * @param systemName    name of the system of the component
     * @param componentName name for validation
     * @return true on success, false otherwise
     */
    public boolean validateComponentName(String systemName, String componentName) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        Component component = persistenceBean.findSingle(Component.class, Component.NQ_BY_NAME_AND_SYSTEM,
                new Parameters().add("name", componentName).add("system", system));
        return component == null;
    }

    /**
     * Each system should have unique name
     *
     * @param systemName name of the system
     * @return true on success, false otherwise
     */
    public boolean validateSystemName(String systemName) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        return system == null;
    }

    /**
     * Each group should have unique name per system
     *
     * @param systemName name of the system of the group
     * @param groupName  name for validation
     * @return true on success, false otherwise
     */
    public boolean validateGroupName(String systemName, String groupName) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        ComponentGroup componentGroup = persistenceBean.findSingle(ComponentGroup.class, ComponentGroup.NQ_BY_NAME_AND_SYSTEM,
                new Parameters().add("name", groupName).add("system", system));
        return componentGroup == null;
    }
}
