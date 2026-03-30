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
//     Oracle - initial API and implementation from Oracle TopLink
package org.eclipse.persistence.testing.dbdriver.emulateddb;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Emulated database result set.
 * This extracts data from a list of DatabaseRows.
 */
class EmulatedResultSet implements ResultSet {
    private List<Map<List<?>,List<?>>> rows;
    private int index;

    EmulatedResultSet(List<Map<List<?>,List<?>>> rows) {
        this.rows = rows;
        this.index = 0;
    }

    /**
     * Return the database records.
     */
    List<Map<List<?>,List<?>>> getRows() {
        return rows;
    }

    @Override
    public boolean next() {
        this.index++;
        return this.index <= this.rows.size();
    }

    @Override
    public void close() {
    }

    @Override
    public boolean wasNull() {
        return false;
    }

    @Override
    public String getString(int columnIndex) {
        return (String)getObject(columnIndex);
    }

    @Override
    public boolean getBoolean(int columnIndex) {
        Boolean value = (Boolean) getObject(columnIndex);
        return value != null && value;
    }

    @Override
    public byte getByte(int columnIndex) {
        Number value = (Number)getObject(columnIndex);
        return value == null ? 0 : value.byteValue();
    }

    @Override
    public short getShort(int columnIndex) {
        Number value = (Number)getObject(columnIndex);
        return value == null ? 0 : value.shortValue();
    }

    @Override
    public int getInt(int columnIndex) {
        Number value = (Number)getObject(columnIndex);
        if (value == null) {
            return 0;
        } else {
            return value.intValue();
        }
    }

    @Override
    public long getLong(int columnIndex) {
        Number value = (Number)getObject(columnIndex);
        if (value == null) {
            return 0;
        } else {
            return value.longValue();
        }
    }

    @Override
    public float getFloat(int columnIndex) {
        Number value = (Number)getObject(columnIndex);
        if (value == null) {
            return 0;
        } else {
            return value.floatValue();
        }
    }

    @Override
    public double getDouble(int columnIndex) {
        Number value = (Number)getObject(columnIndex);
        if (value == null) {
            return 0;
        } else {
            return value.doubleValue();
        }
    }

    @Override
    @Deprecated(since="1.2")
    public BigDecimal getBigDecimal(int columnIndex, int scale) {
        Number number = (Number) getObject(columnIndex);
        return convertNumber2BigDecimal(number);
    }

    @Override
    public byte[] getBytes(int columnIndex) {
        return (byte[])getObject(columnIndex);
    }

    @Override
    public java.sql.Date getDate(int columnIndex) {
        java.util.Date date = (java.util.Date)getObject(columnIndex);
        if ((date != null) && (!(date instanceof java.sql.Date))) {
            date = new java.sql.Date(date.getTime());
        }
        return (java.sql.Date)date;
    }

