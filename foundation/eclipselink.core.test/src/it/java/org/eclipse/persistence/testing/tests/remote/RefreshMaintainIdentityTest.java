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

import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;

import java.util.Enumeration;
import java.util.Vector;

public class RefreshMaintainIdentityTest extends TestCase {

    private Vector masters = null;
    private Vector slaves = null;
    private boolean mastersShouldRefresh = false;
    private boolean slavesShouldRefresh = false;

    public RefreshMaintainIdentityTest() {
        setDescription("Test to ensure identity is maintained across valueholders in remote sessions.");
    }

    @Override
    public void reset() {
        getAbstractSession().rollbackTransaction();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        getSession().getDescriptor(Master.class).setShouldAlwaysRefreshCacheOnRemote(mastersShouldRefresh);
        getSession().getDescriptor(Slave.class).setShouldAlwaysRefreshCacheOnRemote(slavesShouldRefresh);

    }

    @Override
    public void setup() {
        mastersShouldRefresh = getSession().getDescriptor(Master.class).shouldAlwaysRefreshCacheOnRemote();
        slavesShouldRefresh = getSession().getDescriptor(Slave.class).shouldAlwaysRefreshCacheOnRemote();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        getAbstractSession().beginTransaction();


    }

    @Override
    public void test() {
        getSession().getDescriptor(Master.class).setShouldAlwaysRefreshCacheOnRemote(true);
        getSession().getDescriptor(Slave.class).setShouldAlwaysRefreshCacheOnRemote(true);

        slaves = getAllSlaves();
        masters = getAllMasters();

        for (Enumeration masterEnum = masters.elements(); masterEnum.hasMoreElements(); ) {
            Master master = (Master)masterEnum.nextElement();
            master.getSlaves();
        }

        slaves = getAllSlaves();
        masters = getAllMasters();
    }

    @Override
    protected void verify() {
        for (Enumeration slave_enum = slaves.elements(); slave_enum.hasMoreElements(); ) {
            Slave slave = (Slave)slave_enum.nextElement();
            if (!slave.getMaster().getSlaves().contains(slave)) {
                throw new TestErrorException("Identity Violated");
            }
        }
    }

    protected Vector getAllMasters() {
        ReadAllQuery query = new ReadAllQuery(Master.class);
        return (Vector)getSession().executeQuery(query);
    }

    protected Vector getAllSlaves() {
        ReadAllQuery query = new ReadAllQuery(Slave.class);
        return (Vector)getSession().executeQuery(query);
    }

}

