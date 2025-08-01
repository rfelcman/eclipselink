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
package org.eclipse.persistence.testing.tests.unitofwork.writechanges;

import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.queries.DeleteAllQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;


/**
 *  @author  smcritch
 */
public class WriteChanges_DeleteAll_TestCase extends AutoVerifyTestCase {
    protected Exception exception;

    @Override
    public void test() {
        UnitOfWork uow = getSession().acquireUnitOfWork();
        try {
            Vector employees = uow.readAllObjects(Employee.class);
            uow.writeChanges();
            DeleteAllQuery deleteAllQuery = new DeleteAllQuery();
            deleteAllQuery.setReferenceClass(Employee.class);
            deleteAllQuery.setObjects(employees);
            uow.executeQuery(deleteAllQuery);
        } catch (Exception e) {
            exception = e;
        } finally {
            uow.release();
        }
    }

    @Override
    public void verify() {
        if (exception == null) {
            throw new TestErrorException("Exception not thrown when attempting an ObjectLevelDeleteAllQuery after writeChanges().");
        } else if (!(exception instanceof ValidationException)) {
            throw new TestErrorException("Wrong exception thrown.", exception);
        }
    }

    @Override
    public void reset() {
        exception = null;
    }
}
