package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;
import com.serhiivasylchenko.persistence.System;
import com.serhiivasylchenko.utils.Parameters;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public class WorkflowManager {

    private PersistenceBean persistenceBean;

    public WorkflowManager() {
        persistenceBean = new PersistenceBean();
        EntityManager em = EMProvider.getInstance().getEntityManagerFactory().createEntityManager();
        persistenceBean.setEntityManager(em);
    }

    public void addComponent(String systemName, String compName, String description) {
        System system = persistenceBean.findSingle(System.class, System.NQ_BY_NAME, new Parameters().add("name", systemName));
        Component component = new Component(system, compName, description);
        persistenceBean.persist(component);
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
        List<System> systemList = persistenceBean.find(System.class, System.NQ_ALL, new Parameters());

        return systemList;
    }


}
