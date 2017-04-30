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
//        SessionFactory factory;
//        try {
//            factory = new Configuration().configure().buildSessionFactory();
//        } catch (Throwable ex) {
//            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "Failed to create sessionFactory object: ", ex);
//            throw new ExceptionInInitializerError(ex);
//        }

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
        List<System> systemList = persistenceBean.find(System.class, System.NQ_ALL, new Parameters());

        return systemList;
    }


}
