/*
 * Copyright (c) 1998, 2025 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 1998, 2024 IBM Corporation. All rights reserved.
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

package org.eclipse.persistence.testing.tests.jpa.jpql.advanced;

import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.PessimisticLockException;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import jakarta.persistence.TypedQuery;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.persistence.annotations.BatchFetchType;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.PessimisticLock;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.QueryType;
import org.eclipse.persistence.config.ResultSetConcurrency;
import org.eclipse.persistence.config.ResultSetType;
import org.eclipse.persistence.config.ResultType;
import org.eclipse.persistence.descriptors.invalidation.DailyCacheInvalidationPolicy;
import org.eclipse.persistence.descriptors.invalidation.TimeToLiveCacheInvalidationPolicy;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.jpa.JpaQuery;
import org.eclipse.persistence.queries.Cursor;
import org.eclipse.persistence.queries.ReadQuery;
import org.eclipse.persistence.queries.ScrollableCursor;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.testing.framework.QuerySQLTracker;
import org.eclipse.persistence.testing.framework.jpa.junit.JUnitTestCase;
import org.eclipse.persistence.testing.models.jpa.advanced.Address;
import org.eclipse.persistence.testing.models.jpa.advanced.AdvancedTableCreator;
import org.eclipse.persistence.testing.models.jpa.advanced.Buyer;
import org.eclipse.persistence.testing.models.jpa.advanced.Department;
import org.eclipse.persistence.testing.models.jpa.advanced.Employee;
import org.eclipse.persistence.testing.models.jpa.advanced.Employee.Gender;
import org.eclipse.persistence.testing.models.jpa.advanced.EmployeePopulator;
import org.eclipse.persistence.testing.models.jpa.advanced.entities.EntityFloat;
import org.eclipse.persistence.testing.tests.jpa.jpql.JUnitDomainObjectComparer;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * <b>Purpose</b>: Test advanced JPA Query functionality.
 * <p>
 * <b>Description</b>: This tests query hints, caching and query optimization.
 *
 */
public class AdvancedQueryTest extends JUnitTestCase {

    static JUnitDomainObjectComparer comparer; //the global comparer object used in all tests

    public AdvancedQueryTest() {
        super();
    }

    public AdvancedQueryTest(String name) {
        super(name);
        setPuName(getPersistenceUnitName());
    }

    @Override
    public String getPersistenceUnitName() {
        return "advanced";
    }

    // This method is run at the start of EVERY test case method.

    @Override
    public void setUp() {

    }

    // This method is run at the end of EVERY test case method.

    @Override
    public void tearDown() {
        clearCache();
    }

    //This suite contains all tests contained in this class

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("AdvancedQueryTest");
        suite.addTest(new AdvancedQueryTest("testSetup"));
        suite.addTest(new AdvancedQueryTest("testQueryCacheFirstCacheHits"));
        suite.addTest(new AdvancedQueryTest("testQueryCacheOnlyCacheHits"));
        suite.addTest(new AdvancedQueryTest("testQueryCacheOnlyCacheHitsOnSession"));
        suite.addTest(new AdvancedQueryTest("testQueryPrimaryKeyCacheHits"));
        suite.addTest(new AdvancedQueryTest("testQueryExactPrimaryKeyCacheHits"));
        suite.addTest(new AdvancedQueryTest("testQueryTypeCacheHits"));
        suite.addTest(new AdvancedQueryTest("testQueryCache"));
        suite.addTest(new AdvancedQueryTest("testQueryREADLock"));
        suite.addTest(new AdvancedQueryTest("testQueryWRITELock"));
        suite.addTest(new AdvancedQueryTest("testQueryOPTIMISTICLock"));
        suite.addTest(new AdvancedQueryTest("testQueryOPTIMISTIC_FORCE_INCREMENTLock"));
        suite.addTest(new AdvancedQueryTest("testQueryPESSIMISTIC_READLock"));
        suite.addTest(new AdvancedQueryTest("testQueryPESSIMISTIC_WRITELock"));
        suite.addTest(new AdvancedQueryTest("testQueryPESSIMISTIC_READ_TIMEOUTLock"));
        suite.addTest(new AdvancedQueryTest("testQueryPESSIMISTIC_WRITE_TIMEOUTLock"));
        suite.addTest(new AdvancedQueryTest("testQueryPESSIMISTICLockWithLimit"));
        suite.addTest(new AdvancedQueryTest("testPESSIMISTIC_LockWithDefaultTimeOutUnit"));
        suite.addTest(new AdvancedQueryTest("testPESSIMISTIC_LockWithSecondsTimeOutUnit"));
        suite.addTest(new AdvancedQueryTest("testObjectResultType"));
        suite.addTest(new AdvancedQueryTest("testNativeResultType"));
        suite.addTest(new AdvancedQueryTest("testCursors"));
        suite.addTest(new AdvancedQueryTest("testFetchGroups"));
        suite.addTest(new AdvancedQueryTest("testMultipleNamedJoinFetchs"));
        suite.addTest(new AdvancedQueryTest("testNativeQueryTransactions"));
        suite.addTest(new AdvancedQueryTest("testLockWithSecondaryTable"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingJOIN"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingEXISTS"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingIN"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingIN5"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingIN2"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingCursor"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingPagination"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingPagination2"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingReadObject"));
        suite.addTest(new AdvancedQueryTest("testBasicMapBatchFetchingJOIN"));
        suite.addTest(new AdvancedQueryTest("testBasicMapBatchFetchingEXISTS"));
        suite.addTest(new AdvancedQueryTest("testBasicMapBatchFetchingIN"));
        suite.addTest(new AdvancedQueryTest("testBatchFetchingINCache"));
        suite.addTest(new AdvancedQueryTest("testBasicMapJoinFetching"));
        suite.addTest(new AdvancedQueryTest("testBasicMapLeftJoinFetching"));
        suite.addTest(new AdvancedQueryTest("testJoinFetching"));
        suite.addTest(new AdvancedQueryTest("testJoinFetchingCursor"));
        suite.addTest(new AdvancedQueryTest("testJoinFetchingPagination"));
        suite.addTest(new AdvancedQueryTest("testMapKeyJoinFetching"));
        suite.addTest(new AdvancedQueryTest("testMapKeyBatchFetching"));
        suite.addTest(new AdvancedQueryTest("testJPQLCacheHits"));
        suite.addTest(new AdvancedQueryTest("testCacheIndexes"));
        suite.addTest(new AdvancedQueryTest("testSQLHint"));
        suite.addTest(new AdvancedQueryTest("testQueryPESSIMISTIC_FORCE_INCREMENTLock"));
        suite.addTest(new AdvancedQueryTest("testVersionChangeWithReadLock"));
        suite.addTest(new AdvancedQueryTest("testVersionChangeWithWriteLock"));
        suite.addTest(new AdvancedQueryTest("testNamedQueryAnnotationOverwritePersistenceXML"));
        suite.addTest(new AdvancedQueryTest("testFloatSortWithPessimisticLock"));
        suite.addTest(new AdvancedQueryTest("testFloatQualifiedIdProjectionWithPessimisticLock"));
        suite.addTest(new AdvancedQueryTest("testFloatSimpleIdProjectionWithPessimisticLock"));
        suite.addTest(new AdvancedQueryTest("testTearDown"));
        return suite;
    }

    /**
     * The setup is done as a test, both to record its failure, and to allow execution in the server.
     */
    public void testSetup() {
        clearCache();
        DatabaseSession session = getPersistenceUnitServerSession();
        new AdvancedTableCreator().replaceTables(session);

        //create a new EmployeePopulator
        EmployeePopulator employeePopulator = new EmployeePopulator(supportsStoredProcedures());
        //initialize the global comparer object
        comparer = new JUnitDomainObjectComparer();
        //set the session for the comparer to use
        comparer.setSession((AbstractSession)session.getActiveSession());
        //Populate the tables
        employeePopulator.buildExamples();
        //Persist the examples in the database
        employeePopulator.persistExample(session);
        // EntityFloat instances to test issue #2301
        EntityFloatPopulator.populate(session);
    }

    public void testTearDown() {
        JUnitTestCase.closeEntityManagerFactory(getPersistenceUnitName());
    }

    /**
     * Test that a cache hit will occur on a primary key query.
     */
    public void testQueryPrimaryKeyCacheHits() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(0);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheByPrimaryKey);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Employee queryResult = (Employee)query.getSingleResult();
            if (queryResult != employee) {
                fail("Employees are not equal: " + employee + ", " + queryResult);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }


    /**
     * Test using the hint hint.
     */
    public void testSQLHint() {
        if (!getDatabaseSession().getPlatform().isOracle()) {
            return;
        }
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());

            Query query = em.createNamedQuery("findAllAddressesByPostalCode");
            query.setParameter("postalcode", "K2H8C2");
            query.getResultList();