    @Override
    public Time getTime(int columnIndex) {
        return (Time)getObject(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) {
        return (Timestamp)getObject(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) {
        return (InputStream)getObject(columnIndex);
    }

    @Override
    @Deprecated(since="1.2")
    public InputStream getUnicodeStream(int columnIndex) {
        return (InputStream)getObject(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) {
        return (InputStream)getObject(columnIndex);
    }

    @Override
    public String getString(String columnName) {
        return (String)getObject(columnName);
    }

    @Override
    public boolean getBoolean(String columnName) {
        return (Boolean) getObject(columnName);
    }

    @Override
    public byte getByte(String columnName) {
        return ((Number)getObject(columnName)).byteValue();
    }

    @Override
    public short getShort(String columnName) {
        return ((Number)getObject(columnName)).shortValue();
    }

    @Override
    public int getInt(String columnName) {
        Number value = (Number)getObject(columnName);
        if (value == null) {
            return 0;
        } else {
            return value.intValue();
        }
    }

    @Override
    public long getLong(String columnName) {
        Number value = (Number)getObject(columnName);
        if (value == null) {
            return 0;
        } else {
            return value.longValue();
        }
    }

    @Override
    public float getFloat(String columnName) {
        Number value = (Number)getObject(columnName);
        if (value == null) {
            return 0;
        } else {
            return value.floatValue();
        }
    }

    @Override
    public double getDouble(String columnName) {
        Number value = (Number)getObject(columnName);
        if (value == null) {
            return 0;
        } else {
            return value.doubleValue();
        }
    }

    @Override
    @Deprecated(since="1.2")
    public BigDecimal getBigDecimal(String columnName, int scale) {
        Number number = (Number) getObject(columnName);
        return convertNumber2BigDecimal(number);
    }

    @Override
    public byte[] getBytes(String columnName) {
        return (byte[])getObject(columnName);
    }

    @Override
    public java.sql.Date getDate(String columnName) {
        java.util.Date date = (java.util.Date)getObject(columnName);
        if ((date != null) && (!(date instanceof java.sql.Date))) {
            date = new java.sql.Date(date.getTime());
        }
        return (java.sql.Date)date;
    }

    @Override
    public Time getTime(String columnName) {
        return (Time)getObject(columnName);
    }

    @Override
    public Timestamp getTimestamp(String columnName) {
        return (Timestamp)getObject(columnName);
    }

    @Override
    public InputStream getAsciiStream(String columnName) {
        return (InputStream)getObject(columnName);
    }

    @Override
    @Deprecated(since="1.2")
    public InputStream getUnicodeStream(String columnName) {
        return (InputStream)getObject(columnName);
    }

    @Override
    public InputStream getBinaryStream(String columnName) {
        return (InputStream)getObject(columnName);
    }

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {
    }

    @Override
    public String getCursorName() {
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() {
        return new EmulatedResultSetMetaData(this);
    }

    @Override
    public Object getObject(int columnIndex) {
        final Collection<List<?>> values = rows.get(this.index - 1).values();
        return new ArrayList<>(values).get(columnIndex - 1);
    }

    @Override
    public Object getObject(String columnName) {
        return this.rows.get(this.index - 1).get(columnName);
    }

    @Override
    public int findColumn(String columnName) {
        return 0;
    }

    @Override
    public Reader getCharacterStream(int columnIndex) {
        return (Reader)getObject(columnIndex);
    }

    @Override
    public Reader getCharacterStream(String columnName) {
        return (Reader)getObject(columnName);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) {
        Number number = (Number) getObject(columnIndex);
        return convertNumber2BigDecimal(number);
    }

    @Override
    public BigDecimal getBigDecimal(String columnName) {
        Number number = (Number) getObject(columnName);
        return convertNumber2BigDecimal(number);
    }

    @Override
    public boolean isBeforeFirst() {
        return this.index == 0;
    }

    @Override
    public boolean isAfterLast() {
        return (this.index - 1) == this.rows.size();
    }

    @Override
    public boolean isFirst() {
        return this.index == 1;
    }

    @Override
    public boolean isLast() {
        return this.index == this.rows.size();
    }

    @Override
    public void beforeFirst() {
        this.index = 0;
    }

    @Override
    public void afterLast() {
        this.index = this.rows.size() + 1;
    }

    @Override
    public boolean first() {
        this.index = 1;
        return true;
    }

    @Override
    public boolean last() {
        this.index = this.rows.size();
        return true;
    }

    @Override
    public int getRow() {
        return index;
    }

    @Override
    public boolean absolute(int row) {
        this.index = row;
        return true;
    }

    @Override
    public boolean relative(int rows) {
        this.index = index + rows;
        return true;
    }

    @Override
    public boolean previous() {
        this.index--;
        return true;
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
    public int getType() {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() {
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated() {
        return false;
    }

    @Override
    public boolean rowInserted() {
        return false;
    }

    @Override
    public boolean rowDeleted() {
        return false;
    }

    @Override
    public void updateNull(int columnIndex) {
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) {
    }

    @Override
    public void updateByte(int columnIndex, byte x) {
    }

    @Override
    public void updateShort(int columnIndex, short x) {
    }

    @Override
    public void updateInt(int columnIndex, int x) {
    }

    @Override
    public void updateLong(int columnIndex, long x) {
    }

    @Override
    public void updateFloat(int columnIndex, float x) {
    }

    @Override
    public void updateDouble(int columnIndex, double x) {
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) {
    }

    @Override
    public void updateString(int columnIndex, String x) {
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) {
    }

    @Override
    public void updateDate(int columnIndex, java.sql.Date x) {
    }

    @Override
    public void updateTime(int columnIndex, Time x) {
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) {
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) {
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) {
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) {
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scale) {
    }

    @Override
    public void updateObject(int columnIndex, Object x) {
    }

    @Override
    public void updateNull(String columnName) {
    }

    @Override
    public void updateBoolean(String columnName, boolean x) {
    }

    @Override
    public void updateByte(String columnName, byte x) {
    }

    @Override
    public void updateShort(String columnName, short x) {
    }

    @Override
    public void updateInt(String columnName, int x) {
    }

    @Override
    public void updateLong(String columnName, long x) {
    }

    @Override
    public void updateFloat(String columnName, float x) {
    }

    @Override
    public void updateDouble(String columnName, double x) {
    }

    @Override
    public void updateBigDecimal(String columnName, BigDecimal x) {
    }

    @Override
    public void updateString(String columnName, String x) {
    }

    @Override
    public void updateBytes(String columnName, byte[] x) {
    }

    @Override
    public void updateDate(String columnName, java.sql.Date x) {
    }

    @Override
    public void updateTime(String columnName, Time x) {
    }

    @Override
    public void updateTimestamp(String columnName, Timestamp x) {
    }

    @Override
    public void updateAsciiStream(String columnName, InputStream x, int length) {
    }

    @Override
    public void updateBinaryStream(String columnName, InputStream x, int length) {
    }

    @Override
    public void updateCharacterStream(String columnName, Reader reader, int length) {
    }

    @Override
    public void updateObject(String columnName, Object x, int scale) {
    }

    @Override
    public void updateObject(String columnName, Object x) {
    }

    @Override
    public void insertRow() {
    }

    @Override
    public void updateRow() {
    }

    @Override
    public void deleteRow() {
    }

    @Override
    public void refreshRow() {
    }

    @Override
    public void cancelRowUpdates() {
    }

    @Override
    public void moveToInsertRow() {
    }

    @Override
    public void moveToCurrentRow() {
    }

    @Override
    public Statement getStatement() {
        return null;
    }

    @Override
    public Object getObject(int i, Map<String,Class<?>> map) {
        return getObject(i);
    }

    @Override
    public Ref getRef(int i) {
        return null;
    }

    @Override
    public Blob getBlob(int i) {
        return (Blob)getObject(i);
    }

    @Override
    public Clob getClob(int i) {
        return (Clob)getObject(i);
    }

    @Override
    public Array getArray(int i) {
        return null;
    }

    @Override
    public Object getObject(String colName, Map<String,Class<?>> map) {
        return getObject(colName);
    }

    @Override
    public Ref getRef(String colName) {
        return null;
    }

    @Override
    public Blob getBlob(String colName) {
        return (Blob)getObject(colName);
    }

    @Override
    public Clob getClob(String colName) {
        return (Clob)getObject(colName);
    }

    @Override
    public Array getArray(String colName) {
        return null;
    }

    @Override
    public java.sql.Date getDate(int columnIndex, Calendar cal) {
        return getDate(columnIndex);
    }

    @Override
    public java.sql.Date getDate(String columnName, Calendar cal) {
        return getDate(columnName);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) {
        return null;
    }

    @Override
    public Time getTime(String columnName, Calendar cal) {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) {
        return getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(String columnName, Calendar cal) {
        return getTimestamp(columnName);
    }

    @Override
    public URL getURL(int columnIndex) {
        return null;
    }

    @Override
    public URL getURL(String columnName) {
        return null;
    }

    @Override
    public void updateRef(int columnIndex, Ref x) {
    }

    @Override
    public void updateRef(String columnName, Ref x) {
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) {
    }

    @Override
    public void updateBlob(String columnName, Blob x) {
    }

    @Override
    public void updateClob(int columnIndex, Clob x) {
    }

    @Override
    public void updateClob(String columnName, Clob x) {
    }

    @Override
    public void updateArray(int columnIndex, Array x) {
    }

    @Override
    public void updateArray(String columnName, Array x) {
    }

    @Override
    public int getHoldability() {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) {
        return null;
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) {
        return null;
    }

    @Override
    public NClob getNClob(int columnIndex) {
        return null;
    }

   @Override
   public NClob getNClob(String columnLabel) {
        return null;
    }

   @Override
   public String getNString(int columnIndex) {
       return null;
   }

   @Override
   public String getNString(String columnLabel) {
       return null;
   }

   @Override
   public RowId getRowId(int columnIndex) {
       return null;
   }

   @Override
   public RowId getRowId(String columnLabel) {
       return null;
   }

   @Override
   public SQLXML getSQLXML(int columnIndex) {
       return null;
   }

   @Override
   public SQLXML getSQLXML(String columnLabel) {
       return null;
   }

   @Override
   public boolean isClosed() {
       return false;
   }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream stream, long length) {
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream stream) {
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream stream, long length) {
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream stream) {
    }

    @Override
    public void updateBlob(int columnIndex, InputStream stream, long length) {
    }

    @Override
    public void updateBlob(int columnIndex, InputStream stream) {
    }

    @Override
    public void updateBlob(String columnLabel, InputStream stream, long length) {
    }

    @Override
    public void updateBlob(String columnLabel, InputStream stream) {
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream stream, long length) {
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream stream) {
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream stream, long length) {
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream stream) {
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader reader) {
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) {
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) {
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) {
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) {
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) {
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader reader) {
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) {
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) {
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) {
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) {
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) {
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) {
    }

    @Override
    public void updateNClob(int columnIndex, NClob nclob) {
    }

    @Override
    public void updateNClob(String columnLabel, NClob nclob) {
    }

    @Override
    public void updateNString(int columnIndex, String nString) {
    }

    @Override
    public void updateNString(String columnLabel, String nString) {
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML sqlxml) {
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML sqlxml) {
    }

    @Override
    public void updateRowId(int columnIndex, RowId rowid) {
    }

    @Override
    public void updateRowId(String columnLabel, RowId rowid) {
    }

    @Override
    public boolean isWrapperFor(Class<?> iFace) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iFace) {
        return iFace.cast(this);
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) {
        return null;
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) {
        return null;
    }

    private BigDecimal convertNumber2BigDecimal(Number number) {
        if (number == null) {
            return null;
        }
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        }
        return new BigDecimal(number.longValue());
    }
}
