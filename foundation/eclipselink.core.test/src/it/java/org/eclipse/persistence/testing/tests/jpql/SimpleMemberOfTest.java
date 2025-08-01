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
package org.eclipse.persistence.testing.tests.jpql;

import org.eclipse.persistence.testing.models.employee.domain.Project;

import java.util.Vector;

public class SimpleMemberOfTest extends org.eclipse.persistence.testing.tests.jpql.JPQLTestCase {
    @Override
    public void setup() {
        Vector allProjects = getSomeProjects();
        Vector selectedProjects = new Vector();
        Project currentProject;
        for (int i = 0; i < allProjects.size(); i++) {
            currentProject = (Project)allProjects.get(i);
            if (shouldIncludeProject(currentProject)) {
                selectedProjects.add(currentProject);
            }
        }

        String ejbqlString = "SELECT OBJECT(proj) FROM Employee emp, Project proj " + " WHERE  (proj.teamLeader MEMBER OF emp.manager.managedEmployees) " + "AND (emp.lastName = \"Chan\")";

        setEjbqlString(ejbqlString);
        setOriginalOject(selectedProjects);

        super.setup();
    }

    public boolean shouldIncludeProject(Project someProject) {
        return someProject.getName().equals("Enterprise System") || someProject.getName().equals("Problem Reporting System");
    }
}
