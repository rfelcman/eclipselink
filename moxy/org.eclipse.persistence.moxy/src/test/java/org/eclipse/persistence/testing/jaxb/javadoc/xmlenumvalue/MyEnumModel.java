/*
 * Copyright (c) 2011, 2024 Oracle and/or its affiliates. All rights reserved.
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
//     Praba Vijayaratnam - 2.4 - initial implementation
package org.eclipse.persistence.testing.jaxb.javadoc.xmlenumvalue;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyEnumModel {

    private MyEnum e;

    public void setMyEnum(MyEnum myEnum) {
        e = myEnum;

    }

    public MyEnum getMyEnum() {
        return e;
    }

    public boolean equals(Object object) {
        MyEnumModel example = ((MyEnumModel) object);
        return example.e.equals(this.e);
    }
}
