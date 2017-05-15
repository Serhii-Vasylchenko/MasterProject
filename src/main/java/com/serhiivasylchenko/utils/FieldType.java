package com.serhiivasylchenko.utils;

/**
 * @author Serhii Vasylchenko
 */
public enum FieldType {
    INT_NUMBER("Integer number"),
    FLOAT_NUMBER("Float number"),
    TEXT("Text"),
    CHOICE_BOX("Choice box");

    private final String name;

    FieldType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