            if (!(counter.getSqlStatements().get(0)).contains("/*")) {
                fail("SQL hint was not used: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test that a cache hit will occur on a primary key query.
     */
    public void testQueryTypeCacheHits() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(0);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.QUERY_TYPE, QueryType.ReadObject);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Employee queryResult = (Employee)query.getSingleResult();
            if (queryResult != employee) {
                fail("Employees are not equal: " + employee + ", " + queryResult);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test fetch groups.
     */
    public void testFetchGroups() {
        if (!isWeavingEnabled()) {
            return;
        }
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(0);

            rollbackTransaction(em);
            closeEntityManager(em);
            clearCache();
            em = createEntityManager();
            beginTransaction(em);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.id = :id");
            query.setHint(QueryHints.FETCH_GROUP_ATTRIBUTE, "firstName");
            query.setHint(QueryHints.FETCH_GROUP_ATTRIBUTE, "lastName");
            query.setParameter("id", employee.getId());
            Employee queryResult = (Employee)query.getSingleResult();
            if (counter.getSqlStatements().size() != 1) {
                fail("More than fetch group selected: " + counter.getSqlStatements());
            }
            queryResult.getGender();
            if (counter.getSqlStatements().size() != 2) {
                fail("Access to unfetch did not cause fetch: " + counter.getSqlStatements());
            }
            verifyObject(employee);
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test multiple fetch joining from named queries.
     */
    public void testMultipleNamedJoinFetchs() {
        if (!isWeavingEnabled()) {
            return;
        }
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            clearCache();
            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            TypedQuery<Employee> query = em.createNamedQuery("findAllEmployeesJoinAddressPhones", Employee.class);
            List<Employee> result = query.getResultList();
            if (counter.getSqlStatements().size() != 1) {
                fail("More than join fetches selected: " + counter.getSqlStatements());
            }
            for (Employee each : result) {
                each.getAddress().getCity();
                each.getPhoneNumbers().size();
            }
            if (counter.getSqlStatements().size() != 1) {
                fail("Join fetches triggered query: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test cursored queries.
     */
    public void testCursors() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        try {
            // Test cusored stream.
            Query query = em.createQuery("Select employee from Employee employee");
            query.setHint(QueryHints.CURSOR, true);
            query.setHint(QueryHints.CURSOR_INITIAL_SIZE, 2);
            query.setHint(QueryHints.CURSOR_PAGE_SIZE, 5);
            query.setHint(QueryHints.CURSOR_SIZE, "Select count(*) from CMP3_EMPLOYEE");
            Cursor cursor = (Cursor)query.getSingleResult();
            cursor.nextElement();
            cursor.size();
            cursor.close();

            // Test cursor result API.
            JpaQuery jpaQuery = (JpaQuery)((EntityManager)em.getDelegate()).createQuery("Select employee from Employee employee");
            jpaQuery.setHint(QueryHints.CURSOR, true);
            cursor = jpaQuery.getResultCursor();
            cursor.nextElement();
            cursor.size();
            cursor.close();

            // Test scrollable cursor.
            jpaQuery = (JpaQuery)((EntityManager)em.getDelegate()).createQuery("Select employee from Employee employee");
            jpaQuery.setHint(QueryHints.SCROLLABLE_CURSOR, true);
            jpaQuery.setHint(QueryHints.RESULT_SET_CONCURRENCY, ResultSetConcurrency.ReadOnly);
            String resultSetType = ResultSetType.DEFAULT;
            // HANA supports only TYPE_FORWARD_ONLY, see bug 384116
            if (getPlatform().isHANA()) {
                resultSetType = ResultSetType.ForwardOnly;
            }
            jpaQuery.setHint(QueryHints.RESULT_SET_TYPE, resultSetType);
            ScrollableCursor scrollableCursor = (ScrollableCursor)jpaQuery.getResultCursor();
            scrollableCursor.next();
            scrollableCursor.close();

        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
        }
    }

    /**
     * Test the result type of various queries.
     */
    public void testObjectResultType() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(0);

            // Test multi object, as an array.
            query = em.createQuery("Select employee, employee.address, employee.id from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Object[] arrayResult = (Object[])query.getSingleResult();
            if ((arrayResult.length != 3) && (arrayResult[0] != employee) || (arrayResult[1] != employee.getAddress()) || (!arrayResult[2].equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }
            List listResult = query.getResultList();
            arrayResult = (Object[])listResult.get(0);
            if ((arrayResult.length != 3) || (arrayResult[0] != employee) || (arrayResult[1] != employee.getAddress()) || (!arrayResult[2].equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }

            // Test single object, as an array.
            query = em.createQuery("Select employee.id from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Array);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            arrayResult = (Object[])query.getSingleResult();
            if ((arrayResult.length != 1) || (!arrayResult[0].equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }
            listResult = query.getResultList();
            arrayResult = (Object[])listResult.get(0);
            if ((arrayResult.length != 1) || (!arrayResult[0].equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }

            // Test multi object, as a Map.
            query = em.createQuery("Select employee, employee.address, employee.id from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Map mapResult = (Map)query.getSingleResult();
            if ((mapResult.size() != 3) ||(mapResult.get("employee") != employee) || (mapResult.get("address") != employee.getAddress()) || (!mapResult.get("id").equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }
            listResult = query.getResultList();
            mapResult = (Map)listResult.get(0);
            if ((mapResult.size() != 3) ||(mapResult.get("employee") != employee) || (mapResult.get("address") != employee.getAddress()) || (!mapResult.get("id").equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }

            // Test single object, as a Map.
            query = em.createQuery("Select employee.id from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            mapResult = (Map)query.getSingleResult();
            if ((mapResult.size() != 1) || (!mapResult.get("id").equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }
            listResult = query.getResultList();
            mapResult = (Map)listResult.get(0);
            if ((mapResult.size() != 1) || (!mapResult.get("id").equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }

            // Test single object, as an array.
            query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.QUERY_TYPE, QueryType.Report);
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Array);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            arrayResult = (Object[])query.getSingleResult();
            if (arrayResult[0] != employee) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }

            // Test single object, as value.
            query = em.createQuery("Select employee.id from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Object valueResult = query.getSingleResult();
            if (! valueResult.equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }
            listResult = query.getResultList();
            valueResult = listResult.get(0);
            if (! valueResult.equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }

            // Test multi object, as value.
            query = em.createQuery("Select employee.id, employee.firstName from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Value);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            valueResult = query.getSingleResult();
            if (! valueResult.equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }

            // Test single object, as attribute.
            query = em.createQuery("Select employee.id from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Attribute);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            valueResult = query.getSingleResult();
            if (! valueResult.equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }
            listResult = query.getResultList();
            valueResult = listResult.get(0);
            if (! valueResult.equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }
        } finally {
            rollbackTransaction(em);
        }
    }

    /**
     * Test the result type of various native queries.
     */
    public void testNativeResultType() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        try {
            // Load an employee into the cache.
            Query query = em.createNativeQuery("Select * from CMP3_EMPLOYEE employee", Employee.class);
            List result = query.getResultList();
            Employee employee = (Employee)result.get(0);

            // Test multi object, as an array.
            query = em.createNativeQuery("Select employee.F_NAME, employee.EMP_ID from CMP3_EMPLOYEE employee where employee.EMP_ID = ? and employee.F_NAME = ?");
            query.setParameter(1, employee.getId());
            query.setParameter(2, employee.getFirstName());
            Object[] arrayResult = (Object[])query.getSingleResult();
            if ((arrayResult.length != 2) || (!arrayResult[0].equals(employee.getFirstName())) && (!arrayResult[1].equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }
            List listResult = query.getResultList();
            arrayResult = (Object[])listResult.get(0);
            if ((arrayResult.length != 2) || (!arrayResult[0].equals(employee.getFirstName())) && (!arrayResult[1].equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }

            // Test single object, as an array.
            query = em.createNativeQuery("Select employee.EMP_ID from CMP3_EMPLOYEE employee where employee.EMP_ID = ? and employee.F_NAME = ?");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Array);
            query.setParameter(1, employee.getId());
            query.setParameter(2, employee.getFirstName());
            arrayResult = (Object[])query.getSingleResult();
            if ((arrayResult.length != 1) || (!Integer.valueOf(((Number)arrayResult[0]).intValue()).equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }
            listResult = query.getResultList();
            arrayResult = (Object[])listResult.get(0);
            if ((arrayResult.length != 1) || (!Integer.valueOf(((Number)arrayResult[0]).intValue()).equals(employee.getId()))) {
                fail("Array result not correct: " + Arrays.toString(arrayResult));
            }

            // Test multi object, as a Map.
            query = em.createNativeQuery("Select employee.F_NAME, employee.EMP_ID from CMP3_EMPLOYEE employee where employee.EMP_ID = ? and employee.F_NAME = ?");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
            query.setParameter(1, employee.getId());
            query.setParameter(2, employee.getFirstName());
            Map mapResult = (Map)query.getSingleResult();
            if ((mapResult.size() != 2) || (!mapResult.get("F_NAME").equals(employee.getFirstName())) || (!(Integer.valueOf(((Number)mapResult.get("EMP_ID")).intValue())).equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }
            listResult = query.getResultList();
            mapResult = (Map)listResult.get(0);
            if ((mapResult.size() != 2) || (!mapResult.get("F_NAME").equals(employee.getFirstName())) || (!(Integer.valueOf(((Number)mapResult.get("EMP_ID")).intValue())).equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }

            // Test single object, as a Map.
            query = em.createNativeQuery("Select employee.EMP_ID from CMP3_EMPLOYEE employee where employee.EMP_ID = ? and employee.F_NAME = ?");
            query.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
            query.setParameter(1, employee.getId());
            query.setParameter(2, employee.getFirstName());
            mapResult = (Map)query.getSingleResult();
            if ((mapResult.size() != 1) || (!(Integer.valueOf(((Number)mapResult.get("EMP_ID")).intValue())).equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }
            listResult = query.getResultList();
            mapResult = (Map)listResult.get(0);
            if ((mapResult.size() != 1) || (!(Integer.valueOf(((Number)mapResult.get("EMP_ID")).intValue())).equals(employee.getId()))) {
                fail("Map result not correct: " + mapResult);
            }

            // Test single object, as value.
            query = em.createNativeQuery("Select employee.EMP_ID from CMP3_EMPLOYEE employee where employee.EMP_ID = ? and employee.F_NAME = ?");
            query.setParameter(1, employee.getId());
            query.setParameter(2, employee.getFirstName());
            Object valueResult = query.getSingleResult();
            if (!(Integer.valueOf(((Number)valueResult).intValue())).equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }
            listResult = query.getResultList();
            valueResult = listResult.get(0);
            if (!(Integer.valueOf(((Number)valueResult).intValue())).equals(employee.getId())) {
                fail("Value result not correct: " + valueResult);
            }
        } finally {
            rollbackTransaction(em);
        }
    }

    /**
     * Test that a cache hit will occur on a primary key query.
     */
    public void testQueryExactPrimaryKeyCacheHits() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(0);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.id = :id");
            query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheByExactPrimaryKey);
            query.setParameter("id", employee.getId());
            Employee queryResult = (Employee)query.getSingleResult();
            if (queryResult != employee) {
                fail("Employees are not equal: " + employee + ", " + queryResult);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test that the transaction is committed for a single native query transaction.
     */
    public void testNativeQueryTransactions() {
        Employee emp = (Employee)getPersistenceUnitServerSession().readObject(Employee.class);
        if (emp == null) {
            fail("Test problem: no Employees in the db, nothing to update");
        }
        EntityManager em = createEntityManager();
        beginTransaction(em);
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            Query query = em.createNativeQuery("Update CMP3_EMPLOYEE set F_NAME = 'Bobo' where EMP_ID = " + emp.getId());
            query.executeUpdate();
            commitTransaction(em);
            closeEntityManager(em);
            em = createEntityManager();
            beginTransaction(em);
            query = em.createNativeQuery("Select * from CMP3_EMPLOYEE where F_NAME = 'Bobo' AND EMP_ID = " + emp.getId());
            if (query.getResultList().isEmpty()) {
                fail("Native query did not commit transaction.");
            } else {
                // clean up - bring back the original name
                em.setFlushMode(FlushModeType.COMMIT);
                query = em.createNativeQuery("Update CMP3_EMPLOYEE set F_NAME = '"+emp.getFirstName()+"' where EMP_ID = " + emp.getId());
                query.executeUpdate();
                commitTransaction(em);
            }
        } finally {
            if (isTransactionActive(em)) {
                rollbackTransaction(em);
            }
            closeEntityManager(em);
        }
    }

    /**
     * Test that a cache hit will occur on a query.
     */
    public void testQueryCacheFirstCacheHits() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(result.size() - 1);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.firstName = :firstName");
            query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheThenDatabase);
            query.setParameter("firstName", employee.getFirstName());
            Employee queryResult = (Employee)query.getSingleResult();
            if (!queryResult.getFirstName().equals(employee.getFirstName())) {
                fail("Employees are not equal: " + employee + ", " + queryResult);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test that a cache hit will occur on a query.
     */
    public void testQueryCacheOnlyCacheHits() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(result.size() - 1);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.firstName = :firstName");
            query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheOnly);
            query.setParameter("firstName", employee.getFirstName());
            // Test that list works as well.
            query.getResultList();
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test that a cache hit will occur on a query when the object is not in the unit of work/em.
     */
    public void testQueryCacheOnlyCacheHitsOnSession() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(result.size() - 1);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            rollbackTransaction(em);
            closeEntityManager(em);
            em = createEntityManager();
            beginTransaction(em);
            query = em.createQuery("Select employee from Employee employee where employee.id = :id");
            query.setHint(QueryHints.QUERY_TYPE, QueryType.ReadObject);
            query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheOnly);
            query.setParameter("id", employee.getId());
            if (query.getSingleResult() == null) {
                fail("Query did not check session cache.");
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
            rollbackTransaction(em);
            closeEntityManager(em);
            em = createEntityManager();
            beginTransaction(em);
            query = em.createQuery("Select employee from Employee employee where employee.id = :id");
            query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheOnly);
            query.setParameter("id", employee.getId());
            if (query.getResultList().size() != 1) {
                fail("Query did not check session cache.");
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            if (counter != null) {
                counter.remove();
            }
            rollbackTransaction(em);
            closeEntityManager(em);
        }
    }

    /**
     * Test the query cache.
     */
    public void testQueryCache() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            JpaQuery jpaQuery = (JpaQuery)((EntityManager)em.getDelegate()).createNamedQuery("CachedAllEmployees");
            List result = jpaQuery.getResultList();
            ReadQuery readQuery = (ReadQuery)jpaQuery.getDatabaseQuery();
            if (readQuery.getQueryResultsCachePolicy() == null) {
                fail("Query cache not set.");
            }
            if (readQuery.getQueryResultsCachePolicy().getMaximumCachedResults() != 200) {
                fail("Query cache size not set.");
            }
            if (!(readQuery.getQueryResultsCachePolicy().getCacheInvalidationPolicy() instanceof TimeToLiveCacheInvalidationPolicy)) {
                fail("Query cache invalidation not set.");
            }
            if (((TimeToLiveCacheInvalidationPolicy)readQuery.getQueryResultsCachePolicy().getCacheInvalidationPolicy()).getTimeToLive() != 50000) {
                fail("Query cache invalidation time not set.");
            }

            jpaQuery = (JpaQuery)((EntityManager)em.getDelegate()).createNamedQuery("CachedTimeOfDayAllEmployees");
            readQuery = (ReadQuery)jpaQuery.getDatabaseQuery();
            if (readQuery.getQueryResultsCachePolicy() == null) {
                fail("Query cache not set.");
            }
            if (readQuery.getQueryResultsCachePolicy().getMaximumCachedResults() != 200) {
                fail("Query cache size not set.");
            }
            if (!(readQuery.getQueryResultsCachePolicy().getCacheInvalidationPolicy() instanceof DailyCacheInvalidationPolicy)) {
                fail("Query cache invalidation not set.");
            }
            Calendar calendar = ((DailyCacheInvalidationPolicy)readQuery.getQueryResultsCachePolicy().getCacheInvalidationPolicy()).getExpiryTime();
            if ((calendar.get(Calendar.HOUR_OF_DAY) != 23 )
                    && (calendar.get(Calendar.MINUTE) != 59)
                    && (calendar.get(Calendar.SECOND) != 59)) {
                fail("Query cache invalidation time not set.");
            }

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            Query query = em.createNamedQuery("CachedAllEmployees");
            if (result.size() != query.getResultList().size()) {
                fail("List result size is not correct on 2nd cached query.");
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Query cache was not used: " + counter.getSqlStatements());
            }
            clearCache();
            // Preload uow to test query cache in uow.
            em.createQuery("Select e from Employee e").getResultList();
            query.getResultList();
            if (result.size() != query.getResultList().size()) {
                fail("List result size is not correct on cached query in unit of work.");
            }
            clearCache();
            // Also test query cache in early transaction.
            em.persist(new Address());
            em.flush();
            query.getResultList();
            if (result.size() != query.getResultList().size()) {
                fail("List result size is not correct on cached query in transaction.");
            }
            // Query by primary key.
            query = em.createNamedQuery("CachedNoEmployees");
            if (!query.getResultList().isEmpty()) {
                fail("List result size is not correct.");
            }
            // Also test empty query cache.
            counter.remove();
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            if (!query.getResultList().isEmpty()) {
                fail("List result size is not correct.");
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Query cache was not used: " + counter.getSqlStatements());
            }
            rollbackTransaction(em);
            beginTransaction(em);
            query = em.createNamedQuery("CachedEmployeeJoinAddress");
            result = query.getResultList();
            // Test that an insert triggers the query cache to invalidate.
            Employee employee = new Employee();
            Address address = new Address();
            address.setCity("Ottawa");
            employee.setAddress(address);
            em.persist(employee);
            commitTransaction(em);
            beginTransaction(em);
            query = em.createNamedQuery("CachedEmployeeJoinAddress");
            if ((result.size() + 1) != query.getResultList().size()) {
                fail("Query result cache not invalidated.");
            }
            address = em.merge(address);
            address.setCity("nowhere");
            commitTransaction(em);
            beginTransaction(em);
            query = em.createNamedQuery("CachedEmployeeJoinAddress");
            if (result.size() != query.getResultList().size()) {
                fail("Query result cache not invalidated.");
            }
            em.remove(em.find(Employee.class, employee.getId()));
            commitTransaction(em);
            query = em.createNamedQuery("CachedEmployeeJoinAddress");
            if (result.size() != query.getResultList().size()) {
                fail("Query result cache not invalidated on delete.");
            }
        } finally {
            closeEntityManagerAndTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    public void testQueryREADLock(){
        // Cannot create parallel entity managers in the server.
        if (isOnServer()) {
            return;
        }

        // Load an employee into the cache.
        EntityManager em = createEntityManager();
        List result = em.createQuery("Select employee from Employee employee").getResultList();
        Employee employee = (Employee) result.get(0);
        Exception optimisticLockException = null;

        try {
            beginTransaction(em);

            // Query by primary key.
            Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setLockMode(LockModeType.READ);
            query.setHint(QueryHints.REFRESH, true);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Employee queryResult = (Employee) query.getSingleResult();
            queryResult.toString();

            EntityManager em2 = createEntityManager();

            try {
                beginTransaction(em2);
                Employee employee2 = em2.find(Employee.class, employee.getId());
                employee2.setFirstName("Read");
                commitTransaction(em2);
            } catch (RuntimeException ex) {
                rollbackTransaction(em2);
                throw ex;
            } finally {
                closeEntityManagerAndTransaction(em2);
            }

            try {
                em.flush();
            } catch (PersistenceException exception) {
                if (exception instanceof OptimisticLockException) {
                    optimisticLockException = exception;
                } else {
                    throw exception;
                }
            }

            rollbackTransaction(em);
        } catch (RuntimeException ex) {
            if (isTransactionActive(em)){
                rollbackTransaction(em);
            }

            throw ex;
        } finally {
            closeEntityManager(em);
        }

        assertNotNull("Proper exception not thrown when Query with LockModeType.READ is used.", optimisticLockException);
    }

    public void testQueryWRITELock(){
        // Cannot create parallel transactions.
        if (isOnServer()) {
            return;
        }

        // Load an employee into the cache.
        EntityManager em = createEntityManager();
        List result = em.createQuery("Select employee from Employee employee").getResultList();
        Employee employee = (Employee) result.get(0);
        Exception optimisticLockException = null;

        try {
            beginTransaction(em);

            // Query by primary key.
            Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
            query.setLockMode(LockModeType.WRITE);
            query.setHint(QueryHints.REFRESH, true);
            query.setParameter("id", employee.getId());
            query.setParameter("firstName", employee.getFirstName());
            Employee queryResult = (Employee) query.getSingleResult();

            EntityManager em2 = createEntityManager();

            try {
                beginTransaction(em2);

                Employee employee2 = em2.find(Employee.class, queryResult.getId());
                employee2.setFirstName("Write");
                commitTransaction(em2);
            } catch (RuntimeException ex) {
                rollbackTransaction(em2);
                closeEntityManager(em2);
                throw ex;
            } finally {
                closeEntityManagerAndTransaction(em2);
            }

            commitTransaction(em);
        } catch (RollbackException exception) {
            if (exception.getCause() instanceof OptimisticLockException){
                optimisticLockException = exception;
            }
        } catch (RuntimeException ex) {
            if (isTransactionActive(em)) {
                rollbackTransaction(em);
            }

            closeEntityManager(em);

            throw ex;
        } finally {
            closeEntityManagerAndTransaction(em);
        }

        assertNotNull("Proper exception not thrown when Query with LockModeType.WRITE is used.", optimisticLockException);
    }

    public void testQueryOPTIMISTICLock(){
        // Cannot create parallel entity managers in the server.
        if (! isOnServer()) {
            // Load an employee into the cache.
            EntityManager em = createEntityManager();
            List result = em.createQuery("Select employee from Employee employee").getResultList();
            Employee employee = (Employee) result.get(0);
            Exception optimisticLockException = null;

            try {
                beginTransaction(em);

                // Query by primary key.
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                query.setLockMode(LockModeType.OPTIMISTIC);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.toString();

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);
                    Employee employee2 = em2.find(Employee.class, employee.getId());
                    employee2.setFirstName("Optimistic");
                    commitTransaction(em2);
                } catch (RuntimeException ex) {
                    rollbackTransaction(em2);
                    throw ex;
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                try {
                    em.flush();
                } catch (PersistenceException exception) {
                    if (exception instanceof OptimisticLockException) {
                        optimisticLockException = exception;
                    } else {
                        throw exception;
                    }
                }

                rollbackTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)){
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.READ is used.", optimisticLockException);
        }
    }

    public void testQueryOPTIMISTIC_FORCE_INCREMENTLock(){
        // Cannot create parallel transactions.
        if (! isOnServer()) {
            // Load an employee into the cache.
            EntityManager em = createEntityManager();
            List result = em.createQuery("Select employee from Employee employee").getResultList();
            Employee employee = (Employee) result.get(0);
            Exception optimisticLockException = null;

            try {
                beginTransaction(em);

                // Query by primary key.
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                query.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);

                    Employee employee2 = em2.find(Employee.class, queryResult.getId());
                    employee2.setFirstName("OptimisticForceIncrement");
                    commitTransaction(em2);
                } catch (RuntimeException ex) {
                    rollbackTransaction(em2);
                    throw ex;
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                commitTransaction(em);
            } catch (RollbackException exception) {
                if (exception.getCause() instanceof OptimisticLockException){
                    optimisticLockException = exception;
                }
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                closeEntityManager(em);

                throw ex;
            } finally {
                closeEntityManagerAndTransaction(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.WRITE is used.", optimisticLockException);
        }
    }

    public void testQueryPESSIMISTIC_READLock() {
        if ((getPersistenceUnitServerSession()).getPlatform().isHANA()) {
            // HANA currently doesn't support pessimistic locking with queries on multiple tables
            // feature is under development (see bug 384129), but test should be skipped for the time being
            return;
        }
        // Cannot create parallel entity managers in the server.
        if (! isOnServer() && isSelectForUpateSupported()) {
            EntityManager em = createEntityManager();
            PessimisticLockException pessimisticLockException = null;

            try {
                beginTransaction(em);

                EntityManager em2 = createEntityManager();
                try {
                    beginTransaction(em2);

                    List employees2 = em2.createQuery("Select employee from Employee employee").getResultList(); //
                    Employee employee2 = (Employee) employees2.get(0);

                    // Find all the departments and lock them.
                    List employees = em.createQuery("Select employee from Employee employee").setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
                    Employee employee = (Employee) employees.get(0);
                    employee.setFirstName("New Pessimistic Employee");

                    Map<String, Object> properties = new HashMap<>();
                    properties.put(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 0);
                    em2.lock(employee2, LockModeType.PESSIMISTIC_READ, properties);
                    employee2.setFirstName("Invalid Lock Employee");

                    commitTransaction(em2);
                } catch (jakarta.persistence.PessimisticLockException ex) {
                    pessimisticLockException = ex;
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.PESSIMISTIC is used.", pessimisticLockException);
        }
    }

    public void testQueryPESSIMISTIC_WRITELock() {
        if ((getPersistenceUnitServerSession()).getPlatform().isHANA()) {
            // HANA currently doesn't support pessimistic locking with queries on multiple tables
            // feature is under development (see bug 384129), but test should be skipped for the time being
            return;
        }
        // Cannot create parallel entity managers in the server.
        if (! isOnServer() && isSelectForUpateSupported()) {
            EntityManager em = createEntityManager();
            Exception pessimisticLockException = null;

            try {
                beginTransaction(em);

                EntityManager em2 = createEntityManager();
                try {
                    beginTransaction(em2);

                    List employees2 = em2.createQuery("Select employee from Employee employee").getResultList(); //
                    Employee employee2 = (Employee) employees2.get(0);

                    // Find all the departments and lock them.
                    List employees = em.createQuery("Select employee from Employee employee").setLockMode(LockModeType.PESSIMISTIC_READ).getResultList();
                    Employee employee = (Employee) employees.get(0);
                    employee.setFirstName("New Pessimistic Employee");


                    Map<String, Object> properties = new HashMap<>();
                    properties.put(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 0);
                    em2.lock(employee2, LockModeType.PESSIMISTIC_READ, properties);
                    employee2.setFirstName("Invalid Lock Employee");

                    commitTransaction(em2);
                } catch (jakarta.persistence.PessimisticLockException ex) {
                    pessimisticLockException = ex;
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.PESSIMISTIC is used.", pessimisticLockException);
        }
    }

    public void testQueryPESSIMISTIC_FORCE_INCREMENTLock() {
        if ((getPersistenceUnitServerSession()).getPlatform().isHANA()) {
            // HANA currently doesn't support pessimistic locking with queries on multiple tables
            // feature is under development (see bug 384129), but test should be skipped for the time being
            return;
        }
        if (isSelectForUpateSupported()) {
            Employee employee = null;
            Integer version1;

            EntityManager em = createEntityManager();
            beginTransaction(em);

            try {
                employee = new Employee();
                employee.setFirstName("Guillaume");
                employee.setLastName("Aujet");
                em.persist(employee);
                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }
                closeEntityManager(em);
                throw ex;
            }

            version1 = employee.getVersion();

            try {
                beginTransaction(em);
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName").setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.setLastName("Auger");
                commitTransaction(em);

                employee = em.find(Employee.class, employee.getId());
                assertTrue("The version was not updated on the pessimistic lock.", version1 < employee.getVersion());
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }
                closeEntityManager(em);
                throw ex;
            }

            //Verify if the entity has been updated correctly by using PESSIMISTIC_FORCE_INCREMENT as PESSIMISTIC_WRITE
            try {
                beginTransaction(em);
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName").setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                rollbackTransaction(em);

                assertEquals("The last name is not updated by using PESSIMISTIC_FORCE_INCREMENT.", "Auger", queryResult.getLastName());
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }
        }
    }

    public void testQueryPESSIMISTIC_READ_TIMEOUTLock() {
        ServerSession session = getPersistenceUnitServerSession();

        // Cannot create parallel entity managers in the server.
        // Lock timeout only supported on Oracle.
        if (! isOnServer() && session.getPlatform().supportsWaitForUpdate()) {
            EntityManager em = createEntityManager();
            List result = em.createQuery("Select employee from Employee employee").getResultList();
            Employee employee = (Employee) result.get(0);
            Exception lockTimeOutException = null;

            try {
                beginTransaction(em);

                // Query by primary key.
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                query.setLockMode(LockModeType.PESSIMISTIC_READ);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.toString();

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);

                    // Query by primary key.
                    Query query2 = em2.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                    query2.setLockMode(LockModeType.PESSIMISTIC_READ);
                    query2.setHint(QueryHints.REFRESH, true);
                    query2.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 5000);
                    query2.setParameter("id", employee.getId());
                    query2.setParameter("firstName", employee.getFirstName());
                    Employee employee2 = (Employee) query2.getSingleResult();
                    employee2.setFirstName("Invalid Lock Employee");
                    commitTransaction(em2);
                } catch (PersistenceException ex) {
                    if (ex instanceof jakarta.persistence.LockTimeoutException) {
                        lockTimeOutException = ex;
                    } else {
                        throw ex;
                    }
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.PESSIMISTIC is used.", lockTimeOutException);
        }
    }

    public void testQueryPESSIMISTIC_WRITE_TIMEOUTLock() {
        ServerSession session = getPersistenceUnitServerSession();

        // Cannot create parallel entity managers in the server.
        // Lock timeout only supported on Oracle.
        if (! isOnServer() && session.getPlatform().supportsWaitForUpdate()) {
            EntityManager em = createEntityManager();
            List result = em.createQuery("Select employee from Employee employee").getResultList();
            Employee employee = (Employee) result.get(0);
            Exception lockTimeOutException = null;

            try {
                beginTransaction(em);

                // Query by primary key.
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.toString();

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);

                    // Query by primary key.
                    Query query2 = em2.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                    query2.setLockMode(LockModeType.PESSIMISTIC_WRITE);
                    query2.setHint(QueryHints.REFRESH, true);
                    query2.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 5000);
                    query2.setParameter("id", employee.getId());
                    query2.setParameter("firstName", employee.getFirstName());
                    Employee employee2 = (Employee) query2.getSingleResult();
                    employee2.setFirstName("Invalid Lock Employee");
                    commitTransaction(em2);
                } catch (PersistenceException ex) {
                    if (ex instanceof jakarta.persistence.LockTimeoutException) {
                        lockTimeOutException = ex;
                    } else {
                        throw ex;
                    }
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.PESSIMISTIC is used.", lockTimeOutException);
        }
    }

    public void testPESSIMISTIC_LockWithDefaultTimeOutUnit() {
        ServerSession session = getPersistenceUnitServerSession();

        // Cannot create parallel entity managers in the server.
        // Lock timeout only supported on Oracle.
        if (! isOnServer() && session.getPlatform().supportsWaitForUpdate()) {
            EntityManager em = createEntityManager();
            // sleep for 2 seconds (2000 milliseconds)
            // this timeout value must be smaller, than PESSIMISTIC_LOCK_TIMEOUT property for the second query
            // to execute this test without LockTimeoutException
            Thread pesimisticLockRunnerThread = new Thread(new PesimisticLockRunner(em, 2000));
            List result = em.createQuery("Select employee from Employee employee").getResultList();
            Employee employee = (Employee) result.get(0);
            Exception lockTimeOutException = null;

            try {
                beginTransaction(em);

                // Query by primary key.
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                query.setLockMode(LockModeType.PESSIMISTIC_READ);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.toString();

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);

                    // Query by primary key.
                    Query query2 = em2.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                    query2.setLockMode(LockModeType.PESSIMISTIC_READ);
                    query2.setHint(QueryHints.REFRESH, true);
                    query2.setParameter("id", employee.getId());
                    query2.setParameter("firstName", employee.getFirstName());
                    // Set timeout for 4000 milliseconds (4 seconds)
                    query2.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 4000);

                    // Release (rollback) locked rows by first query in second thread after timeout
                    pesimisticLockRunnerThread.start();

                    Employee employee2 = (Employee) query2.getSingleResult();
                    employee2.setFirstName("Invalid Lock Employee");
                    commitTransaction(em2);
                } catch (PersistenceException ex) {
                    if (ex instanceof jakarta.persistence.LockTimeoutException) {
                        lockTimeOutException = ex;
                    } else {
                        throw ex;
                    }
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }
            try {
                pesimisticLockRunnerThread.join(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                fail("PesimisticLockRunnerThread failed with:" + ex);
            }

            Assert.assertNull("A jakarta.persistence.LockTimeoutException was unexpectedly thrown", lockTimeOutException);
        }
    }

    public void testPESSIMISTIC_LockWithSecondsTimeOutUnit() {
        ServerSession session = getPersistenceUnitServerSession();

        // Cannot create parallel entity managers in the server.
        if (! isOnServer() && session.getPlatform().supportsWaitForUpdate()) {
            EntityManager em = createEntityManager();
            // sleep for 4 seconds (4000 milliseconds)
            // this timeout value must be higher, than PESSIMISTIC_LOCK_TIMEOUT property for the second query
            // a LockTimeoutException is expected to be thrown since the lock timeout should still be in effect
            Thread pesimisticLockRunnerThread = new Thread(new PesimisticLockRunner(em, 4000));
            List result = em.createQuery("Select employee from Employee employee").getResultList();
            Employee employee = (Employee) result.get(0);
            Exception lockTimeOutException = null;

            try {
                beginTransaction(em);

                // Query by primary key.
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                query.setLockMode(LockModeType.PESSIMISTIC_READ);
                query.setHint(QueryHints.REFRESH, true);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.toString();

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);

                    // Query by primary key.
                    Query query2 = em2.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName");
                    query2.setLockMode(LockModeType.PESSIMISTIC_READ);
                    query2.setHint(QueryHints.REFRESH, true);
                    // Set timeout for 2 seconds
                    // It's smaller, than lock of the first query => throws LockTimeoutException
                    query2.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 2);
                    query2.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT_UNIT, "SECONDS");
                    query2.setParameter("id", employee.getId());
                    query2.setParameter("firstName", employee.getFirstName());

                    // Release (rollback) locked rows by first query in second thread after timeout
                    pesimisticLockRunnerThread.start();

                    Employee employee2 = (Employee) query2.getSingleResult();
                    employee2.setFirstName("Invalid Lock Employee");
                    commitTransaction(em2);
                } catch (PersistenceException ex) {
                    if (ex instanceof jakarta.persistence.LockTimeoutException) {
                        lockTimeOutException = ex;
                    } else {
                        throw ex;
                    }
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }
            try {
                pesimisticLockRunnerThread.join(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                fail("PesimisticLockRunnerThread failed with:" + ex);
            }

            Assert.assertNotNull("A jakarta.persistence.LockTimeoutException was expected to be thrown", lockTimeOutException);
        }
    }

    private class PesimisticLockRunner implements Runnable {
        private EntityManager em;
        private long timeout;

        public PesimisticLockRunner(EntityManager em, long timeout) {
            this.em = em;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            // sleep for "timeout" milliseconds) to allow the first lock to timeout
            try {
                Thread.sleep(timeout);
            } catch (Exception e) {
            }
            rollbackTransaction(em);
        }
    }

    public void testLockWithSecondaryTable() {
        if ((getPersistenceUnitServerSession()).getPlatform().isHANA()) {
            // HANA currently doesn't support pessimistic locking with queries on multiple tables
            // feature is under development (see bug 384129), but test should be skipped for the time being
            return;
        }
        // Cannot create parallel entity managers in the server.
        if (! isOnServer() && isSelectForUpateSupported()) {
            EntityManager em = createEntityManager();
            Exception pessimisticLockException = null;

            try {
                beginTransaction(em);

                EntityManager em2 = createEntityManager();

                try {
                    beginTransaction(em2);

                    List employees2 = em2.createQuery("Select employee from Employee employee").getResultList();
                    Employee employee2 = (Employee) employees2.get(0);

                    // Find all the employees and lock them.
                    List employees = em.createQuery("Select employee from Employee employee").setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
                    Employee employee = (Employee) employees.get(0);
                    employee.setSalary(90000);

                    Map<String, Object> properties = new HashMap<>();
                    properties.put(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 0);
                    em2.lock(employee2, LockModeType.PESSIMISTIC_WRITE, properties);
                    employee2.setSalary(100000);
                    commitTransaction(em2);
                } catch (PessimisticLockException ex) {
                        pessimisticLockException = ex;
                } finally {
                    closeEntityManagerAndTransaction(em2);
                }

                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                throw ex;
            } finally {
                closeEntityManager(em);
            }

            assertNotNull("Proper exception not thrown when Query with LockModeType.PESSIMISTIC is used.", pessimisticLockException);
        }
    }

    public void testVersionChangeWithReadLock() {
        if (isSelectForUpateNoWaitSupported()){
            Employee employee = null;
            Integer version1;

            EntityManager em = createEntityManager();
            beginTransaction(em);

            try {
                employee = new Employee();
                employee.setFirstName("Version Change");
                employee.setLastName("Readlock");
                em.persist(employee);
                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                closeEntityManager(em);
                throw ex;
            }

            version1 = employee.getVersion();

            try {
                beginTransaction(em);
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName").setLockMode(LockModeType.PESSIMISTIC_READ);
                query.setHint(QueryHints.REFRESH, true);
                query.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 0);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.setLastName("Burger");
                commitTransaction(em);

                employee = em.find(Employee.class, employee.getId());
                assertTrue("The version was not updated on the pessimistic read lock.", version1 < employee.getVersion());
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }
                throw ex;
            } finally {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }
                closeEntityManager(em);
            }
        }
    }

    public void testNamedQueryAnnotationOverwritePersistenceXML() {
        EntityManager em = createEntityManager();
        try {
            beginTransaction(em);
            Query query = em.createNamedQuery("findAllEmployeesByIdAndFirstName");
            Map<String, Object> hints = query.getHints();
            assertEquals("query hint", "15000", hints.get(QueryHints.PESSIMISTIC_LOCK_TIMEOUT));
            rollbackTransaction(em);
        } catch(Exception ex){
            if (isTransactionActive(em)) {
                rollbackTransaction(em);
            }
            throw ex;
        } finally{
            if (isTransactionActive(em)) {
                rollbackTransaction(em);
            }
            closeEntityManager(em);
        }
    }

    public void testVersionChangeWithWriteLock() {
        if (isSelectForUpateNoWaitSupported()) {
            Employee employee = null;
            Integer version1;

            EntityManager em = createEntityManager();
            beginTransaction(em);

            try {
                employee = new Employee();
                employee.setFirstName("Version Change");
                employee.setLastName("Writelock");
                em.persist(employee);
                commitTransaction(em);
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }

                closeEntityManager(em);
                throw ex;
            }

            version1 = employee.getVersion();

            try {
                beginTransaction(em);
                Query query = em.createQuery("Select employee from Employee employee where employee.id = :id and employee.firstName = :firstName").setLockMode(LockModeType.PESSIMISTIC_WRITE);
                query.setHint(QueryHints.REFRESH, true);
                query.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 0);
                query.setParameter("id", employee.getId());
                query.setParameter("firstName", employee.getFirstName());
                Employee queryResult = (Employee) query.getSingleResult();
                queryResult.setLastName("Burger");
                commitTransaction(em);

                employee = em.find(Employee.class, employee.getId());
                assertTrue("The version was not updated on the pessimistic write lock.", version1 < employee.getVersion());
            } catch (RuntimeException ex) {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }
                closeEntityManager(em);
                throw ex;
            } finally {
                if (isTransactionActive(em)) {
                    rollbackTransaction(em);
                }
                closeEntityManager(em);
            }

        }
    }

    /**
     * Test batch fetching.
     */
    public void testBatchFetchingIN() {
        testBatchFetching(BatchFetchType.IN, 1000);
    }

    /**
     * Test batch fetching.
     */
    public void testBatchFetchingIN5() {
        testBatchFetching(BatchFetchType.IN, 5);
    }

    /**
     * Test batch fetching.
     */
    public void testBatchFetchingIN2() {
        testBatchFetching(BatchFetchType.IN, 2);
    }

    /**
     * Test batch fetching.
     */
    public void testBatchFetchingJOIN() {
        testBatchFetching(BatchFetchType.JOIN, 0);
    }

    /**
     * Test batch fetching.
     */
    public void testBatchFetchingEXISTS() {
        testBatchFetching(BatchFetchType.EXISTS, 0);
    }

    /**
     * Test batch fetching.
     */
    public void testBatchFetching(BatchFetchType type, int size) {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Employee> query = em.createQuery("Select e from Employee e where e.gender = :g1 or e.gender = :g2", Employee.class);
            query.setHint(QueryHints.BATCH_SIZE, size);
            query.setHint(QueryHints.BATCH_TYPE, type);
            query.setHint(QueryHints.BATCH, "e.address");
            query.setHint(QueryHints.BATCH, "e.manager");
            query.setHint(QueryHints.BATCH, "e.projects");
            query.setHint(QueryHints.BATCH, "e.managedEmployees");
            query.setHint(QueryHints.BATCH, "e.responsibilities");
            query.setHint(QueryHints.BATCH, "e.dealers");
            query.setHint(QueryHints.BATCH, "e.phoneNumbers");
            //query.setHint(QueryHints.BATCH, "e.department"); is join fetched already.
            query.setHint(QueryHints.BATCH, "e.workWeek");
            query.setParameter("g1", Gender.Male);
            query.setParameter("g2", Gender.Female);
            List<Employee> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            for (Employee employee : results) {
                employee.getAddress();
                employee.getManager();
                employee.getProjects().size();
                employee.getManagedEmployees().size();
                employee.getResponsibilities().size();
                employee.getDealers().size();
                employee.getPhoneNumbers().size();
                employee.getWorkWeek().size();
            }
            int queries = 11;
            if (size == 2) {
                queries = 55;
            } else if (size == 5) {
                queries = 30;
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() > queries) {
                fail("Should have been " + queries + " queries but was: " + counter.getSqlStatements().size());
            }
            if (type != BatchFetchType.JOIN) {
                for (String sql : counter.getSqlStatements()) {
                    if ((sql.contains("DISTINCT")) && (!sql.contains("PROJ_TYPE"))) {
                        fail("SQL should not contain DISTINCT: " + sql);
                    }
                }
            }
            clearCache();
            for (Employee employee : results) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test batch fetching with IN and a partial cache.
     */
    public void testBatchFetchingINCache() {
        testBatchFetchingINCache(1);
        testBatchFetchingINCache(2);
        testBatchFetchingINCache(3);
        testBatchFetchingINCache(4);
        testBatchFetchingINCache(5);
    }

    /**
     * Test batch fetching with IN and a partial cache.
     * This tests that the paging is working correctly for different page sizes and empty and disjoint pages.
     */
    public void testBatchFetchingINCache(int batchSize) {
        clearCache();
        EntityManager em = createEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("Select a from Address a where exists (Select e from Employee e where e.address = a and e.gender = :g1)", Employee.class);
            query.setParameter("g1", Gender.Female);
            query.getResultList();
            query = em.createQuery("Select e from Employee e where e.gender = :g1 or e.gender = :g2", Employee.class);
            query.setHint(QueryHints.BATCH_SIZE, batchSize);
            query.setHint(QueryHints.BATCH_TYPE, BatchFetchType.IN);
            query.setHint(QueryHints.BATCH, "e.address");
            query.setHint(QueryHints.BATCH, "e.manager");
            query.setParameter("g1", Gender.Male);
            query.setParameter("g2", Gender.Female);
            List<Employee> results = query.getResultList();
            for (Employee employee : results) {
                employee.getAddress();
                employee.getManager();
            }
            clearCache();
            for (Employee employee : results) {
                verifyObject(employee);
            }
        } finally {
            closeEntityManagerAndTransaction(em);
        }
    }

    /**
     * Test join fetching.
     */
    public void testJoinFetching() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Employee> query = em.createQuery("Select e from Employee e where e.gender = :g1 or e.gender = :g2", Employee.class);
            query.setHint(QueryHints.LEFT_FETCH, "e.address");
            //query.setHint(QueryHints.LEFT_FETCH, "e.manager"); - has eagers
            //query.setHint(QueryHints.LEFT_FETCH, "e.projects"); - has eagers
            //query.setHint(QueryHints.LEFT_FETCH, "e.managedEmployees"); - has eagers
            query.setHint(QueryHints.LEFT_FETCH, "e.responsibilities");
            query.setHint(QueryHints.LEFT_FETCH, "e.dealers");
            query.setHint(QueryHints.LEFT_FETCH, "e.phoneNumbers");
            //query.setHint(QueryHints.BATCH, "e.department"); is join fetched already.
            query.setHint(QueryHints.LEFT_FETCH, "e.workWeek");
            query.setParameter("g1", Gender.Male);
            query.setParameter("g2", Gender.Female);
            List<Employee> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            for (Employee employee : results) {
                employee.getAddress();
                employee.getResponsibilities().size();
                employee.getDealers().size();
                employee.getPhoneNumbers().size();
                employee.getWorkWeek().size();
            }
            int queries = 1;
            if (isWeavingEnabled() && counter.getSqlStatements().size() > queries) {
                fail("Should have been " + queries + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Employee employee : results) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test batch fetching of maps.
     */
    public void testBasicMapBatchFetchingJOIN() {
        testBasicMapBatchFetching(BatchFetchType.JOIN, 0);
    }

    /**
     * Test batch fetching of maps.
     */
    public void testBasicMapBatchFetchingIN() {
        testBasicMapBatchFetching(BatchFetchType.IN, 100);
    }

    /**
     * Test batch fetching of maps.
     */
    public void testBasicMapBatchFetchingEXISTS() {
        testBasicMapBatchFetching(BatchFetchType.EXISTS, 0);
    }

    /**
     * Test batch fetching of maps.
     */
    public void testBasicMapBatchFetching(BatchFetchType type, int size) {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Buyer> query = em.createQuery("Select b from Buyer b where b.name like :name", Buyer.class);
            query.setHint(QueryHints.BATCH_SIZE, size);
            query.setHint(QueryHints.BATCH_TYPE, type);
            query.setHint(QueryHints.BATCH, "e.creditCards");
            query.setHint(QueryHints.BATCH, "e.creditLines");
            query.setParameter("name", "%Gold%");
            List<Buyer> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 3) {
                fail("Should have been 3 query but was: " + counter.getSqlStatements().size());
            }
            for (Buyer buyer : results) {
                buyer.getCreditCards().size();
                buyer.getCreditLines().size();
            }
            int queries = 4;
            if (isWeavingEnabled() && counter.getSqlStatements().size() > queries) {
                fail("Should have been " + queries + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Buyer buyer : results) {
                verifyObject(buyer);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test join fetching of maps.
     */
    public void testBasicMapJoinFetching() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Buyer> query = em.createQuery("Select b from Buyer b where b.name like :name", Buyer.class);
            query.setHint(QueryHints.FETCH, "e.creditCards");
            query.setHint(QueryHints.FETCH, "e.creditLines");
            query.setParameter("name", "%Gold%");
            List<Buyer> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 2) {
                fail("Should have been 2 query but was: " + counter.getSqlStatements().size());
            }
            for (Buyer buyer : results) {
                buyer.getCreditCards().size();
                buyer.getCreditLines().size();
            }
            int queries = 2;
            if (isWeavingEnabled() && counter.getSqlStatements().size() > queries) {
                fail("Should have been " + queries + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Buyer buyer : results) {
                verifyObject(buyer);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test join fetching of maps.
     */
    public void testBasicMapLeftJoinFetching() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Buyer> query = em.createQuery("Select b from Buyer b where b.name like :name", Buyer.class);
            query.setHint(QueryHints.LEFT_FETCH, "e.creditCards");
            query.setHint(QueryHints.LEFT_FETCH, "e.creditLines");
            query.setParameter("name", "%Gold%");
            List<Buyer> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 2) {
                fail("Should have been 2 query but was: " + counter.getSqlStatements().size());
            }
            boolean found = false;
            for (Buyer buyer : results) {
                found = found || !buyer.getCreditCards().isEmpty();
                found = found || !buyer.getCreditLines().isEmpty();
            }
            assertTrue("No data to join.", found);
            int queries = 2;
            if (isWeavingEnabled() && counter.getSqlStatements().size() > queries) {
                fail("Should have been " + queries + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Buyer buyer : results) {
                verifyObject(buyer);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test join fetching of maps.
     */
    public void testMapKeyJoinFetching() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Department> query = em.createQuery("Select d from ADV_DEPT d", Department.class);
            query.setHint(QueryHints.LEFT_FETCH, "d.equipment");
            query.setHint(QueryHints.LEFT_FETCH, "d.employees");
            query.setHint(QueryHints.LEFT_FETCH, "d.managers");
            List<Department> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 1) {
                fail("Should have been 13 queries but was: " + counter.getSqlStatements().size());
            }
            int queries = 1;
            for (Department department : results) {
                queries = queries + department.getEquipment().size();
                department.getEmployees().size();
                department.getManagers().size();
            }
            assertTrue("No data to join.", queries > 1);
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 1) {
                fail("Should have been " + 1 + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Department department : results) {
                verifyObject(department);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test join fetching of maps.
     */
    public void testMapKeyBatchFetching() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Department> query = em.createQuery("Select d from ADV_DEPT d", Department.class);
            query.setHint(QueryHints.BATCH, "d.equipment");
            query.setHint(QueryHints.BATCH, "d.employees");
            query.setHint(QueryHints.BATCH, "d.managers");
            List<Department> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 1) {
                fail("Should have been 1 queries but was: " + counter.getSqlStatements().size());
            }
            int queries = 1;
            for (Department department : results) {
                queries = queries + department.getEquipment().size();
                department.getEmployees().size();
                department.getManagers().size();
            }
            assertTrue("No data to join.", queries > 1);
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 4) {
                fail("Should have been " + 4 + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Department department : results) {
                verifyObject(department);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test batch fetching using first/max results.
     */
    public void testBatchFetchingPagination() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Employee> query = em.createQuery("Select e from Employee e", Employee.class);
            query.setHint(QueryHints.BATCH_TYPE, BatchFetchType.IN);
            query.setHint(QueryHints.BATCH_SIZE, 5);
            query.setHint(QueryHints.BATCH, "e.address");
            query.setHint(QueryHints.BATCH, "e.manager");
            query.setFirstResult(5);
            query.setMaxResults(5);
            List<Employee> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            if (results.size() > 5) {
                fail("Should have only returned 5 objects but was: " + results.size());
            }
            for (Employee employee : results) {
                employee.getAddress();
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 2) {
                fail("Should have been 2 queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Employee employee : results) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test join fetching using first/max results.
     */
    public void testJoinFetchingPagination() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Employee> query = em.createQuery("Select e from Employee e", Employee.class);
            query.setHint(QueryHints.LEFT_FETCH, "e.address");
            query.setHint(QueryHints.LEFT_FETCH, "e.phoneNumbers");
            query.setFirstResult(5);
            query.setMaxResults(5);
            List<Employee> results = query.getResultList();
            int nExpectedStatements = 3;
            if (usesSOP()) {
                // In SOP case there are no sql to read PhoneNumbers - they are read from sopObject instead.
                nExpectedStatements = 1;
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() != nExpectedStatements) {
                fail("Should have been " + nExpectedStatements + " query but was: " + counter.getSqlStatements().size());
            }
            if (results.size() > 5) {
                fail("Should have only returned 5 objects but was: " + results.size());
            }
            for (Employee employee : results) {
                employee.getAddress();
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() > nExpectedStatements) {
                fail("Should have been " + nExpectedStatements + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            counter.remove();
            counter = null;
            for (Employee employee : results) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test batch fetching using read object query.
     */
    public void testBatchFetchingReadObject() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            Query query = em.createQuery("Select e from Employee e");
            query.setHint(QueryHints.BATCH, "e.managedEmployees");
            query.setHint(QueryHints.BATCH, "e.managedEmployees.address");
            query.setHint(QueryHints.QUERY_TYPE, QueryType.ReadObject);
            Employee result = (Employee)query.getSingleResult();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            for (Employee employee : result.getManagedEmployees()) {
                employee.getAddress();
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 3) {
                fail("Should have been 3 queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            verifyObject(result);
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test batch fetching using first/max results.
     */
    public void testBatchFetchingPagination2() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            TypedQuery<Employee> query = em.createQuery("Select e from Employee e", Employee.class);
            query.setHint(QueryHints.BATCH, "e.address");
            query.setHint(QueryHints.BATCH, "e.manager");
            query.setFirstResult(5);
            query.setMaxResults(5);
            List<Employee> results = query.getResultList();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            if (results.size() > 5) {
                fail("Should have only returned 5 objects but was: " + results.size());
            }
            for (Employee employee : results) {
                employee.getAddress();
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() > 2) {
                fail("Should have been 2 queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Employee employee : results) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test batch fetching using a cursor.
     */
    public void testBatchFetchingCursor() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            Query query = em.createQuery("Select e from Employee e");
            query.setHint(QueryHints.BATCH_TYPE, String.valueOf(BatchFetchType.IN)); // Test as String as well.
            query.setHint(QueryHints.BATCH, "e.address");
            query.setHint(QueryHints.BATCH, "e.manager");
            query.setHint(QueryHints.CURSOR_PAGE_SIZE, 5);
            query.setHint(QueryHints.CURSOR_INITIAL_SIZE, 2);
            @SuppressWarnings({"unchecked"})
            Iterator<Employee> results = (Iterator<Employee>)query.getSingleResult();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            int count = 0;
            List<Employee> employees = new ArrayList<>();
            while (results.hasNext()) {
                Employee employee = results.next();
                employee.getAddress();
                count++;
            }
            if (isWeavingEnabled() && counter.getSqlStatements().size() > (count/5 + 2)) {
                fail("Should have been " + (count/5 + 2) + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Employee employee : employees) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test join fetching using a cursor.
     */
    public void testJoinFetchingCursor() {
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        // Count SQL.
        QuerySQLTracker counter = new QuerySQLTracker(getPersistenceUnitServerSession());
        try {
            Query query = em.createQuery("Select e from Employee e order by e.id"); // Currently need to order for multiple 1-m joins.
            query.setHint(QueryHints.LEFT_FETCH, "e.address");
            //query.setHint(QueryHints.LEFT_FETCH, "e.manager"); - has eagers
            //query.setHint(QueryHints.LEFT_FETCH, "e.projects"); - has eagers
            //query.setHint(QueryHints.LEFT_FETCH, "e.managedEmployees"); - has eagers
            query.setHint(QueryHints.LEFT_FETCH, "e.responsibilities");
            query.setHint(QueryHints.LEFT_FETCH, "e.dealers");
            query.setHint(QueryHints.LEFT_FETCH, "e.phoneNumbers");
            //query.setHint(QueryHints.BATCH, "e.department"); is join fetched already.
            query.setHint(QueryHints.LEFT_FETCH, "e.workWeek");
            query.setHint(QueryHints.CURSOR_PAGE_SIZE, 5);
            query.setHint(QueryHints.CURSOR_INITIAL_SIZE, 2);
            @SuppressWarnings({"unchecked"})
            Iterator<Employee> results = (Iterator<Employee>)query.getSingleResult();
            if (isWeavingEnabled() && counter.getSqlStatements().size() != 1) {
                fail("Should have been 1 query but was: " + counter.getSqlStatements().size());
            }
            int count = 0;
            List<Employee> employees = new ArrayList<>();
            while (results.hasNext()) {
                Employee employee = results.next();
                employees.add(employee);
                employee.getAddress();
                employee.getResponsibilities().size();
                employee.getDealers().size();
                employee.getPhoneNumbers().size();
                employee.getWorkWeek().size();
                count++;
            }
            int queries = 1;
            if (isWeavingEnabled() && counter.getSqlStatements().size() > queries) {
                fail("Should have been " + queries + " queries but was: " + counter.getSqlStatements().size());
            }
            clearCache();
            for (Employee employee : employees) {
                verifyObject(employee);
            }
        } finally {
            rollbackTransaction(em);
            closeEntityManager(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test cache hits on pk JPQL queries.
     */
    public void testJPQLCacheHits() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee");
            List result = query.getResultList();
            Employee employee = (Employee)result.get(result.size() - 1);

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.id = :id");
            query.setParameter("id", employee.getId());
            Employee queryResult = (Employee)query.getSingleResult();
            if (!queryResult.getId().equals(employee.getId())) {
                fail("Employees are not equal: " + employee + ", " + queryResult);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit did not occur: " + counter.getSqlStatements());
            }
        } finally {
            rollbackTransaction(em);
            if (counter != null) {
                counter.remove();
            }
        }
    }

    /**
     * Test cache indexes.
     */
    public void testCacheIndexes() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        QuerySQLTracker counter = null;
        Buyer buyer = null;
        Employee employee = null;
        String lastName = null;
        try {
            // Load an employee into the cache.
            Query query = em.createQuery("Select employee from Employee employee where employee.lastName = 'Chanley'");
            List result = query.getResultList();
            employee = (Employee)result.get(0);
            lastName = employee.getLastName();

            // Count SQL.
            counter = new QuerySQLTracker(getPersistenceUnitServerSession());
            // Query by primary key.
            query = em.createQuery("Select employee from Employee employee where employee.firstName = :firstName and employee.lastName = :lastName");
            query.setParameter("firstName", employee.getFirstName());
            query.setParameter("lastName", employee.getLastName());
            counter.getSqlStatements().clear();
            Employee queryResult = (Employee)query.getSingleResult();
            if (queryResult != employee) {
                fail("Employees are not equal: " + employee + ", " + queryResult);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
            employee.setLastName("fail");
            commitTransaction(em);
            query = em.createQuery("Select employee from Employee employee where employee.firstName = :firstName and employee.lastName = :lastName");
            query.setParameter("firstName", employee.getFirstName());
            query.setParameter("lastName", lastName);
            counter.getSqlStatements().clear();
            try {
                queryResult = null;
                queryResult = (Employee)query.getSingleResult();
            } catch (NoResultException ignore) {}
            if (queryResult != null) {
                fail("Employees should not be found, " + queryResult);
            }
            if (counter.getSqlStatements().isEmpty()) {
                fail("Cache hit should not occur: " + counter.getSqlStatements());
            }
            closeEntityManager(em);
            em = createEntityManager();
            beginTransaction(em);
            buyer = new Buyer();
            buyer.setName("index");
            buyer.setDescription("description");
            em.persist(buyer);
            commitTransaction(em);
            query = em.createQuery("Select b from Buyer b where b.name = :name");
            query.setParameter("name", buyer.getName());
            counter.getSqlStatements().clear();
            Buyer queryResult2 = (Buyer)query.getSingleResult();
            if (!queryResult2.getName().equals(buyer.getName())) {
                fail("Buyers are not equal: " + buyer + ", " + queryResult2);
            }
            if (!counter.getSqlStatements().isEmpty()) {
                fail("Cache hit do not occur: " + counter.getSqlStatements());
            }
        } finally {
            if (counter != null) {
                counter.remove();
            }
            closeEntityManagerAndTransaction(em);
            em = createEntityManager();
            beginTransaction(em);
            if (buyer != null) {
                buyer = em.find(Buyer.class, buyer.getId());
                em.remove(buyer);
            }
            if (employee != null) {
                Employee reset = em.find(Employee.class, employee.getId());
                reset.setLastName(lastName);
            }
            commitTransaction(em);
            closeEntityManagerAndTransaction(em);
        }
    }

    public void testQueryPESSIMISTICLockWithLimit() throws InterruptedException {
        if (!isSelectForUpateSupported()) {
            return;
        }
        clearCache();
        EntityManager em = createEntityManager();
        beginTransaction(em);
        try {
            TypedQuery<Employee> query = em.createQuery("Select e from Employee e where e.lastName != :lastName", Employee.class);
            query.setParameter("lastName", "Chanley");
            query.setHint(QueryHints.PESSIMISTIC_LOCK, PessimisticLock.Lock);
            query.setFirstResult(5);
            query.setMaxResults(2);
            List<Employee> results = query.getResultList();
            final Employee e = results.get(0);
            final String name = e.getFirstName();
            if (results.size() > 2) {
                fail("Should have only returned 2 objects but was: " + results.size());
            }
            clearCache();

            final EntityManager em2 = createEntityManager();
            try {
                // P2 (Non-repeatable read)
                Runnable runnable = new Runnable() {
                    @Override
                        public void run() {
                        try {
                            beginTransaction(em2);
                            TypedQuery<Employee> query2 = em2.createQuery("select e from Employee e where e.id = :id", Employee.class);
                            query2.setParameter("id", e.getId());
                            query2.setHint(QueryHints.PESSIMISTIC_LOCK_TIMEOUT, 5000);
                            Employee emp = query2.getSingleResult(); // might wait for lock to be released
                            emp.setFirstName("Trouba");
                            commitTransaction(em2); // might wait for lock to be released
                        } catch (jakarta.persistence.RollbackException ex) {
                            if (!ex.getMessage().contains("org.eclipse.persistence.exceptions.DatabaseException")) {
                                ex.printStackTrace();
                                fail("it's not the right exception:" + ex);
                            }
                        } finally {
                            if (isTransactionActive(em2)) {
                                rollbackTransaction(em2);
                            }
                        }
                    }
                };

                Thread t2 = new Thread(runnable);
                t2.start();
                Thread.sleep(1000); // allow t2 to attempt update
                em.refresh(e);
                assertEquals("pessimistic lock failed: parallel transaction modified locked entity (non-repeatable read)", name, e.getFirstName());
                rollbackTransaction(em); // release lock
                t2.join(); // wait until t2 finished
            } finally {
                if (isTransactionActive(em2)) {
                    rollbackTransaction(em2);
                }
                closeEntityManager(em2);
            }
        } finally {
            if (isTransactionActive(em)) {
                rollbackTransaction(em);
            }
            closeEntityManager(em);
        }
    }

    // Based on reproduction scenario from issue #2301 (https://github.com/eclipse-ee4j/eclipselink/issues/2301)
    public void testFloatSortWithPessimisticLock() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        List<EntityFloat> entities;
        try {
            entities = em.createQuery("SELECT f FROM EntityFloat f WHERE (f.height < ?1) ORDER BY f.height DESC, f.length",
                           EntityFloat.class)
                    .setParameter(1, 8.0)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE) // Cause of issue
                    .setMaxResults(2)
                    .getResultList();
            commitTransaction(em);
        } catch (PersistenceException ex) {
            rollbackTransaction(em);
            throw ex;
        }
        assertEquals(2, entities.size());
        assertEquals(70071, entities.get(0).getId());
        assertEquals(70077, entities.get(1).getId());
    }

    // Based on reproduction scenario from issue #2339
    public void testFloatQualifiedIdProjectionWithPessimisticLock() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        List<Integer> results;
        try {
            results = em.createQuery("SELECT f.id FROM EntityFloat f ORDER BY f.width DESC", Integer.class)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .setMaxResults(1)
                    .getResultList();
            commitTransaction(em);
        } catch (PersistenceException ex) {
            rollbackTransaction(em);
            throw ex;
        }
        assertEquals(1, results.size());
        assertEquals(70077, results.get(0).intValue());
    }

    // Based on reproduction scenario from issue #2339
    public void testFloatSimpleIdProjectionWithPessimisticLock() {
        EntityManager em = createEntityManager();
        beginTransaction(em);
        List<Integer> results;
        try {
            results = em.createQuery("SELECT id FROM EntityFloat ORDER BY width DESC", Integer.class)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .setMaxResults(1)
                    .getResultList();
            commitTransaction(em);
        } catch (PersistenceException ex) {
            rollbackTransaction(em);
            throw ex;
        }
        assertEquals(1, results.size());
        assertEquals(70077, results.get(0).intValue());
    }

}
