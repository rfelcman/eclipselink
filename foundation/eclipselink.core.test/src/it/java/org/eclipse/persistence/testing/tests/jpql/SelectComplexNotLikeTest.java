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

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

public class SelectComplexNotLikeTest extends org.eclipse.persistence.testing.tests.jpql.JPQLTestCase {
    protected final static int MIN_FIRSTNAME_LENGTH = 4;

    @Override
    public void setup() {
        // Bug 223005: Verify that we have at least 1 employee with the required field length otherwise an EclipseLinkException will be thrown
        Employee emp = getEmployeeWithRequiredNameLength(MIN_FIRSTNAME_LENGTH, getName());
        String firstName = emp.getFirstName();
        StringBuilder partialFirstName = new StringBuilder();
        partialFirstName.append(firstName.charAt(0));
        partialFirstName.append("_");
        partialFirstName.append(firstName.substring(2, 4));
        partialFirstName.append( "%");

        ExpressionBuilder builder = new ExpressionBuilder();
        Expression whereClause = builder.get("firstName").notLike(partialFirstName.toString());

        ReadAllQuery raq = new ReadAllQuery();
        raq.setReferenceClass(Employee.class);
        raq.setSelectionCriteria(whereClause);

        setOriginalOject(getSession().executeQuery(raq));
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();

        String ejbqlString = "SELECT OBJECT(emp) FROM Employee emp WHERE emp.firstName NOT LIKE \"" + partialFirstName + "\"";

        setEjbqlString(ejbqlString);
        //setOriginalOject(emp);
        super.setup();

    }
}
