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

import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TestProblemException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Vector;

public class ReadAllBindAllParametersTest extends AutoVerifyTestCase {
    protected boolean shouldBindAllParametersSessionOriginal;
    protected Writer originalWriter;
    protected int originalMessageLogging;
    protected ReadAllQuery query;
    protected Vector v;

    public ReadAllBindAllParametersTest() {
        v = new Vector(2);
        v.add(new BigDecimal(1001));
        v.add(new BigDecimal(1002));

        setName("ReadAllBindAllParametersTest");
        setDescription("Tests all combinations of shouldBindAllParameters attributes on Session and Query");

    }

    @Override
    protected void setup() {
        shouldBindAllParametersSessionOriginal = getSession().getPlatform().shouldBindAllParameters();

        originalMessageLogging = getSession().getLogLevel();
        originalWriter = getSession().getLog();

        getSession().setLog(new StringWriter());
        getSession().setLogLevel(SessionLog.FINE);
    }

    protected boolean shouldBind() {
        return (!query.shouldIgnoreBindAllParameters() && query.shouldBindAllParameters()) || (query.shouldIgnoreBindAllParameters() && getSession().getPlatform().shouldBindAllParameters());
    }

    protected boolean wasBound() {
        return getSession().getLog().toString().contains("bind =>");
    }

    @Override
    protected void test() {
        for (int i = 0; i <= 1; i++) {
            getSession().getPlatform().setShouldBindAllParameters(i != 0);
            for (int j = 0; j <= 2; j++) {
                query = new ReadAllQuery(Employee.class);
                ExpressionBuilder builder = new ExpressionBuilder();
                Vector vExp = new Vector(2);
                vExp.add(builder.getParameter("p1"));
                query.addArgument("p1");
                vExp.add(builder.getParameter("p2"));
                query.addArgument("p2");
                Expression exp = builder.get("id").in(vExp);
                query.setSelectionCriteria(exp);

                switch (j) {
                case 0:
                    // nothing to do - just test the default:
                    // query.bindAllParameters == Undefined
                    break;
                case 1:
                    // query.bindAllParameters == False
                    query.setShouldBindAllParameters(false);
                    break;
                case 2:
                    // query.bindAllParameters == True
                    query.setShouldBindAllParameters(true);
                    break;
                }

                // clear the writer's buffer
                ((StringWriter)getSession().getLog()).getBuffer().setLength(0);
                try {
                    getSession().executeQuery(query, v);
                } catch (DatabaseException e) {
                    throw new TestProblemException("executeQuery threw DatabaseException");
                }
                if (shouldBind() != wasBound()) {
                    return;
                }
            }
        }
    }

    @Override
    protected void verify() throws Exception {
        if (shouldBind() != wasBound()) {
            String message;
            if (shouldBind()) {
                message = "Failed to bind";
            } else {
                message = "Wrongfully bound";
            }

            throw new TestErrorException(message);
        }
    }

    @Override
    public void reset() {
        getSession().getPlatform().setShouldBindAllParameters(shouldBindAllParametersSessionOriginal);

        getSession().setLog(originalWriter);
        getSession().setLogLevel(originalMessageLogging);
    }
}
