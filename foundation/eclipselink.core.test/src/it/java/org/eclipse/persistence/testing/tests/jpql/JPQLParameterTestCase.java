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

import org.eclipse.persistence.testing.framework.TestErrorException;

import java.util.Vector;

public class JPQLParameterTestCase extends JPQLTestCase {
    private Vector arguments;
    private Vector argumentNames;

    @Override
    public void test() {
        try {
            populateQuery();
            getSession().logMessage("Running EJBQL -> " + getEjbqlString());
            setReturnedObjects(getSession().executeQuery(getQuery(), getArguments()));
        } catch (Exception e) {
            throw new TestErrorException(e.getMessage());
        }
    }

    @Override
    public boolean hasArguments() {
        return (arguments != null) && (!arguments.isEmpty());
    }

    @Override
    public Vector getArguments() {
        return arguments;
    }

    public Vector getArgumentNames() {
        return argumentNames;
    }

    //populate the arguments of the query
    public void populateQuery() {
        if (getArgumentNames() == null) {
            return;
        }
        getQuery().setArguments(getArgumentNames());
    }

    @Override
    public void setArguments(Vector theArguments) {
        this.arguments = theArguments;
    }

    public void setArgumentNames(Vector theArgumentNames) {
        this.argumentNames = theArgumentNames;
    }
}
