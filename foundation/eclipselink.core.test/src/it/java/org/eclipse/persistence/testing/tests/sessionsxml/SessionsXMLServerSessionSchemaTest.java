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
package org.eclipse.persistence.testing.tests.sessionsxml;

import org.eclipse.persistence.sessions.factories.SessionManager;
import org.eclipse.persistence.sessions.factories.XMLSessionConfigLoader;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;


/**
 * Tests a basic session xml file that is built and validated against the
 * XML Schema for a server session
 *
 * @author Guy Pelletier
 * @version 1.0
 */
public class SessionsXMLServerSessionSchemaTest extends AutoVerifyTestCase {
    ServerSession serverSession;

    public SessionsXMLServerSessionSchemaTest() {
        setDescription("Test loading of a session xml with a server session against the XML Schema");
    }

    @Override
    public void reset() {
        if (serverSession != null && serverSession.isConnected()) {
            serverSession.logout(); // If session is logged in, log it out
            SessionManager.getManager().getSessions().remove(serverSession);
            serverSession = null;
        }
    }

    @Override
    public void test() {
        XMLSessionConfigLoader loader = new XMLSessionConfigLoader("org/eclipse/persistence/testing/models/sessionsxml/XMLSchemaServerSession.xml");

        // log in the session
            serverSession = (ServerSession)SessionManager.getManager().getSession(loader, "ServerSession", getClass().getClassLoader(), false, true); // don't refresh the session
    }

    @Override
    protected void verify() {
        if (serverSession == null) {
            throw new TestErrorException("Server session is null");
        }

        if (serverSession.getDescriptor(Employee.class) == null) {
            throw new TestErrorException("Missing a descriptor from the Employee project");
        }
    }
}
