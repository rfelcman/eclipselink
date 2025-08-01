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
package org.eclipse.persistence.testing.tests.optimization.queryandsqlcounting.querycache;

import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadQuery;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

/**
 * Ensure partial attribute queries work with query caching
 */
public class PartialAttributeQueryCacheTest extends NamedQueryQueryCacheTest {
    public PartialAttributeQueryCacheTest() {
        setDescription("Ensure queries using partial attributes can use query caching.");
    }

    @Override
    @SuppressWarnings("deprecation")
    public ReadQuery getQueryForTest() {
        ReadAllQuery testQuery = (ReadAllQuery)super.getQueryForTest();
        testQuery.addPartialAttribute("firstName");
        testQuery.addPartialAttribute("lastName");
        testQuery.dontMaintainCache();
        testQuery.cacheQueryResults();
        return testQuery;
    }

    @Override
    public void verify() {
        super.verify();
        for (Object o : (Vector) results) {
            Employee emp = (Employee) o;
            if ((emp.getFirstName() == null) || (emp.getLastName() == null)) {
                throw new TestErrorException("Returned query result was missing data for partial object " + "query with query caching turned on.");
            }
            if (emp.getSalary() > 0) {
                throw new TestErrorException("Additional results were returned in a partial object " + " query with caching turned on.");
            }
        }
    }
}
