/*
 * Copyright (c) 1998, 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

// Contributors:
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.testing.tests.feature;

import org.eclipse.persistence.sessions.Connector;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.JNDIConnector;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestDataSource;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.naming.TestContext;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * test the JNDIConnector
 */
public class JNDIConnectionTest extends AutoVerifyTestCase {
    Connector connector;

    public JNDIConnectionTest() {
        setDescription("Connect to the database using the JNDIConnector");
    }

    @Override
    public void reset() {
        ((org.eclipse.persistence.sessions.DatabaseSession)getSession()).logout();
        getSession().getLogin().setConnector(connector);
        ((org.eclipse.persistence.sessions.DatabaseSession)getSession()).login();
    }

    @Override
    protected void setup() {
        ((org.eclipse.persistence.sessions.DatabaseSession)getSession()).logout();

        DatabaseLogin login = getSession().getLogin();
        connector = login.getConnector();// save the connector to restore later

        String dataSourceName = "JNDI test DataSource";
        DataSource dataSource = new TestDataSource(login.getDriverClassName(), login.getConnectionString(), (Properties)login.getProperties().clone());
        Context context;
        try {
            context = new TestContext(dataSourceName, dataSource);
        } catch (NamingException e) {
            throw new RuntimeException("JNDI problem");
        }

        login.setConnector(new JNDIConnector(context, dataSourceName));
    }

    @Override
    public void test() {
        ((org.eclipse.persistence.sessions.DatabaseSession)getSession()).login();
    }

    @Override
    protected void verify() {
        if (!getSession().isConnected()) {
            throw new TestErrorException("Session not connected via JNDI-supplied DataSource.");
        }
    }
}
