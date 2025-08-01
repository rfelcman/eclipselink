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

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.models.transparentindirection.AbstractOrder;
import org.eclipse.persistence.testing.models.transparentindirection.AbstractOrderLine;
import org.eclipse.persistence.testing.models.transparentindirection.AbstractSalesRep;
import org.eclipse.persistence.testing.models.transparentindirection.IndirectListProject;
import org.eclipse.persistence.testing.models.transparentindirection.Order;
import org.eclipse.persistence.testing.models.transparentindirection.OrderLine;
import org.eclipse.persistence.testing.models.transparentindirection.SalesRep;

import java.util.Collection;
import java.util.Vector;

/**
 * Test the IndirectList with assorted DatabaseSessions and UnitsOfWork.
 * @author Big Country
 */
public class IndirectListTestDatabase extends IndirectContainerTestDatabase {

    int orderId;

    public IndirectListTestDatabase(String name) {
        super(name);
    }

    @Override
    protected AbstractOrder buildOrderShell() {
        return new Order();
    }

    @Override
    protected AbstractOrder buildTestOrderShell(String customerName) {
        return new Order(customerName);
    }

    @Override
    protected AbstractOrderLine newOrderLine(String item, int quanity) {
        return new OrderLine(item, quanity);
    }

    @Override
    protected AbstractSalesRep newSalesRep(String name) {
        return new SalesRep(name);
    }

    /**
     * build the TopLink project
     */
    public Project setUpProjectFromCode() {
        return new IndirectListProject();
    }

    /**
     * set up test fixtures:
     *   log in to database
     */
    @Override
    protected void setUp() {
        super.setUp();
        AbstractOrder order = this.buildTestOrder3();
        this.writeNewOrder(order);
        orderId = order.id;
        this.getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }
    @Override
    public void tearDown() {
        Order order = new Order();
        order.id = orderId;
        getAbstractSession().deleteObject(order);
        this.getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
                super.tearDown();
    }


    /**
     * Bug#4530516  Test the combination of TransparentIndirectionPolicy with conform and anyOf...isNull
     */
    public void testConformWithAnyOfIsNull() {
            UnitOfWork uow = getSession().acquireUnitOfWork();
            readNullName(uow);
            readNullName(uow);
    }

    private void readNullName(UnitOfWork uow)
    {
        ReadAllQuery readAllQuery = new ReadAllQuery(Order.class);
        ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        Expression e = expressionBuilder.anyOf("lines").get("itemName").isNull();
        readAllQuery.setSelectionCriteria(e);
        readAllQuery.conformResultsInUnitOfWork();

        Vector vector = (Vector) uow.executeQuery(readAllQuery);
        assertEquals("Result should have one element", 1, vector.size());
        Order order = (Order) vector.get(0);

        AbstractOrderLine line = (AbstractOrderLine) ((Collection)order.getLineContainer()).toArray()[0];
        assertNull("itemName in OrderLine should be null", line.getKey());
    }
}
