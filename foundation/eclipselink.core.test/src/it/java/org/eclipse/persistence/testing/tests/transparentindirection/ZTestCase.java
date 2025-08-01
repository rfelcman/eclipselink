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
package org.eclipse.persistence.testing.tests.transparentindirection;

import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * provide support for zunit stuff...
 */
public class ZTestCase extends AutoVerifyTestCase {
    private final String zName;

    /**
     * Construct a test with the specified name.
     */
    public ZTestCase(String zName) {
        super();
        this.zName = zName;
        this.setName(this.getName() + "." + zName + "()");
        this.initialize();
    }

    /**
     * Assert that the elements in two vectors are equal. If they are not,
     * throw an AssertionFailedError. Order of the elements is significant.
     * @param message the error message
     * @param expected the expected value of an vector
     * @param actual the actual value of an vector
     */
    protected void assertElementsEqual(String message, Vector expected, Vector actual) {
        if (expected == actual) {
            return;
        }
        if (expected.size() != actual.size()) {
            fail(notEqualsMessage(message, expected, actual));
        }
        for (int i = 0; i < expected.size(); i++) {
            Object e1 = expected.get(i);
            Object e2 = actual.get(i);
            if (e1 == null) {// avoid null pointer exception
                if (e2 != null) {
                    fail(notEqualsMessage(message, expected, actual));
                }
            } else {
                if (!e1.equals(e2)) {
                    fail(notEqualsMessage(message, expected, actual));
                }
            }
        }
    }

    /**
     * Assert that the elements in two vectors are equal. If they are not,
     * throw an AssertionFailedError. Order of the elements is significant.
     * @param expected the expected value of an vector
     * @param actual the actual value of an vector
     */
    protected void assertElementsEqual(Vector expected, Vector actual) {
        this.assertElementsEqual("", expected, actual);
    }

    /**
     * Assert that the elements in two vectors are equal. If they are not,
     * throw an AssertionFailedError. The order of the elements is ignored.
     * @param message the error message
     * @param expected the expected value of an vector
     * @param actual the actual value of an vector
     */
    protected static void assertUnorderedElementsEqual(String message, Vector expected, Vector actual) {
        if (expected == actual) {
            return;
        }
        if (expected.size() != actual.size()) {
            fail(notEqualsMessage(message, expected, actual));
        }
        Vector temp = (Vector)actual.clone();
        for (int i = 0; i < expected.size(); i++) {
            Object e1 = expected.get(i);
            if (e1 == null) {// avoid null pointer exception
                if (!removeNullElement(temp)) {
                    fail(notEqualsMessage(message, expected, actual));
                }
            } else {
                if (!temp.remove(e1)) {
                    fail(notEqualsMessage(message, expected, actual));
                }
            }
        }
    }

    /**
     * Assert that the elements in two vectors are equal. If they are not,
     * throw an AssertionFailedError. The order of the elements is ignored.
     * @param expected the expected value of an vector
     * @param actual the actual value of an vector
     */
    public static void assertUnorderedElementsEqual(Vector expected, Vector actual) {
        assertUnorderedElementsEqual("", expected, actual);
    }

    /**
     * Return the zunit name of the test.
     */
    public String getZName() {
        return zName;
    }

    /**
     * Initialize the instance. Useful for subclasses.
     */
    protected void initialize() {
    }

    /**
     * invoke the test method
     */
    protected void invokeTest() throws Throwable {
        Method method = this.methodNamed(this.getZName());
        try {
            method.invoke(this);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException("The method '" + method + "' (and its class) must be public.");
        } catch (InvocationTargetException ite) {
            ite.fillInStackTrace();
            throw ite.getTargetException();
        }
    }

    /**
     * Return the zero-argument method with the specified name.
     */
    private Method methodNamed(String name) {
        try {
            return this.getClass().getMethod(zName);
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("Method named " + name + " not found.");
        }
    }

    /**
     * Return a nicely formatted message for when the actual value did not
     * equal the expected one.
     */
    public static String notEqualsMessage(String message, Object expected, Object actual) {
        StringBuilder buffer = new StringBuilder(250);
        if ((message != null) && (!message.isEmpty())) {
            buffer.append(message);
            buffer.append(" ");
        }
        buffer.append("expected: \"");
        buffer.append(expected);
        buffer.append("\" but was: \"");
        buffer.append(actual);
        buffer.append("\"");
        return buffer.toString();
    }

    /**
     * Remove the first null element found in the specified vector.
     * Return true if a null element was found and removed.
     * Return false if a null element was not found.
     */
    private static boolean removeNullElement(Vector v) {
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i) == null) {
                v.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Override the TestCase method to call the zunit method.
     */
    @Override
    public void reset() {
        this.tearDown();
    }

    /**
     * Override the TestCase method to call the zunit method.
     */
    @Override
    protected void setup() {
        this.setUp();
    }

    /**
     * Set up the fixture (e.g. open a network connection).
     * This method is called before a test is executed.
     */
    @Override
    protected void setUp() {
    }

    /**
     * Tear down the fixture (e.g. close a network connection).
     * This method is called after a test is executed.
     */
    @Override
    protected void tearDown() {
    }

    /**
     * Reflectively invoke the test method.
     */
    @Override
    protected void test() throws Exception {
        try {
            this.invokeTest();
        } catch (Throwable ex) {
            throw new TestErrorException("Error in test Case: " + this.getName(), ex);
        }
    }
}
