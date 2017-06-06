package com.serhiivasylchenko.persistence;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
@Entity
public class ParameterList extends CachedPersistable {

    private static final long serialVersionUID = -1398177335594023415L;

    @OneToOne
    private TechnicalEntity parentEntity;

    @OneToMany(mappedBy = "parameterList", orphanRemoval = true)
    private List<Field> fields;

    public ParameterList(TechnicalEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    public ParameterList() {
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public TechnicalEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(TechnicalEntity parentEntity) {
        this.parentEntity = parentEntity;
    }
}
