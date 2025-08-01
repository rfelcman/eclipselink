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
package org.eclipse.persistence.testing.tests.optimisticlocking;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.optimisticlocking.LockInObject;
import org.eclipse.persistence.testing.models.optimisticlocking.TimestampInObject;

public class TimestampNewObjectInCache extends TestCase {
    protected Object objectToBeRead;
    protected Object lockingObject;
    public boolean isTio;

    public TimestampNewObjectInCache(Object o) {
        setName("TimestampNewObjectInCache(" + o.getClass().getName() + ")");
        lockingObject = o;
        this.setDescription("This tests whether a new object that uses Timestamp or Version locking is added to Cache.");
    }

    @Override
    public void test() {
        UnitOfWork uow = this.getSession().acquireUnitOfWork();
        ExpressionBuilder bldr = new ExpressionBuilder();
        ReadObjectQuery queryObject = new ReadObjectQuery();
        queryObject.checkCacheOnly();

        if (lockingObject instanceof TimestampInObject tio) {
            isTio = true;
            uow.registerObject(tio);
            uow.commit();
            Expression exp = bldr.get("id").equal(tio.id);
            queryObject.setSelectionCriteria(exp);
            queryObject.setReferenceClass(TimestampInObject.class);
            objectToBeRead = getSession().executeQuery(queryObject);

        } else if (lockingObject instanceof LockInObject ov) {
            isTio = false;
            uow.registerObject(ov);
            uow.commit();
            Expression exp = bldr.get("id").equal(ov.id);
            queryObject.setSelectionCriteria(exp);
            queryObject.setReferenceClass(LockInObject.class);
            objectToBeRead = getSession().executeQuery(queryObject);
        }
    }

    @Override
    public void verify() {
        if (objectToBeRead != lockingObject) {
            if (isTio) {
                throw new TestErrorException("New Objects using Timestamp locking are not put in Cache.");
            } else {
                throw new TestErrorException("New Objects using Version locking are not put in Cache.");
            }
        }
    }
}
