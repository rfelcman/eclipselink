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
package org.eclipse.persistence.testing.tests.queries.oracle;

import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TestWarningException;
import org.eclipse.persistence.testing.models.mapping.Employee;

public class ChangedHintStringTest extends TestCase {
    static String FIRST_HINT_STRING = "/*+ ALL_ROWS */";
    static String SECOND_HINT_STRING = "/*+ FIRST_ROWS */";
    DatabaseQuery query;

    public ChangedHintStringTest() {
        setName("ChangedHintStringTest");
        setDescription("Tests setting a hint, executing, setting a different hint and executing again");
    }

    @Override
    public void setup() {
        if (!(getSession().getPlatform().isOracle()))
            throw new TestWarningException("This test case is only intended for Oracle");
        query = new ReadAllQuery(Employee.class);
        /*
    ExpressionBuilder raqb = new ExpressionBuilder(Employee.class);
    ExpressionBuilder rqb = new ExpressionBuilder();
    ReportQuery rq = new ReportQuery(Phone.class,rqb);
    BasicReadTest test = new BasicReadTest(query);
    Expression exp = rqb.get("id").equal(raqb.get("id"));
    rq.setSelectionCriteria(exp);
    rq.addAttribute("id");
    rq.setHintString(test.INNER_HINT);
    Expression expression = raqb.get("id").in(rq);
    query.setSelectionCriteria(expression);
    */
    }

    @Override
    public void reset() {
    }

    @Override
    public void test() {
        for (int i = 1; i < 3; i++) {
            if ((i % 2) != 0) {
                query.setHintString(FIRST_HINT_STRING);
            } else
                query.setHintString(SECOND_HINT_STRING);
            getSession().executeQuery(query);
        }
    }

    @Override
    public void verify() {
        String sqlString = query.getSQLString();
        if (!sqlString.contains(SECOND_HINT_STRING))
            throw new TestErrorException("Wrong or no hint string in SQL");
    }

}
