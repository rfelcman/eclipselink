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
package org.eclipse.persistence.testing.tests.optimization.queryandsqlcounting.querycache;

import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestErrorException;

import java.util.Vector;

/**
 * Test a cached query when executed in a UnitOfWork
 *
 * Ensure that all the results of a cached query are registerd when used in a UOW
 */
public class UnitOfWorkQueryCacheTest extends NamedQueryQueryCacheTest {
    protected UnitOfWork uow = null;

    public UnitOfWorkQueryCacheTest() {
        setDescription("Ensure cached queries work in a UnitOfWork.");
    }

    @Override
    public void setup() {
        super.setup();
        uow = getSession().acquireUnitOfWork();
    }

    @Override
    public Session getSessionForQueryTest() {
        return uow;
    }

    @Override
    public void verify() {
        super.verify();
        for (Object o : (Vector) results) {
            if (!((UnitOfWorkImpl) getSessionForQueryTest()).isObjectRegistered(o)) {
                throw new TestErrorException("Query results were not registered in the UOW " + " after being returned from a query with cached results");
            }
        }
    }
}
