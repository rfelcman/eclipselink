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
package org.eclipse.persistence.testing.tests.queries;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;

/**
 * bug 3570561: CONFORMING QUERY THROWS INDIRECTION EXCEPTION EVEN WHEN ALL IND. TRIGGERED
 * <p>
 * In this bug, if a user set shouldThrowConformExceptions, we were still
 * throwing an indirection exception, even though conforming is smart enough to
 * do something else in this case.
 * <p>
 * It is API that got removed from sessions.UnitOfWork.
 *  @author  smcritch
 */
public class ConformingThrowConformExceptionsTest extends AutoVerifyTestCase {
    public ConformingThrowConformExceptionsTest() {
    }

    @Override
    public void test() throws Exception {
        UnitOfWork uow = getSession().acquireUnitOfWork();

        // populate UOW.  This avoids a future optimization where we will
        // skip conforming if UOW empty.
        Vector employees = uow.readAllObjects(Employee.class);

        // Makes test silly that this is hidden api.  Actually it was replaced
        // by in-memory query indirection policy, but there is no equivalent
        // functionality that way at all.
        uow.setShouldThrowConformExceptions(UnitOfWorkImpl.THROW_ALL_CONFORM_EXCEPTIONS);

        ReadAllQuery query = new ReadAllQuery(Employee.class);
        query.conformResultsInUnitOfWork();

        ExpressionBuilder emp = new ExpressionBuilder();
        Expression exp = emp.get("address").get("city").equal("Ottawa");
        query.setSelectionCriteria(exp);

        try {
            uow.executeQuery(query);
        } catch (Exception e) {
            throw new TestErrorException("should not have had a valueholder exception", e);
        }
    }
}
