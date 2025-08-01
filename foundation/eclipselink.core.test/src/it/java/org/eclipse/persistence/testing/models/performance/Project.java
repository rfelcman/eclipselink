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
package org.eclipse.persistence.testing.models.performance;

import java.io.Serializable;

/**
 * <b>Purpose</b>: Abstract superclass for Large &amp; Small projects in Employee Demo
 * <p><b>Description</b>:     Project is an example of an abstract superclass. It demonstrates how class inheritance can be mapped to database tables.
 * It's subclasses are concrete and may or may not add columns through additional tables. The PROJ_TYPE field in the
 * database table indicates which subclass to instantiate. Projects are involved in a M:M relationship with employees.
 * The Employee classs maintains the definition of the relation table.
 * @see LargeProject
 * @see SmallProject
 */
public abstract class Project implements Serializable {
    protected long id;
    protected long version;
    protected String name;
    protected String description;
    protected Employee teamLeader;

    public Project() {
        this.name = "";
        this.description = "";
    }

    public String getDescription() {
        return description;
    }

    /**
     * Return the persistent identifier of the receiver.
     */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Employee getTeamLeader() {
        return teamLeader;
    }

    public long getVersion() {
        return version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the persistent identifier of the receiver.
     */
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamLeader(Employee teamLeader) {
        this.teamLeader = teamLeader;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
