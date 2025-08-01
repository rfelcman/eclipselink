/*
 * Copyright (c) 2018, 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

package org.eclipse.persistence.testing.tests.collections.map;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.Association;
import org.eclipse.persistence.queries.ReportQuery;
import org.eclipse.persistence.queries.ReportQueryResult;
import org.eclipse.persistence.testing.models.collections.map.DirectEntity1MMapHolder;
import org.eclipse.persistence.testing.tests.queries.report.ReportQueryTestCase;

import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

public class MapEntryDirectEntity1MReportQueryTest extends ReportQueryTestCase{

    @Override
    protected void buildExpectedResults() {
        Vector holders = getSession().readAllObjects(DirectEntity1MMapHolder.class);

        for (Enumeration e = holders.elements(); e.hasMoreElements(); ) {
            DirectEntity1MMapHolder holder = (DirectEntity1MMapHolder)e.nextElement();
            for (Map.Entry value : (Iterable<Map.Entry>) holder.getDirectToEntityMap().entrySet()) {
                Object[] result = new Object[1];
                Map.Entry entry = value;
                result[0] = new Association(entry.getKey(), entry.getValue());
                addResult(result, null);
            }
        }
    }

    @Override
    protected void removeFromResult(ReportQueryResult result, Vector expected) {
        for (Enumeration e = expected.elements(); e.hasMoreElements();) {
            ReportQueryResult expectedResult = (ReportQueryResult)e.nextElement();
            Association expectedAssocication = (Association)expectedResult.getByIndex(0);
            Association resultAssocication = (Association)result.getByIndex(0);
            if (expectedAssocication.getKey().equals(resultAssocication.getKey()) && expectedAssocication.getValue().equals(resultAssocication.getValue())) {
                expected.remove(expectedResult);
                return;
            }
        }
        getSession().logMessage("missing element: " + result);
    }

    @Override
    protected void setup() throws Exception {
        super.setup();
        reportQuery = new ReportQuery(new ExpressionBuilder());

        reportQuery.setReferenceClass(DirectEntity1MMapHolder.class);
        reportQuery.addAttribute("entry", reportQuery.getExpressionBuilder().anyOf("directToEntityMap").mapEntry());
    }
}
