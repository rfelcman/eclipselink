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
package org.eclipse.persistence.testing.tests.workbenchintegration.QueryOptions;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;

import java.util.Vector;


public class UseDistinctTest extends AutoVerifyTestCase {
    private Vector employees;
    private ReadAllQuery query;

    public UseDistinctTest() {
        setDescription("Test use distinct option");
    }

    @Override
    public void reset() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {
    }

    @Override
    public void test() {
        query =
                (ReadAllQuery)getSession().getDescriptor(org.eclipse.persistence.testing.models.employee.domain.Employee.class).getQueryManager().getQuery("useDistinctQuery");
        ReadAllQuery queryCopy = (ReadAllQuery)query.clone();
        queryCopy.setSelectionCriteria(new ExpressionBuilder().anyOf("phoneNumbers").get("areaCode").equal("613"));
        employees = (Vector)getSession().executeQuery(queryCopy);
    }

    @Override
    public void verify() {
        if (employees.size() != 10) {
            throw new org.eclipse.persistence.testing.framework.TestErrorException("ReadAllQuery with useDistinct test failed. Mismatched objects returned");
        }
    }
}
