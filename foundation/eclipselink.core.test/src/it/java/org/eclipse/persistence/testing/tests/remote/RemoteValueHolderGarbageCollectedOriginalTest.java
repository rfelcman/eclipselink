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
package org.eclipse.persistence.testing.tests.remote;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

public class RemoteValueHolderGarbageCollectedOriginalTest extends TestCase {
    protected Employee originalEmp;
    protected Session originalSession;

    public RemoteValueHolderGarbageCollectedOriginalTest(Session originalSession) {
        this.originalSession = originalSession;
        setDescription("Tests committing changes in the UnitOfWork when the originals have been Garbage Collected from the Server");
    }

    @Override
    public void reset() {
        getAbstractSession().rollbackTransaction();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {
        getAbstractSession().beginTransaction();
        originalEmp = (Employee)getSession().readObject(Employee.class);
        //Trigger the indirection on the CLient
        originalEmp.getAddress();
    }

    @Override
    public void test() {
        UnitOfWork uow = getSession().acquireUnitOfWork();
        Employee employeeClone = (Employee)uow.registerObject(originalEmp);
        this.originalSession.getIdentityMapAccessor().initializeAllIdentityMaps();
        employeeClone.setLastName("Something other than what it was");
        try {
            uow.commit();
            employeeClone = (Employee)getSession().readObject(originalEmp);
            employeeClone.getAddress();
        } catch (Exception exception) {
            throw new TestErrorException("Test failed.  The RemoteValueHolder did not merge correctly when Original had been garbage collected.  Exception:" +
                    exception);
        }
    }
}
