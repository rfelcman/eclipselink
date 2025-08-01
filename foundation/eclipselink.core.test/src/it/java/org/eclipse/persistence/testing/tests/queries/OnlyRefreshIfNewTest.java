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
package org.eclipse.persistence.testing.tests.queries;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

public class OnlyRefreshIfNewTest extends AutoVerifyTestCase {
    protected Employee employeeObject;
    protected String firstName;
    public ClassDescriptor empDescriptor;

    public OnlyRefreshIfNewTest() {
        setDescription("This test verifies that the shouldOnlyRefreshCacheIfNewerVersion() feature works properly.");
    }

    @Override
    public void reset() {
        this.empDescriptor.dontAlwaysRefreshCache();
        this.empDescriptor.dontOnlyRefreshCacheIfNewerVersion();
        this.empDescriptor.dontDisableCacheHits();

        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    protected void setup() {
        this.empDescriptor = getSession().getClassDescriptor(Employee.class);
        this.empDescriptor.alwaysRefreshCache();
        this.empDescriptor.onlyRefreshCacheIfNewerVersion();
        this.empDescriptor.disableCacheHits();

        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        this.employeeObject = (Employee)getSession().readObject(Employee.class, new ExpressionBuilder().get("firstName").equal("Charles"));
    }

    @Override
    public void test() {
        // Change the object
        this.firstName = "Chelmsford";
        employeeObject.setFirstName(firstName);

        // Refresh the employee using the one in the DB -- city should change
        getSession().readObject(employeeObject);
    }

    @Override
    protected void verify() {
        if (!employeeObject.getFirstName().equals(this.firstName)) {
            throw new TestErrorException("The onlyRefreshIfNew test failed on first name.");
        }
    }
}
