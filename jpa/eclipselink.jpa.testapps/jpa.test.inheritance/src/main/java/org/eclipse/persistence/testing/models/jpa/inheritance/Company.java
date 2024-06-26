/*
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


package org.eclipse.persistence.testing.models.jpa.inheritance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.TABLE;

@Entity
@Table(name="CMP3_COMPANY")
public class Company implements Serializable {
    private Number id;
    private String name;
    private Collection<Vehicle> vehicles;
    private Set<Engineer> engineers;

    public Company() {
        vehicles = new Vector<>();
    }

    @Id
    @GeneratedValue(strategy=TABLE, generator="COMPANY_TABLE_GENERATOR")
    @TableGenerator(
        name="COMPANY_TABLE_GENERATOR",
        table="CMP3_INHERITANCE_SEQ",
        pkColumnName="SEQ_NAME",
        valueColumnName="SEQ_COUNT",
        pkColumnValue="COMPANY_SEQ"
    )
    @Column(name="ID")
    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    @OneToMany(mappedBy="company")
    public Set<Engineer> getEngineers() {
        return engineers;
    }

    public void setEngineers(Set<Engineer> engineers) {
        this.engineers = engineers;
    }

    @OneToMany(cascade=ALL, mappedBy="owner")
    public Collection<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Collection<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Column(name="NAME")
    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }
}
