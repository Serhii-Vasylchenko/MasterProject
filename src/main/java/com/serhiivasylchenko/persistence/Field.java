package com.serhiivasylchenko.persistence;

import com.serhiivasylchenko.utils.FieldType;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries(
        @NamedQuery(name = Field.FIELD_GET_BY_PARAMETER_LIST_ORDERED, query = "SELECT x FROM Field x WHERE x.parameterList = :parameterList ORDER BY x.id")
)
@SuppressWarnings("unused")
public class Field extends Named {

    private static final long serialVersionUID = -5386828594342673263L;

    public static final String FIELD_GET_BY_PARAMETER_LIST_ORDERED = "field.get.by.parameter.list";

    @Column
    private FieldType fieldType;

    @ManyToOne
    private ParameterList parameterList;

    @Column
    private int intValue;

    @Column
    private float floatValue;

    @Column
    @ColumnDefault(value = "-1") // -1 means no option selected
    private int selectedStringIndex;

    @ElementCollection
    @Column
    private List<String> choiceStrings;

    public Field(ParameterList parameterList, String name, FieldType fieldType) {
        super(name);
        this.parameterList = parameterList;
        this.fieldType = fieldType;
    }

    public Field() {
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

    public int getSelectedStringIndex() {
        return selectedStringIndex;
    }

    public void setSelectedStringIndex(int selectedStringIndex) {
        this.selectedStringIndex = selectedStringIndex;
    }

    public List<String> getChoiceStrings() {
        return choiceStrings;
    }

    public void setChoiceStrings(List<String> choiceStrings) {
        this.choiceStrings = choiceStrings;
    }
}
