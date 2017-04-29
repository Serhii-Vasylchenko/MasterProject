package com.serhiivasylchenko.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class System extends TechnicalEntity {
    @Column
    @OneToMany
    private Set<Component> components;

    public System(String name, String description) {
        super(name, description);
    }

    public System() {
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }
}
