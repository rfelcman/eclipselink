/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
package org.eclipse.persistence.internal.jpa.config.partitioning;

import org.eclipse.persistence.internal.jpa.config.MetadataImpl;
import org.eclipse.persistence.internal.jpa.metadata.partitioning.PinnedPartitioningMetadata;
import org.eclipse.persistence.jpa.config.PinnedPartitioning;

/**
 * JPA scripting API implementation.
 *
 * @author Guy Pelletier
 * @since EclipseLink 2.5.1
 */
public class PinnedPartitioningImpl extends MetadataImpl<PinnedPartitioningMetadata> implements PinnedPartitioning {

    public PinnedPartitioningImpl() {
        super(new PinnedPartitioningMetadata());
    }

    @Override
    public PinnedPartitioning setConnectionPool(String connectionPool) {
        getMetadata().setConnectionPool(connectionPool);
        return this;
    }

    @Override
    public PinnedPartitioning setName(String name) {
        getMetadata().setName(name);
        return this;
    }

}
