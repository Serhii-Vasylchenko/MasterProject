package com.serhiivasylchenko.core;

import com.serhiivasylchenko.persistence.Persistable;
import com.serhiivasylchenko.utils.Parameters;
import org.hibernate.annotations.QueryHints;
import org.hibernate.exception.JDBCConnectionException;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.net.ConnectException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Serhii Vasylchenko
 */

public class PersistenceBean {

    private EntityManager em;

//    @Inject
//    Event<AbstractEvent> eventBus;

    private static final AtomicBoolean connected = new AtomicBoolean(true);

    @PersistenceContext(unitName = "em")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return this.em;
    }

    public void persist(Persistable... entities) {
        try {
            em.getTransaction().begin();

            for (Persistable entity : entities) {
                if (entity.getId() == 0) {
                    this.em.persist(entity);
                } else {
                    this.em.merge(entity);
                }
            }

            this.em.flush();

            em.getTransaction().commit();

            this.setConnected();
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    public <T extends Persistable> T merge(T entity) {
        try {
            T result = this.em.merge(entity);

            this.setConnected();
            return result;
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    public void delete(Persistable entity) throws Exception {
        try {
            this.em.remove(entity);

            this.setConnected();
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    public <T> T find(Class<T> clazz, long id) {
        try {
            T result = this.em.find(clazz, id);

            this.setConnected();
            return result;
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    public <T> T find(Class<T> clazz, long id, Map<String, Object> hints) {
        try {
            T result = this.em.find(clazz, id, hints);

            this.setConnected();
            return result;
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    public <T extends Persistable> EntityGraph<T> createEntityGraph(Class<T> clazz) {
        return this.em.createEntityGraph(clazz);
    }

    public <T extends Persistable> T refresh(T persistable) {
        return this.find((Class<T>) persistable.getClass(), persistable.getId());
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return this.em.getCriteriaBuilder();
    }

    public <T> List<T> find(CriteriaQuery<T> criteriaQuery) {
        try {
            List<T> result = this.em.createQuery(criteriaQuery).getResultList();

            this.setConnected();
            return result;
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    public <T> List<T> findByIds(Class<T> theClass, Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final CriteriaBuilder cb = this.em.getCriteriaBuilder();
        final CriteriaQuery<T> cq = cb.createQuery(theClass);
        final Root<T> rootEntry = cq.from(theClass);
        final CriteriaQuery<T> all = cq.select(rootEntry);

        all.where(rootEntry.get("id").in(ids));

        return this.find(all);
    }

    public <T> T findSingle(Class<T> theClass, String queryName, Parameters parameters) {
        List<T> result = this.find(theClass, queryName, parameters);

        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * Execute a count query for a certain table
     *
     * @param queryName  - the query describing the count
     * @param parameters - parameters needed for the count
     * @return - a long number for the counted objects
     */
    public long count(String queryName, Parameters parameters) {
        return this.findSingle(Long.class, queryName, parameters);
    }

    public <T> List<T> find(Class<T> theClass, String queryName, Parameters parameters) {
        return this.find(theClass, queryName, parameters, null);
    }

    public <T> List<T> find(Class<T> theClass, String queryName, Parameters parameters, EntityGraph entityGraph) {
        try {
            em.getTransaction().begin();

            TypedQuery<T> query = this.em.createNamedQuery(queryName, theClass);

            if (parameters != null) {
                parameters.get().entrySet().forEach(x -> query.setParameter(x.getKey(), x.getValue()));
            }

            if (entityGraph != null) {
                query.setHint(QueryHints.LOADGRAPH, entityGraph);
            }

            List<T> result = query.getResultList();

            em.getTransaction().commit();

            this.setConnected();
            return result;
        } catch (Exception e) {
            this.checkForConnectionProblem(e);
            throw e;
        }
    }

    private void setConnected() {
        if (connected.compareAndSet(false, true)) {

            // notify system that database is currently available
//            DatabaseFailureEvent event = new DatabaseFailureEvent();
//            event.setAvailable(true);
//
//            this.eventBus.fire(event);
        }
    }

    private void setDisconnected() {
        if (connected.compareAndSet(true, false)) {

            // notify system that database is currently unavailable
//            DatabaseFailureEvent event = new DatabaseFailureEvent();
//            event.setAvailable(false);
//
//            this.eventBus.fire(event);
        }
    }

    private void checkForConnectionProblem(Throwable sourceException) {
        boolean connectExceptionFound = false;

        Throwable exception = sourceException;
        while ((exception = exception.getCause()) != null) {
            if (exception instanceof ConnectException || exception instanceof JDBCConnectionException) {
                connectExceptionFound = true;
                break;
            }
        }

        if (connectExceptionFound) {
            this.setDisconnected();
        }
    }
}
