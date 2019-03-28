/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog;

import dk.dbc.monitoring.errorlog.model.ErrorLogEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * This class contains the errorlog API
 */
@Stateless
public class ErrorLog {
    @PersistenceContext(unitName = "errorlogPU")
    EntityManager entityManager;

    public ErrorLog() {}

    public ErrorLog(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Persists {@link ErrorLogEntity}
     * @param errorLogEntity errorlog entity to persist
     */
    public void persist(ErrorLogEntity errorLogEntity) {
        entityManager.persist(errorLogEntity);
    }

    /**
     * Retrieves errorlog entity with given ID from store
     * @param id ID of entity to retrieve
     * @return Optional with entity or null
     */
    public Optional<ErrorLogEntity> get(int id) {
        return Optional.ofNullable(entityManager.find(ErrorLogEntity.class, id));
    }
}
