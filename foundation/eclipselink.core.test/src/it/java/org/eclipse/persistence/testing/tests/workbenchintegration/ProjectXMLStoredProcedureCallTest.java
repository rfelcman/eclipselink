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
package org.eclipse.persistence.testing.tests.workbenchintegration;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.StoredProcedureCall;
import org.eclipse.persistence.sessions.DatabaseRecord;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.factories.XMLProjectReader;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TestWarningException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Vector;


public class ProjectXMLStoredProcedureCallTest extends TestCase {
    private DatabaseQuery query, UNamedQuery = null;
    private StoredProcedureCall storedProcedureCall, UNamedstoredProcedureCall = null;
    private ClassDescriptor employeeDescriptor;

    public ProjectXMLStoredProcedureCallTest() {
        setDescription("Tests that sepecified stored procedure can read from XML and execute correctly.");
    }

    @Override
    public void reset() {
    }

    @Override
    public void setup() {
        // right now only the stored procedure is set up in Oracle
        if (!(getSession().getPlatform().isOracle())) {
            throw new TestWarningException("This test can only be run in Oracle");
        }
    }

    @Override
    public void test() {
        Project project =
            XMLProjectReader.read("MWIntegrationCustomSQLEmployeeProject.xml", getClass().getClassLoader());
        employeeDescriptor = project.getDescriptor(Employee.class);
        testNamedArgumentStoredProcedureCall();
        testUNamedArgumentStoredProcedureCall();
    }

    // for passed-in named argument

    private void testNamedArgumentStoredProcedureCall() {
        query = employeeDescriptor.getQueryManager().getQuery("StoredProcedureCallInDataReadQuery");
        storedProcedureCall = (StoredProcedureCall)query.getCall();
    }

    // for passed-in no named argument

    private void testUNamedArgumentStoredProcedureCall() {
        UNamedQuery = employeeDescriptor.getQueryManager().getQuery("UNamedStoredProcedureCallInDataReadQuery");
        UNamedstoredProcedureCall = (StoredProcedureCall)UNamedQuery.getCall();
    }

    @Override
    protected void verify() {
        verifyNamedArgumentStoredProcedureCall();
        verifyUNamedArgumentStoredProcedureCall();
    }

    private void verifyNamedArgumentStoredProcedureCall() {
        if (query == null) {
            throw new TestErrorException("The query was incorrectly either read from or write to XML. Expected: [StoredProcedureCallInDataReadQuery]");
        }
        if (storedProcedureCall == null) {
            throw new TestErrorException("The stored procedure was incorrectly either read from or write to XML.");
        }

        Vector parameters = new Vector();

        DatabaseRecord row = (DatabaseRecord)((Vector)getSession().executeQuery(query, parameters)).get(0);

        Integer P_INOUT_FIELD_NAME = (Integer)row.get("P_INOUT_FIELD_NAME");
        Integer P_OUT_FIELD_NAME = (Integer)row.get("P_OUT_FIELD_NAME");

        if (!P_INOUT_FIELD_NAME.equals(1000) || !P_OUT_FIELD_NAME.equals(100)) {
            throw new TestErrorException("Stored Procedure which write to or read from XML does not execute as expected.");
        }
    }

    private void verifyUNamedArgumentStoredProcedureCall() {
        if (UNamedQuery == null) {
            throw new TestErrorException("The UNamed query name was not incorrectly either read from or write to XML. Expected: [UNamedStoredProcedureCallInDataReadQuery]");
        }
        if (UNamedstoredProcedureCall == null) {
            throw new TestErrorException("The UNamed stored procedure was incorrectly either read from or write to XML.");
        }

        Vector parameters = new Vector();
        DatabaseRecord unamedrow =
            (DatabaseRecord)((Vector)getSession().executeQuery(UNamedQuery, parameters)).get(0);

        Integer UNAMED_P_INOUT_FIELD_NAME = (Integer)unamedrow.get("P_INOUT_FIELD_NAME");
        Integer UNAMED_P_OUT_FIELD_NAME = (Integer)unamedrow.get("P_OUT_FIELD_NAME");

        if (!UNAMED_P_INOUT_FIELD_NAME.equals(1000) ||
            !UNAMED_P_OUT_FIELD_NAME.equals(100)) {
            throw new TestErrorException("UNnamed Stored Procedure which write to or read from XML dose not execute as expected.");
        }
    }
}
