package com.serhiivasylchenko.persistence;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Serhii Vasylchenko
 */
@MappedSuperclass
public class Named extends CachedPersistable {

    private static final long serialVersionUID = 8406296394361403000L;

    public Named(Named p, boolean deep) {
        super(p, deep);
        this.name = "undefined";
    }

    public Named(String name) {
        this.name = name;
    }

    public Named() {

    }

    @Column(nullable = false)
    private String name;

    @Override
    public String toString() {
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
