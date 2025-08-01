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
//     dclarke - Dynamic Persistence
//       http://wiki.eclipse.org/EclipseLink/Development/Dynamic
//       (https://bugs.eclipse.org/bugs/show_bug.cgi?id=200045)
//     mnorman - tweaks to work from Ant command-line,
//               get database properties from System, etc.
//
package org.eclipse.persistence.testing.tests.dynamic.simple.sequencing;

//JUnit4 imports

import org.eclipse.persistence.dynamic.DynamicClassLoader;
import org.eclipse.persistence.dynamic.DynamicHelper;
import org.eclipse.persistence.dynamic.DynamicTypeBuilder;
import org.eclipse.persistence.sequencing.UnaryTableSequence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import static org.eclipse.persistence.testing.tests.dynamic.DynamicTestingHelper.createSession;

public class UnaryTableSequencingTestSuite extends BaseSequencingTestSuite {

    @BeforeClass
    public static void setUp() {
        session = createSession();
        dynamicHelper = new DynamicHelper(session);
        DynamicClassLoader dcl = dynamicHelper.getDynamicClassLoader();
        Class<?> dynamicType = dcl.createDynamicClass("simple.sequencing." + ENTITY_TYPE);
        DynamicTypeBuilder typeBuilder = new DynamicTypeBuilder(dynamicType, null, TABLE_NAME);
        typeBuilder.setPrimaryKeyFields("SID");
        typeBuilder.addDirectMapping("id", int.class, "SID");
        typeBuilder.addDirectMapping("value1", String.class, "VAL_1");
        // configureSequencing
        UnaryTableSequence sequence = new UnaryTableSequence(SEQ_TABLE_NAME);
        sequence.setCounterFieldName("SEQ_VALUE");
        sequence.setPreallocationSize(5);
        session.getProject().getLogin().setDefaultSequence(sequence);
        sequence.onConnect(session.getPlatform());
        typeBuilder.configureSequencing(sequence, SEQ_TABLE_NAME, "SID");

        dynamicHelper.addTypes(true, true, typeBuilder.getType());
    }

    @AfterClass
    public static void tearDown() {
        session.executeNonSelectingSQL("DROP TABLE " + TABLE_NAME);
        session.executeNonSelectingSQL("DROP TABLE " + SEQ_TABLE_NAME);
        session.logout();
        session = null;
        dynamicHelper = null;
    }

    @After
    public void clearSimpleTypeInstances() throws Exception {
        session.executeNonSelectingSQL("DELETE FROM " + TABLE_NAME);
        session.executeNonSelectingSQL("UPDATE " + SEQ_TABLE_NAME + " SET SEQ_VALUE = 0");
        session.getSequencingControl().resetSequencing();
        session.getSequencingControl().initializePreallocated();
    }

}
