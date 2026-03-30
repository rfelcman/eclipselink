/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation. All rights reserved.
 * Copyright (c) 1998, 2022 Oracle and/or its affiliates. All rights reserved.
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
//                 Bug 256284: Closing an EMF where the database is unavailable results in deployment exception on redeploy.
package org.eclipse.persistence.testing.dbdriver.wrapper;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class ConnectionWrapper implements Connection {

    Connection conn;
    boolean broken;
    String exceptionString;
    public static String defaultExceptionString = "ConnectionWrapper: broken";

    public ConnectionWrapper(Connection conn) {
        this.conn = conn;
        this.broken = DriverWrapper.newConnectionsBroken;
        this.exceptionString = DriverWrapper.newConnectionsBrokenExceptionString;
    }

    public void breakConnection() {
        breakConnection(defaultExceptionString);
    }
    public void breakConnection(String newExceptionString) {
        broken = true;
        exceptionString = newExceptionString;
    }
    public void repairConnection() {
        broken = false;
        exceptionString = null;
    }

    public boolean broken() {
        return broken;
    }
    public String getExceptionString() {
        return exceptionString;
    }

    @Override
    public Statement createStatement() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.commit();
    }

    @Override
    public void rollback() throws SQLException {
        conn.rollback();
        if(broken) {
            throw new SQLException(getExceptionString());
        }
    }

    @Override
    public void close() throws SQLException {
        conn.close();
        if(broken) {
            throw new SQLException(getExceptionString());
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency)
    throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String,Class<?>> getTypeMap() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String,Class<?>> map) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        conn.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency,
                                     int resultSetHoldability) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
    throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        if(broken) {
            throw new SQLException(getExceptionString());
        }
        return conn.prepareStatement(sql, columnNames);
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) {
        return null;
    }

    @Override
    public Blob createBlob() {
        return null;
    }

    @Override
    public Clob createClob() {
        return null;
    }

    @Override
    public NClob createNClob() {
        return null;
    }

    @Override
    public SQLXML createSQLXML() {
        return null;
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) {
        return null;
    }

    @Override
    public Properties getClientInfo() {
        return null;
    }

    @Override
    public String getClientInfo(String name) {
        return null;
    }

    @Override
    public boolean isValid(int timeout) {
        return false;
    }

    @Override
    public void setClientInfo(String name, String value) {
    }

    @Override
    public void setClientInfo(Properties properties) {
    }

    // From java.sql.Wrapper

    @Override
    public boolean isWrapperFor(Class<?> iFace) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iFace) {
        return iFace.cast(this);
    }

    @Override
    public int getNetworkTimeout(){return 0;}

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds){}

    @Override
    public void abort(Executor executor){}

    @Override
    public String getSchema(){return null;}

    @Override
    public void setSchema(String schema){}
}
