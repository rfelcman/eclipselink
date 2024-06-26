/*
 * Copyright (c) 2013, 2024 Oracle and/or its affiliates. All rights reserved.
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
//     Guy Pelletier - initial API and implementation
package org.eclipse.persistence.internal.jpa.config.sequencing;

import java.util.ArrayList;

import org.eclipse.persistence.internal.jpa.config.MetadataImpl;
import org.eclipse.persistence.internal.jpa.config.tables.IndexImpl;
import org.eclipse.persistence.internal.jpa.config.tables.UniqueConstraintImpl;
import org.eclipse.persistence.internal.jpa.metadata.sequencing.TableGeneratorMetadata;
import org.eclipse.persistence.jpa.config.Index;
import org.eclipse.persistence.jpa.config.TableGenerator;
import org.eclipse.persistence.jpa.config.UniqueConstraint;

/**
 * JPA scripting API implementation.
 *
 * @author Guy Pelletier
 * @since EclipseLink 2.5.1
 */
public class TableGeneratorImpl extends MetadataImpl<TableGeneratorMetadata> implements TableGenerator {

    public TableGeneratorImpl() {
        super(new TableGeneratorMetadata());

        getMetadata().setIndexes(new ArrayList<>());
        getMetadata().setUniqueConstraints(new ArrayList<>());
    }

    @Override
    public Index addIndex() {
        IndexImpl index = new IndexImpl();
        getMetadata().getIndexes().add(index.getMetadata());
        return index;
    }

    @Override
    public UniqueConstraint addUniqueConstraint() {
        UniqueConstraintImpl uniqueConstraint = new UniqueConstraintImpl();
        getMetadata().getUniqueConstraints().add(uniqueConstraint.getMetadata());
        return uniqueConstraint;
    }

    @Override
    public TableGenerator setAllocationSize(Integer allocationSize) {
        getMetadata().setAllocationSize(allocationSize);
        return this;
    }

    @Override
    public TableGenerator setCatalog(String catalog) {
        getMetadata().setCatalog(catalog);
        return this;
    }

    @Override
    public TableGenerator setCreationSuffix(String creationSuffix) {
        getMetadata().setCreationSuffix(creationSuffix);
        return this;
    }

    @Override
    public TableGenerator setInitialValue(Integer initialValue) {
        getMetadata().setInitialValue(initialValue);
        return this;
    }

    @Override
    public TableGenerator setName(String name) {
        getMetadata().setGeneratorName(name);
        return this;
    }

    @Override
    public TableGenerator setPKColumnName(String pkColumnName) {
        getMetadata().setPkColumnName(pkColumnName);
        return this;
    }

    @Override
    public TableGenerator setPKColumnValue(String pkColumnValue) {
        getMetadata().setPkColumnValue(pkColumnValue);
        return this;
    }

    @Override
    public TableGenerator setSchema(String schema) {
        getMetadata().setSchema(schema);
        return this;
    }

    @Override
    public TableGenerator setTable(String table) {
        getMetadata().setName(table);
        return this;
    }

    @Override
    public TableGenerator setValueColumnName(String valueColumnName) {
        getMetadata().setValueColumnName(valueColumnName);
        return this;
    }

}
