/*******************************************************************************
 * Copyright (c) 1998, 2015 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Denise Smith - August 2013
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.xsitype;

import org.eclipse.persistence.testing.jaxb.JAXBWithJSONTestCases;

public class TypeAttributeInheritanceTestCases extends JAXBWithJSONTestCases {
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/jaxb/xsitype/child.xml";
    private final static String JSON_RESOURCE = "org/eclipse/persistence/testing/jaxb/xsitype/child.json";
    private final static String XML_WRITE_RESOURCE = "org/eclipse/persistence/testing/jaxb/xsitype/childWrite.xml";
    private final static String JSON_WRITE_RESOURCE = "org/eclipse/persistence/testing/jaxb/xsitype/childWrite.json";

    public TypeAttributeInheritanceTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setControlJSON(JSON_RESOURCE);
        setWriteControlDocument(XML_WRITE_RESOURCE);
        setWriteControlJSON(JSON_WRITE_RESOURCE);
        Class[] classes = new Class[2];
        classes[0] = Root.class;
        classes[1] = Child.class;
        setClasses(classes);
    }

    protected Object getControlObject() {
        return new Child();
    }

}