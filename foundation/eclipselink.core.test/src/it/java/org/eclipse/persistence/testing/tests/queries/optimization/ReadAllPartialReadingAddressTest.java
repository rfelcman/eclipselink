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
package org.eclipse.persistence.testing.tests.queries.optimization;

import org.eclipse.persistence.testing.framework.ReadAllTest;
import org.eclipse.persistence.testing.models.employee.domain.Address;

public class ReadAllPartialReadingAddressTest extends ReadAllTest {
    public String attribute;

    public ReadAllPartialReadingAddressTest(int size, String attribute) {
        super(Address.class, size);
        setName("PartialReadingAddressTest" + attribute);
        this.attribute = attribute;
    }

    public ReadAllPartialReadingAddressTest(Class<?> referenceClass, int originalObjectsSize) {
        super(referenceClass, originalObjectsSize);
    }
}
