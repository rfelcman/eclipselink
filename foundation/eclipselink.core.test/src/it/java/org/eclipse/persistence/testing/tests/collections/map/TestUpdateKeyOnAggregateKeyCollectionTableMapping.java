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
//     tware - initial implementation
package org.eclipse.persistence.testing.tests.collections.map;

import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.collections.map.AggregateEntityMapHolder;
import org.eclipse.persistence.testing.models.collections.map.AggregateMapKey;
import org.eclipse.persistence.testing.models.collections.map.EntityMapValue;

import java.util.List;

public class TestUpdateKeyOnAggregateKeyCollectionTableMapping extends TestCase {

    private AggregateEntityMapHolder holder = null;

    @Override
    public void setup(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        holder = new AggregateEntityMapHolder();
        EntityMapValue value = new EntityMapValue();
        value.setId(1);
        AggregateMapKey key = new AggregateMapKey();
        key.setKey(11);
        holder.addAggregateToEntityMapItem(key, value);


        EntityMapValue value2 = new EntityMapValue();
        value2.setId(2);
        key = new AggregateMapKey();
        key.setKey(22);
        holder.addAggregateToEntityMapItem(key, value2);
        uow.registerObject(holder);
        uow.registerObject(value);
        uow.registerObject(value2);
        uow.commit();
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void test(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        holder = (AggregateEntityMapHolder)uow.readObject(holder);

        for (Object o : holder.getAggregateToEntityMap().keySet()) {
            AggregateMapKey key = (AggregateMapKey) o;
            key.setKey(key.getKey() + 1);
        }
        uow.commit();
    }

    @Override
    public void verify(){
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
        holder = (AggregateEntityMapHolder)getSession().readObject(holder);
        if (holder == null){
            throw new TestErrorException("AggregateKeyMapHolder could not be read.");
        }
        if (holder.getAggregateToEntityMap().size() != 2){
            throw new TestErrorException("Incorrect Number of MapEntityValues was read.");
        }

        AggregateMapKey mapKey = new AggregateMapKey();
        mapKey.setKey(11);
        EntityMapValue value = (EntityMapValue)holder.getAggregateToEntityMap().get(mapKey);
        if (value != null){
            throw new TestErrorException("EntityMapValue 11 not changed.");
        }

        mapKey = new AggregateMapKey();
        mapKey.setKey(12);
        value = (EntityMapValue)holder.getAggregateToEntityMap().get(mapKey);
        if (value == null){
            throw new TestErrorException("EntityMapValue 11 not changed to 12.");
        }

        mapKey = new AggregateMapKey();
        mapKey.setKey(22);
        value = (EntityMapValue)holder.getAggregateToEntityMap().get(mapKey);
        if (value != null){
            throw new TestErrorException("EntityMapValue 22 not changed.");
        }

        mapKey = new AggregateMapKey();
        mapKey.setKey(23);
        value = (EntityMapValue)holder.getAggregateToEntityMap().get(mapKey);
        if (value == null){
            throw new TestErrorException("EntityMapValue 22 not changed to 23.");
        }

    }

    @Override
    public void reset(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        uow.deleteObject(holder);
        List keys = uow.readAllObjects(EntityMapValue.class);
        uow.deleteAllObjects(keys);
        uow.commit();
    }

}
