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
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.collections.Restaurant;
import org.eclipse.persistence.tools.schemaframework.PopulationManager;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class BatchReadingTest extends TestCase {
    BatchFetchType batchType;

    public Vector result;

    public BatchReadingTest(BatchFetchType batchType) {
        this.batchType = batchType;
        setName(getName() + batchType);
    }

    @Override
    public void setup() {
        //getAbstractSession().beginTransaction();
    }

    @Override
    public void test() {
        ReadAllQuery query = new ReadAllQuery();
        query.setReferenceClass(Restaurant.class);
        query.addBatchReadAttribute("menus");
        result = (Vector)getSession().executeQuery(query);

    }

    @Override
    public void verify() {
        PopulationManager manager = PopulationManager.getDefaultManager();
        List<Object> v = manager.getAllObjectsForClass(Restaurant.class);
        for (Enumeration enumtr = result.elements(); enumtr.hasMoreElements(); ) {
            Restaurant resDatabase = (Restaurant)enumtr.nextElement();
            for (Object enum1: v) {
                Restaurant resPop = (Restaurant)enum1;
                if (resDatabase.getName().equals(resPop.getName())) {
                    if (!((AbstractSession)getSession()).compareObjects(resDatabase, resPop)) {
                        throw new TestErrorException("Batchreading - one To Many Relationship : Object from database (" +
                                                                                          resDatabase +
                                                                                          ")is not equal to Object from PopulationManager(" +
                                                                                          resPop + ")");
                    }
                }

            }
        }
    }
}
