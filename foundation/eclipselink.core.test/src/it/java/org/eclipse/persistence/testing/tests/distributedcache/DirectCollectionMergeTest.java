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
package org.eclipse.persistence.testing.tests.distributedcache;

import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

public class DirectCollectionMergeTest extends DistributedCacheMergeTest {
    public DirectCollectionMergeTest() {
        super();
    }

    @Override
    protected void modifyCollection(UnitOfWork uow, Object objectToModify) {
        ((Employee)objectToModify).addResponsibility((String)newItemForCollection());
    }

    @Override
    protected int getCollectionSize(Object rootObject) {
        return ((Employee)rootObject).getResponsibilitiesList().size();
    }
/*
    protected Object buildOriginalObject() {
        //return "one more thing to do";
    }
        */

         @Override
         protected Object buildOriginalObject() {
        Employee emp = new Employee();
        emp.setFirstName("Sally");
        emp.setLastName("Hamilton");
        emp.setFemale();
        return emp;
    }

    protected Object newItemForCollection() {
      return "one more thing to do";
    }
}
