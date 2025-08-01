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
package org.eclipse.persistence.testing.tests.identitymaps.cache;

import org.eclipse.persistence.internal.identitymaps.CacheIdentityMap;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

public class InsertTest extends AutoVerifyTestCase {
    CacheIdentityMap cache;
    org.eclipse.persistence.testing.models.employee.domain.Employee employee;

    public InsertTest(CacheIdentityMap cache) {
        this.cache = cache;
    }

    @Override
    public void test() {
        Employee employee = new Employee();
        java.math.BigDecimal id = new java.math.BigDecimal(7777);
        java.util.Vector primaryKeys = new java.util.Vector();

        employee.setId(id);
        employee.setFirstName("Joe");
        employee.setLastName("Blow");
        primaryKeys.add(id);
        cache.put(primaryKeys, employee, null, 0);

        this.employee = employee;
    }

    @Override
    public void verify() {
        // Check to see if element just inserted is last element.
        java.math.BigDecimal id = new java.math.BigDecimal(7777);
        java.util.Vector primaryKeys = new java.util.Vector();
        primaryKeys.add(id);

        if (!cache.containsKey(primaryKeys)) {
            throw new TestErrorException("Cache identity map " + cache + " did not insert " + employee + " " + primaryKeys + " into the cache.");
        }
    }
}
