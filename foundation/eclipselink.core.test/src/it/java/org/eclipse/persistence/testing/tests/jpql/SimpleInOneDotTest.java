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
import org.eclipse.persistence.testing.models.employee.domain.PhoneNumber;

class SimpleInOneDotTest extends org.eclipse.persistence.testing.tests.jpql.JPQLTestCase {
    @Override
    public void setup() {
        Employee emp;
        emp = (Employee)getSomeEmployees().get(0);
        PhoneNumber empPhoneNumbers = (PhoneNumber)emp.getPhoneNumbers().get(0);

        String ejbqlString = "SELECT OBJECT(emp) FROM Employee emp, IN (emp.phoneNumbers) phone " + "WHERE phone.areaCode = \"" + empPhoneNumbers.getAreaCode() + "\"";

        setEjbqlString(ejbqlString);
        setOriginalOject(emp);

        super.setup();

    }
}
