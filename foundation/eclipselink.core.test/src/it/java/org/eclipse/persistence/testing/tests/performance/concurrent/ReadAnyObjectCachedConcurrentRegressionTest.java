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
package org.eclipse.persistence.testing.tests.performance.concurrent;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.testing.framework.ConcurrentPerformanceRegressionTest;
import org.eclipse.persistence.testing.models.performance.toplink.Employee;

import java.util.List;

/**
 * This test compares the concurrency of read object cache hits.
 * This test must be run on a multi-CPU machine to be meaningful.
 */
public class ReadAnyObjectCachedConcurrentRegressionTest extends ConcurrentPerformanceRegressionTest {
    protected int index;
    protected List allObjects;

    public ReadAnyObjectCachedConcurrentRegressionTest() {
        setDescription("This tests the concurrency of read-object cache hits.");
    }

    /**
     * Find all employees.
     */
    @Override
    public void setup() {
        super.setup();
        allObjects = getServerSession().acquireClientSession().readAllObjects(Employee.class);
    }

    /**
     * Cached read-object.
     */
    @Override
    public void runTask() throws Exception {
        int currentIndex = index;
        if (currentIndex >= allObjects.size()) {
            index = 0;
            currentIndex = 0;
        }
        index++;
        Object employee = allObjects.get(currentIndex);
        Session client = getServerSession().acquireClientSession();
        client.readObject(employee);
        client.release();
    }
}
