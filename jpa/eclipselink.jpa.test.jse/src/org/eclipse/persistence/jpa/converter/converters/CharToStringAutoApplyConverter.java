/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     12/05/2016-2.6 Jody Grassel
 *       - 443546: Converter autoApply does not work for primitive types
 ******************************************************************************/

package org.eclipse.persistence.jpa.converter.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class CharToStringAutoApplyConverter implements AttributeConverter<Character, String> {
    public static boolean convertToDatabaseTriggered = false;
    public static boolean convertToEntityTriggered = false;
    
    public static Character ctdcVal = null;
    public static String cteaVal = null;
    
    public static void reset() {
        convertToDatabaseTriggered = false;
        convertToEntityTriggered = false;
        ctdcVal = null;
        cteaVal = null;
    }
    
    @Override
    public String convertToDatabaseColumn(Character attribute) {
        convertToDatabaseTriggered = true;
        ctdcVal = attribute;
        return Character.toString(attribute);
    }

    @Override
    public Character convertToEntityAttribute(String dbData) {
        convertToEntityTriggered = true;
        cteaVal = dbData;
        return Character.valueOf(dbData.charAt(0));
    }

}