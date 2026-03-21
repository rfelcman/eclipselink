/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation. All rights reserved.
 * Copyright (c) 1998, 2024 Oracle and/or its affiliates. All rights reserved.
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
//     ailitchev - Bug 256296: Reconnect fails when session loses connectivity;
//                 Bug 256284: Closing anEMF where the database is unavailable results in deployment exception on redeploy.
package org.eclipse.persistence.testing.dbdriver.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

public class DriverWrapper implements Driver {

    // the wrapped driver name
    static String driverName;
    // the wrapped driver
    static Driver driver;

    // if set to true then methods called on the driver throw exception
    static boolean driverBroken;
    static String driverBrokenExceptionString;
    public static String defaultDriverBrokenExceptionString = "DriverWrapper: driver is broken";

    // if set to true then methods called on the connections already acquired throw exception
    static boolean oldConnectionsBroken;
    static String oldConnectionsBrokenExceptionString;
    public static String defaultOldConnectionsBrokenExceptionString =  "DriverWrapper: old connections are broken";

    // if set to true then methods called on the newly acquired connections will throw exception
    static boolean newConnectionsBroken;
    static String newConnectionsBrokenExceptionString;
    public static String defaultNewConnectionsBrokenExceptionString =  "DriverWrapper: new connections are broken";

    // all created ConnectionWrappers are cached
    static HashSet<ConnectionWrapper> connections = new HashSet<>();

    // register with DriverManager
    static {
        try {
            DriverManager.registerDriver(new DriverWrapper());
        } catch (SQLException ex) {
            throw new RuntimeException("registerDriver failed for DriverWrapper", ex);
        }
    }

    public DriverWrapper() {
    }

    public static String codeUrl(String url) {
        return url.replace(':', '*');
    }
    public static String decodeUrl(String url) {
        return url.replace('*', ':');
    }
    public static void initialize(String newDriverName) {
        clear();
        driverName = newDriverName;
    }

    public static void breakDriver() {
        breakDriver(defaultDriverBrokenExceptionString);
    }
    public static void breakDriver(String exceptionString) {
        driverBroken = true;
        driverBrokenExceptionString = exceptionString;
    }
    public static void repairDriver() {
        driverBroken = false;
        driverBrokenExceptionString = null;
    }

    public static void breakOldConnections() {
        breakOldConnections(defaultOldConnectionsBrokenExceptionString);
    }
    public static void breakOldConnections(String exceptionString) {
        oldConnectionsBroken = true;
        oldConnectionsBrokenExceptionString = exceptionString;
        for (ConnectionWrapper connection : connections) {
            connection.breakConnection(oldConnectionsBrokenExceptionString);
        }
    }
    public static void repairOldConnections() {
        oldConnectionsBroken = false;
        oldConnectionsBrokenExceptionString = null;
        for (ConnectionWrapper connection : connections) {
            connection.repairConnection();
        }
    }

    public static void breakNewConnections() {
        breakNewConnections(defaultNewConnectionsBrokenExceptionString);
    }
    public static void breakNewConnections(String exceptionString) {
        newConnectionsBroken = true;
        newConnectionsBrokenExceptionString = exceptionString;
    }
    public static void repairNewConnections() {
        newConnectionsBroken = false;
        newConnectionsBrokenExceptionString = null;
    }

    public static void breakAll() {
        breakDriver();
        breakNewConnections();
        breakOldConnections();
    }

    public static void repairAll() {
        repairDriver();
        repairNewConnections();
        repairOldConnections();
    }

    public static void clear() {
        repairAll();
        Iterator<ConnectionWrapper> it = connections.iterator();
        while(it.hasNext()) {
            try {
                it.next().close();
            } catch (SQLException ex) {
                //ignore
            }
        }
        connections.clear();
        driver = null;
        driverName = null;
    }

    static Driver getDriver() {
        if(driver == null) {
            try {
                driver = (Driver) Class.forName(driverName, true, Thread.currentThread().getContextClassLoader()).getConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("DriverWrapper: failed to instantiate " + driverName, ex);
            }
        }
        return driver;
    }

    public static boolean driverBroken() {
        return driverBroken;
    }
    public static String getDriverBrokenExceptionString() {
        return driverBrokenExceptionString;
    }

    public static boolean oldConnectionsBroken() {
        return oldConnectionsBroken;
    }
    public static String getOldConnectionsBrokenExceptionString() {
        return oldConnectionsBrokenExceptionString;
    }

    public static boolean newConnectionsBroken() {
        return newConnectionsBroken;
    }
    public static String getNewConnectionsBrokenExceptionString() {
        return newConnectionsBrokenExceptionString;
    }

    /*
     * The following methods implement Driver interface
     */
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if(driverBroken) {
            throw new SQLException(getDriverBrokenExceptionString());
        }
        String decodedUrl = decodeUrl(url);
        if(driverName != null) {
            Connection internalConn = getDriver().connect(decodedUrl, info);
            if (internalConn == null) {
                // The driver should return "null" if it realizes it is the wrong kind of driver to connect to the given URL.
                return null;
            }
            ConnectionWrapper conn = new ConnectionWrapper(internalConn);
            connections.add(conn);
            return conn;
        } else {
            // non-initialized DriverWrapper should be ignored by DriverManager.
            return null;
        }
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if(driverName != null) {
            if(driverBroken) {
                throw new SQLException(getDriverBrokenExceptionString());
            }
            String decodedUrl = decodeUrl(url);
            return getDriver().acceptsURL(decodedUrl);
        } else {
            // non-initialized DriverWrapper should be ignored by DriverManager.
            return false;
        }
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        if(driverBroken) {
            throw new SQLException(getDriverBrokenExceptionString());
        }
        return getDriver().getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return getDriver().getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return getDriver().getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return getDriver().jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() {return null;}
}
