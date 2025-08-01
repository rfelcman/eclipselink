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
package org.eclipse.persistence.testing.tests.nls.japanese;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.sessions.factories.XMLProjectReader;

/**
 *  This test system uses the Employee test system to test the integration between the
 *  Mapping Workbench and the Foundation Library.  To do this, it writes our test project
 *  to an XML file and then reads the XML file and runs the employee tests on it.
 *  @author Tom Ware
 */
public class NLSEmployeeWorkbenchIntegrationSystem extends NLSEmployeeSystem {

    public static String PROJECT_FILE =
        "org/eclipse/persistence/testing/nlstests/japanese/NLSJapaneseMWIntegrationTestEmployeeProject";

    public ClassDescriptor employeeDescriptor;

    /**
     * Override the constructor for employee system to allow us to read and write XML
     */
    public NLSEmployeeWorkbenchIntegrationSystem() {
        this(PROJECT_FILE + ".xml");
    }

    /**
     * Override the constructor for employee system to allow us to read and write XML
     */
    public NLSEmployeeWorkbenchIntegrationSystem(String fileName) {
        project = null;
        project = XMLProjectReader.read(fileName);
    }
}
