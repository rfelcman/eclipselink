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
package org.eclipse.persistence.testing.tests.queries.inmemory;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

/**
 *  Test added for Odin
 *  Tests running a query which uses 'like' and conformResultsInUnitOfWork
 *  This will only work in JDK 1.4
 */
public class UnitOfWorkConformLikeTest extends TestCase {
    private Vector employees = null;
    private UnitOfWork uow = null;

    public UnitOfWorkConformLikeTest() {
        super();
        employees = new Vector();
    }

    @Override
    public void reset() {
        //clear the cache.
        uow.release();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {

        // read all the employees into the cache.
        uow = getSession().acquireUnitOfWork();
    }

    @Override
    public void test() {
        // get an employee and change that employee to fit our expression
        ExpressionBuilder emp = new ExpressionBuilder();
        Expression betty = emp.get("firstName").equal("Betty");
        Employee employee = (Employee)uow.readObject(Employee.class, betty);
        employee.setFirstName("Molly");

        ReadAllQuery query = new ReadAllQuery();
        query.setReferenceClass(Employee.class);
        query.conformResultsInUnitOfWork();

        // Query with a like
        ExpressionBuilder bldr = new ExpressionBuilder();
        Expression exp = bldr.get("firstName").like("%o%");
        query.setSelectionCriteria(exp);
        employees = (Vector)uow.executeQuery(query);

    }

    @Override
    public void verify() {
        if (employees.size() != 5) {
            throw new TestErrorException("Expected 5 employees but retured " + employees.size() + " for in-memory 'like' query.");
        }
    }
}
