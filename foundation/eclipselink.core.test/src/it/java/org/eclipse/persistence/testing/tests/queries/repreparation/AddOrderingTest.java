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

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

public class AddOrderingTest extends AutoVerifyTestCase {
    ReportQuery reportQuery;
    Vector results;

    public AddOrderingTest() {
        setDescription("Test if SQL is reprepared the second time");
    }

    @Override
    public void reset() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();

        reportQuery = new ReportQuery(new ExpressionBuilder());
        reportQuery.setReferenceClass(Employee.class);
        reportQuery.addAttribute("firstName");
        results = (Vector)getSession().executeQuery(reportQuery);
    }

    @Override
    public void test() {
        reportQuery.addOrdering(reportQuery.getExpressionBuilder().get("firstName").descending());
        results = (Vector)getSession().executeQuery(reportQuery);
    }

    @Override
    public void verify() {
        if (!reportQuery.getCall().getSQLString().equals("SELECT t0.F_NAME FROM EMPLOYEE t0, SALARY t1 WHERE (t1.EMP_ID = t0.EMP_ID) ORDER BY t0.F_NAME DESC")) {
            throw new org.eclipse.persistence.testing.framework.TestErrorException("AddOrderingTest failed.");
        }
    }
}
