package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
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

    public void addComponentGroup(String systemName, String parentGroupName, String groupName, String description) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        ComponentGroup componentGroup = new ComponentGroup(system, groupName, description);
        if (parentGroupName != null && !parentGroupName.isEmpty()) {
            ComponentGroup parentGroup = persistenceBean.findSingle(ComponentGroup.class,
                    ComponentGroup.NQ_BY_NAME_AND_SYSTEM,
                    new Parameters()
                            .add("name", parentGroupName)
                            .add("system", system));
            if (parentGroup != null) {
                componentGroup.setParentGroup(parentGroup);
            } else {
                LOGGER.warn("ComponentGroup with the name '" + groupName + "' is not found!");
            }
        }
        persistenceBean.persist(componentGroup);
    }

    public void addComponent(String systemName, String groupName, String compName, String description) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        ComponentGroup componentGroup = persistenceBean.findSingle(ComponentGroup.class,
                ComponentGroup.NQ_BY_NAME_AND_SYSTEM,
                new Parameters()
                        .add("name", groupName)
                        .add("system", system));
        Component component;
        if (componentGroup != null) {
            component = new Component(system, componentGroup, compName, description);
        } else {
            component = new Component(system, null, compName, description);
            LOGGER.warn("ComponentGroup with the name '" + groupName + "' is not found!");
        }

        persistenceBean.persist(component);
    }

    public void modifySystem(String systemName, System system) {

    }

    public void modifyGroup(String systemName, String groupName, ComponentGroup componentGroup) {

    }

    public void modifyComponent(String systemName, String compName, Component component) {

    }

    public void deleteSystem(String systemName) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        if (system != null) {
            try {
                persistenceBean.delete(system);
            } catch (Exception e) {
                LOGGER.error("Something wrong on deleting the system '" + systemName + "'", e);
            }
            return;
        }
        LOGGER.error("System with name '" + systemName + "' is not found");
    }

    public void deleteEntityFromSystem(String systemName, String entityName) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        if (system == null) {
            LOGGER.error("System with name '" + systemName + "' is not found");
            return;
        }
        Component component = persistenceBean.findSingle(Component.class, Component.NQ_BY_NAME_AND_SYSTEM,
                new Parameters()
                        .add("name", entityName)
                        .add("system", system));
        if (component != null) {
            try {
                persistenceBean.delete(component);
            } catch (Exception e) {
                LOGGER.error("Something wrong on deleting the component '" + entityName + "' in system '" + systemName + "'", e);
            }
            return;
        }
        ComponentGroup componentGroup = persistenceBean.findSingle(ComponentGroup.class, ComponentGroup.NQ_BY_NAME_AND_SYSTEM,
                new Parameters()
                        .add("name", entityName)
                        .add("system", system));
        if (componentGroup != null) {
            try {
                persistenceBean.delete(componentGroup);
            } catch (Exception e) {
                LOGGER.error("Something wrong on deleting the component group '" + entityName + "' in system '" + systemName + "'", e);
            }
            return;
        }
        LOGGER.warn("Entity with name '" + entityName + "' is not found for the system '" + systemName + "'!");
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
