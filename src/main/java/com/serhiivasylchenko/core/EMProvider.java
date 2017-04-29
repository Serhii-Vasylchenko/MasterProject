package com.serhiivasylchenko.core;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Serhii Vasylchenko
 */
public class EMProvider {
    private static final EMProvider singleton = new EMProvider();

    private EntityManagerFactory emf;

    private EMProvider() {
    }

    public static EMProvider getInstance() {
        return singleton;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("em");
        }
        return emf;
    }

    public void closeEmf() {
        if (emf.isOpen() || emf != null) {
            emf.close();
        }
        emf = null;
    }
}
