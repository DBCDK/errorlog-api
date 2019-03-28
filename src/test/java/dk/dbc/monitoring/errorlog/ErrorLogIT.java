/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog;

import dk.dbc.monitoring.errorlog.model.ErrorLogEntity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ErrorLogIT extends IntegrationTest {
    ErrorLog errorLog;

    @Before
    public void createErrorLog() {
        errorLog = new ErrorLog();
        errorLog.entityManager = entityManager;
    }

    @Test
    public void persist() {
        final ErrorLogEntity entity = new ErrorLogEntity()
                .withApp("testApp")
                .withNamespace("testNamespace")
                .withHost("testHost")
                .withContainer("testContainer")
                .withMessage("testMessage")
                .withTstamp(OffsetDateTime.ofInstant(
                        Instant.now(), ZoneId.systemDefault()));
        transaction.run(() -> {
            errorLog.persist(entity);
            entityManager.flush();
            entityManager.refresh(entity);
        });

        assertThat(entity.getId(), is(notNullValue()));
    }

    @Test
    public void get() {
        final ErrorLogEntity entity = transaction.run(() -> errorLog.get(1).orElse(null));

        assertThat("ErrorLogEntity", entity, is(notNullValue()));
        assertThat("ErrorLogEntity.message", entity.getMessage(), is("app_1_message_1"));
    }

    @Test
    public void getWhenIdDoesNotExist() {
        final ErrorLogEntity entity = transaction.run(() -> errorLog.get(4242).orElse(null));

        assertThat(entity, is(nullValue()));
    }
}