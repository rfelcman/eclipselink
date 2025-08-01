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
package org.eclipse.persistence.testing.tests.queries.report;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.testing.models.employee.domain.LargeProject;

import java.util.Enumeration;
import java.util.Vector;

/**
 * ReportQuery test for Scenario 1.1
 * SELECT F_NAME, L_NAME FROM EMPLOYEE
 */
public class Scenario1_5a extends ReportQueryTestCase {
    public Scenario1_5a() {
        setDescription("DTF mappings of subclass");
    }

    @Override
    protected void buildExpectedResults() {
        Vector projects = getSession().readAllObjects(LargeProject.class);

        for (Enumeration e = projects.elements(); e.hasMoreElements(); ) {
            LargeProject project = (LargeProject)e.nextElement();
            Object[] result = new Object[2];
            result[0] = project.getName();
            result[1] = project.getBudget();
            addResult(result, null);
        }
    }

    @Override
    protected void setup() throws Exception {
        super.setup();
        reportQuery = new ReportQuery(new ExpressionBuilder());

        reportQuery.setReferenceClass(LargeProject.class);
        reportQuery.addAttribute("name");
        reportQuery.addAttribute("budget");
    }
}
