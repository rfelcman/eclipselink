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
package org.eclipse.persistence.testing.tests.remote;

import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.tests.queries.options.QueryOptionEmployee;

public class RefreshRemoteIdentityMapResultsTest extends TestCase {
    protected QueryOptionEmployee originalObject;
    protected String firstName;

    public RefreshRemoteIdentityMapResultsTest() {
        setDescription("This test verifies if the refresh remote identity map feature works properly");
    }

    @Override
    public void reset() {
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
    }

    @Override
    protected void setup() {
        originalObject =
                (QueryOptionEmployee)getSession().executeQuery("refreshRemoteIdentityMapResultsQuery", QueryOptionEmployee.class);
    }

    @Override
    public void test() {
        firstName = originalObject.getName();
        originalObject.setName("Godzilla");

        //      ((ReadObjectQuery)getSession().getDescriptor(org.eclipse.persistence.demos.employee.domain.Employee.class).getQueryManager().getQuery("refreshRemoteIdentityMapResultsQuery")).setSelectionObject(originalObject);
        getSession().executeQuery("refreshRemoteIdentityMapResultsQuery", QueryOptionEmployee.class);
    }

    @Override
    protected void verify() {
        if (!(originalObject.getName().equals(firstName))) {
            throw new TestErrorException("The refresh remote identity map results test failed.");
        }
    }
}
