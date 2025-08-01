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
package org.eclipse.persistence.testing.tests.performance;

import org.eclipse.persistence.Version;
import org.eclipse.persistence.internal.helper.Helper;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.testing.framework.TestModel;
import org.eclipse.persistence.testing.framework.TestSuite;
import org.eclipse.persistence.testing.models.performance.Address;
import org.eclipse.persistence.testing.models.performance.EmploymentPeriod;
import org.eclipse.persistence.testing.models.performance.toplink.Employee;
import org.eclipse.persistence.testing.models.performance.toplink.EmployeeSystem;
import org.eclipse.persistence.testing.models.performance.toplink.PhoneNumber;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllAddressTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllBigBadObjectTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllCompletelyBatchedEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllCompletelyEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllCompletelyJoinedEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllComplexExpressionEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllComplexExpressionUnitOfWorkConformEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllExpressionInheritanceProjectTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllHugeCacheAddressTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllInMemoryComplexExpressionEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllInMemoryEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllUnitOfWorkConformingEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadAllUnitOfWorkEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectByPrimaryKeyAddressTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectByPrimaryKeyBigBadObjectTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectByPrimaryKeyEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectCompletelyEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectComplexExpressionEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectInMemoryComplexExpressionEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectInMemoryEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.reading.ReadObjectNamedQueryEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.writing.ComplexUpdateEmployeeUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.InsertAddressUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.InsertBigBadObjectUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.InsertDeleteAddressUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.InsertEmployeeUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.UnitOfWorkNoChangesClientSessionEmployeeTest;
import org.eclipse.persistence.testing.tests.performance.writing.UpdateAddressUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.UpdateBigBadObjectUnitOfWorkTest;
import org.eclipse.persistence.testing.tests.performance.writing.UpdateEmployeeUnitOfWorkTest;

import java.io.IOException;
import java.io.Writer;

/**
 * This tests the performance of various toplink operations/ fine grained use cases. Its purpose is
 * to compare the test result with previous release/label results. It also provides a useful test
 * for profiling performance.
 */
public class PerformanceTestModel extends TestModel {
    protected Session oldSession;

    public PerformanceTestModel() {
        setDescription("This tests the performance of various toplink operations/ fine grained use cases.");
    }

    @Override
    public void addRequiredSystems() {
        addRequiredSystem(new EmployeeSystem());
        addRequiredSystem(new org.eclipse.persistence.testing.models.bigbad.BigBadSystem());
    }

    @Override
    public void addTests() {
        addTest(getReadingTestSuite());
        addTest(getWritingTestSuite());
    }

