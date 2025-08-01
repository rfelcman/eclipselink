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

import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.ConcurrentPerformanceComparisonTest;
import org.eclipse.persistence.testing.models.performance.toplink.Employee;

import java.util.List;

/**
 * This test compares the concurrency of read all.
 * This test must be run on a multi-CPU machine to be meaningful.
 */
public class ReadAllUOWConcurrentTest extends ConcurrentPerformanceComparisonTest {
    protected List allObjects;

    public ReadAllUOWConcurrentTest() {
        setDescription("This tests the concurrency of read-all.");
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
     * Find all employees
     */
    @Override
    public void runTask() throws Exception {
        UnitOfWork uow = getSession().acquireUnitOfWork();
        uow.readAllObjects(Employee.class);
        uow.release();
    }
}
