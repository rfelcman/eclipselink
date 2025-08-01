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
package org.eclipse.persistence.testing.tests.collections;

import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.collections.Menu;
import org.eclipse.persistence.testing.models.collections.Restaurant;

import java.util.Hashtable;

public class OTMPrivateOwnedDeleteObjectTest extends AutoVerifyTestCase {
    public OTMPrivateOwnedDeleteObjectTest() {
        super();
    }

    @Override
    public void reset() {
        rollbackTransaction();
    }

    @Override
    protected void setup() {
        beginTransaction();
    }

    @Override
    protected void test() {
        UnitOfWork uow = getSession().acquireUnitOfWork();
        Restaurant res = (Restaurant)getSession().readObject(Restaurant.class);
        Restaurant resClone = (Restaurant)uow.registerObject(res);
        resClone.getMenus();
        getSession().getIdentityMapAccessor().initializeIdentityMap(Menu.class);
        resClone.setMenus(new Hashtable());
        uow.commit();
        if (!res.getMenus().isEmpty()) {
            throw new TestErrorException("Not All were Deleted");
        }
    }

    /**
     * Verify if the objects match completely through allowing the session to use the descriptors.
     * This will compare the objects and all of their privately owned parts.
     */
    @Override
    protected void verify() {
    }
}
