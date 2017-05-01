package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public class WorkflowManager {

    private PersistenceBean persistenceBean = new PersistenceBean();

    public WorkflowManager() {
        EntityManager em = Persistence.createEntityManagerFactory("em").createEntityManager();
        persistenceBean.setEntityManager(em);
    }

    public void addComponent(String systemName, String compName, String description) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        Component component = new Component(system, compName, description);
        persistenceBean.persist(component);
    }

    public void addSystem(String name, String description) {
        System system = new System(name, description);
        persistenceBean.persist(system);
    }

    public void modifyComponent(Long id, Component component) {

    }

    public void removeComponent(Long id) {

    }

    public List<Component> getComponentListByGroup(ComponentGroup group) {
        List<Component> components = new ArrayList<>();

        return components;
    }

    public ComponentGroup getComponentGroup(Component component) {
        ComponentGroup componentGroup = null;

        return componentGroup;
    }

    public List<System> getSystemList() {
        return persistenceBean.find(System.class, System.NQ_ALL, new Parameters());
    }

    public List<Component> getComponentList(String systemName) {
        return persistenceBean.find(Component.class, Component.NQ_BY_SYSTEM_NAME, new Parameters().add("systemName", systemName));
    }
}
