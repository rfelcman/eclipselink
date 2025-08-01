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

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 * Test selecting using an object's primary key to ensure that it does not go to the databaase.
 */
public class UnitOfWorkConformObjectTest extends TestCase {
    protected boolean expected;
    protected UnitOfWork uow;
    protected Object result;
    protected ReadObjectQuery query;

    public UnitOfWorkConformObjectTest(ReadObjectQuery query, boolean size) {
        this.expected = size;
        this.query = query;
        setDescription("Test that the query is done on the unit of work changes.");
    }

    @Override
    public void reset() {
        uow.release();
    }

    @Override
    protected void setup() {
        uow = getSession().acquireUnitOfWork();
        uow.readAllObjects(Employee.class);
        Employee newEmployee = new org.eclipse.persistence.testing.models.employee.domain.Employee();
        newEmployee.setFirstName("Bob");
        newEmployee.setLastName(null);
        uow.registerObject(newEmployee);
        uow.deleteObject(uow.readObject(Employee.class, new ExpressionBuilder().get("firstName").equal("Sarah")));

        org.eclipse.persistence.tools.schemaframework.PopulationManager manager = org.eclipse.persistence.tools.schemaframework.PopulationManager.getDefaultManager();
        Employee example = (Employee)manager.getObject(Employee.class, "0001");
        Employee clone = (Employee)uow.readObject(example);
        clone.setLastName("Bobo");
    }

    @Override
    public void test() {
        this.query.setShouldPrepare(false);
        this.result = this.uow.executeQuery(this.query);
    }

    @Override
    protected void verify() {
        if ((this.result == null) && this.expected) {
            throw new TestErrorException("Query should have not have returned null");
        }

        if ((this.result != null) && (!this.expected)) {
            throw new TestErrorException("Query should have returned null");
        }
    }
}
