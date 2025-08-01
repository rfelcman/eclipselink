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
import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 * Test to ensure read times are correctly transferred from the session to the
 * UnitOfWork
 */
public class CacheExpiryUnitOfWorkReadTest extends CacheExpiryTest {

    protected Employee sessionEmployee = null;
    protected UnitOfWork uow = null;

    public CacheExpiryUnitOfWorkReadTest() {
        setDescription("Test to ensure read time is correct in the UnitOfWork cache.");
    }

    @Override
    public void setup() {
        super.setup();
        uow = getSession().acquireUnitOfWork();
    }

    @Override
    public void test() {
        sessionEmployee = (Employee)getSession().readObject(Employee.class);
        Employee uowEmployee = (Employee)uow.readObject(sessionEmployee);
        uow.release();
    }

    @Override
    public void verify() {
        if (((UnitOfWorkImpl)uow).getIdentityMapAccessorInstance().getCacheKeyForObject(sessionEmployee).getReadTime() !=
            ((AbstractSession)getSession()).getIdentityMapAccessorInstance().getCacheKeyForObject(sessionEmployee).getReadTime()) {
            throw new TestErrorException("The read time in the UnitOfWork cache is different from the read " +
                                         "time in the session cache.");
        }
    }

    @Override
    public void reset() {
        uow.release();
        super.reset();
    }
}
