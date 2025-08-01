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

public class SelectComplexAbsTest extends org.eclipse.persistence.testing.tests.jpql.JPQLTestCase {
    @Override
    public void setup() {
        Vector employees = getSomeEmployees();

        Employee emp1 = (Employee)employees.get(0);
        Employee emp2 = (Employee)employees.lastElement();

        String ejbqlString;

        ejbqlString = "SELECT OBJECT(emp) FROM Employee emp WHERE ";
        ejbqlString = ejbqlString + "(ABS(emp.salary) = ";
        ejbqlString = ejbqlString + emp1.getSalary() + ")";
        ejbqlString = ejbqlString + " OR (ABS(emp.salary) = ";
        ejbqlString = ejbqlString + emp2.getSalary() + ")";
        setEjbqlString(ejbqlString);
        Vector employeesUsed = new Vector();
        employeesUsed.add(emp1);
        employeesUsed.add(emp2);
        setOriginalOject(employeesUsed);
        super.setup();
    }
}
