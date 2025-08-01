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
package org.eclipse.persistence.testing.models.inheritance;

import org.eclipse.persistence.indirection.ValueHolder;
import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.tools.schemaframework.ViewDefinition;

import java.io.Serializable;
import java.util.Vector;

public class Vehicle implements Serializable {
    public Number id;
    public ValueHolderInterface owner;
    public Integer passengerCapacity;
    public Vector partNumbers;

    public Vehicle() {
        owner = new ValueHolder();
        partNumbers = new Vector();
    }

    public void addPartNumber(String aPartNumber) {
        partNumbers.add(aPartNumber);
    }

    public void change() {
    }

    public ValueHolderInterface getOwner() {
        return owner;
    }

    /**
     * Return the view for Sybase.
     */
    public static ViewDefinition oracleView() {
        ViewDefinition definition = new ViewDefinition();

        definition.setName("AllVehicles");
        definition.setSelectClause("Select V.*, F.FUEL_CAP, F.FUEL_TYP, B.DESCRIP, B.DRIVER_ID, C.CDESCRIP" + " from VEHICLE V, FUEL_VEH F, BUS B, CAR C" + " where V.ID = F.ID (+) AND V.ID = B.ID (+) AND V.ID = C.ID (+)");

        return definition;
    }

    public void setOwner(Company ownerCompany) {
        owner.setValue(ownerCompany);
    }

    public void setPassengerCapacity(Integer capacity) {
        passengerCapacity = capacity;
    }

    /**
     * Return the view for Sybase.
     */
    public static ViewDefinition sybaseView() {
        ViewDefinition definition = new ViewDefinition();

        definition.setName("AllVehicles");
        definition.setSelectClause("Select V.*, F.FUEL_CAP, F.FUEL_TYP, B.DESCRIP, B.DRIVER_ID, C.CDESCRIP" + " from VEHICLE V, FUEL_VEH F, BUS B, CAR C" + " where V.ID *= F.ID AND V.ID *= B.ID AND V.ID *= C.ID");

        return definition;
    }

    /**
     * Return the view for MySQL.  It is supported as of MySQL 5.0.1
     */
    public static ViewDefinition mySQLView() {
        ViewDefinition definition = new ViewDefinition();

        definition.setName("AllVehicles");
        definition.setSelectClause("Select V.*, F.FUEL_CAP, F.FUEL_TYP, B.DESCRIP, B.DRIVER_ID, C.CDESCRIP" + " from VEHICLE V left join FUEL_VEH F on V.ID = F.ID" + " left join BUS B on V.ID = B.ID left join CAR C on V.ID = C.ID");

        return definition;
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + id + ")";
    }
}
