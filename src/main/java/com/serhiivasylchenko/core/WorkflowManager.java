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

    private static final Logger LOGGER = Logger.getLogger(WorkflowManager.class);

    private PersistenceBean persistenceBean = PersistenceBean.getInstance();

    public WorkflowManager() {
//        EntityManager em = Persistence.createEntityManagerFactory("em").createEntityManager();
//        persistenceBean.setEntityManager(em);
        persistenceBean.init();
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

    public void modifyComponent(Long id, Component component) {

    }

    public void removeComponent(Long id) {

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
