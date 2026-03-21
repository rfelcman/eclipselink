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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Emulated database connection.
 * This connection performs operations in-memory, simulating a database.
 * This is used in performance testing to isolated EclipseLink's performance and
 * minimize the database overhead and in-consistency.
 */
public class EmulatedConnection implements Connection {
    private Map<String, List<Map<List<?>,List<?>>>> rows;
    private Connection connection;
    private DatabaseMetaData databaseMetaData;
    private boolean inFailureState = false;
    private String url;
    private Properties info;

    public EmulatedConnection() {
        this.rows = new LinkedHashMap<>();
    }

    public EmulatedConnection(String url, Properties info) {
        this();
        this.url = url;
        this.info = info;
    }

    public EmulatedConnection(Connection connection) {
        this();
        this.connection = connection;
    }

    /**
     * Return the real connection.
     */
    public Connection getRealConnection() {
        return connection;
    }

    /**
     * Return the rows for the sql.
     */
    List<Map<List<?>,List<?>>> getRows(String sql) {
        return rows.computeIfAbsent(sql, k -> new ArrayList<>());
    }

    /**
     * Return the rows for the sql.
     */
    public void putRows(String sql, List<Map<List<?>,List<?>>> rows) {
        this.rows.put(sql, rows);
    }

    public String getURL() {
        return this.url;
    }

    public Properties getInfo() {
        return this.info;
    }

    public void causeCommError() {
        this.inFailureState = true;
    }

    public boolean isInFailureState() {
        return this.inFailureState;
    }

    @Override
    public Statement createStatement() throws SQLException {
        if (!EmulatedDriver.emulate) {
            return connection.createStatement();
        }
        return new EmulatedStatement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        if (!EmulatedDriver.emulate || (sql.contains("DUAL")) || (sql.contains("dual"))) {
            return connection.prepareStatement(sql);
        }
        return new EmulatedStatement(sql, this);
    }

    @Override
    public CallableStatement prepareCall(String sql) {
        return null;
    }

    @Override
    public String nativeSQL(String sql) {
        return sql;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if (!EmulatedDriver.emulate) {
            connection.setAutoCommit(autoCommit);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        if (!EmulatedDriver.emulate) {
            connection.getAutoCommit();
        }
        return false;
    }

    @Override
    public void commit() throws SQLException {
        if (!EmulatedDriver.emulate) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (!EmulatedDriver.emulate) {
            connection.rollback();
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    //======================================================================
    // Advanced features:
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        if (connection != null) {
            return connection.getMetaData();
        }
        if (this.databaseMetaData == null) {
            this.databaseMetaData = new EmulatedDatabaseMetaData(this);
        }
        return this.databaseMetaData;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public void setCatalog(String catalog) {
    }

    @Override
    public String getCatalog() {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) {
    }

    @Override
    public int getTransactionIsolation() {
        return Connection.TRANSACTION_NONE;
    }

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) {
        return new EmulatedStatement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() {
        return null;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) {
    }

    @Override
    public void setHoldability(int holdability) {
    }

    @Override
    public int getHoldability() {
        return 0;
    }

    @Override
    public Savepoint setSavepoint() {
        return null;
    }

    @Override
    public Savepoint setSavepoint(String name) {
        return null;
    }

    @Override
    public void rollback(Savepoint savepoint) {
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) {
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return new EmulatedStatement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return prepareStatement(sql);
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
        // TODO: or false?
        return true;
    }

    @Override
    public void setClientInfo(String name, String value) {
    }

    @Override
    public void setClientInfo(Properties properties) {
    }

    @Override
    public boolean isWrapperFor(Class<?> iFace) {
        return false;
    }

    @Override
    public <T>T unwrap(Class<T> iFace) {
        return iFace.cast(this);
    }

    @Override
    public int getNetworkTimeout() {
        return 0;
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) {}

    @Override
    public void abort(Executor executor) {}

    @Override
    public String getSchema() {
        return null;
    }

    @Override
    public void setSchema(String schema) {}
}
