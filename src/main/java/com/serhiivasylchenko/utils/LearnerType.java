package com.serhiivasylchenko.utils;

/**
 * @author Serhii Vasylchenko
 */
public enum LearnerType {
    MLP_CLASSIFIER_LINEAR("MLP Classifier Linear");

    private final String name;

    LearnerType(String s) {
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
