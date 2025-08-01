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

import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.factories.ProjectClassGenerator;
import org.eclipse.persistence.sessions.factories.XMLProjectReader;
import org.eclipse.persistence.sessions.factories.XMLProjectWriter;
import org.eclipse.persistence.testing.framework.TestErrorException;

/**
 *  @version $Header: WorkbenchIntegrationSystemHelper.java 31-jul-2007.11:40:26 gpelleti Exp $
 *  @author  gpelleti
 *  @since   11g
 */
public class WorkbenchIntegrationSystemHelper {

    /**
     * For the given project, generate the class file, compile it and set
     * it to be the project.
     */
    public static Project buildProjectClass(Project project, String filename) {
        ProjectClassGenerator generator = new ProjectClassGenerator(project, filename, filename + ".java");
        generator.generate();

        try {
            boolean result = Compiler.compile(filename + ".java");
            if (!result) {
                throw new TestErrorException("Project class generation compile failed. This could either be a legitimate compile " +
                         "failure, or could result if you do not have the tools.jar from your JDK on the classpath.");
            }
            Class<?> projectClass = Class.forName(filename);
            return (Project) projectClass.getConstructor().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException("Project class generation failed.It may be possible to solve this issue by adding the tools.jar from your JDK to the classpath.", exception);
        }
    }

    /**
     * For the given project, generate the project xml and read it back in.
     */
    public static Project buildProjectXML(Project project, String filename) {
        return buildProjectXML(project, filename, project.getClass().getClassLoader());
    }

    /**
     * For the given project, generate the project xml and read it back in.
     */
    public static Project buildProjectXML(Project project, String filename, ClassLoader loader) {
        XMLProjectWriter.write(filename + ".xml", project);
        return XMLProjectReader.read(filename + ".xml", loader);
    }
}
