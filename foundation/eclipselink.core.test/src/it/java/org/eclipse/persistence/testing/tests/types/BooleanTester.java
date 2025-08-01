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
package org.eclipse.persistence.testing.tests.types;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.tools.schemaframework.FieldDefinition;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;

import java.util.Vector;

/**
 *  Tests both boolean and Boolean access to the database.
 */
public class BooleanTester extends TypeTester {
    public boolean booleanValue;
    public Boolean booleanClassValue;

    public BooleanTester() {
        this(true);
    }

    public BooleanTester(boolean testValue) {
        super(Boolean.valueOf(testValue).toString());
        booleanValue = testValue;
        booleanClassValue = testValue;
    }

    public static RelationalDescriptor descriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();

        /* First define the class, table and descriptor properties. */
        descriptor.setJavaClass(BooleanTester.class);
        descriptor.setTableName("BOOLEANS");
        descriptor.setPrimaryKeyFieldName("NAME");

        /* Next define the attribute mappings. */
        descriptor.addDirectMapping("testName", "getTestName", "setTestName", "NAME");
        descriptor.addDirectMapping("booleanValue", "BOOLEANV");
        descriptor.addDirectMapping("booleanClassValue", "BOOLEANC");
        return descriptor;
    }

    public static RelationalDescriptor descriptorWithAccessors() {
        RelationalDescriptor descriptor = new RelationalDescriptor();

        /* First define the class, table and descriptor properties. */
        descriptor.setJavaClass(BooleanTester.class);
        descriptor.setTableName("BOOLEANS");
        descriptor.setPrimaryKeyFieldName("NAME");

        /* Next define the attribute mappings. */
        try {
            descriptor.addDirectMapping("testName", "getTestName", "setTestName", "NAME");
            descriptor.addDirectMapping("booleanValue", "getBooleanValue", "setBooleanValue", "BOOLEANV");
            descriptor.addDirectMapping("booleanClassValue", "getBooleanClassValue", "setBooleanClassValue", "BOOLEANC");
        } catch (DescriptorException exception) {
        }
        return descriptor;
    }

    public Boolean getBooleanClassValue() {
        return booleanClassValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanClassValue(Boolean boolValue) {
        booleanClassValue = boolValue;
    }

    public void setBooleanClassValue(boolean boolValue) {
        booleanClassValue = boolValue;
    }

    public void setBooleanValue(Boolean boolValue) {
        booleanValue = boolValue;
    }

    public void setBooleanValue(boolean boolValue) {
        booleanValue = boolValue;
    }

    /**
     *Return a platform independant definition of the database table.
     */
    public static TableDefinition tableDefinition(Session session) {
        TableDefinition definition = TypeTester.tableDefinition();
        FieldDefinition fieldDef;

        definition.setName("BOOLEANS");
        fieldDef = new FieldDefinition("BOOLEANV", Boolean.class);
        fieldDef.setShouldAllowNull(false);
        definition.addField(fieldDef);

        fieldDef = new FieldDefinition("BOOLEANC", Boolean.class);
        fieldDef.setShouldAllowNull(false);
        definition.addField(fieldDef);

        return definition;
    }

    public static Vector testInstances() {
        Vector tests = new Vector(2);

        tests.add(new BooleanTester(true));
        tests.add(new BooleanTester(false));
        return tests;
    }

    public String toString() {
        return "BooleanTester(" + getBooleanValue() + ")";
    }
}
