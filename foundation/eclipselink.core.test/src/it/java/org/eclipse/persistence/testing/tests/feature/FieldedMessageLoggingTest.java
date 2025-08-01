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

import org.eclipse.persistence.logging.DefaultSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;

/**
 * Test the Logging message containing formatting string.  This is for bug211741.
 */
public class FieldedMessageLoggingTest extends AutoVerifyTestCase {
    SessionLog oldLog;
    protected Exception caughtException = null;

    public FieldedMessageLoggingTest() {
        setDescription("Tests that message Logging containing formatting string like '{0' - does not cause a parse exception");
    }

     @Override
     public void setup() {
        caughtException = null;
        oldLog = getSession().getSessionLog();
        DefaultSessionLog newLog = new DefaultSessionLog();
        newLog.setLevel(SessionLog.FINE);

        getSession().setSessionLog(newLog);
    }

    @Override
    public void test() {
        Session s = getSession();
        try {
           s.getSessionLog().log(SessionLog.FINE, "Time {0.5HR}");
        }catch(Exception e) {
            caughtException = e;
        }
    }

    @Override
    public void verify() throws Exception {
        if (caughtException != null) {
            throw caughtException;
        }
    }

    @Override
    public void reset() {
        getSession().setSessionLog(oldLog);
    }
}
