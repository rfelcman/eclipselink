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

import org.eclipse.persistence.queries.ObjectLevelReadQuery;
import org.eclipse.persistence.queries.ReadQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

public class ConformingQueryCacheTest extends UnitOfWorkQueryCacheTest {
    public ConformingQueryCacheTest() {
        setDescription("Ensure results can be conformed when a cached query is run in a UnitOfWork.");
    }

    @Override
    public ReadQuery getQueryForTest() {
        ReadQuery query = super.getQueryForTest();
        ((ObjectLevelReadQuery)query).conformResultsInUnitOfWork();
        return query;
    }

    @Override
    public void test() {
        super.test();
        Employee emp = (Employee) ((Vector)results).get(0);
        emp.setFirstName("Modified");
        Employee newEmp = new Employee();
        newEmp.setFirstName("Brooks");
        newEmp.setLastName("Hatlen");
        ((UnitOfWork)getSessionForQueryTest()).registerObject(newEmp);
        results = getSessionForQueryTest().executeQuery(NamedQueryQueryCacheTest.CACHING_QUERY_NAME);
    }

    @Override
    public void verify() {
        super.verify();
        for (Object o : (Vector) results) {
            Employee emp = (Employee) o;
            if (!emp.getFirstName().startsWith("B")) {
                throw new TestErrorException("Employee returned from cached query results does not conform.");
            }
        }
    }
}
