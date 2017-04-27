package com.serhiivasylchenko.persistence;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Serhii Vasylchenko
 */

@MappedSuperclass
public abstract class Persistable implements Comparable<Persistable>, Serializable {

    @Id
    @GeneratedValue
    private long id;

    public Persistable() {
    }

    public Persistable(Persistable p, boolean deep) {
        this.id = p.id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns a descriptive name of the entity. Default is returning the id.
     *
     * @return descriptive name for display
     */
    public String getDisplayName() {
        return String.valueOf(this.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        // compare the type and id of the object
        if (o instanceof HibernateProxy) {
            // In case it is a Hibernate proxy object, which might not have been yet initialized, we have to compare the meta data.

            HibernateProxy proxy = (HibernateProxy) o;
            LazyInitializer li = proxy.getHibernateLazyInitializer();

            if (this.getClass() != li.getPersistentClass()) {
                return false;
            }

            // just compare the ids if the entity was already stored in the database
            if (this.id > 0) {
                return Objects.equals(this.id, li.getIdentifier());
            }
        } else {
            if (this.getClass() != o.getClass()) {
                return false;
            }

            // just compare the ids if the entity was already stored in the database
            if (this.id > 0) {
                Persistable that = (Persistable) o;
                return this.id == that.getId();
            }
        }

        return super.equals(o);
    }

    @Override
    public int compareTo(Persistable o) {
        return Long.compare(this.getId(), o.getId());
    }

    public String generateString() {
        String displayName = this.getDisplayName();
        String idValue = String.valueOf(this.getId());

        StringBuilder buffer = new StringBuilder();
        buffer.append(this.getClass().getSimpleName());
        if (displayName != null && !Objects.equals(displayName, idValue)) {
            buffer.append(" '").append(displayName).append("'");
        }
        buffer.append(" [").append(idValue).append("]");

        return buffer.toString();
    }
}
