package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.*;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;

import java.util.List;

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
     * @param system    name of the system of the component
     * @param componentName name for validation
     * @return true on success, false otherwise
     */
    public boolean validateComponentName(System system, String componentName) {
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
     * @param system name of the system of the group
     * @param groupName  name for validation
     * @return true on success, false otherwise
     */
    public boolean validateGroupName(System system, String groupName) {
        ComponentGroup componentGroup = persistenceBean.findSingle(ComponentGroup.class, ComponentGroup.NQ_BY_NAME_AND_SYSTEM,
                new Parameters().add("name", groupName).add("system", system));
        return componentGroup == null;
    }

    public boolean validateFieldName(TechnicalEntity entity, String fieldName) {
        List<Field> fields = persistenceBean.find(Field.class, Field.NQ_BY_PARAMETER_LIST_ORDERED,
                new Parameters().add("parameterList", entity.getParameterList()));
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return false;
            }
        }
        return true;
    }
}
