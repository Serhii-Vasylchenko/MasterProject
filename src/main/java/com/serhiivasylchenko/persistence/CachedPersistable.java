package com.serhiivasylchenko.persistence;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author Serhii Vasylchenko
 */
@Entity
// see "Pitfall 2" http://blog.jhades.org/setup-and-gotchas-of-the-hibernate-second-level-and-query-caches/
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "SecondLevel")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CachedPersistable extends Persistable {

    private static final long serialVersionUID = 2016061901L;

    public CachedPersistable(CachedPersistable p, boolean deep) {
        super(p, deep);
    }

    protected CachedPersistable() {
    }
}

