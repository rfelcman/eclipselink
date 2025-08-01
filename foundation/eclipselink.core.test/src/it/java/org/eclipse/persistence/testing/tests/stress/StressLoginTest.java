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
package org.eclipse.persistence.testing.tests.stress;

import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Test login many times.
 */

public class StressLoginTest extends AutoVerifyTestCase {
    public int stressLevel;
public StressLoginTest(int stressLevel)
{
    this.stressLevel = stressLevel;
}
@Override
public void reset( )
{
    ((DatabaseSession) getSession()).logout();
    ((DatabaseSession) getSession()).login();
}
@Override
public void test( )
{
    Vector sessions =  new Vector();

    try {
        for (int i = 0; i < stressLevel; i++) {
            DatabaseSession session = new Project(getSession().getDatasourceLogin().clone()).createDatabaseSession();
            session.login();
            sessions.add(session);
        }
        getSession().readObject(Address.class);
    } finally {
        for (Enumeration sessionEnum = sessions.elements(); sessionEnum.hasMoreElements(); ) {
            ((DatabaseSession) sessionEnum.nextElement()).logout();
        }
    }
    getSession().readObject(Address.class);
}
}
