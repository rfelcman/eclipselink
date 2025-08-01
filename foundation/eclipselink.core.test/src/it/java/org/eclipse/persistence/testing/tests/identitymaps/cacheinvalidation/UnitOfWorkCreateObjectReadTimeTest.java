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
package org.eclipse.persistence.testing.tests.identitymaps.cacheinvalidation;

import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 * Test that the read time for objects created on a unit of work is correctly set
 */
public class UnitOfWorkCreateObjectReadTimeTest extends CacheExpiryTest {

    protected Employee employee = null;

    public UnitOfWorkCreateObjectReadTimeTest() {
        setDescription("Test that the read time for objects created on a UnitOfWork is correctly set.");
    }

    @Override
    public void test() {
        employee = new Employee();
        employee.setFirstName("Charley");
        employee.setLastName("Dickens");
        UnitOfWork uow = getSession().acquireUnitOfWork();
        uow.registerObject(employee);
        uow.commit();
    }

    @Override
    public void verify() {
        if (((AbstractSession)getSession()).getIdentityMapAccessorInstance().getCacheKeyForObject(employee).getReadTime() == 0) {
            throw new TestErrorException("Objects created on a UnitOfWork do not have read time set.");
        }
    }

}
