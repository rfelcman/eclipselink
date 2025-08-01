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
import org.eclipse.persistence.testing.framework.TestErrorException;

import java.util.Vector;

/**
 * This test was designed to detect a bug in which if a cache identity map was of size 2 then deleting
 * from this identity map would result in a NullPointerException
 */
public class CustomDeleteTest extends org.eclipse.persistence.testing.framework.AutoVerifyTestCase {
    public CacheIdentityMap cache;

    public CustomDeleteTest(CacheIdentityMap cache) {
        this.cache = cache;
    }

    @Override
    public void setup() {
        org.eclipse.persistence.testing.models.employee.domain.Employee employee = new org.eclipse.persistence.testing.models.employee.domain.Employee();
        java.math.BigDecimal id = new java.math.BigDecimal(7777);
        java.util.Vector primaryKeys = new java.util.Vector();
        employee.setId(id);
        employee.setFirstName("Joe");
        employee.setLastName("Blow");
        primaryKeys.add(id);
        cache.put(primaryKeys, employee, null, 0);
        employee = new org.eclipse.persistence.testing.models.employee.domain.Employee();
        id = new java.math.BigDecimal(5678);
        primaryKeys = new Vector();
        employee.setId(id);
        employee.setFirstName("Joeline");
        employee.setLastName("Carson");
        primaryKeys.add(id);
        cache.put(primaryKeys, employee, null, 0);

        id = new java.math.BigDecimal(5978);
        primaryKeys = new Vector();
        employee.setId(id);
        employee.setFirstName("Joel");
        employee.setLastName("Cars");
        primaryKeys.add(id);
        cache.put(primaryKeys, employee, null, 0);

    }

    @Override
    public void test() {
        try {
            Vector primaryKeys = new Vector();
            primaryKeys.add(new java.math.BigDecimal(5678));
            cache.remove(primaryKeys, null);
        } catch (NullPointerException e) {
            throw new TestErrorException("Error deleteing from cache when size is 2");
        }
    }

    @Override
    public void verify() {
    }
}
