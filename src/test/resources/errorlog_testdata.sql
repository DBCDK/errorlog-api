 /*
 * Copyright Dansk Bibliotekscenter a/s. Licensed under GPLv3
 * See license text in LICENSE.txt or at https://opensource.dbc.dk/licenses/gpl-3.0/
 */

INSERT INTO errorlog(app, namespace, host, container, message, tstamp, team, cause, context)
VALUES ('app_1', 'prod_A', 'host_1', 'app_1_container', 'app_1_message_1', now(), 'teamX', 'EJBException',
        '{"key1":"value1","key2": "value2"}'::JSONB);