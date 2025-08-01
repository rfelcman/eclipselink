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
package org.eclipse.persistence.testing.tests.queries.repreparation;

import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

@SuppressWarnings("deprecation")
public class AddPartialAttributeTest extends TestCase {
    private ReadAllQuery query;
    private Vector employees;

    public AddPartialAttributeTest() {
        setDescription("Test if SQL is reprepared the second time");
    }

    @Override
    public void reset() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();

        query = new ReadAllQuery(Employee.class);
        employees = (Vector)getSession().executeQuery(query);
    }

    @Override
    public void test() {
        query.dontMaintainCache();
        query.addPartialAttribute("lastName");
        employees = (Vector)getSession().executeQuery(query);
    }

    @Override
    public void verify() {
        if (!query.getCall().getSQLString().equals("SELECT t0.EMP_ID, t0.L_NAME FROM EMPLOYEE t0, SALARY t1 WHERE (t1.EMP_ID = t0.EMP_ID)")) {
            throw new TestErrorException("AddPartialAttributeTest failed.");
        }
    }
}

