package com.serhiivasylchenko.persistence;

import com.serhiivasylchenko.utils.FieldType;
import org.datavec.api.writable.Writable;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
@Entity
@NamedQueries(
        @NamedQuery(name = Field.NQ_BY_PARAMETER_LIST_ORDERED, query = "SELECT x FROM Field x WHERE x.parameterList = :parameterList ORDER BY x.id")
)
@SuppressWarnings("unused")
public class Field extends Named implements Writable {

    private static final long serialVersionUID = -5386828594342673263L;

    public static final String NQ_BY_PARAMETER_LIST_ORDERED = "nq.field.get.by.parameter.list";

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

    @Column
    private String description;

    @Column
//    @ColumnDefault(value = "false")
    private boolean isTarget;

    public Field(ParameterList parameterList, String name, String description, FieldType fieldType) {
        super(name);
        this.parameterList = parameterList;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setTarget(boolean target) {
        isTarget = target;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        if (this.intValue != 0) {
            dataOutput.writeInt(this.intValue);
        } else if (this.floatValue != 0) {
            dataOutput.writeFloat(this.floatValue);
        } else if (this.selectedStringIndex != -1) {
            dataOutput.writeInt(this.selectedStringIndex);
        } else {
            dataOutput.writeInt(0);
        }
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public float toFloat() {
        return 0;
    }

    @Override
    public int toInt() {
        return 0;
    }

    @Override
    public long toLong() {
        return 0;
    }
}
