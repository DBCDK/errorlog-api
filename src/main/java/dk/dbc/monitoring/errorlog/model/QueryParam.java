/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog.model;

import java.time.Instant;

public class QueryParam {
    private String team;
    private Instant from;
    private Instant until;

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

    @Override
    public String toString() {
        return "QueryParam{" +
                "team='" + team + '\'' +
                ", from=" + from +
                ", until=" + until +
                '}';
    }
}
