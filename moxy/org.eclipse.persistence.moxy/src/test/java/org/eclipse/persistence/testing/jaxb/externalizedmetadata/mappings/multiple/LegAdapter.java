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
// dmccann - November 23/2010 - 2.2 - Initial implementation
package org.eclipse.persistence.testing.jaxb.externalizedmetadata.mappings.multiple;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

public final class LegAdapter extends XmlAdapter<String, String> {
    public LegAdapter() {}

    @Override
    public String marshal(String arg0) throws Exception {
        return "CAD$";
    }

    @Override
    public String unmarshal(String arg0) throws Exception {
        return null;
    }
}
