package com.serhiivasylchenko.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class ParameterList extends CachedPersistable {

    private static final long serialVersionUID = -1398177335594023415L;

    @Column
    @OneToMany(mappedBy = "parameterList", orphanRemoval = true)
    private List<Field> fields;

    public ParameterList() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
