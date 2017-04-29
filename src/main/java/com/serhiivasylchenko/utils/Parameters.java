package com.serhiivasylchenko.utils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serhii Vasylchenko
 */
public class Parameters {

    private final Map<String, Object> map = new HashMap<>();

    public Parameters add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public Map<String, Object> get() {
        return this.map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Parameters that = (Parameters) o;

        return new EqualsBuilder() //
                .append(this.map, that.map) //
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37) //
                .append(this.map) //
                .toHashCode();
    }
}
