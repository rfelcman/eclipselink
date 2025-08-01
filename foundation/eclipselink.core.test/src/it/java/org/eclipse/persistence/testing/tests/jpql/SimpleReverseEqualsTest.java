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
package org.eclipse.persistence.testing.tests.jpql;

import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

public class SimpleReverseEqualsTest extends org.eclipse.persistence.testing.tests.jpql.JPQLTestCase {
    @Override
    public void setup() {
        Vector employees = getSession().readAllObjects(Employee.class);

        String ejbqlString = null;
        Employee emp = (Employee)employees.get(0);
        ejbqlString = "SELECT OBJECT(emp) FROM Employee emp WHERE ";
        ejbqlString = ejbqlString + "\"" + emp.getFirstName() + "\"";
        ejbqlString = ejbqlString + " = emp.firstName";
        setEjbqlString(ejbqlString);
        super.setup();
    }

    @Override
    public void verify() throws Exception {
        // This method is derived from class org.eclipse.persistence.testing.ejb.EJBQLTesting.EJBQLTestCase
        // to do: code goes here
    }
}
