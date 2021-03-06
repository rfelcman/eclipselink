/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Blaise Doughan - 2.5.1 - initial implementation
package org.eclipse.persistence.testing.jaxb.typevariable;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="root")
public class ExtendedList2Root {

    public ExtendedList2 foo;

    @Override
    public boolean equals(Object obj) {
        if(null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        ExtendedList2Root test = (ExtendedList2Root) obj;
        if(!foo.equals(test.foo)) {
            return false;
        }
        return true;
    }

}
