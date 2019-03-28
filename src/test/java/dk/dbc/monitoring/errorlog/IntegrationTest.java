/*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

package dk.dbc.monitoring.errorlog;

import dk.dbc.commons.persistence.TransactionScopedPersistenceContext;
import org.junit.Before;
import org.junit.BeforeClass;
import org.postgresql.ds.PGSimpleDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_DRIVER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_URL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;

public abstract class IntegrationTest {
    static PGSimpleDataSource datasource;
    static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void setup() {
        datasource = createDataSource();
        entityManagerFactory = createEntityManagerFactory();
        createDatabase();
    }

    public static PGSimpleDataSource createDataSource() {
        final PGSimpleDataSource datasource = new PGSimpleDataSource();
        datasource.setDatabaseName("errorlog");
        datasource.setServerName("localhost");
        datasource.setPortNumber(Integer.parseInt(System.getProperty(
            "postgresql.port", "5432")));
        datasource.setUser(System.getProperty("user.name"));
        datasource.setPassword(System.getProperty("user.name"));
        return datasource;
    }

    public static void createDatabase() {
        final DatabaseMigrator databaseMigrator = new DatabaseMigrator(datasource);
        databaseMigrator.migrate();
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        final Map<String, String> entityManagerProperties = new HashMap<>();
        entityManagerProperties.put(JDBC_USER, datasource.getUser());
        entityManagerProperties.put(JDBC_PASSWORD, datasource.getPassword());
        entityManagerProperties.put(JDBC_URL, datasource.getUrl());
        entityManagerProperties.put(JDBC_DRIVER, "org.postgresql.Driver");
        entityManagerProperties.put("eclipselink.logging.level", "FINE");
        return entityManagerFactory = Persistence.createEntityManagerFactory(
                "errorlogIT", entityManagerProperties);
    }

    public static void executeScript(File script, Charset encoding) {
        try (
            Connection conn = datasource.getConnection();
            FileInputStream fstream = new FileInputStream(script);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new DataInputStream(fstream), encoding))) {
            final StringBuilder scriptLines = new StringBuilder();

            // Read file line by line, and append to StringBuilder
            String line;
            while ((line = reader.readLine()) != null) {
                scriptLines.append(line);
                scriptLines.append("\n");
            }
            conn.prepareStatement(scriptLines.toString()).executeUpdate();
        } catch (SQLException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    EntityManager entityManager;
    TransactionScopedPersistenceContext transaction;

    @Before
    public void resetDatabase() throws SQLException {
        try (Connection conn = datasource.getConnection();
             Statement statement = conn.createStatement()) {
            statement.executeUpdate("DELETE FROM errorlog");
            statement.executeUpdate("ALTER SEQUENCE errorlog_id_seq RESTART");
            executeScript(new File("src/test/resources/errorlog_testdata.sql"),
                    StandardCharsets.UTF_8);
        }
    }

    @Before
    public void createEntityManager() {
        entityManager = entityManagerFactory.createEntityManager();
        transaction = new TransactionScopedPersistenceContext(entityManager);
    }
}
