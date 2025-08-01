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
package org.eclipse.persistence.testing.tests.performance.java;

import org.eclipse.persistence.testing.framework.PerformanceComparisonTestCase;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

/**
 * This test compares the performance for Calendar getTime.
 */
public class DatePrintingTest extends PerformanceComparisonTestCase {
    protected Date date = new Date();

    public DatePrintingTest() {
        setName("DatePrintingPerformanceComparisonTest");
        setDescription("This test compares the performance in printing dates.");
        addCalendarPrintTest();
    }

    /**
     * Prints using deprecate APIs.
     */
    @Override
    @SuppressWarnings("deprecation")
    public void test() throws Exception {
        StringWriter writer = new StringWriter();
        writer.write(String.valueOf(this.date.getYear()));
        writer.write(String.valueOf(this.date.getDate()));
        writer.write(String.valueOf(this.date.getMonth()));
        writer.write(String.valueOf(this.date.getHours()));
        writer.write(String.valueOf(this.date.getMinutes()));
        writer.write(String.valueOf(this.date.getSeconds()));
        writer.toString();
    }

    /**
     * Print through converting to calendar.
     */
    public void addCalendarPrintTest() {
        PerformanceComparisonTestCase test = new PerformanceComparisonTestCase() {
            @Override
            public void test() {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DatePrintingTest.this.date);
                StringWriter writer = new StringWriter();
                writer.write(String.valueOf(calendar.get(Calendar.YEAR)));
                writer.write(String.valueOf(calendar.get(Calendar.DATE)));
                writer.write(String.valueOf(calendar.get(Calendar.MONTH)));
                writer.write(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
                writer.write(String.valueOf(calendar.get(Calendar.MINUTE)));
                writer.write(String.valueOf(calendar.get(Calendar.SECOND)));
                writer.toString();
            }
        };
        test.setName("CalendarPrintTest");
        //test.setAllowableDecrease(-50);
        addTest(test);
    }
}
