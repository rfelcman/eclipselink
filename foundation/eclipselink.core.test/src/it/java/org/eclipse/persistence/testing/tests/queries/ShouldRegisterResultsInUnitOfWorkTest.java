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

import org.eclipse.persistence.descriptors.WrapperPolicy;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

import java.util.Enumeration;
import java.util.Vector;

/**
 * <b>Purpose:</b>Tests conform without registering with a wrapper policy.
 * @author Stephen McRitchie
 * @bug 2612601
 */
public class ShouldRegisterResultsInUnitOfWorkTest extends ConformResultsInUnitOfWorkTest {
    Employee newEmp;
    Employee deletedEmp;
    Employee registeredEmp;
    boolean descriptorSetting;

    public ShouldRegisterResultsInUnitOfWorkTest(boolean descriptorSetting) {
        setShouldUseWrapperPolicy(true);
        this.descriptorSetting = descriptorSetting;
        if (descriptorSetting) {
            setName("DescriptorShouldRegisterResultsInUnitOfWorkTest");
        }
    }

    @Override
    public void buildConformQuery() {
        conformedQuery = new ReadAllQuery(Employee.class);
        conformedQuery.conformResultsInUnitOfWork();
        if (!descriptorSetting) {
            conformedQuery.setShouldRegisterResultsInUnitOfWork(false);
        }
    }

    @Override
    public void prepareTest() {
        // load in a deleted object, and 1 registered object.
        registeredEmp = (Employee)unitOfWork.readObject(Employee.class);
        ReadObjectQuery query = new ReadObjectQuery(Employee.class);
        ExpressionBuilder builder = new ExpressionBuilder();
        query.setSelectionCriteria(builder.notEqual(registeredEmp));
        deletedEmp = (Employee)unitOfWork.executeQuery(query);
        unitOfWork.deleteObject(deletedEmp);

        if (descriptorSetting) {
            getSession().getDescriptor(Employee.class).setShouldRegisterResultsInUnitOfWork(false);
        }
    }

    @Override
    public void test() {
        result = unitOfWork.executeQuery(conformedQuery);
    }

    @Override
    public void verify() {
        try {
            // Check that no employees were registered and put in the UOW cache.
            Vector registeredEmployees = (unitOfWork).getIdentityMapAccessor().getAllFromIdentityMap(null, Employee.class, null, null);
            if (registeredEmployees.size() != 2) {
                throw new TestErrorException("Should be only two employees registered in UOW cache, not: " + registeredEmployees.size());
            }
            Vector employees = (Vector)result;
            if (employees.size() != 11) {
                throw new TestErrorException("11 employees should be returned by the query, not: " + employees.size());
            }
            WrapperPolicy policy = getSession().getDescriptor(Employee.class).getWrapperPolicy();
            UnitOfWorkImpl uow = (UnitOfWorkImpl)unitOfWork;
            Object unwrappedRegistered = policy.unwrapObject(registeredEmp, uow);
            Object unwrappedDeleted = policy.unwrapObject(deletedEmp, uow);
            for (Enumeration enumtr = employees.elements(); enumtr.hasMoreElements();) {
                Object next = policy.unwrapObject(enumtr.nextElement(), uow);
                if (unwrappedRegistered == next) {
                    registeredEmp = null;
                } else if (unwrappedDeleted == next) {
                    deletedEmp = null;
                }
            }

            // Check that unwrapping triggered all the objects to be cloned.
            registeredEmployees = (unitOfWork).getIdentityMapAccessor().getAllFromIdentityMap(null, Employee.class, null, null);
            if (registeredEmployees.size() != 12) {
                throw new TestErrorException("Should now be 12 employees registered in UOW cache, not: " + registeredEmployees.size());
            }
            if (registeredEmp != null) {
                throw new TestErrorException("The registered employee was not included in the result.");
            } else if (deletedEmp == null) {
                throw new TestErrorException("The deleted employee was included in the result.");
            }
        } finally {
            unitOfWork.release();
            newEmp = null;
            registeredEmp = null;
            deletedEmp = null;
        }
    }

    @Override
    public void reset() {
        if (descriptorSetting) {
            getSession().getDescriptor(Employee.class).setShouldRegisterResultsInUnitOfWork(true);
        }
        super.reset();
    }
}
