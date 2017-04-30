package com.serhiivasylchenko.persistence;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries({
        @NamedQuery(name = System.NQ_ALL, query = "SELECT x FROM System x"),
        @NamedQuery(name = System.NQ_BY_NAME, query = "SELECT x FROM System x WHERE x.name = :name")
})
public class System extends TechnicalEntity {

    public static final String NQ_ALL = "nq.get.all";
    public static final String NQ_BY_NAME = "nq.get.by.name";

    @OneToMany(mappedBy = "system", cascade = CascadeType.PERSIST)
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
