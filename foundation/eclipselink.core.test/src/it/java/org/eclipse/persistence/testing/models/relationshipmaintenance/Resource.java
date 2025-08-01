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
package org.eclipse.persistence.testing.models.relationshipmaintenance;

import org.eclipse.persistence.indirection.ValueHolder;
import org.eclipse.persistence.indirection.ValueHolderInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Resource {
    public int id;
    public String name;
    public ValueHolderInterface office;
    protected PropertyChangeListener topLinkListener;

    public Resource() {
        office = new ValueHolder();
    }

    public Resource(String name, FieldOffice office) {
        this();
        this.name = name;
        this.office = new ValueHolder(office);
    }

    /**
     * @return int
     */
    public int getId() {
        return id;
    }

    public FieldOffice getOffice() {
        return (FieldOffice)office.getValue();
    }

    public String getName() {
        return this.name;
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/30/01 6:53:59 PM)
     * @param newId int
     */
    public void setId(int newId) {
        propertyChange("id", this.id, newId);
        id = newId;
    }

    public void setOffice(FieldOffice newOffice) {
        propertyChange("office", office.getValue(), newOffice);
        office.setValue(newOffice);
    }

    public void setName(String newName) {
        propertyChange("name", this.name, newName);
        this.name = newName;
    }

    public String toString() {
        return getClass().getSimpleName() + "(" + id + ", " + System.identityHashCode(this) + ")";
    }

    /**
     * PUBLIC:
     * Return the PropertyChangeListener for the object.
     */
    public PropertyChangeListener getTrackedPropertyChangeListener() {
        return this.topLinkListener;
    }

    /**
     * PUBLIC:
     * Set the PropertyChangeListener for the object.
     */
    public void setTrackedPropertyChangeListener(PropertyChangeListener listener) {
        this.topLinkListener = listener;
    }

    public void propertyChange(String propertyName, Object oldValue, Object newValue) {
        if (topLinkListener != null) {
            if (oldValue != newValue) {
                topLinkListener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
            }
        }
    }
}
