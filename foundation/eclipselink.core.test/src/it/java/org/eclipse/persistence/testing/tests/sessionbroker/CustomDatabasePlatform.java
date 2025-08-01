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
//     dminsky - initial API and implementation
package org.eclipse.persistence.testing.tests.sessionbroker;

import org.eclipse.persistence.expressions.ExpressionOperator;
import org.eclipse.persistence.internal.helper.ClassConstants;
import org.eclipse.persistence.platform.database.DatabasePlatform;

import java.util.Vector;

/**
 * Database platform containing a custom platform function
 */
public class CustomDatabasePlatform extends DatabasePlatform {

    public static int OPERATOR_SELECTOR = 1234;

    public CustomDatabasePlatform() {
        super();
        initializePlatformOperators();
    }

    @Override
    protected void initializePlatformOperators() {
        super.initializePlatformOperators();

        // Create user-defined function
        ExpressionOperator operator = new ExpressionOperator();
        operator.setSelector(OPERATOR_SELECTOR);

        Vector<String> v = new Vector<>();
        v.add("upper(");
        v.add(")");

        operator.printsAs(v);
        operator.bePostfix();
        operator.setNodeClass(ClassConstants.FunctionExpression_Class);

        addOperator(operator);
    }

}
