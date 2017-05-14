package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.persistence.TechnicalEntity;
import com.serhiivasylchenko.utils.Parameters;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public class WorkflowManager {

    private static WorkflowManager instance;

    private static final Logger LOGGER = Logger.getLogger(WorkflowManager.class);

    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    private WorkflowManager() {
    }

    public static WorkflowManager getInstance() {
        if (instance == null) {
            instance = new WorkflowManager();
        }
        return instance;
    }

    public void addSystem(String name, String description) {
        System system = new System(name, description);
        persistenceBean.persist(system);
    }

    public void addComponentGroup(System system, ComponentGroup parentGroup, String groupName, String description) {
        ComponentGroup componentGroup = new ComponentGroup(system, groupName, description);
        if (parentGroup != null) {
            componentGroup.setParentGroup(parentGroup);
        } else {
            LOGGER.warn("ComponentGroup with the name '" + groupName + "' is not found!");
        }
        persistenceBean.persist(componentGroup);
    }

    public void addComponent(System system, ComponentGroup componentGroup, String compName, String description) {
        Component component;
        if (componentGroup != null) {
            component = new Component(system, componentGroup, compName, description);
        } else {
            component = new Component(system, null, compName, description);
        }
        persistenceBean.persist(component);
    }

    public void modifySystem(String systemName, System system) {

    }

    public void modifyGroup(String systemName, String groupName, ComponentGroup componentGroup) {

    }

    public void modifyComponent(String systemName, String compName, Component component) {

    }

    public void deleteEntity(TechnicalEntity entity) {
        try {
            persistenceBean.delete(entity);
        } catch (Exception e) {
            LOGGER.error("Something wrong on deleting the entity '" + entity.toString() + "'", e);
        }
    }

    public List<System> getSystemList() {
        return persistenceBean.find(System.class, System.NQ_ALL, new Parameters());
    }

    public List<Component> getComponentList(String systemName) {
        return persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM_NAME, new Parameters().add("systemName", systemName));
    }

    public List<ComponentGroup> getGroupList(String systemName) {
        List<ComponentGroup> componentGroups = persistenceBean.find(ComponentGroup.class, ComponentGroup.NQ_BY_SYSTEM_NAME, new Parameters().add("systemName", systemName));
        componentGroups = checkForChildren(componentGroups);

        return componentGroups;
    }

    private List<ComponentGroup> checkForChildren(List<ComponentGroup> componentGroups) {
        List<ComponentGroup> withChildren = new ArrayList<>();
        withChildren.addAll(componentGroups);
        componentGroups.forEach(componentGroup -> withChildren.addAll(checkForChildren(componentGroup.getComponentGroupChildren())));

        return withChildren;
    }
}
