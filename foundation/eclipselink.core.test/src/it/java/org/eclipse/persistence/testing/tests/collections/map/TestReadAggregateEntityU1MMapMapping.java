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

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.indirection.IndirectMap;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.UnidirectionalOneToManyMapping;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.eclipse.persistence.testing.framework.TestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.collections.map.AggregateEntityU1MMapHolder;
import org.eclipse.persistence.testing.models.collections.map.AggregateMapKey;
import org.eclipse.persistence.testing.models.collections.map.EntityMapValue;

import java.util.List;

public class TestReadAggregateEntityU1MMapMapping extends TestCase {

    protected List holders = null;
    protected int fetchJoinRelationship = 0;
    protected int oldFetchJoinValue = 0;
    protected OneToManyMapping mapping = null;
    protected Expression holderExp;

    public TestReadAggregateEntityU1MMapMapping(){
        super();
    }

    public TestReadAggregateEntityU1MMapMapping(int fetchJoin){
        this();
        fetchJoinRelationship = fetchJoin;
        setName("TestReadAggregateEntityU1MMapMapping fetchJoin = " + fetchJoin);
    }

    @Override
    public void setup(){
        mapping = (UnidirectionalOneToManyMapping)getSession().getProject().getDescriptor(AggregateEntityU1MMapHolder.class).getMappingForAttributeName("aggregateToEntityMap");
        oldFetchJoinValue = mapping.getJoinFetch();
        mapping.setJoinFetch(fetchJoinRelationship);
        getSession().getProject().getDescriptor(AggregateEntityU1MMapHolder.class).reInitializeJoinedAttributes();

        UnitOfWork uow = getSession().acquireUnitOfWork();
        AggregateEntityU1MMapHolder holder = new AggregateEntityU1MMapHolder();
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
        holderExp = (new ExpressionBuilder()).get("id").equal(holder.getId());
        getSession().getIdentityMapAccessor().initializeAllIdentityMaps();
    }

    @Override
    public void test(){
        holders = getSession().readAllObjects(AggregateEntityU1MMapHolder.class, holderExp);
    }

    @Override
    public void verify(){
        if (holders == null || holders.size() != 1){
            throw new TestErrorException("Incorrect number of MapHolders was read.");
        }
        AggregateEntityU1MMapHolder holder = (AggregateEntityU1MMapHolder)holders.get(0);

        if (!((IndirectMap)holder.getAggregateToEntityMap()).getValueHolder().isInstantiated() && fetchJoinRelationship > 0){
            throw new TestErrorException("Relationship was not properly joined.");
        }

        if (holder.getAggregateToEntityMap().size() != 2){
            throw new TestErrorException("Incorrect Number of MapEntityValues was read.");
        }
        AggregateMapKey mapKey = new AggregateMapKey();
        mapKey.setKey(11);
        EntityMapValue value = (EntityMapValue)holder.getAggregateToEntityMap().get(mapKey);
        if (value.getId() != 1){
            throw new TestErrorException("Incorrect MapEntityValues was read.");
        }
    }

    @Override
    public void reset(){
        UnitOfWork uow = getSession().acquireUnitOfWork();
        for (Object object : holders) {
            AggregateEntityU1MMapHolder holder = (AggregateEntityU1MMapHolder) object;
            for (Object o : holder.getAggregateToEntityMap().keySet()) {
                uow.deleteObject(holder.getAggregateToEntityMap().get(o));
            }
        }
        uow.deleteAllObjects(holders);
        uow.commit();
        if (!verifyDelete(holders.get(0))){
            throw new TestErrorException("Delete was unsuccessful.");
        }
        mapping.setJoinFetch(oldFetchJoinValue);
    }

}

