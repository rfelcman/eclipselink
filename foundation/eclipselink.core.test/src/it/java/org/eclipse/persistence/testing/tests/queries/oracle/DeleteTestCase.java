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
package org.eclipse.persistence.testing.tests.queries.oracle;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.DescriptorQueryManager;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.DeleteObjectQuery;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.models.mapping.Employee;

public class DeleteTestCase extends TestCase {
    DescriptorQueryManager qm;
    static String HINT_STRING = "/*+ RULE */";

    public DeleteTestCase() {
        setName("Delete Test");
        setDescription("Tests the use of a hint in an delete");
    }

    @Override
    public void setup() {
        ClassDescriptor employeeDescriptor = getSession().getDescriptor(Employee.class);
        qm = employeeDescriptor.getQueryManager();

        DeleteObjectQuery deleteQuery = new DeleteObjectQuery();
        qm.setDeleteQuery(deleteQuery);

        Employee emp = new Employee();
        emp.firstName = "Matt";
        emp.lastName = "MacIvor";
        emp.sex = "male";

        getAbstractSession().writeObject(emp);
    }

    @Override
    public void reset() {
        qm.setUpdateQuery(null);
    }

    @Override
    public void test() {
        DatabaseQuery updateQuery = qm.getDeleteQuery();
        updateQuery.setHintString(HINT_STRING);
        Employee emp =
            (Employee)getSession().readObject(Employee.class, new ExpressionBuilder().get("firstName").equal("Matt"));

        getAbstractSession().deleteObject(emp);
    }
}
