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
package org.eclipse.persistence.testing.tests.jpql;

import org.eclipse.persistence.testing.framework.TestSuite;

public class JPQLValidationTestSuite extends TestSuite {
    @Override
    public void addTests() {
        JPQLExceptionTest.addTestsTo(this);

        addTest(IdentifierTest.badIdentifierTest1());
        addTest(IdentifierTest.badIdentifierTest2());

        //validate that the parsing is done only once
        addTest(new ParseOnceTest());

        addTest(new ParameterNameMismatchTest());
        addTest(new OneToManyJoinOptimizationTest());
    }
}
