package com.serhiivasylchenko.persistence;

/**
 * @author Serhii Vasylchenko
 */

import com.serhiivasylchenko.utils.Status;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * Conditional represents any entity which has different status like active or inactive
 */
@MappedSuperclass
public abstract class Conditional extends CachedPersistable {

    private static final long serialVersionUID = -7464534889459915099L;

    @Column(nullable = false, length = 28)
    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    public Conditional(Conditional p, boolean deep) {
        super(p, deep);
        this.status = p.status;
    }

    public Conditional() {
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        //Status oldValue = this.status;
        this.status = status;
        //this.firePropertyChange("status", oldValue, status);
    }
}

