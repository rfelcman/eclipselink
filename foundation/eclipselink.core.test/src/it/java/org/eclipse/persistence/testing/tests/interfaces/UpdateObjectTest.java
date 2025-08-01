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
package org.eclipse.persistence.testing.tests.interfaces;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TransactionalTestCase;
import org.eclipse.persistence.testing.models.interfaces.Employee;
import org.eclipse.persistence.testing.models.interfaces.Phone;

public class UpdateObjectTest extends TransactionalTestCase {
    public Employee orig;

    @Override
    public void test() {
        Session session = getSession();
        UnitOfWork uow = session.acquireUnitOfWork();
        this.orig = Employee.example1();
        Employee e = (Employee)uow.registerObject(this.orig);
        Phone ph = (Phone)uow.readObject(Phone.class);
        ph.setNumber("5555567");
        e.setContact(ph);
        try {
            uow.commit();
        } catch (org.eclipse.persistence.exceptions.DatabaseException ex) {
            throw new TestErrorException("Inserted instead of updating" + System.lineSeparator() +
                    ex);
        }
    }
}
