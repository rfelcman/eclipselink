/*
 * Copyright (c) 1998, 2021 Oracle and/or its affiliates. All rights reserved.
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

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.exceptions.EclipseLinkException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.mappings.ManyToManyMapping;


//Created by Ian Reid
//Date: Feb 27, 2k3

public class InvalidDataModificationEventCodeTest_ManyToManyMapping extends ExceptionTest {

    public InvalidDataModificationEventCodeTest_ManyToManyMapping() {
        setDescription("This tests Invalid Data Modification Event Code (ManyToManyMapping) (TL-ERROR 36)");
    }

    @Override
    protected void setup() {
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();

        expectedException = DescriptorException.invalidDataModificationEventCode(null, null);
    }

    @Override
    public void test() {
        try {
            ManyToManyMapping aMapping = new ManyToManyMapping();

            String[] args = { "BAD_CODE", "BAD_CODE", "BAD_CODE" };
            aMapping.performDataModificationEvent(args, (AbstractSession)getSession());
        } catch (EclipseLinkException exception) {
            caughtException = exception;
        }
    }

}
