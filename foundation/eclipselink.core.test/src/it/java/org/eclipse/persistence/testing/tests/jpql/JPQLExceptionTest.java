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
package org.eclipse.persistence.testing.tests.jpql;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.exceptions.EclipseLinkException;
import org.eclipse.persistence.exceptions.JPQLException;
import org.eclipse.persistence.queries.ObjectLevelReadQuery;
import org.eclipse.persistence.queries.ReadAllQuery;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.framework.TestSuite;

import java.util.Vector;

public class JPQLExceptionTest extends JPQLTestCase {
    public EclipseLinkException expectedException;
    public EclipseLinkException caughtException;
    public ClassDescriptor oldDescriptor;

    public static JPQLExceptionTest recognitionExceptionTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.syntaxErrorAt(null, 0, 0, null, null);
        theTest.setEjbqlString("SELECT OBJECT(emp) FROW Employee emp");
        theTest.setName("Recognition Exception Test");
        return theTest;
    }

    public static JPQLExceptionTest missingSelectExceptionTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.unexpectedToken(null, 0, 0, null, null);
        theTest.setEjbqlString("OBJECT(emp) FROM Employee emp");
        theTest.setName("Missing Select Exception Test");
        return theTest;
    }

    public static JPQLExceptionTest malformedEjbqlExceptionTest1() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.unexpectedToken(null, 0, 0, null, null);
        theTest.setEjbqlString("SELECT OBJECT(emp) FROM Employee emp WHERE emp.firstName == \"Fred\"");
        theTest.setName("Malformed EJBQL Exception Test1");
        return theTest;
    }

    public static JPQLExceptionTest malformedEjbqlExceptionTest2() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.unexpectedEOF(null, 0, 0, null);
        theTest.setEjbqlString("SELECT OBJECT(emp) FROM Employee emp WHERE emp.firstName = \"Fred\" AND 1");
        theTest.setName("Malformed EJBQL Exception Test2");
        return theTest;
    }

    public static JPQLExceptionTest malformedEjbqlExceptionTest3() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.unexpectedEOF(null, 0, 0, null);
        theTest.setEjbqlString("SELECT OBJECT(emp) FROM Employee emp WHERE emp.firstName = \"Fred\" OR \"Freda\"");
        theTest.setName("Malformed EJBQL Exception Test3");
        return theTest;
    }

    public static JPQLExceptionTest malformedEjbqlExceptionTest4() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.unexpectedToken(null, 0, 0, null, null);
        theTest.setEjbqlString("SLEECT OBJECT(emp) FROM Employee emp WHERE emp.firstName = \"Fred\" OR \"Freda\"");
        theTest.setName("Malformed EJBQL Exception Test4");
        return theTest;
    }

    public static JPQLExceptionTest badAliasExceptionTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.aliasResolutionException(null, 0, 0, null);
        theTest.setEjbqlString("SELECT OBJECT(emp) FROM Employee employee WHERE emp.firstName = \"Fred\"");
        theTest.setName("Bad Alias Exception Test 2");
        return theTest;
    }

    public static JPQLExceptionTest generalExceptionTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.unexpectedToken(null, 0, 0, null, null);
        theTest.setEjbqlString("SELECT FROM EMPLOYEE emp");
        theTest.setName("General Exception test");

        return theTest;
    }

    public static JPQLExceptionTest classNotFoundExceptionTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();

        theTest.expectedException = JPQLException.resolutionClassNotFoundException(null, null);
        theTest.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Address.class);
        theTest.setEjbqlString("SELECT OBJECT(a) FROM AddressBean a WHERE a.city = \"Ottawa\"");
        theTest.setName("Class Not Found Exception test");

        return theTest;
    }

    public static JPQLExceptionTest aliasResolutionException() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.aliasResolutionException(null, 0, 0, null);
        theTest.setName("Bad Alias Exception test 1");
        theTest.setEjbqlString("SELECT OBJECT(nullRoot) FROM Employee emp WHERE emp.firstName = \"Fred\"");

        return theTest;
    }

    public static JPQLExceptionTest missingDescriptorExceptionTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.entityTypeNotFound2(null, 0, 0, null);
        theTest.setName("Missing Descriptor Exception test");
        theTest.setEjbqlString("SELECT OBJECT(i) FROM Integer i WHERE i.city = \"Ottawa\"");

        return theTest;
    }

    public static JPQLExceptionTest expressionNotSupportedTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.expressionNotSupported(null, null);
        theTest.setName("Expression Not Supported Exception test");
        theTest.setEjbqlString("SELECT OBJECT(emp) FROM Employee emp WHERE emp.phoneNumbers IS EMPTY");

        return theTest;
    }

    public static JPQLExceptionTest memberOfNotSupportedTest() {
        JPQLExceptionTest theTest = new JPQLExceptionTest();
        theTest.expectedException = JPQLException.expressionNotSupported(null, null);
        theTest.setName("MEMBER OF Not Supported Exception test");
        theTest.setEjbqlString("SELECT OBJECT(proj) FROM Employee emp, Project proj " + " WHERE  (proj.teamLeader MEMBER OF emp.manager.managedEmployees) " + "AND (emp.lastName = \"Chan\")");
        return theTest;
    }

    public static void addTestsTo(TestSuite theSuite) {
        //Descriptor exception will happen first--this makes the next test unnecessary
        //theSuite.addTest(EJBQLExceptionTest.classNotFoundExceptionTest());
        theSuite.addTest(JPQLExceptionTest.generalExceptionTest());
        theSuite.addTest(JPQLExceptionTest.missingDescriptorExceptionTest());
        theSuite.addTest(JPQLExceptionTest.aliasResolutionException());
        theSuite.addTest(JPQLExceptionTest.recognitionExceptionTest());

        theSuite.addTest(JPQLExceptionTest.malformedEjbqlExceptionTest1());
        theSuite.addTest(JPQLExceptionTest.malformedEjbqlExceptionTest2());
        theSuite.addTest(JPQLExceptionTest.malformedEjbqlExceptionTest3());
        theSuite.addTest(JPQLExceptionTest.malformedEjbqlExceptionTest4());
        theSuite.addTest(JPQLExceptionTest.missingSelectExceptionTest());
        theSuite.addTest(JPQLExceptionTest.badAliasExceptionTest());
        // Removed by JGL - IS [NOT] EMPTY is now supported - BUG 2775179
        // theSuite.addTest(EJBQLExceptionTest.expressionNotSupportedTest());
        // Removed by JED - Member of is now supported
        // theSuite.addTest(EJBQLExceptionTest.memberOfNotSupportedTest());
    }

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void test() {
        try {
            getSession().logMessage("Running EJBQL -> " + getEjbqlString());
            setReturnedObjects(getSession().executeQuery(getQuery()));
        } catch (EclipseLinkException tle) {
            caughtException = tle;
        } catch (Exception e) {
            caughtException = new TestErrorException(e.getMessage());
        }
    }

    @Override
    public void verify() {
        if (caughtException == null) {
            throw new TestErrorException("The proper exception was not thrown:" + System.lineSeparator() + "caught exception was null! \n\n[EXPECTING] " + expectedException);
        }

        if (caughtException instanceof JPQLException) {
            return;
        }

        if (caughtException.getErrorCode() == expectedException.getErrorCode()) {
            return;
        }
        if (caughtException.getClass() == JPQLException.class) {
            Vector exceptions = (Vector)((JPQLException)caughtException).getInternalExceptions();
            if (!exceptions.isEmpty()) {
                JPQLException internalException = (JPQLException)exceptions.get(0);
                if (internalException.getErrorCode() == expectedException.getErrorCode()) {
                    return;
                }
            }
        }
        throw new TestErrorException("The proper exception was not thrown:" + System.lineSeparator() + "[CAUGHT] " + caughtException + "\n\n[EXPECTING] " + expectedException);
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public ObjectLevelReadQuery getQuery() {
        if (theQuery == null) {
            if (useReportQuery) {
                setQuery(buildReportQuery());
            } else {
                setQuery(new ReadAllQuery());
            }
            getQuery().setEJBQLString(getEjbqlString());
            getQuery().setReferenceClass(getReferenceClass());
        }
        return theQuery;
    }
}
