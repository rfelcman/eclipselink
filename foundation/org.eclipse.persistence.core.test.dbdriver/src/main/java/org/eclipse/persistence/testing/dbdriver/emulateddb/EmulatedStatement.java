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

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Emulated database statement.
 */
public class EmulatedStatement implements PreparedStatement {
    private EmulatedConnection connection;
    private String sql;
    private List<Object> parameters;
    private int batch;

    EmulatedStatement(EmulatedConnection connection) {
        this.connection = connection;
        this.parameters = new ArrayList<>();
    }

    EmulatedStatement(String sql, EmulatedConnection connection) {
        this(connection);
        this.sql = sql;
    }

    public void checkForError() throws SQLException{
        if ((connection).isInFailureState()) {
            throw new SQLException("Communication Failure occurred", "", 17004);
        }
    }

    /**
     * If the rows have not been fetched, fetch them from the database.
     */
    List<Map<List<?>,List<?>>> fetchRows() throws SQLException {
        List<Map<List<?>,List<?>>> rows = this.connection.getRows(this.sql);
        if (rows == null) {
            String sqlWithParams = null;
            for (Object parameter: this.parameters) {
                //There is not any different handling for numeric, character and date type like apostrophe wrapper
                sqlWithParams = sql.replaceFirst("\\?", parameter.toString());
            }
            rows = this.connection.getRows(sqlWithParams);
        }
        //Second fallback to fetch from real database
        if (rows == null) {
            Connection realConnection = this.connection.getRealConnection();
            if (realConnection == null) {
                this.connection.putRows(this.sql, new ArrayList<>(0));
            } else {
                PreparedStatement statement = realConnection.prepareStatement(this.sql);
                for (int index = 0; index < this.parameters.size(); index++) {
                    statement.setObject(index + 1, this.parameters.get(index));
                }
                ResultSet result = statement.executeQuery();
                rows = new ArrayList<>();
                ResultSetMetaData metaData = result.getMetaData();
                while (result.next()) {
                    Map<List<?>, List<?>> row = new LinkedHashMap<>();
                    for (int column = 0; column < metaData.getColumnCount(); column++) {
                        row.put(List.of(metaData.getColumnName(column + 1)), List.of(result.getObject(column + 1)));
                    }
                    rows.add(row);
                }
                result.close();
                statement.close();
                this.connection.putRows(this.sql, rows);
            }
        }
        return rows;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        checkForError();
        return new EmulatedResultSet(fetchRows());
    }

    @Override
    public int executeUpdate() throws SQLException {
        checkForError();
        return 1;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        setObject(parameterIndex, null);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
    }

