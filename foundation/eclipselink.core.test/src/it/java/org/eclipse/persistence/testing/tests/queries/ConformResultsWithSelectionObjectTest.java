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

import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.legacy.Employee;

public class ConformResultsWithSelectionObjectTest extends ConformResultsInUnitOfWorkTest {
    Object selectionObject;

    @Override
    public void buildConformQuery() {
        conformedQuery = new ReadObjectQuery();
        ((ReadObjectQuery)conformedQuery).setSelectionObject(selectionObject);
        conformedQuery.conformResultsInUnitOfWork();
    }

    @Override
    public void prepareTest() {
        selectionObject = new Employee();
        ((Employee)selectionObject).firstName = "Bobert";
        ((Employee)selectionObject).lastName = "Schmit";
        unitOfWork.registerObject(selectionObject);
    }

    @Override
    public void verify() {
        if (result == null) {
            throw new TestErrorException("object existed in unit of work but not returned in query");
        }
    }
}
