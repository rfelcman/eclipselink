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
package org.eclipse.persistence.testing.tests.identitymaps;

import org.eclipse.persistence.internal.identitymaps.CacheIdentityMap;
import org.eclipse.persistence.internal.identitymaps.IdentityMap;
import org.eclipse.persistence.internal.identitymaps.NoIdentityMap;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Register multiple objects in an identity map.
 * <p>
 * FullIdentityMap should store and correctly maintain identity for all objects.
 * NoIdentityMap should not store or maintain identity for any objects.
 * Fixed size identity maps should correctly store all objects,
 * but only retain their specified fixed size number of objects.
*/
public class MultipleRegisterTest extends TestCase {
    IdentityMap map;
    Vector primaryKeys;
    Vector originalObjects;
    Vector retrievedObjects;

    public MultipleRegisterTest(IdentityMap map, Vector keys, Vector testObjects) {
        this.map = map;
        primaryKeys = keys;
        originalObjects = testObjects;
    }

    @Override
    public void test() {
        for (int index = 0; index < primaryKeys.size(); index++) {
            Vector key = (Vector)primaryKeys.get(index);
            Object value = originalObjects.get(index);
            map.put(key, value, null, 0);
        }

        retrievedObjects = new Vector();
        for (int index = 0; index < primaryKeys.size(); index++) {
            Vector key = (Vector)primaryKeys.get(index);
            Object value = map.get(key);
            retrievedObjects.add(value);
        }
    }

    @Override
    public void verify() {
        if (map instanceof NoIdentityMap) {
            verify((NoIdentityMap)map);
        } else if (map instanceof CacheIdentityMap) {
            verifyFixedSize();
        } else {
            if (originalObjects.size() != retrievedObjects.size()) {
                throw new TestErrorException(originalObjects.size() + " objects were registered " + "in the identity map but only " + retrievedObjects.size() + " were retrieved.");
            }

            Hashtable mismatches = verifyObjectIdentity();
            if (!mismatches.isEmpty()) {
                throw new TestErrorException(mismatches.size() + " mismatches occurred while" + " retrieving objects from a full identity map. The mismatches are " + mismatches + ".");
            }
        }
    }

    public void verify(NoIdentityMap map) {
        if (map.getSize() != 0) {
            throw new TestErrorException("Objects were registered in a NoIdentityMap " + "but there are " + map.getSize() + " objects in the map.");
        }

        Enumeration values = retrievedObjects.elements();
        while (values.hasMoreElements()) {
            if (values.nextElement() != null) {
                throw new TestErrorException("A non-null value was retrieved from a no identity map.");
            }
        }
    }

    public void verifyFixedSize() {
        if (!(map.getSize() <= map.getMaxSize())) {
            throw new TestErrorException("Fixed size identity map " + map + " contains " + map.getSize() + " objects. " + "The specified maximum size for this map is " + map.getMaxSize() + ".");
        }

        Hashtable mismatches = verifyObjectIdentity();
        int expectedMismatches = originalObjects.size() - map.getMaxSize();
        if (mismatches.size() != expectedMismatches) {
            throw new TestErrorException(mismatches.size() + "mismatches occurred while" + "retrieving objects from the identity map. " + expectedMismatches + "were expected. The mismatches were " + mismatches + ".");
        }
    }

    public Hashtable verifyObjectIdentity() {
        Hashtable mismatches = new Hashtable();

        for (int index = 0; index < originalObjects.size(); index++) {
            Object original = originalObjects.get(index);
            Object retrieved = retrievedObjects.get(index);
            if (original != retrieved) {
                mismatches.put(original, retrieved);
            }
        }

        return mismatches;
    }
}