    public TestSuite getReadingTestSuite() {
        TestSuite suite = new TestSuite();
        suite.setName("ReadingTestSuite");
        suite.setDescription("This suite tests reading performance.");

        PerformanceTest test;
        suite.addTest(new ReadObjectByPrimaryKeyEmployeeTest());
        test = new ReadObjectByPrimaryKeyEmployeeTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadObjectByPrimaryKeyEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadObjectByPrimaryKeyEmployeeTest();
        test.setShouldUseEmulatedDB(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadObjectByPrimaryKeyAddressTest());
        test = new ReadObjectByPrimaryKeyAddressTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadObjectByPrimaryKeyAddressTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadObjectByPrimaryKeyBigBadObjectTest());
        test = new ReadObjectByPrimaryKeyBigBadObjectTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadObjectByPrimaryKeyBigBadObjectTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadObjectCompletelyEmployeeTest());
        test = new ReadObjectCompletelyEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadObjectComplexExpressionEmployeeTest());
        //test = new ReadObjectComplexExpressionEmployeeTest();
        //test.setShouldCache(true);
        //suite.addTest(test);
        test = new ReadObjectComplexExpressionEmployeeTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        suite.addTest(new ReadObjectInMemoryEmployeeTest());
        suite.addTest(new ReadObjectInMemoryComplexExpressionEmployeeTest());

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadObjectNamedQueryEmployeeTest());
        test = new ReadObjectNamedQueryEmployeeTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadObjectNamedQueryEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        suite.addTest(new ReadAllEmployeeTest());
        test = new ReadAllEmployeeTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllEmployeeTest();
        test.setShouldUseEmulatedDB(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllUnitOfWorkEmployeeTest());
        test = new ReadAllUnitOfWorkEmployeeTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllUnitOfWorkEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllComplexExpressionUnitOfWorkConformEmployeeTest());
        test = new ReadAllComplexExpressionUnitOfWorkConformEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllUnitOfWorkConformingEmployeeTest());
        test = new ReadAllUnitOfWorkConformingEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllComplexExpressionEmployeeTest());
        test = new ReadAllComplexExpressionEmployeeTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllComplexExpressionEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        suite.addTest(new ReadAllInMemoryEmployeeTest());
        suite.addTest(new ReadAllInMemoryComplexExpressionEmployeeTest());

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllCompletelyEmployeeTest());
        test = new ReadAllCompletelyEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllCompletelyJoinedEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllCompletelyBatchedEmployeeTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllAddressTest());
        test = new ReadAllAddressTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllAddressTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ReadAllBigBadObjectTest());
        test = new ReadAllBigBadObjectTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllBigBadObjectTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        suite.addTest(new ReadAllExpressionInheritanceProjectTest());
        test = new ReadAllExpressionInheritanceProjectTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllExpressionInheritanceProjectTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - uite.addTest(new ReadAllHugeCacheAddressTest());
        test = new ReadAllHugeCacheAddressTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ReadAllHugeCacheAddressTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        return suite;
    }

    public TestSuite getWritingTestSuite() {
        TestSuite suite = new TestSuite();
        suite.setName("WritingTestSuite");
        suite.setDescription("This suite tests unit of work and writing performance.");

        PerformanceTest test;
        // remove dynamic tests as not recommend usage - suite.addTest(new UpdateEmployeeUnitOfWorkTest());
        test = new UpdateEmployeeUnitOfWorkTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new UpdateEmployeeUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new UpdateEmployeeUnitOfWorkTest();
        test.setShouldUseEmulatedDB(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new ComplexUpdateEmployeeUnitOfWorkTest());
        test = new ComplexUpdateEmployeeUnitOfWorkTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new ComplexUpdateEmployeeUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new UpdateAddressUnitOfWorkTest());
        test = new UpdateAddressUnitOfWorkTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new UpdateAddressUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new UpdateBigBadObjectUnitOfWorkTest());
        test = new UpdateBigBadObjectUnitOfWorkTest();
        test.setShouldCache(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        //test = new UpdateBigBadObjectUnitOfWorkTest();
        //test.setShouldUseParameterizedSQL(true);
        //suite.addTest(test);

        suite.addTest(new UnitOfWorkNoChangesClientSessionEmployeeTest());

        // remove dynamic tests as not recommend usage - suite.addTest(new InsertAddressUnitOfWorkTest());
        test = new InsertAddressUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        suite.addTest(new InsertEmployeeUnitOfWorkTest());
        test = new InsertEmployeeUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new InsertEmployeeUnitOfWorkTest();
        test.setShouldUseEmulatedDB(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new InsertBigBadObjectUnitOfWorkTest());
        test = new InsertBigBadObjectUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new InsertDeleteAddressUnitOfWorkTest());
        test = new InsertDeleteAddressUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        //Comment out all mass insert tests because they hangs when being run with Oracle 11gR1 database
        /*
        // remove dynamic tests as not recommend usage - suite.addTest(new MassInsertEmployeeUnitOfWorkTest());
        test = new MassInsertEmployeeUnitOfWorkTest();
        test.setShouldBatch(true);
        suite.addTest(test);
        test = new MassInsertEmployeeUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new MassInsertEmployeeUnitOfWorkTest();
        test.setShouldBatch(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new MassInsertAddressUnitOfWorkTest());
        test = new MassInsertAddressUnitOfWorkTest();
        test.setShouldBatch(true);
        suite.addTest(test);
        test = new MassInsertAddressUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new MassInsertAddressUnitOfWorkTest();
        test.setShouldBatch(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);

        // remove dynamic tests as not recommend usage - suite.addTest(new VeryVeryMassInsertAddressUnitOfWorkTest());
        test = new VeryVeryMassInsertAddressUnitOfWorkTest();
        test.setShouldBatch(true);
        suite.addTest(test);
        test = new VeryVeryMassInsertAddressUnitOfWorkTest();
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        test = new VeryVeryMassInsertAddressUnitOfWorkTest();
        test.setShouldBatch(true);
        test.setShouldUseParameterizedSQL(true);
        suite.addTest(test);
        */

        return suite;
    }

    @Override
    public void setup() {
        for (int j = 0; j < 100; j++) {
            Employee empInsert = new Employee();
            empInsert.setFirstName("Brendan");
            empInsert.setMale();
            empInsert.setLastName("" + j);
            empInsert.setSalary(100000);
            EmploymentPeriod employmentPeriod = new EmploymentPeriod();
            java.sql.Date startDate = Helper.dateFromString("1901-12-31");
            java.sql.Date endDate = Helper.dateFromString("1895-01-01");
            employmentPeriod.setEndDate(endDate);
            employmentPeriod.setStartDate(startDate);
            empInsert.setPeriod(employmentPeriod);
            empInsert.setAddress(new Address());
            empInsert.getAddress().setCity("Nepean");
            empInsert.getAddress().setPostalCode("N5J2N5");
            empInsert.getAddress().setProvince("ON");
            empInsert.getAddress().setStreet("1111 Mountain Blvd. Floor 13, suite " + j);
            empInsert.getAddress().setCountry("Canada");
            empInsert.addPhoneNumber(new PhoneNumber("Work Fax", "613", "2255943"));
            empInsert.addPhoneNumber(new PhoneNumber("Home", "613", "2224599"));
            getDatabaseSession().insertObject(empInsert);
        }

        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        oldSession = getSession();
        DatabaseSession session = getSession().getProject().createServerSession();

        // Enable binding for 10.1.3 runs.
        if (Version.getVersion().contains("10.1.3")) {
            session.getLogin().setShouldBindAllParameters(true);
        }

        // Add a large project to test performance of large projects.
        session.addDescriptors(new org.eclipse.persistence.testing.models.interfaces.InterfaceHashtableProject());
        session.setSessionLog(getSession().getSessionLog());
        session.login();
        getExecutor().setSession(session);
    }

    @Override
    public void reset() {
        if (oldSession != null) {
            getDatabaseSession().logout();
            getExecutor().setSession(oldSession);
        }
    }

    /**
     * Also log the baseline version that was set.
     */
    @Override
    protected void logHeadNote(Writer log) {
        super.logHeadNote(log);
        try {
            if (!System.getProperties().containsKey("toplink.loadbuild.baseline-version")) {
                log.write(getIndentationString() + "BASELINE VERSION: NONE SET" + System.lineSeparator());
            } else {
                String baselineVersion = System.getProperties().getProperty("toplink.loadbuild.baseline-version");
                log.write(getIndentationString() + "BASELINE VERSION: " + baselineVersion + System.lineSeparator());
            }
        } catch (IOException exception) {
        }
    }
}
