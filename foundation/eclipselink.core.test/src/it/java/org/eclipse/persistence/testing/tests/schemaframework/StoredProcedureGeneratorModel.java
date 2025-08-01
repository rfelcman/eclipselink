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
package org.eclipse.persistence.testing.tests.schemaframework;

import org.eclipse.persistence.testing.framework.TestModel;
import org.eclipse.persistence.testing.framework.TestSuite;
import org.eclipse.persistence.testing.framework.TestWarningException;
import org.eclipse.persistence.testing.models.collections.CollectionsSystem;

/**
 * Test the stored procedure generator.
 * Note this just tests the generation of the procedures,
 * it does not test running them, or compiling the amendment class.
 * On Oracle procedures are not compiled until run, so most errors will not be found.
 *
 * SPGExecuteStoredProcedureTest has been added to execute and verify the generated stored procedures. - ET
 *
 */
public class StoredProcedureGeneratorModel extends TestModel {
    public StoredProcedureGeneratorModel() {
    }

    @Override
    public void addTests() {
        addTest(getBasicTestSuite());
    }

    @Override
    public void addRequiredSystems() {
        if (!(getSession().getPlatform().isOracle() || getSession().getPlatform().isSybase() || getSession().getPlatform().isSQLAnywhere() || getSession().getPlatform().isSQLServer() || getSession().getPlatform().isMySQL())) {
            throw new TestWarningException("Store procedure generation is only supported on Oracle, Sybase, MySQL and SQL Server.");
        }

        // Need a System with no optimistic locking
        addRequiredSystem(new CollectionsSystem());
    }

    private TestSuite getBasicTestSuite() {
        TestSuite suite = new TestSuite();
        suite.setName("Basic Stored Procedure Generator test suite");

        suite.addTest(new SPGBasicTest());
        suite.addTest(new SPGGenerateAmendmentClassTest());
        suite.addTest(new SPGExecuteStoredProcedureTest());

        return suite;
    }
}
