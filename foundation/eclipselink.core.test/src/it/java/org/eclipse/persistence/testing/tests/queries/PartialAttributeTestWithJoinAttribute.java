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
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 * Bug 5200555
 * A partial attribute query where the partial attribute is also joined should
 * not result in a null pointer exception.
 */
@SuppressWarnings("deprecation")
public class PartialAttributeTestWithJoinAttribute extends TestCase {
    protected int joinFetch;

    public PartialAttributeTestWithJoinAttribute() {
        setDescription("A partial attribute query where the partial attribute is also joined should not result in a null pointer exception.");
    }

    @Override
    public void setup() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        ClassDescriptor descriptor = getSession().getClassDescriptor(Employee.class);
        OneToOneMapping mapping = (OneToOneMapping)descriptor.getMappingForAttributeName("address");
        joinFetch = mapping.getJoinFetch();
        mapping.useInnerJoinFetch();
        descriptor.reInitializeJoinedAttributes();
    }

    @Override
    public void reset() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        ClassDescriptor descriptor = getSession().getClassDescriptor(Employee.class);
        OneToOneMapping mapping = (OneToOneMapping)descriptor.getMappingForAttributeName("address");
        mapping.setJoinFetch(joinFetch);
        descriptor.reInitializeJoinedAttributes();
    }

    @Override
    public void test() {
        ReadAllQuery query = new ReadAllQuery(Employee.class);
        query.dontMaintainCache();
        query.addPartialAttribute("address");
        try {
            getSession().executeQuery(query);
        } catch (RuntimeException exception) {

            throw exception;
        }
    }
}
