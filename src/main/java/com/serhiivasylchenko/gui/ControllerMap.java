package com.serhiivasylchenko.gui;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serhii Vasylchenko
 */
public class ControllerMap {

    private static ControllerMap instance;

    private Map<Class, Object> controllers;

    private ControllerMap() {
        controllers = new HashMap<>();
    }

    public static ControllerMap getInstance() {
        if (instance == null) {
            instance = new ControllerMap();
        }
        return instance;
    }

    public void addController(Object controller) {
        this.controllers.put(controller.getClass(), controller);
    }

    public Object getController(Class clazz) {
        return this.controllers.get(clazz);
    }
}
