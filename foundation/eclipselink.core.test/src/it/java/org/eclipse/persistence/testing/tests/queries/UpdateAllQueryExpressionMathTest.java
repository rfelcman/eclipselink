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
package org.eclipse.persistence.testing.tests.queries;

import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.expressions.ExpressionMath;
import org.eclipse.persistence.queries.UpdateAllQuery;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.insurance.Claim;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Tests an update all query that uses the ExpressionMath in the set clause
 *
 * @author Guy Pelletier
 * @version 1.0 April 13/04
 */
public class UpdateAllQueryExpressionMathTest extends AutoVerifyTestCase {
    private Session m_session;
    private Hashtable m_originalClaims;
    private Hashtable m_updatedClaims;

    public UpdateAllQueryExpressionMathTest() {
    }

    @Override
    public void reset() {
        m_session.getIdentityMapAccessor().initializeIdentityMaps();
        rollbackTransaction();
    }

    @Override
    protected void setup() {
        m_session = getSession();
        beginTransaction();
        m_session.getIdentityMapAccessor().initializeIdentityMaps();
        m_originalClaims = getClaims();
    }

    @Override
    public void test() {
        ExpressionBuilder eb = new ExpressionBuilder();
        UpdateAllQuery updateQuery = new UpdateAllQuery(org.eclipse.persistence.testing.models.insurance.Claim.class);
        updateQuery.setSelectionCriteria(eb.get("amount").greaterThan(1000));
        updateQuery.addUpdate(eb.get("amount"), ExpressionMath.multiply(eb.get("amount"), 1.10));
        m_session.executeQuery(updateQuery);
    }

    @Override
    protected void verify() {
        m_session.getIdentityMapAccessor().initializeIdentityMaps();// Ensure we read from the database
        m_updatedClaims = getClaims();

        Enumeration e = m_originalClaims.keys();

        while (e.hasMoreElements()) {
            Long id = (Long)e.nextElement();
            Float original = (Float)m_originalClaims.get(id);
            Float updated = (Float)m_updatedClaims.get(id);

            if (original.compareTo(1001F) < 0) {// Ensure it was not updated
                if (updated.compareTo(original) != 0) {
                    throw new TestErrorException("Claim amount was updated when it shouldn't have been");
                }
            } else {// Ensure it was properly updated
                if (updated.compareTo(original * 1.10f) != 0) {
                    throw new TestErrorException("Claim amount (" + original + ") was NOT properly updated. Value = " + updated);
                }
            }
        }
    }

    private Hashtable getClaims() {
        Hashtable claimsToReturn = new Hashtable();
        Vector claims = m_session.readAllObjects(org.eclipse.persistence.testing.models.insurance.Claim.class);
        Enumeration e = claims.elements();

        while (e.hasMoreElements()) {
            Claim claim = (Claim)e.nextElement();
            claimsToReturn.put(claim.getId(), claim.getAmount());
        }

        return claimsToReturn;
    }
}
