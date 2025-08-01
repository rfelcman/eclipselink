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
package org.eclipse.persistence.testing.tests.queries.optimization;

import org.eclipse.persistence.testing.framework.ReadAllTest;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;
import org.eclipse.persistence.testing.models.employee.domain.PhoneNumber;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class tests the batch reading feature.
 */
public class ReadAllPartialReadingTest extends ReadAllTest {
    public String attribute;

    public ReadAllPartialReadingTest(int size, String attribute) {
        super(Employee.class, size);
        setName("PartialReadingTest" + attribute);
        this.attribute = attribute;
    }

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void verify() throws Exception {
        super.verify();

        org.eclipse.persistence.internal.queries.ContainerPolicy cp = getQuery().getContainerPolicy();

        if (cp.isCursorPolicy()) {
            for (Enumeration enumtr = ((Vector)objectsFromDatabase).elements();
                     enumtr.hasMoreElements();) {
                verifyEmployee((Employee)enumtr.nextElement());
            }
        } else {
            for (Object iter = cp.iteratorFor(objectsFromDatabase); cp.hasNext(iter);) {
                verifyEmployee((Employee)cp.next(iter, getAbstractSession()));
            }
        }
    }

    protected void verifyEmployee(Employee employee) {
        if (!employee.getLastName().isEmpty()) {
            throw new TestErrorException("last name was read.");
        }
        if (this.attribute.equals("firstName") && employee.getFirstName().isEmpty()) {
            throw new TestErrorException("first name was not read.");
        }
        if (this.attribute.equals("address") && (employee.getAddress() == null)) {
            throw new TestErrorException("address was not read.");
        }
        if (this.attribute.equals("address") && (employee.getPeriod() == null)) {
            throw new TestErrorException("period was not read.");
        }
        if (this.attribute.equals("address") && (employee.getAddress().getCity() == null)) {
            throw new TestErrorException("city was not read.");
        }
        if (this.attribute.equals("address") && (employee.getPeriod().getStartDate() == null)) {
            throw new TestErrorException("start date was not read.");
        }
        if (this.attribute.equals("city") && (employee.getAddress() == null)) {
            throw new TestErrorException("address was not read.");
        }
        if (this.attribute.equals("city") && (employee.getAddress().getCity() == "")) {
            throw new TestErrorException("address city was not read.");
        }
        if (this.attribute.equals("city") && (employee.getAddress().getCountry() != "")) {
            throw new TestErrorException("address country was read.");
        }
        if (this.attribute.equals("areaCode")) {
            if (!employee.phoneNumbers.isInstantiated()) {
                throw new TestErrorException("phone value holder not instantiated.");
            }
            if (((PhoneNumber)employee.getPhoneNumbers().get(0)).getAreaCode() == "") {
                throw new TestErrorException("phone area-code was not read.");
            }
        }
    }
}
