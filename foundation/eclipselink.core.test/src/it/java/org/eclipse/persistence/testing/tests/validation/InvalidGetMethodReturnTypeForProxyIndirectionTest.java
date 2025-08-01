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
package org.eclipse.persistence.testing.tests.validation;

import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.exceptions.EclipseLinkException;
import org.eclipse.persistence.exceptions.IntegrityChecker;
import org.eclipse.persistence.internal.indirection.ProxyIndirectionPolicy;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.testing.models.employee.domain.Employee;


//Created by Ian Reid
//Date: Mar 5, 2k3

public class InvalidGetMethodReturnTypeForProxyIndirectionTest extends ExceptionTest {

    public InvalidGetMethodReturnTypeForProxyIndirectionTest() {
        setDescription("This tests Invalid Get Method Return Type For Proxy Indirection (TL-ERROR 161)");
    }

    IntegrityChecker orgIntegrityChecker;
    Class<Employee> attributeType = org.eclipse.persistence.testing.models.employee.domain.Employee.class;
    Class<?>[] targetInterfaces = { InvalidAttributeTypeForProxyIndirectionTest.class };
    OneToManyMapping mapping = new OneToManyMapping();

    @Override
    protected void setup() {
        //setup need to remove setup null pointer thrown error
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(attributeType);
        mapping.setDescriptor(descriptor);
        mapping.setAttributeName("manager");

        expectedException = DescriptorException.invalidGetMethodReturnTypeForProxyIndirection(attributeType, targetInterfaces, mapping);
        orgIntegrityChecker = getSession().getIntegrityChecker();
        getSession().setIntegrityChecker(new IntegrityChecker());
        getSession().getIntegrityChecker().dontCatchExceptions();
    }

    @Override
    public void reset() {
        if (orgIntegrityChecker != null)
            getSession().setIntegrityChecker(orgIntegrityChecker);
    }

    @Override
    public void test() {
        ProxyIndirectionPolicy policy = new ProxyIndirectionPolicy();
        mapping.setReferenceClass(attributeType);
        mapping.addTargetForeignKeyFieldName("EMPLOYEE.MANAGER_ID", "EMPLOYEE.EMP_ID");
        policy.setMapping(mapping);
        try {
            policy.validateGetMethodReturnType(attributeType, getSession().getIntegrityChecker());
        } catch (EclipseLinkException exception) {
            caughtException = exception;
        }
    }
}
