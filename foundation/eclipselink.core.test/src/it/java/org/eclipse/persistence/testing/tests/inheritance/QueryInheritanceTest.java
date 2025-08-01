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
package org.eclipse.persistence.testing.tests.inheritance;

import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TestProblemException;
import org.eclipse.persistence.testing.models.inheritance.Dog;

import java.util.Vector;

public class QueryInheritanceTest extends TestCase {


    public QueryInheritanceTest() {
        setDescription("Verifies that Named queries are inheritedd correctly");
    }

    @Override
    public void reset() {
        getAbstractSession().rollbackTransaction();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void setup() {
        getAbstractSession().beginTransaction();
    }

    @Override
    public void test() {
        Vector vector = (Vector)getSession().executeQuery("InheritanceReadAll", Dog.class);
        if (vector.isEmpty()){
            throw new TestProblemException("No Dogs found at all");
        }
        for (Object o : vector) {
            if (!(o instanceof Dog)) {
                throw new TestErrorException("Failed to inherit query correctly");
            }
        }
    }

}
