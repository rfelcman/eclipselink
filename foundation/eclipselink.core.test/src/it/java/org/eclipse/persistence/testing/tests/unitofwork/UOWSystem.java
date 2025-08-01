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
package org.eclipse.persistence.testing.tests.unitofwork;

import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestSystem;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;

import java.util.Vector;


public class UOWSystem extends TestSystem {

    @Override
    public void addDescriptors(DatabaseSession session) {
        Vector descriptors = new Vector();

        descriptors.add(MailAddress.descriptor());
        descriptors.add(Person.descriptor());
        descriptors.add(Contact.descriptor());
        descriptors.add(Weather.descriptor());
        descriptors.add(ConcurrentAddress.descriptor());
        descriptors.add(ConcurrentPerson.descriptor());
        descriptors.add(ConcurrentProject.descriptor());
        descriptors.add(ConcurrentPhoneNumber.descriptor());
        descriptors.add(ConcurrentLargeProject.descriptor());
        descriptors.add(MutableAttributeObject.descriptor());

        session.addDescriptors(descriptors);
    }

    @Override
    public void createTables(DatabaseSession session) {
        SchemaManager schemaManager = new SchemaManager(session);

        schemaManager.replaceObject(Contact.tableDefinition());
        schemaManager.replaceObject(Person.tableDefinition());
        schemaManager.replaceObject(MailAddress.tableDefinition());
        schemaManager.replaceObject(Weather.tableDefinition());
        schemaManager.replaceObject(ConcurrentAddress.tableDefinition());
        schemaManager.replaceObject(ConcurrentPerson.tableDefinition());
        schemaManager.replaceObject(ConcurrentProject.tableDefinition());
        schemaManager.replaceObject(ConcurrentPhoneNumber.tableDefinition());
        schemaManager.replaceObject(MutableAttributeObject.tableDefinition());

        schemaManager.createSequences();
    }

    @Override
    public void populate(DatabaseSession session) {
        UnitOfWork uow = session.acquireUnitOfWork();

        Person employee1 = Person.example1();
        Person employee2 = Person.example2();
        Weather weather1 = Weather.example1();
        Weather weather2 = Weather.example2();

        uow.registerObject(employee1);
        uow.registerObject(employee2);
        uow.registerObject(weather1);
        uow.registerObject(weather2);
        uow.registerObject(ConcurrentPerson.example());
        uow.commit();
    }
}
