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
package org.eclipse.persistence.testing.tests.feature;

import org.eclipse.persistence.sessions.CopyGroup;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TransactionalTestCase;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 *    Test the object copying feature.
 */
public class ObjectCopyingTest extends TransactionalTestCase {
    public ObjectCopyingTest() {
        setDescription("Test the object copying feature.");
    }

    @Override
    public void test() {
        Employee original = (Employee)getSession().readObject(Employee.class);
        CopyGroup group = new CopyGroup();
        group.setShouldResetPrimaryKey(true);
        Employee copy = (Employee)getSession().copy(original, group);
        UnitOfWork uow = getSession().acquireUnitOfWork();
        uow.registerObject(copy);
        uow.commit();

        copy = (Employee)getSession().readObject(copy);
        if ((original == copy) || (original.getAddress() == copy.getAddress())) {
            throw new TestErrorException("Copies are not copies.");
        }
        if ((!original.getFirstName().equals(copy.getFirstName())) || (!original.getAddress().getCity().equals(copy.getAddress().getCity()))) {
            throw new TestErrorException("Copies are not the same.");
        }
    }
}
