/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "errorlog")
@JsonInclude(JsonInclude.Include.NON_NULL)
 @SqlResultSetMappings({
        @SqlResultSetMapping(
                name = ErrorLogEntity.QUERY_GET_SUMMARY,
                classes = {
                        @ConstructorResult(
                                targetClass = dk.dbc.monitoring.errorlog.model.ErrorLogSummary.class,
                                columns = {
                                        @ColumnResult(name = "namespace", type = String.class),
                                        @ColumnResult(name = "app", type = String.class),
                                        @ColumnResult(name = "cause", type = String.class),
                                        @ColumnResult(name = "count"),
                                        @ColumnResult(name = "grouping_namespace"),
                                        @ColumnResult(name = "grouping_app"),
                                        @ColumnResult(name = "grouping_cause")})})
})
@NamedNativeQueries({
    @NamedNativeQuery(name = ErrorLogEntity.QUERY_GET_SUMMARY,
            // Grouping operations are used in conjunction with grouping sets
            // to distinguish result rows. The arguments to the GROUPING operation
            // are not actually evaluated, but they must match exactly expressions
            // given in the GROUP BY clause of the associated query level. Bits
            // are assigned with the rightmost argument being the least-significant
            // bit; each bit is 0 if the corresponding expression is included in
            // the grouping criteria of the grouping set generating the result row,
            // and 1 if it is not.
            query = "SELECT namespace, app, cause, count(*), " +
                    "GROUPING(namespace) grouping_namespace, " +
                    "GROUPING(app) grouping_app, " +
                    "GROUPING(cause) grouping_cause " +
                    "FROM errorlog " +
                    "WHERE team = ? AND tstamp >= ? AND tstamp <= ? " +
                    "GROUP BY ROLLUP(namespace, app, cause) " +
                    "ORDER BY namespace DESC, app DESC, cause DESC;",
            resultSetMapping = ErrorLogEntity.QUERY_GET_SUMMARY),
})
public class ErrorLogEntity {
    public static final String QUERY_GET_SUMMARY = "ErrorLogEntity.getSummary";

    @Id
    @SequenceGenerator(
            name = "errorlog_id_seq",
            sequenceName = "errorlog_id_seq",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "errorlog_id_seq")
    @Column(updatable = false)
    private Integer id;

    private String app;
    private String namespace;
    private String host;
    private String container;
    private String message;
    private String team;
    private String logger;
    private String cause;
    private String stacktrace;

    private Date tstamp;

    @Convert(converter = ErrorLogEntityContextConverter.class)
    private Map<String, String> context;

    public Integer getId() {
        return id;
    }

    public ErrorLogEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getApp() {
        return app;
    }

    public ErrorLogEntity withApp(String app) {
        this.app = app;
        return this;
    }

    public String getNamespace() {
        return namespace;
    }

    public ErrorLogEntity withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getHost() {
        return host;
    }

    public ErrorLogEntity withHost(String host) {
        this.host = host;
        return this;
    }

    public String getContainer() {
        return container;
    }

    public ErrorLogEntity withContainer(String container) {
        this.container = container;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorLogEntity withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTeam() {
        return team;
    }

    public ErrorLogEntity withTeam(String team) {
        this.team = team;
        return this;
    }

    public String getLogger() {
        return logger;
    }

    public ErrorLogEntity withLogger(String logger) {
        this.logger = logger;
        return this;
    }

    public String getCause() {
        return cause;
    }

    public ErrorLogEntity withCause(String cause) {
        this.cause = cause;
        return this;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public ErrorLogEntity withStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
        return this;
    }

    public Date getTstamp() {
        return tstamp;
    }

    public ErrorLogEntity withTstamp(OffsetDateTime tstamp) {
        if (tstamp != null) {
            this.tstamp = Date.from(tstamp.toInstant());
        }
        return this;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public ErrorLogEntity withContext(Map<String, String> context) {
        this.context = context;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ErrorLogEntity that = (ErrorLogEntity) o;

        if (!Objects.equals(id, that.id)) {
            return false;
        }
        if (!Objects.equals(app, that.app)) {
            return false;
        }
        if (!Objects.equals(namespace, that.namespace)) {
            return false;
        }
        if (!Objects.equals(host, that.host)) {
            return false;
        }
        if (!Objects.equals(container, that.container)) {
            return false;
        }
        if (!Objects.equals(message, that.message)) {
            return false;
        }
        if (!Objects.equals(team, that.team)) {
            return false;
        }
        if (!Objects.equals(logger, that.logger)) {
            return false;
        }
        if (!Objects.equals(cause, that.cause)) {
            return false;
        }
        if (!Objects.equals(stacktrace, that.stacktrace)) {
            return false;
        }
        if (!Objects.equals(tstamp, that.tstamp)) {
            return false;
        }
        return Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (app != null ? app.hashCode() : 0);
        result = 31 * result + (namespace != null ? namespace.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (container != null ? container.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (team != null ? team.hashCode() : 0);
        result = 31 * result + (logger != null ? logger.hashCode() : 0);
        result = 31 * result + (cause != null ? cause.hashCode() : 0);
        result = 31 * result + (stacktrace != null ? stacktrace.hashCode() : 0);
        result = 31 * result + (tstamp != null ? tstamp.hashCode() : 0);
        result = 31 * result + (context != null ? context.hashCode() : 0);
        return result;
    }
}
