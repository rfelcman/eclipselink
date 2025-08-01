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
package org.eclipse.persistence.testing.tests.events;

import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.events.Address;
import org.eclipse.persistence.testing.models.events.EmailAccount;
import org.eclipse.persistence.testing.models.events.Phone;

public class BuildOnRefreshEventHookTest extends EventHookTestCase {
    @Override
    public void setup() {
        super.setup();
        getDatabaseSession().writeObject(getEmailAccount());
        getDatabaseSession().writeObject(getPhoneNumber());
        getDatabaseSession().writeObject(getAddress());
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
    }

    @Override
    public void test() {
        setEmailAccount((EmailAccount)getSession().refreshObject(getEmailAccount()));
        setPhoneNumber((Phone)getSession().refreshObject(getPhoneNumber()));
        setAddress((Address)getSession().refreshObject(getAddress()));
    }

    @Override
    protected void verify() {
        if (!getEmailAccount().postBuildExecuted) {
            throw new TestErrorException("Event hook failed. The post build method on " + getEmailAccount() + " failed to execute.");
        }

        if (!getPhoneNumber().postBuildExecuted) {
            throw new TestErrorException("Event hook failed. The post build method on " + getPhoneNumber() + " failed to execute.");
        }

        if (!getAddressListener().postBuildExecuted) {
            throw new TestErrorException("Event hook failed. The post build method on " + getAddressListener() + " failed to execute.");
        }
    }
}
