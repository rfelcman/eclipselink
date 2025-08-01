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
package org.eclipse.persistence.testing.tests.transparentindirection;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TransactionalTestCase;
import org.eclipse.persistence.testing.models.transparentindirection.Dog;
import org.eclipse.persistence.testing.models.transparentindirection.SalesRep;

public class UpdateDogTest extends TransactionalTestCase {
    public Dog theDog;
    public SalesRep newRep;

    @Override
    public void test() {
        Session session = getSession();
        UnitOfWork uow = session.acquireUnitOfWork();
        theDog = (Dog)uow.readObject(Dog.class);
        ExpressionBuilder eb = new ExpressionBuilder();
        newRep = (SalesRep)uow.readObject(SalesRep.class, eb.get("name").notEqual(theDog.getOwner().getKey()));
        theDog.setOwner(newRep);
        try {
            uow.commit();
        } catch (org.eclipse.persistence.exceptions.DatabaseException ex) {
            throw new TestErrorException(ex.toString());
        }
    }

    @Override
    public void verify() {
        Session session = getSession();
        session.getIdentityMapAccessor().initializeIdentityMap(Dog.class);
        theDog = (Dog)session.readObject(Dog.class);
        if (newRep.getKey() != theDog.getOwner().getKey()) {
            throw new TestErrorException("The Dog object was not properly updated!");
        }
    }
}
