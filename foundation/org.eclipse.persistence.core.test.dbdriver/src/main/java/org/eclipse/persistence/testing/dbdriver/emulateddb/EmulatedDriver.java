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
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.testing.dbdriver.emulateddb;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Emulated database driver.
 */
public class EmulatedDriver implements Driver {

    static {
        try {
            DriverManager.registerDriver(new EmulatedDriver());
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /** Allow toggling of emulation. */
    public static boolean emulate = true;

    /** Cache the connection. */
    private Connection connection;

    public static boolean fullFailure = false;

    public EmulatedDriver() {
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (! acceptsURL(url)) {
            return null;
        }
        if (fullFailure) {
            throw new SQLException("Connections unavailable");
        }
        if (connection == null) {
            if ("jdbc:emulateddriver".equals(url)) {
                return new EmulatedConnection(url, info);
            } else {
                connection = new EmulatedConnection(DriverManager.getConnection(url.substring("emulate:".length()), info));
            }
        }
        return connection;
    }

    @Override
    public boolean acceptsURL(String url) {
        return url.contains("emulate:") || url.contains("jdbc:emulateddriver");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }
}
