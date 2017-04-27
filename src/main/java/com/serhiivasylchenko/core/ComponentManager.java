package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Component;
import com.serhiivasylchenko.persistence.ComponentGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */


public class ComponentManager {


    public void addComponent(Component component, ComponentGroup group) {

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

}
