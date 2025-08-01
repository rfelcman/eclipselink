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
package org.eclipse.persistence.testing.tests.performance.java;

import org.eclipse.persistence.testing.framework.ConcurrentPerformanceComparisonTest;
import org.eclipse.persistence.testing.models.performance.Employee;

/**
 * This test tries to recreate concurrency issues in lazy initialization,
 * and also measure their performance.
 */
public class VolatileLazyInitConcurrentTest extends ConcurrentPerformanceComparisonTest {
    public VolatileLazyInitConcurrentTest() {
        setDescription("This test tries to recreate concurrency issues in lazy initialization and also measure their performance.");
    }

    protected volatile Employee employee;

    @Override
    public void runTask() throws Exception {
        Employee employee = getEmployee();
    }

    public Employee getEmployee() {
        if (employee == null) {
            synchronized (this) {
                if (employee == null) {
                    employee = new Employee();
                }
            }
        }
        return employee;
    }
}
