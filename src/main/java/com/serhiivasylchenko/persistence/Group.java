package com.serhiivasylchenko.persistence;

import java.util.List;

/**
 * @author Serhii Vasylchenko
 */
public interface Group {
    Persistable getParent();

    List<Persistable> getChildren();

    List<Persistable> getChildrenSorted();
}
