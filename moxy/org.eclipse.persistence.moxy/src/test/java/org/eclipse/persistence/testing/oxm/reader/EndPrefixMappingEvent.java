/*
 * Copyright (c) 2011, 2020 Oracle and/or its affiliates. All rights reserved.
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
//     Blaise Doughan - 2.2 - initial implementation
package org.eclipse.persistence.testing.oxm.reader;

public class EndPrefixMappingEvent extends Event {

    private String prefix;

    public EndPrefixMappingEvent(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public boolean equals(Object object) {
        if(!super.equals(object)) {
            return false;
        }
        EndPrefixMappingEvent testEvent = (EndPrefixMappingEvent) object;
        return equals(prefix, testEvent.getPrefix());
    }

}
