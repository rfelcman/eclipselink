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
package org.eclipse.persistence.testing.tests.aggregate;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.ReadObjectTest;

import java.util.List;

public class BatchReadingWithAggregateCollectionMapping extends ReadObjectTest {

    Class<?> cls;
    // Must be Agent or Builder
    public BatchReadingWithAggregateCollectionMapping(Class<?> cls) {
        super();
        this.cls = cls;
        setName(getName() + AgentBuilderHelper.getNameInBrackets(cls));
    }

    // Must be Agent or Builder
    public BatchReadingWithAggregateCollectionMapping(Object originalObject) {
        super(originalObject);
    }

    @Override
    public void reset() {

    }

    @Override
    public void setup() {
    }

    @Override
    public void test() {
        if(cls == null) {
            cls = originalObject.getClass();
        }
        ReadAllQuery query = new ReadAllQuery(cls);
        query.addBatchReadAttribute("houses");
        query.setSelectionCriteria(new ExpressionBuilder().get("lastName").equal("Jordan"));
        List objects = (List)getSession().executeQuery(query);
        Object object = objects.get(0);
        List houses = AgentBuilderHelper.getHouses(object);
    }

    @Override
    public void verify() {
    }
}

