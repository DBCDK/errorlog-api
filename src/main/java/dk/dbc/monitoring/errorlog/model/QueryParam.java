/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog.model;

import java.time.Instant;

public class QueryParam {
    private String namespace;
    private String app;
    private String team;
    private Instant from;
    private Instant until;
    private int limit = Integer.MAX_VALUE;
    private int offset = 0;

    public String getNamespace() {
        return namespace;
    }

    public QueryParam withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public String getApp() {
        return app;
    }

    public QueryParam withApp(String app) {
        this.app = app;
        return this;
    }

    public String getTeam() {
        return team;
    }

    public QueryParam withTeam(String team) {
        this.team = team;
        return this;
    }

    public Instant getFrom() {
        return from;
    }

    public QueryParam withFrom(Instant from) {
        this.from = from;
        return this;
    }

    public Instant getUntil() {
        return until;
    }

    public QueryParam withUntil(Instant until) {
        this.until = until;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public QueryParam withLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public QueryParam withOffset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public String toString() {
        return "QueryParam{" +
                "namespace='" + namespace + '\'' +
                ", app='" + app + '\'' +
                ", team='" + team + '\'' +
                ", from=" + from +
                ", until=" + until +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
