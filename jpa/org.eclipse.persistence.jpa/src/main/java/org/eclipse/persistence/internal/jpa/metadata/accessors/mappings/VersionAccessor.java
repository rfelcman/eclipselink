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
//     05/16/2008-1.0M8 Guy Pelletier
//       - 218084: Implement metadata merging functionality between mapping files
//     04/27/2010-2.1 Guy Pelletier
//       - 309856: MappedSuperclasses from XML are not being initialized properly
//     03/24/2011-2.3 Guy Pelletier
//       - 337323: Multi-tenant with shared schema support (part 1)
//      //     30/05/2012-2.4 Guy Pelletier
//       - 354678: Temp classloader is still being used during metadata processing
package org.eclipse.persistence.internal.jpa.metadata.accessors.mappings;

import java.time.Instant;
import java.time.LocalDateTime;

import org.eclipse.persistence.descriptors.InstantLockingPolicy;
import org.eclipse.persistence.descriptors.LocalDateTimeLockingPolicy;
import org.eclipse.persistence.descriptors.TimestampLockingPolicy;
import org.eclipse.persistence.descriptors.VersionLockingPolicy;
import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.jpa.metadata.MetadataDescriptor;
import org.eclipse.persistence.internal.jpa.metadata.MetadataLogger;
import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.ClassAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAnnotatedElement;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAnnotation;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataClass;

/**
 * INTERNAL:
 * A basic version accessor.
 * <p>
 * Key notes:
 * - any metadata mapped from XML to this class must be compared in the
 *   equals method.
 * - any metadata mapped from XML to this class must be handled in the merge
 *   method. (merging is done at the accessor/mapping level)
 * - any metadata mapped from XML to this class must be initialized in the
 *   initXMLObject  method.
 * - methods should be preserved in alphabetical order.
 *
 * @author Guy Pelletier
 * @since EclipseLink 1.0
 */
public class VersionAccessor extends BasicAccessor {
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public VersionAccessor() {
        super("<version>");
    }

    /**
     * INTERNAL:
     */
    public VersionAccessor(MetadataAnnotation version, MetadataAnnotatedElement annotatedElement, ClassAccessor classAccessor) {
        super(version, annotatedElement, classAccessor);
    }

    /**
     * INTERNAL:
     */
    @Override
    public boolean equals(Object objectToCompare) {
        return super.equals(objectToCompare) && objectToCompare instanceof VersionAccessor;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * INTERNAL:
     * Returns true if the given class is a valid version locking type.
     */
    protected boolean isValidVersionLockingType(MetadataClass cls) {
        return (cls.isClass(int.class) ||
                cls.isClass(Integer.class) ||
                cls.isClass(short.class) ||
                cls.isClass(Short.class) ||
                cls.isClass(long.class) ||
                cls.isClass(Long.class) ||
                cls.isClass(java.sql.Timestamp.class) ||
                cls.isClass(LocalDateTime.class) ||
                cls.isClass(Instant.class));
    }

    /**
     * INTERNAL:
     * Process a version accessor.
     */
    @Override
    public void process() {
        // This will initialize the m_field variable. Accessible through getField().
        super.process();

        // Process an @Version or version element if there is one.
        if (getDescriptor().usesOptimisticLocking()) {
            // Ignore the version locking if it is already set.
            getLogger().logConfigMessage(MetadataLogger.IGNORE_VERSION_LOCKING, this);
        } else {
            MetadataClass lockType = getRawClass();
            getDatabaseField().setTypeName(getJavaClassName(lockType));

            if (isValidVersionLockingType(lockType)) {
                for (MetadataDescriptor owningDescriptor : getOwningDescriptors()) {
                    VersionLockingPolicy policy = createVersionLockingPolicy(lockType.getName(), getDatabaseField());
                    policy.storeInObject();
                    policy.setIsCascaded(getDescriptor().usesCascadedOptimisticLocking());
                    owningDescriptor.setOptimisticLockingPolicy(policy);
                }
            } else {
                throw ValidationException.invalidTypeForVersionAttribute(getAttributeName(), lockType, getJavaClass());
            }
        }
    }

    // Create VersionLockingPolicy corresponding to field type name
    private static VersionLockingPolicy createVersionLockingPolicy(String typeName, DatabaseField field) {
        switch (typeName) {
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
            case "short":
            case "java.lang.Short":
                return new VersionLockingPolicy(field);
            case "java.sql.Timestamp":
                return new TimestampLockingPolicy(field);
            case "java.time.LocalDateTime":
                return new LocalDateTimeLockingPolicy(field);
            case "java.time.Instant":
                return new InstantLockingPolicy(field);
            // This is not accessible as long as isValidTimestampVersionLockingType(MetadataClass) check
            // is not broken so this exception always means bug in the code.
            default:
                throw new UnsupportedOperationException("Cannot create VersionLockingPolicy for " + typeName);
        }
    }

}
