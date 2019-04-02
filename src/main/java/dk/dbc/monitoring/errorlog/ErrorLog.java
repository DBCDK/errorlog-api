/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog;

import dk.dbc.monitoring.errorlog.model.ErrorLogAppView;
import dk.dbc.monitoring.errorlog.model.ErrorLogEntity;
import dk.dbc.monitoring.errorlog.model.ErrorLogSummary;
import dk.dbc.monitoring.errorlog.model.QueryParam;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.util.List;
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
     * @return Optional with entity
     */
    public Optional<ErrorLogEntity> get(int id) {
        return Optional.ofNullable(entityManager.find(ErrorLogEntity.class, id));
    }

    /**
     * Produces a hierarchical rollup starting with the
     * grand total of errorlog entries (ErrorLogSummary.Kind.TOTAL),
     * after that comes namespace totals as primary group
     * (ErrorLogSummary.Kind.NAMESPACE) followed by app totals
     * (ErrorLogSummary.Kind.APP) and cause totals
     * (ErrorLogSummary.Kind.CAUSE) in hierarchical order.
     * @param queryParam summary query parameters, required are
     *                   'team' and 'from' and 'until' timestamps.
     * @return summary list
     * @throws IllegalArgumentException on missing query parameter
     */
    public List<ErrorLogSummary> getSummary(QueryParam queryParam) throws IllegalArgumentException {
        // In the future these might be made optional
        if (queryParam.getTeam() == null) {
            throw new IllegalArgumentException("queryParam.team can not be null");
        }
        if (queryParam.getFrom() == null) {
            throw new IllegalArgumentException("queryParam.from can not be null");
        }
        if (queryParam.getUntil() == null) {
            throw new IllegalArgumentException("queryParam.until can not be null");
        }
        final TypedQuery<ErrorLogSummary> summaryQuery =
                entityManager.createNamedQuery(ErrorLogEntity.QUERY_GET_SUMMARY, ErrorLogSummary.class)
                .setParameter(1, queryParam.getTeam())
                .setParameter(2, Date.from(queryParam.getFrom()))
                .setParameter(3, Date.from(queryParam.getUntil()));
        return summaryQuery.getResultList();
    }

    /**
     * Produces a view of all errorlog entries for a specific app
     * in a specific namespace.
     * Supports paging through the {@link QueryParam} 'limit' and
     * zero based 'offset' fields.
     * @param queryParam app view query parameters, required are
     *                   'namespace', 'app', 'team' and
     *                   'from' and 'until' timestamps.
     * @return {@link ErrorLogAppView}
     * @throws IllegalArgumentException on missing query parameter
     */
    public ErrorLogAppView getAppView(QueryParam queryParam) throws IllegalArgumentException {
        if (queryParam.getNamespace() == null) {
            throw new IllegalArgumentException("queryParam.namespace can not be null");
        }
        if (queryParam.getApp() == null) {
            throw new IllegalArgumentException("queryParam.app can not be null");
        }
        // In the future these might be made optional
        if (queryParam.getTeam() == null) {
            throw new IllegalArgumentException("queryParam.team can not be null");
        }
        if (queryParam.getFrom() == null) {
            throw new IllegalArgumentException("queryParam.from can not be null");
        }
        if (queryParam.getUntil() == null) {
            throw new IllegalArgumentException("queryParam.until can not be null");
        }
        final TypedQuery<Long> appViewSizeQuery =
                entityManager.createNamedQuery(ErrorLogEntity.QUERY_GET_APP_VIEW_SIZE, Long.class)
                        .setParameter("namespace", queryParam.getNamespace())
                        .setParameter("app", queryParam.getApp())
                        .setParameter("team", queryParam.getTeam())
                        .setParameter("from", Date.from(queryParam.getFrom()))
                        .setParameter("until", Date.from(queryParam.getUntil()));

        final TypedQuery<ErrorLogEntity> appViewQuery =
                entityManager.createNamedQuery(ErrorLogEntity.QUERY_GET_APP_VIEW, ErrorLogEntity.class)
                        .setParameter("namespace", queryParam.getNamespace())
                        .setParameter("app", queryParam.getApp())
                        .setParameter("team", queryParam.getTeam())
                        .setParameter("from", Date.from(queryParam.getFrom()))
                        .setParameter("until", Date.from(queryParam.getUntil()))
                        .setFirstResult(queryParam.getOffset())
                        .setMaxResults(queryParam.getLimit());

        return new ErrorLogAppView(appViewQuery.getResultList(),
                appViewSizeQuery.getSingleResult(), queryParam.getOffset());
    }
}
