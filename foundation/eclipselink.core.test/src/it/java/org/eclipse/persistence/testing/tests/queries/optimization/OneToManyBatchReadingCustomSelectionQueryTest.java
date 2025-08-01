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
package org.eclipse.persistence.testing.tests.queries.optimization;

import org.eclipse.persistence.annotations.BatchFetchType;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.legacy.Shipment;

import java.util.Enumeration;

/**
 * This test checks the outcome of modifying a one to many mapping query to a custom user defined query
 * (that performs a batch read and does a join)
 */
public class OneToManyBatchReadingCustomSelectionQueryTest extends TestCase {
    BatchFetchType batchType;
    public java.util.Vector shipments;

    public OneToManyBatchReadingCustomSelectionQueryTest(BatchFetchType batchType) {
        setDescription("Tests a one to many mapping using a custom selction query to perform batch reading and a join ");
        this.batchType = batchType;
        setName(getName() + batchType);
    }

    @Override
    public void reset() {
        getAbstractSession().rollbackTransaction();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {
        getAbstractSession().beginTransaction();
        if ((batchType == BatchFetchType.IN) && !getSession().getPlatform().isOracle()) {
            throwWarning("Nested arrays not supported on this database");
        }
    }

    @Override
    public void test() {

        ReadAllQuery q = new ReadAllQuery();
        q.setBatchFetchType(batchType);
        q.setReferenceClass(Shipment.class);
        q.addBatchReadAttribute("orders");
        UnitOfWork uow = getSession().acquireUnitOfWork();
        shipments = (java.util.Vector)uow.executeQuery(q);
    }

    @Override
    public void verify() {
        Enumeration enumtr = shipments.elements();
        while (enumtr.hasMoreElements()) {
            Shipment shipment = (Shipment)enumtr.nextElement();
            if (shipment.orders.isEmpty()) {
                throw new TestErrorException("Test failed. Batched objects were not read");
            }
        }
    }
}
