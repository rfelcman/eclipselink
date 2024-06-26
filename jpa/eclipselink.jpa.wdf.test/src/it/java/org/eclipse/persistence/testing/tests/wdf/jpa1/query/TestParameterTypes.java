/*
 * Copyright (c) 2005, 2021 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2005, 2015 SAP. All rights reserved.
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
//     SAP - initial API and implementation

package org.eclipse.persistence.testing.tests.wdf.jpa1.query;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;

import org.eclipse.persistence.testing.framework.wdf.JPAEnvironment;
import org.eclipse.persistence.testing.models.wdf.jpa1.types.BasicTypesFieldAccess;
import org.eclipse.persistence.testing.models.wdf.jpa1.types.UserDefinedEnum;
import org.eclipse.persistence.testing.models.wdf.jpa1.types.UserDefinedSerializable;
import org.eclipse.persistence.testing.tests.wdf.jpa1.JPA1Base;
import org.junit.Test;

public class TestParameterTypes extends JPA1Base {

    @Test
    public void testInsert() {
        JPAEnvironment env = getEnvironment();
        EntityManager em = env.getEntityManager();
        try {
            BasicTypesFieldAccess obj = new BasicTypesFieldAccess(0);
            obj.fill();
            env.beginTransaction(em);
            em.persist(obj);
            env.commitTransactionAndClear(em);
            verify(true, "no Exception");
            obj = em.find(BasicTypesFieldAccess.class, 0);
        } finally {
            closeEntityManager(em);
        }
    }

    private void validatePrimitive(final int id, Validator validator) {
        JPAEnvironment env = getEnvironment();
        EntityManager em = env.getEntityManager();
        try {
            // insert the object
            env.beginTransaction(em);
            BasicTypesFieldAccess obj = new BasicTypesFieldAccess(id);
            validator.set(obj);
            em.persist(obj);
            env.commitTransactionAndClear(em);
            verify(true, "no Exception");
            // find object using query
            if (validator.isComparableJdbcType()) {
                Query query = validator.createQuery(em, id);
                BasicTypesFieldAccess found = (BasicTypesFieldAccess) query.getSingleResult();
                verify(found.getId() == id, "wrong id: " + found.getId());
            }
        } finally {
            closeEntityManager(em);
        }
    }

    private void validateReference(final int id, Validator validator) {
        JPAEnvironment env = getEnvironment();
        EntityManager em = env.getEntityManager();
        try {
            BasicTypesFieldAccess obj = new BasicTypesFieldAccess(id);
            // insert object with non-null field
            env.beginTransaction(em);
            validator.set(obj);
            em.persist(obj);
            env.commitTransactionAndClear(em);
            verify(true, "no Exception");
            // find object using query
            if (validator.isComparableJdbcType()) {
                Query query = validator.createQuery(em, id);
                BasicTypesFieldAccess found = (BasicTypesFieldAccess) query.getSingleResult();
                verify(found.getId() == id, "wrong id: " + found.getId());
            }
        } finally {
            closeEntityManager(em);
        }
    }

    private void validateMutable(final int id, Validator validator) {
        JPAEnvironment env = getEnvironment();
        EntityManager em = env.getEntityManager();
        try {
            BasicTypesFieldAccess obj = new BasicTypesFieldAccess(id);
            // insert object with non-null field
            env.beginTransaction(em);
            validator.set(obj);
            em.persist(obj);
            env.commitTransactionAndClear(em);
            verify(true, "no Exception");
            // find object using query
            if (validator.isComparableJdbcType()) {
                Query query = validator.createQuery(em, id);
                BasicTypesFieldAccess found = (BasicTypesFieldAccess) query.getSingleResult();
                verify(found.getId() == id, "wrong id: " + found.getId());
            }
        } finally {
            closeEntityManager(em);
        }
    }

    // primitive types
    @Test
    public void testPrimitiveBoolean() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveBoolean(true);
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveBoolean = ?1 and b.id = ?2")
                        .setParameter(1, Boolean.TRUE).setParameter(2, id);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }
        };
        validatePrimitive(1, validator);
    }

    @Test
    public void testPrimitiveByte() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimititveByte((byte) 17);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primititveByte = ?1 and b.id = ?2")
                        .setParameter(1, (byte) 17).setParameter(2, id).setParameter(2,
                                id);
            }
        };
        validatePrimitive(2, validator);
    }

    @Test
    public void testPrimitiveChar() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveChar('A');
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveChar = ?1 and b.id = ?2")
                        .setParameter(1, 'A').setParameter(2, id);
            }
        };
        validatePrimitive(3, validator);
    }

    @Test
    public void testPrimitiveShort() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveShort((short) 19);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveShort = ?1 and b.id = ?2")
                        .setParameter(1, (short) 19).setParameter(2, id);
            }
        };
        validatePrimitive(4, validator);
    }

    @Test
    public void testPrimitiveInt() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveInt(88);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveInt = ?1 and b.id = ?2")
                        .setParameter(1, 88).setParameter(2, id);
            }
        };
        validatePrimitive(5, validator);
    }

    @Test
    public void testPrimitiveLong() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveLong(88);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveLong = ?1 and b.id = ?2")
                        .setParameter(1, 88L).setParameter(2, id);
            }
        };
        validatePrimitive(6, validator);
    }

    @Test
    public void testPrimitiveFloat() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveFloat((float) 88.5);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveFloat = ?1 and b.id = ?2")
                        .setParameter(1, Float.valueOf("88.5")).setParameter(2, id);
            }
        };
        validatePrimitive(7, validator);
    }

    @Test
    public void testPrimitiveDouble() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveDouble(99.5);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.primitiveDouble = ?1 and b.id = ?2")
                        .setParameter(1, Double.valueOf(("99.5"))).setParameter(2, id);
            }
        };
        validatePrimitive(8, validator);
    }

    // wrappers of primitive types
    @Test
    public void testWrapperBoolean() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperBoolean(Boolean.TRUE);
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperBoolean = ?1 and b.id = ?2")
                        .setParameter(1, Boolean.TRUE).setParameter(2, id);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }
        };
        validateReference(11, validator);
    }

    @Test
    public void testWrapperByte() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperByte((byte) 17);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperByte = ?1 and b.id = ?2")
                        .setParameter(1, (byte) 17).setParameter(2, id);
            }
        };
        validateReference(12, validator);
    }

    @Test
    public void testWrapperCharacter() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperCharacter('A');
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperCharacter = ?1 and b.id = ?2")
                        .setParameter(1, 'A').setParameter(2, id);
            }
        };
        validateReference(13, validator);
    }

    @Test
    public void testWrapperShort() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperShort((short) 1);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperShort = ?1 and b.id = ?2")
                        .setParameter(1, (short) 1).setParameter(2, id);
            }
        };
        validateReference(14, validator);
    }

    @Test
    public void testWrapperInteger() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperInteger(1);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperInteger = ?1 and b.id = ?2")
                        .setParameter(1, 1).setParameter(2, id);
            }
        };
        validateReference(15, validator);
    }

    @Test
    public void testWrapperLong() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperLong(1L);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperLong = ?1 and b.id = ?2")
                        .setParameter(1, 1L).setParameter(2, id);
            }
        };
        validateReference(16, validator);
    }

    @Test
    public void testWrapperDouble() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperDouble(1.0);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperDouble = ?1 and b.id = ?2")
                        .setParameter(1, Double.valueOf(("1"))).setParameter(2, id);
            }
        };
        validateReference(18, validator);
    }

    @Test
    public void testWrapperFloat() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperFloat(1F);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.wrapperFloat = ?1 and b.id = ?2")
                        .setParameter(1, Float.valueOf("1")).setParameter(2, id);
            }
        };
        validateReference(17, validator);
    }

    // immutable reference types
    @Test
    public void testString2Varchar() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setString2Varchar("VC 1");
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.string2Varchar = ?1 and b.id = ?2")
                        .setParameter(1, "VC 1").setParameter(2, id);
            }
        };
        validateReference(21, validator);
    }

    @Test
    public void testString2Clob() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setString2Clob("VC 1");
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateReference(22, validator);
    }

    @Test
    public void testBigDecimal() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setBigDecimal(new BigDecimal("1.1"));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.bigDecimal = ?1 and b.id = ?2")
                        .setParameter(1, new BigDecimal("1.1")).setParameter(2, id);
            }
        };
        validateReference(23, validator);
    }

    @Test
    public void testBigInteger() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setBigInteger(new BigInteger("11"));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.bigInteger = ?1 and b.id = ?2")
                        .setParameter(1, new BigInteger("11")).setParameter(2, id);
            }
        };
        validateReference(24, validator);
    }

    // mutable types
    @Test
    public void testUtilDate() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setUtilDate(new Date(1000));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.utilDate = ?1 and b.id = ?2")
                        .setParameter(1, new Date(1000), TemporalType.TIMESTAMP).setParameter(2, id);
            }
        };
        validateMutable(31, validator);
    }

    @Test
    public void testUtilCalendar() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setUtilCalendar(new GregorianCalendar(2005, 9, 8, 10, 49));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.utilCalendar = ?1 and b.id = ?2")
                        .setParameter(1, new GregorianCalendar(2005, 9, 8, 10, 49), TemporalType.TIMESTAMP).setParameter(2,
                                id);
            }
        };
        validateMutable(32, validator);
    }

    @Test
    public void testSqlDate() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setSqlDate(java.sql.Date.valueOf("2005-09-08"));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.sqlDate = ?1 and b.id = ?2").setParameter(
                        1, java.sql.Date.valueOf("2005-09-08")).setParameter(2, id);
            }
        };
        validateMutable(33, validator);
    }

    @Test
    public void testSqlTime() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setSqlTime(java.sql.Time.valueOf("10:49:00"));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.sqlTime = ?1 and b.id = ?2").setParameter(
                        1, java.sql.Time.valueOf("10:49:00")).setParameter(2, id);
            }
        };
        validateMutable(34, validator);
    }

    @Test
    public void testSqlTimestamp() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setSqlTimestamp(new java.sql.Timestamp(1000));
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.sqlTimestamp = ?1 and b.id = ?2")
                        .setParameter(1, new java.sql.Timestamp(1000)).setParameter(2, id);
            }
        };
        validateMutable(35, validator);
    }

    // arrays
    @Test
    public void testPrimitiveByteArray2Binary() {
        final byte[] UNCHANGED = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveByteArray2Binary(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery(
                        "select b from BasicTypesFieldAccess b where b.primitiveByteArray2Binary = ?1 and b.id = ?2")
                        .setParameter(1, UNCHANGED).setParameter(2, id);
            }
        };
        validateMutable(41, validator);
    }

    @Test
    public void testPrimitiveByteArray2Longvarbinary() {
        final byte[] UNCHANGED = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveByteArray2Longvarbinary(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(42, validator);
    }

    @Test
    public void testPrimitiveByteArray2Blob() {
        final byte[] UNCHANGED = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveByteArray2Blob(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(43, validator);
    }

    @Test
    public void testPrimitiveCharArray2Varchar() {
        final char[] UNCHANGED = new char[] { 'U', 'N', 'C', 'H', 'A', 'N', 'G', 'E', 'D' };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveCharArray2Varchar(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery(
                        "select b from BasicTypesFieldAccess b where b.primitiveCharArray2Varchar = ?1 and b.id = ?2")
                        .setParameter(1, UNCHANGED).setParameter(2, id);
            }
        };
        validateMutable(44, validator);
    }

    @Test
    public void testPrimitiveCharArray2Clob() {
        final char[] UNCHANGED = new char[] { 'U', 'N', 'C', 'H', 'A', 'N', 'G', 'E', 'D' };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setPrimitiveCharArray2Clob(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(45, validator);
    }

    @Test
    public void testWrapperByteArray2Binary() {
        final Byte[] UNCHANGED = new Byte[] {(byte) 0, (byte) 1, (byte) 2,
                (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7};
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperByteArray2Binary(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery(
                        "select b from BasicTypesFieldAccess b where b.wrapperByteArray2Binary = ?1 and b.id = ?2")
                        .setParameter(1, UNCHANGED).setParameter(2, id);
            }
        };
        validateMutable(46, validator);
    }

    @Test
    public void testWrapperByteArray2Longvarbinary() {
        final Byte[] UNCHANGED = new Byte[] {(byte) 0, (byte) 1, (byte) 2,
                (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7};
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperByteArray2Longvarbinary(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(47, validator);
    }

    @Test
    public void testWrapperByteArray2Blob() {
        final Byte[] UNCHANGED = new Byte[] {(byte) 0, (byte) 1, (byte) 2,
                (byte) 3, (byte) 4, (byte) 5, (byte) 6,
                (byte) 7};
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperByteArray2Blob(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(48, validator);
    }

    @SuppressWarnings("boxing")
    @Test
    public void testWrapperCharArray2Varchar() {
        final Character[] UNCHANGED = new Character[] { 'U', 'N', 'C', 'H', 'A', 'N', 'G', 'E', 'D' };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperCharacterArray2Varchar(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery(
                        "select b from BasicTypesFieldAccess b where b.wrapperCharacterArray2Varchar = ?1 and b.id = ?2")
                        .setParameter(1, UNCHANGED).setParameter(2, id);
            }
        };
        validateMutable(49, validator);
    }

    @SuppressWarnings("boxing")
    @Test
    public void testWrapperCharArray2Clob() {
        final Character[] UNCHANGED = new Character[] { 'U', 'N', 'C', 'H', 'A', 'N', 'G', 'E', 'D' };
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setWrapperCharacterArray2Clob(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(50, validator);
    }

    @Test
    public void testSerializable() {
        Validator validator = new Validator() {
            UserDefinedSerializable UNCHANGED = new UserDefinedSerializable("Unchanged");

            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setSerializable(UNCHANGED);
            }

            @Override
            public boolean isComparableJdbcType() {
                return false;
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                throw new UnsupportedOperationException();
            }
        };
        validateMutable(51, validator);
    }

    @Test
    public void testEnumString() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setEnumString(UserDefinedEnum.HUGO);
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.enumString = ?1 and b.id = ?2")
                        .setParameter(1, UserDefinedEnum.HUGO).setParameter(2, id);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }
        };
        validateReference(52, validator);
    }

    @Test
    public void testEnumOrdinal() {
        Validator validator = new Validator() {
            @Override
            public void set(BasicTypesFieldAccess obj) {
                obj.setEnumOrdinal(UserDefinedEnum.HUGO);
            }

            @Override
            public Query createQuery(EntityManager em, int id) {
                return em.createQuery("select b from BasicTypesFieldAccess b where b.enumOrdinal = ?1 and b.id = ?2")
                        .setParameter(1, UserDefinedEnum.HUGO).setParameter(2, id);
            }

            @Override
            public boolean isComparableJdbcType() {
                return true;
            }
        };
        validateReference(53, validator);
    }

    private interface Validator {
        void set(BasicTypesFieldAccess obj);

        boolean isComparableJdbcType();

        Query createQuery(EntityManager em, int id);
    }
}
