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
package org.eclipse.persistence.testing.tests.remote;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.sessions.server.ClientSession;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.testing.tests.queries.inmemory.QueryCacheHitUndefinedAndDescriptorDisabledTest;

import java.io.StringWriter;

//When query's shouldMaintainCache is undefined and descriptor's shouldDisableCacheHits
//and shouldDisableCacheHitsOnRemote are false, cache is checked and the same object is returned.

public class QueryCacheHitUndefinedAndDescriptorDisabledOnRemoteTest extends QueryCacheHitUndefinedAndDescriptorDisabledTest {
    protected boolean orgDisableCacheHitsOnRemote;
    protected ClassDescriptor serverDescriptor;
    protected boolean orgServerDisableCacheHits;
    protected ServerSession serverSession;

    public QueryCacheHitUndefinedAndDescriptorDisabledOnRemoteTest() {
        setDescription("Test when cache hit is undefined in query and disabled in descriptor, cache is not checked");
    }

    @Override
    protected void setup() {
        super.setup();
        orgDisableCacheHitsOnRemote = descriptor.shouldDisableCacheHitsOnRemote();
        descriptor.setShouldDisableCacheHitsOnRemote(true);

        serverSession = ((ClientSession)RemoteModel.getServerSession()).getParent();

        serverDescriptor = serverSession.getDescriptor(org.eclipse.persistence.testing.models.employee.domain.Employee.class);
        orgServerDisableCacheHits = serverDescriptor.shouldDisableCacheHits();
        serverDescriptor.setShouldDisableCacheHits(true);

        oldLog = serverSession.getSessionLog();
        tempStream = new StringWriter();
        serverSession.setLog(tempStream);
        serverSession.setLogLevel(SessionLog.FINE);
    }

    @Override
    public void reset() {
        descriptor.setShouldDisableCacheHitsOnRemote(orgDisableCacheHitsOnRemote);

        serverDescriptor.setShouldDisableCacheHits(orgServerDisableCacheHits);
        serverSession.setSessionLog(oldLog);

        super.reset();
    }
}
