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
package org.eclipse.persistence.testing.tests.queries;

/**
 * Test the following circumstance:
 *
 * Settings:
 * Maintain Cache: on
 * Refresh: on
 *
 * Test that cached private object members are refreshed from the db on a read
 */
public class CascadingPrivateCacheTest extends CascadingTest {

    public CascadingPrivateCacheTest() {
    }

    @Override
    protected void setTestConfiguration() {
        setConfiguration("CascadingPrivateCacheTest", true, CASCADE_PRIVATE);
    }
}
