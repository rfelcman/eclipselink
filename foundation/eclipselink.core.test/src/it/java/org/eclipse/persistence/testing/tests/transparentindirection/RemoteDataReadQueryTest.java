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

import org.eclipse.persistence.queries.CursoredStream;
import org.eclipse.persistence.queries.DataReadQuery;
import org.eclipse.persistence.queries.ValueReadQuery;
import org.eclipse.persistence.sessions.DataRecord;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TestProblemException;

import java.util.Stack;

/**
 * comment
 */
public class RemoteDataReadQueryTest extends AutoVerifyTestCase {
    Stack stack;

    public RemoteDataReadQueryTest() {
        setDescription("Remote data read queries with various container policies");
    }

    public DataReadQuery buildNewQuery() {
        return new DataReadQuery("select ID, CUSTNAME from ORD");
    }

    /**
     * set up test fixtures:
     *   check session
     */
    @Override
    protected void setup() {
        if (!getSession().isRemoteSession()) {
            throw new TestProblemException("session should be a remote session");
        }
    }

    @Override
    public void test() {
        this.testStackContainerPolicy();
        this.testCursoredStreamPolicy();
    }

    /**
     * assume the stack has already been populated by the previous test
     */
    public void testCursoredStreamPolicy() {
        ValueReadQuery sizeQuery = new ValueReadQuery("select count(*) from ORD");

        DataReadQuery query = this.buildNewQuery();
        query.useCursoredStream(5, 5, sizeQuery);

        CursoredStream stream = (CursoredStream)getSession().executeQuery(query);

        // if we get here, we must not have generated a ClassCastException
        int count = 0;
        while (stream.hasMoreElements()) {
            count++;
            DataRecord row = (DataRecord)stream.nextElement();
            if (row.get("CUSTNAME") == null) {
                throw new TestErrorException("missing data");
            }
        }
        if (count != stack.size()) {
            throw new TestErrorException("stream does not match stack - " + "expected: " + stack.size() + " actual: " + count);
        }
    }

    public void testStackContainerPolicy() {
        DataReadQuery query = this.buildNewQuery();
        query.useCollectionClass(Stack.class);

        stack = (Stack)getSession().executeQuery(query);
        // if we get here, we must not have generated a ClassCastException
        DataRecord row = (DataRecord)stack.peek();
        if (row.get("CUSTNAME") == null) {
            throw new TestErrorException("missing data");
        }
    }
}
