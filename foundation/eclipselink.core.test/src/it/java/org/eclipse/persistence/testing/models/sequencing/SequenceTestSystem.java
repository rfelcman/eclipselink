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
package org.eclipse.persistence.testing.models.sequencing;

import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestSystem;
import org.eclipse.persistence.tools.schemaframework.PopulationManager;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;

public class SequenceTestSystem extends TestSystem {
    org.eclipse.persistence.sessions.Project project;

    // Constructor
    public SequenceTestSystem() {
        project = new TestProjectForSequenceChecking();
    }

    @Override
    public void addDescriptors(DatabaseSession session) {
        if (project == null) {
            project = new TestProjectForSequenceChecking();
        }
        (session).addDescriptors(project);
    }

    @Override
    public void createTables(DatabaseSession session) {
        SchemaManager schemaManager = new SchemaManager(session);

    schemaManager.replaceObject(SeqTestClass1.tableDefinition());
    schemaManager.replaceObject(SeqTestClass2.tableDefinition());
        schemaManager.createSequences();
    }

    @Override
    public void populate(DatabaseSession session) {
        //DB2 and Sybase do not support inserting a numeric value into a String column
        boolean isOracle = session.getLogin().getPlatform().isOracle();
        boolean isSqlServer = session.getLogin().getPlatform().isSQLServer();
        if (isOracle || isSqlServer) {
            PopulationManager manager = PopulationManager.getDefaultManager();
        manager.registerObject(SequenceTestData.example1(), "SequenceTestDataExample1");
        manager.registerObject(SequenceTestData.example2(), "SequenceTestDataExample2");
        manager.registerObject(SequenceTestData.example3(), "SequenceTestDataExample3");
        manager.registerObject(SequenceTestData.example4(), "SequenceTestDataExample4");
        manager.registerObject(SequenceTestData.example5(), "SequenceTestDataExample5");
        manager.registerObject(SequenceTestData.example6(), "SequenceTestDataExample6");

            UnitOfWork uow = session.acquireUnitOfWork();
        uow.registerObject(SequenceTestData.example1());
        uow.registerObject(SequenceTestData.example2());
        uow.registerObject(SequenceTestData.example3());
        uow.registerObject(SequenceTestData.example4());
        uow.registerObject(SequenceTestData.example5());
        uow.registerObject(SequenceTestData.example6());
            uow.commit();
        }
    }
}
