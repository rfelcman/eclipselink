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
package org.eclipse.persistence.testing.tests.interfaces;

import org.eclipse.persistence.exceptions.EclipseLinkException;
import org.eclipse.persistence.testing.framework.ReadAllTest;

public class ReadAllBatchTest extends ReadAllTest {
    public Exception storedException;
    public int expectedExceptionCode;

    public ReadAllBatchTest(Class<?> aClass, int numberOfObjects) {
        super(aClass, numberOfObjects);
        setDescription("Test that the correct exception is thrown when a batch query across a variable 1:1 mapping is attempted. Maybe this will be supported someday.");
        storedException = null;
        expectedExceptionCode = org.eclipse.persistence.exceptions.QueryException.BATCH_READING_NOT_SUPPORTED;
    }

    @Override
    public void reset() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        storedException = null;
    }

    @Override
    public void setup() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        storedException = null;
    }

    @Override
    public void test() {
        try {
            super.test();
        } catch (Exception e) {
            storedException = e;
        }
    }

    @Override
    public void verify() {
        if (storedException == null) {
            throw new org.eclipse.persistence.testing.framework.TestErrorException("NO EXCEPTION THROWN!!!  EXPECTING QueryException");
        }
        if (storedException instanceof EclipseLinkException) {
            if (((EclipseLinkException)storedException).getErrorCode() == expectedExceptionCode) {
                return;
            }
        }
        throw new org.eclipse.persistence.testing.framework.TestErrorException("WRONG EXCEPTION THROWN!!!  EXPECTING QueryException");
    }
}
