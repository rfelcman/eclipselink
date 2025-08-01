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
package org.eclipse.persistence.testing.tests.optimisticlocking;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.optimisticlocking.Guitar;
import org.eclipse.persistence.testing.models.optimisticlocking.RockBand;
import org.eclipse.persistence.testing.models.optimisticlocking.RockMusician;

import java.util.Vector;

/**
 * Test the optimistic locking feature by removing an underlying row from
 * the database.
 */
public class OptimisticLockingPolicyDeleteRowTest extends AutoVerifyTestCase {
    protected Object originalObject;
    protected Class<?> testClass;

    public OptimisticLockingPolicyDeleteRowTest(Class<?> aClass) {
        setDescription("This test verifies that an optimistic lock exception is thrown when underlying database row is delete");
        testClass = aClass;
    }

    protected void guitarSetup() {
        Guitar guitar = Guitar.example1();
        guitar.make = "new";
        getDatabaseSession().writeObject(guitar);
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        originalObject = getSession().readObject(guitar);
    }

    public void guitarTest() {
        getSession().executeNonSelectingCall(new org.eclipse.persistence.queries.SQLCall("DELETE FROM MUSICALINSTRUMENT WHERE ID = " + ((Guitar)originalObject).id));
        getSession().executeNonSelectingCall(new org.eclipse.persistence.queries.SQLCall("DELETE FROM GUITAR WHERE ID = " + ((Guitar)originalObject).id));
    }

    @Override
    public void reset() {
        rollbackTransaction();
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
    }

    protected void rockBandSetup() {
        RockBand band = RockBand.example1();
        band.setBandMembers(new Vector());
        getDatabaseSession().writeObject(band);
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        originalObject = getSession().readObject(band);
    }

    public void rockBandTest() {
        getSession().executeNonSelectingCall(new org.eclipse.persistence.queries.SQLCall("DELETE FROM ROCKBAND WHERE ID = " + ((RockBand)originalObject).id));
    }

    protected void rockMusicianSetup() {
        RockMusician guy = RockMusician.example1();
        guy.band = new org.eclipse.persistence.indirection.ValueHolder();
        guy.mainInstrument = null;
        getDatabaseSession().writeObject(guy);
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        originalObject = getSession().readObject(guy);
    }

    public void rockMusicianTest() {
        getSession().executeNonSelectingCall(new org.eclipse.persistence.queries.SQLCall("DELETE FROM ROCKMUSICIAN WHERE ID = " + ((RockMusician)originalObject).id));
    }

    @Override
    protected void setup() {
        beginTransaction();
        if (testClass.equals(Guitar.class)) {
            guitarSetup();
        }
        if (testClass.equals(RockBand.class)) {
            rockBandSetup();
        }
        if (testClass.equals(RockMusician.class)) {
            rockMusicianSetup();
        }
    }

    @Override
    public void test() {
        if (testClass.equals(Guitar.class)) {
            guitarTest();
        }
        if (testClass.equals(RockBand.class)) {
            rockBandTest();
        }
        if (testClass.equals(RockMusician.class)) {
            rockMusicianTest();
        }
    }

    @Override
    protected void verify() {
        boolean exceptionCaught = false;

        try {
            UnitOfWork uow = getSession().acquireUnitOfWork();
            uow.deleteObject(uow.registerObject(originalObject));
            uow.commit();
        } catch (OptimisticLockException exception) {
            exceptionCaught = true;
        }

        if (!exceptionCaught) {
            throw new TestErrorException("No Optimistic Lock exception was thrown");
        }
    }
}
