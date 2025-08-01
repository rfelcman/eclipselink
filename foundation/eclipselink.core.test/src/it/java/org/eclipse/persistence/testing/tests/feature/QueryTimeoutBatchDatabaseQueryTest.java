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
package org.eclipse.persistence.testing.tests.feature;

import org.eclipse.persistence.descriptors.DescriptorQueryManager;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DataModifyQuery;
import org.eclipse.persistence.sessions.UnitOfWork;

import java.util.List;

/**
 * Bug 214910:  Add query timeout support to batched update queries (Oracle DB 9.0.1+)<br>
 * Test the query timeout feature in batch queries.
 * For data queries , a queryTimeout on the largest DatabaseQuery of the batch will be used.
 * For object queries, a queryTimeout on the largest DescriptorQueryManager (parent) or DatabaseQuery
 * of the batch will be used.
 */
public abstract class QueryTimeoutBatchDatabaseQueryTest extends QueryTimeoutBatchTestCase {

    @Override
    protected  int getParentQueryTimeout() { return 2; }
    @Override
    protected  int getChildQueryTimeout() { return 2; }

    @Override
    protected String getQuerySQLPostfix() {
        return ", SUM(e.address_id) as version from address e, address b, address b, address c, address c, address c, address b";
    }

    @Override
    public void test() {
        UnitOfWork uow = null;
        try {
            uow = setupPlatform();
            initializeDatabase(uow);
            // Get new UOW
            uow = getSession().acquireUnitOfWork();

            int queryCount = 0;
            for (int i = 0; i < getNumberOfInserts(); i++) {
                DataModifyQuery query = new DataModifyQuery();
                queryCount++;
                // The following insert will take around 5.2 seconds per row (the last (address c) is significant)
                // insert into employee (emp_id, version) SELECT 40003, SUM(e.address_id) as version from address e, address b, address b, address c, address c;
                String sBuffer = getQuerySQLPrefix() + (getCurrentIDSequence() + i) +
                        //sBuffer.append(i);
                        getQuerySQLPostfix();

                query.setSQLString(sBuffer);
                // set different query timeouts - the largest will be used
                query.setQueryTimeout(getChildQueryTimeout());
                //session.executeQuery(query);
                String queryName = "query" + i;

                // Force the last query to execute
                if(queryCount == getNumberOfInserts()) {
                    query.setForceBatchStatementExecution(true);
                    // clear last queryTimeout - so we pick up one from a previous appendCall
                    //query.setQueryTimeout(0);
                }
                uow.addQuery(queryName, query);
                uow.executeQuery(query);
            }

            uow.commit();
        } catch (Exception e) {
            /** Throws
             * Internal Exception: java.sql.SQLException: ORA-01013: user requested cancel of current operation
             */
            //e.printStackTrace();
            //System.err.print(e.getMessage());
            if (e instanceof DatabaseException) {
                limitExceeded = true;
                vendorErrorCodeEncountered = ((DatabaseException)e).getDatabaseErrorCode();
                //System.out.println("test completed with timeout of " + getChildQueryTimeout() + " seconds and exception: " + vendorErrorCodeEncountered);
            } else {
                //e.printStackTrace();
            }
            // Release transaction mutex
            ((AbstractSession)uow).rollbackTransaction();
        } finally {
            resetPlatform();
        }
    }

    @Override
    protected List  registerObjects(UnitOfWork uow) {
        return null;
    }

    /**
     * This is a callback from the object loop in registerObjects that allows the test
     * to set a timeout globally on the DescriptorQueryManager
     */
    @Override
    public void setDescriptorLevelQueryTimeout(DescriptorQueryManager queryManager) {
    }

    /**
     * This is a callback from the object loop in registerObjects that allows the test
     * to set a timeout on individual queries
     */
    @Override
    public void setQueryLevelQueryTimeout(UnitOfWork uow, Object object) {
    }

}
