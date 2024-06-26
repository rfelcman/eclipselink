/*
 * Copyright (c) 1998, 2024 Oracle and/or its affiliates. All rights reserved.
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
// Denise Smith - September 10 /2009
package org.eclipse.persistence.testing.jaxb.xmladapter.packagelevel.adapters;

import jakarta.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ClassB createClassB() {
        return new ClassB();
    }

    public ClassA createClassA() {
        return new ClassA();
    }

    public TestObject createTestObject() {
        return new TestObject();
    }
}