    @Override
    @Deprecated(since="1.2")
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
    }

    @Override
    public void clearParameters() {
        parameters.clear();
    }

    //----------------------------------------------------------------------
    // Advanced features:
    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) {
        while (this.parameters.size() < parameterIndex) {
            this.parameters.add(null);
        }
        this.parameters.set(parameterIndex-1, x);
    }

    @Override
    public boolean execute() throws SQLException{
        checkForError();
        return true;
    }

    @Override
    public void addBatch() {
        this.batch++;
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) {
    }

    @Override
    public void setRef(int i, Ref x) {
    }

    @Override
    public void setBlob(int i, Blob x) {
        setObject(i, x);
    }

    @Override
    public void setClob(int i, Clob x) {
        setObject(i, x);
    }

    @Override
    public void setArray(int i, Array x) {
        setObject(i, x);
    }

    @Override
    public ResultSetMetaData getMetaData() {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        setObject(parameterIndex, x);
    }

    @Override
    public void setNull(int paramIndex, int sqlType, String typeName) {
        setObject(paramIndex, null);
    }

    @Override
    public void setURL(int parameterIndex, URL x) {
    }

    @Override
    public ParameterMetaData getParameterMetaData() {
        return null;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        checkForError();
        return new EmulatedResultSet(this.connection.getRows(sql));
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        checkForError();
        return 1;
    }

    @Override
    public void close() {
    }

    @Override
    public int getMaxFieldSize() {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) {
    }

    @Override
    public int getMaxRows() {
        return 0;
    }

    @Override
    public void setMaxRows(int max) {
    }

    @Override
    public void setEscapeProcessing(boolean enable) {
    }

    @Override
    public int getQueryTimeout() {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) {
    }

    @Override
    public void cancel() {
    }

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {
    }

    @Override
    public void setCursorName(String name) {
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        checkForError();
        return true;
    }

    @Override
    public ResultSet getResultSet() {
        return null;
    }

    @Override
    public int getUpdateCount() {
        return 0;
    }

    @Override
    public boolean getMoreResults() {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) {
    }

    @Override
    public int getFetchDirection() {
        return ResultSet.FETCH_UNKNOWN;
    }

    @Override
    public void setFetchSize(int rows) {
    }

    @Override
    public int getFetchSize() {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public void addBatch(String sql) {
    }

    @Override
    public void clearBatch() {
    }

    @Override
    public int[] executeBatch() {
        int[] result = new int[this.batch];
        for (int index = 0; index < this.batch; index++) {
            result[index] = 1;
        }
        this.batch = 0;
        return result;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public boolean getMoreResults(int current) {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        checkForError();
        return 1;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        checkForError();
        return 1;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        checkForError();
        return 1;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        checkForError();
        return true;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        checkForError();
        return true;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        checkForError();
        return true;
    }

    @Override
    public int getResultSetHoldability() {
        return 0;
    }

    @Override
    public void setAsciiStream(int columnIndex, InputStream stream, long length) {
    }

    @Override
    public void setAsciiStream(int columnIndex, InputStream stream) {
    }

    public void setAsciiStream(String columnLabel, InputStream stream, long length) {
    }

    public void setAsciiStream(String columnLabel, InputStream stream) {
    }

    @Override
    public void setBlob(int columnIndex, InputStream stream, long length) {
    }

    @Override
    public void setBlob(int columnIndex, InputStream stream) {
    }

    public void setBlob(String columnLabel, InputStream stream, long length) {
    }

    public void setBlob(String columnLabel, InputStream stream) {
    }

    @Override
    public void setBinaryStream(int columnIndex, InputStream stream, long length) {
    }

    @Override
    public void setBinaryStream(int columnIndex, InputStream stream) {
    }

    public void setBinaryStream(String columnLabel, InputStream stream, long length) {
    }

    public void setBinaryStream(String columnLabel, InputStream stream) {
    }

    @Override
    public void setCharacterStream(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void setCharacterStream(int columnIndex, Reader reader) {
    }

    public void setCharacterStream(String columnLabel, Reader reader, long length) {
    }

    public void setCharacterStream(String columnLabel, Reader reader) {
    }

    @Override
    public void setClob(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void setClob(int columnIndex, Reader reader) {
    }

    public void setClob(String columnLabel, Reader reader, long length) {
    }

    public void setClob(String columnLabel, Reader reader) {
    }

    @Override
    public void setNCharacterStream(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void setNCharacterStream(int columnIndex, Reader reader) {
    }

    public void setNCharacterStream(String columnLabel, Reader reader, long length) {
    }

    public void setNCharacterStream(String columnLabel, Reader reader) {
    }

    @Override
    public void setNClob(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void setNClob(int columnIndex, Reader reader) {
    }

    public void setNClob(String columnLabel, Reader reader, long length) {
    }

    public void setNClob(String columnLabel, Reader reader) {
    }

    @Override
    public void setNClob(int columnIndex, NClob nclob) {
    }

    public void setNClob(String columnLabel, NClob nclob) {
    }

    @Override
    public void setNString(int columnIndex, String nString) {
    }

    public void setNString(String columnLabel, String nString) {
    }

    public void setSQLXML(String columnLabel, SQLXML sqlxml) {
    }

    @Override
    public void setSQLXML(int columnIndex, SQLXML sqlxml) {
    }

    @Override
    public void setRowId(int columnIndex, RowId rowid) {
    }

    public void setRowId(String columnLabel, RowId rowid) {
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public boolean isPoolable() {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) {
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
    public boolean isCloseOnCompletion(){return false;}

    @Override
    public void closeOnCompletion(){}
}
