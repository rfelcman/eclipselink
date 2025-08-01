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
package org.eclipse.persistence.testing.tests.mapping;

import org.eclipse.persistence.internal.sessions.UnitOfWorkImpl;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.mapping.Computer;
import org.eclipse.persistence.testing.models.mapping.Monitor;

public class BidirectionalUOWInsertTest extends AutoVerifyTestCase {
    UnitOfWork unitOfWork;

    public BidirectionalUOWInsertTest() {
        setDescription("Test bidirectional insert in a unit of work.");
    }

    @Override
    public void reset() {
        rollbackTransaction();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    protected void setup() {
        beginTransaction();
    }

    @Override
    protected void test() {
        unitOfWork = getSession().acquireUnitOfWork();

        Computer computer = new Computer();
        Monitor monitor = Monitor.example1();
        computer.setDescription("IBM PS2");
        computer.notMacintosh();
        computer.setMonitor(monitor);
        computer.getMonitor().setComputer(computer);
        computer.setSerialNumber("1124345-1876212-2");

        unitOfWork.registerObject(computer);
        unitOfWork.commit();
    }

    @Override
    protected void verify() {
        int size = ((UnitOfWorkImpl) unitOfWork).getCloneMapping().size();
        if (size != 2) {
            throw new TestErrorException("cloneMapping hashtable contains " + size + " elements, should contain 2.");
        }
    }
}
