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

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.exceptions.EclipseLinkException;
import org.eclipse.persistence.exceptions.IntegrityChecker;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.TransformationMapping;
import org.eclipse.persistence.sessions.DatabaseSession;


//Created by Ian Reid
//Date: Feb 17, 2k3
//uses class org.eclipse.persistence.testing.tests.validation.EmployeeWithProblems
//(not really needed could just use normal Employee)

public class NoAttributeTransformationMethodTest extends ExceptionTest {
    public NoAttributeTransformationMethodTest() {
        super();
        setDescription("This tests No Attribute Transformation Method (TL-ERROR 49) " + " uses EmployeeWithProblems class");
    }

    @Override
    protected void setup() {
        expectedException = DescriptorException.noAttributeTransformationMethod(null);
        orgDescriptor = getSession().getDescriptor(org.eclipse.persistence.testing.tests.validation.EmployeeWithProblems.class);
        orgIntegrityChecker = getSession().getIntegrityChecker();
        getSession().setIntegrityChecker(new IntegrityChecker());
        getSession().getIntegrityChecker().dontCatchExceptions();
    }

    ClassDescriptor orgDescriptor;
    IntegrityChecker orgIntegrityChecker;

    @Override
    public void reset() {
        getSession().getDescriptors().remove(org.eclipse.persistence.testing.tests.validation.EmployeeWithProblems.class);
        if (orgDescriptor != null) {
            ((DatabaseSession)getSession()).addDescriptor(orgDescriptor);
        }
        if (orgIntegrityChecker != null) {
            getSession().setIntegrityChecker(orgIntegrityChecker);
        }
    }

    @Override
    public void test() {
        try {
            ((DatabaseSession)getSession()).addDescriptor(descriptor());
        } catch (EclipseLinkException exception) {
            caughtException = exception;
        }
    }

    public RelationalDescriptor descriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();

        //the following helps cause the correct error to occure.
        descriptor.setJavaClass(org.eclipse.persistence.testing.tests.validation.EmployeeWithProblems.class);

        descriptor.addTableName("EMPLOYEE");
        descriptor.addTableName("ADDRESS");
        descriptor.addPrimaryKeyFieldName("EMPLOYEE.EMP_ID");

        // Descriptor properties.
        descriptor.useFullIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.useRemoteFullIdentityMap();
        descriptor.setRemoteIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("EMP_ID");
        descriptor.setSequenceNumberName("EMP_SEQ");


        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("EMPLOYEE.EMP_ID");
        descriptor.addMapping(idMapping);


        TransformationMapping normalHoursMapping = new TransformationMapping();
        normalHoursMapping.setAttributeName("normalHours");
        //if the following is missing then the correct error will occure.
        //    normalHoursMapping.setAttributeTransformation("buildNormalHours");

        normalHoursMapping.addFieldTransformation("EMPLOYEE.START_TIME", "getStartTime");
        normalHoursMapping.addFieldTransformation("EMPLOYEE.END_TIME", "getEndTime");
        descriptor.addMapping(normalHoursMapping);

        return descriptor;
    }
}
