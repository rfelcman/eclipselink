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
package org.eclipse.persistence.testing.tests.nondeferredwrites;

import org.eclipse.persistence.descriptors.CMPPolicy;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.factories.XMLProjectReader;
import org.eclipse.persistence.sessions.factories.XMLProjectWriter;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;
import org.eclipse.persistence.testing.models.employee.relational.EmployeeProject;

import java.io.File;

public class ProjectXMLTest extends AutoVerifyTestCase {
    public static String PROJECT_FILE = "org/eclipse/persistence/testing/workbench_integration/MWIntegrationTestEmployeeProject.xml";
    public CMPPolicy oldPolicy;
    public Project employeeProject;

    public ProjectXMLTest() {
        setDescription("This test will verify that the isolation setting is set within the project xml");
    }

    @Override
    public void setup() {
        employeeProject = new EmployeeProject();
        ClassDescriptor descriptor = employeeProject.getDescriptor(Employee.class);
        this.oldPolicy = descriptor.getCMPPolicy();
        descriptor.setCMPPolicy(new CMPPolicy());
    }

    @Override
    public void test() {
        // none - after_ejbcreate
        ClassDescriptor descriptor = employeeProject.getDescriptor(Employee.class);
        descriptor.getCMPPolicy().setDeferModificationsUntilCommit(CMPPolicy.NONE);
        descriptor.getCMPPolicy().setNonDeferredCreateTime(CMPPolicy.AFTER_EJBCREATE);
        XMLProjectWriter.write("NonDeferredProject.xml", employeeProject);
        Project project = XMLProjectReader.read("NonDeferredProject.xml", getClass().getClassLoader());
        if (!(project.getDescriptor(Employee.class).getCMPPolicy().getDeferModificationsUntilCommit() == CMPPolicy.NONE)) {
            throw new TestErrorException("modification deferral level 'NONE' not copied to and from XML");
        }
        if (!(project.getDescriptor(Employee.class).getCMPPolicy().getNonDeferredCreateTime() == CMPPolicy.AFTER_EJBCREATE)) {
            throw new TestErrorException("non deferred create time 'AFTER_EJBCREATE' not copied to and from XML");
        }

        descriptor.getCMPPolicy().setDeferModificationsUntilCommit(CMPPolicy.ALL_MODIFICATIONS);
        descriptor.getCMPPolicy().setNonDeferredCreateTime(CMPPolicy.AFTER_EJBPOSTCREATE);
        XMLProjectWriter.write("NonDeferredProject.xml", employeeProject);
        project = XMLProjectReader.read("NonDeferredProject.xml", getClass().getClassLoader());
        if (!(project.getDescriptor(Employee.class).getCMPPolicy().getDeferModificationsUntilCommit() == CMPPolicy.ALL_MODIFICATIONS)) {
            throw new TestErrorException("modification deferral level 'ALL_MODIFICATIONS' not copied to and from XML");
        }
        if (!(project.getDescriptor(Employee.class).getCMPPolicy().getNonDeferredCreateTime() == CMPPolicy.AFTER_EJBPOSTCREATE)) {
            throw new TestErrorException("non deferred create time 'AFTER_EJBPOSTCREATE' not copied to and from XML");
        }

        descriptor.getCMPPolicy().setDeferModificationsUntilCommit(CMPPolicy.UPDATE_MODIFICATIONS);
        descriptor.getCMPPolicy().setNonDeferredCreateTime(CMPPolicy.UNDEFINED);
        XMLProjectWriter.write("NonDeferredProject.xml", employeeProject);
        project = XMLProjectReader.read("NonDeferredProject.xml", getClass().getClassLoader());
        if (!(project.getDescriptor(Employee.class).getCMPPolicy().getDeferModificationsUntilCommit() == CMPPolicy.UPDATE_MODIFICATIONS)) {
            throw new TestErrorException("modification deferral level 'UPDATE_MODIFICATIONS' not copied to and from XML");
        }
        if (!(project.getDescriptor(Employee.class).getCMPPolicy().getNonDeferredCreateTime() == CMPPolicy.UNDEFINED)) {
            throw new TestErrorException("non deferred create time 'UNDEFINED' not copied to and from XML");
        }
    }

    @Override
    public void reset() {
        File file = new File("NonDeferredProject.xml");
        file.delete();
    }
}
