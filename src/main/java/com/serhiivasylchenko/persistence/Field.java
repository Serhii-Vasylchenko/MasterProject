package com.serhiivasylchenko.persistence;

import com.serhiivasylchenko.utils.FieldType;

import javax.persistence.*;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries(
        @NamedQuery(name = Field.FIELD_GET_BY_PARAMETER_LIST, query = "SELECT x FROM Field x WHERE x.parameterList = :parameterList")
)
@SuppressWarnings("unused")
public class Field extends CachedPersistable {

    private static final long serialVersionUID = -5386828594342673263L;

    public static final String FIELD_GET_BY_PARAMETER_LIST = "field.get.by.parameter.list";

    @Column
    private String name;

    @Column
    private FieldType fieldType;

    @ManyToOne
    private ParameterList parameterList;

    @Column
    private int intValue;

    @Column
    private float floatValue;

    @ElementCollection
    @Column
    private List<String> choiceStrings;

    public Field(ParameterList parameterList, String name, FieldType fieldType) {
        this.parameterList = parameterList;
        this.name = name;
        this.fieldType = fieldType;
    }

    public Field() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public ParameterList getParameterList() {
        return parameterList;
    }

    public void setParameterList(ParameterList parameterList) {
        this.parameterList = parameterList;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public List<String> getChoiceStrings() {
        return choiceStrings;
    }

    public void setChoiceStrings(List<String> choiceStrings) {
        this.choiceStrings = choiceStrings;
    }
}
