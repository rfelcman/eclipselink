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
package org.eclipse.persistence.testing.tests.validation;

import org.eclipse.persistence.exceptions.ConversionException;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;

import java.util.Hashtable;


/**
 * @author Guy Pelletier
 * @version 1.0
 */
public class ConversionExceptionFromMappingTest extends AutoVerifyTestCase {
    ConversionException m_exception;

    public ConversionExceptionFromMappingTest() {
        setDescription("Ensures that the correct ConversionException is thrown.");
    }

    @Override
    public void reset() {
    }

    @Override
    public void setup() throws Exception {
    }

    @Override
    public void test() throws Exception {
        DirectToFieldMapping map = new DirectToFieldMapping();
        map.setAttributeName("foobar");
        map.setAttributeClassification(Hashtable.class);

        try {
            map.getObjectValue("foobar", getSession());
        } catch (ConversionException e) {
            m_exception = e;
        }
    }

    @Override
    public void verify() throws Exception {
        if (m_exception.getErrorCode() != ConversionException.COULD_NOT_BE_CONVERTED_EXTENDED) {
            throw new TestErrorException("Invalid conversion exception was thrown");
        }
    }
}
