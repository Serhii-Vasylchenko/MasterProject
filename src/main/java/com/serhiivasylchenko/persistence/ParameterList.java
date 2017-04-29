package com.serhiivasylchenko.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class ParameterList extends CachedPersistable {
    @Column
    @OneToMany
    private Set<Field> fields;

    public ParameterList() {
        fields = new HashSet<>();
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public void modifyField(Long id, Field modifiedField) {

    }

    public void removeField(Long id) {

    }

    public Set<Field> getFields() {
        return fields;
    }

    public void setFields(Set<Field> fields) {
        this.fields = fields;
    }
}
