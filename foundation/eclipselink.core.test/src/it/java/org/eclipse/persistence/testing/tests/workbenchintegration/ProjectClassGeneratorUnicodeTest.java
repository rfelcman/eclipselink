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

import org.eclipse.persistence.mappings.DatabaseMapping;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.factories.ProjectClassGenerator;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;


//Check if ProjectClassGenerator generates unicode escaped characters for non-ASCII
//characters properly by not passing in a boolean to generate().
public class ProjectClassGeneratorUnicodeTest extends AutoVerifyTestCase {

    public static String PROJECT_FILE = "ProjectClassGeneratorUnicodeProject";
    DatabaseMapping unicodeMap;
    Project unicodeProject;

    public ProjectClassGeneratorUnicodeTest() {
        setDescription("Test if ProjectClassGenerator generates unicode escaped characters for non-ASCII characters properly");
    }

    @Override
    protected void setup() throws Exception {
        org.eclipse.persistence.sessions.Project initialProject =
            new org.eclipse.persistence.testing.models.employee.relational.EmployeeProject();
        initialProject.getDescriptor(org.eclipse.persistence.testing.models.employee.domain.Employee.class).getMappingForAttributeName("firstName").setAttributeName("\u5E08\u592B");
        ProjectClassGenerator generator =
            new ProjectClassGenerator(initialProject, PROJECT_FILE, PROJECT_FILE + ".java");
        generator.generate();

        try {
           boolean result = Compiler.compile(PROJECT_FILE + ".java");
           if (!result) {
               throw new TestErrorException("Project class generation compile failed. This could either be a legitimate compile " +
                        "failure, or could result if you do not have the tools.jar from your JDK on the classpath.");
            }
            Class<?> projectClass = Class.forName(PROJECT_FILE);
            unicodeProject = (org.eclipse.persistence.sessions.Project)projectClass.getConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException("Project class generation failed.It may be possible to solve this issue by adding the tools.jar from your JDK to the classpath.", exception);
        }
    }

    @Override
    public void test() {
        unicodeMap =
                unicodeProject.getDescriptor(org.eclipse.persistence.testing.models.employee.domain.Employee.class).getMappingForAttributeName("\u5E08\u592B");
    }

    @Override
    protected void verify() {
        if (unicodeMap == null) {
            throw new TestErrorException("Mapping for unicode does not exist after written out and read in from project class");
        }
    }
}
