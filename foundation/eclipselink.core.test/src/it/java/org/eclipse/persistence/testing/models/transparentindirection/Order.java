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
package org.eclipse.persistence.testing.models.transparentindirection;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Simple order object. Just a test fixture.
 * @author Big Country
 */
public class Order extends AbstractOrder {
    public Collection salesReps;
    public Collection contacts;
    public Collection lines;

    /**
     * TopLink constructor
     */
    public Order() {
        super();
    }

    /**
     * Constructor
     */
    public Order(String customerName) {
        super(customerName);
    }

    @Override
    public void addContact(String contact) {
        contacts.add(contact);
    }

    @Override
    public void addLine(AbstractOrderLine line) {
        lines.add(line);
        line.order = this;
    }

    @Override
    public void addSalesRep(AbstractSalesRep salesRep) {
        salesReps.add(salesRep);
        salesRep.addOrder(this);
    }

    @Override
    public boolean containsContact(String contactName) {
        return contacts.contains(contactName);
    }

    @Override
    public boolean containsLine(AbstractOrderLine line) {
        return lines.contains(line);
    }

    @Override
    public boolean containsSalesRep(AbstractSalesRep salesRep) {
        return salesReps.contains(salesRep);
    }

    @Override
    public Object getContactContainer() {
        return contacts;
    }

    @Override
    public Enumeration getContactStream() {
        return (new Vector(contacts)).elements();
    }

    @Override
    public Object getLineContainer() {
        return lines;
    }

    @Override
    public void clearLines() {
        lines = new Vector();
    }

    @Override
    public Enumeration getLineStream() {
        return (new Vector(lines)).elements();
    }

    @Override
    public int getNumberOfContacts() {
        return contacts.size();
    }

    @Override
    public int getNumberOfLines() {
        return lines.size();
    }

    @Override
    public int getNumberOfSalesReps() {
        return salesReps.size();
    }

    @Override
    public Object getSalesRepContainer() {
        return salesReps;
    }

    @Override
    public Enumeration getSalesRepStream() {
        return (new Vector(salesReps)).elements();
    }

    /**
     * initialize the instance
     */
    @Override
    protected void initialize() {
        super.initialize();
        salesReps = new Vector();
        contacts = new Vector();
        lines = new Vector();
    }

    @Override
    public void removeContact(String contact) {
        contacts.remove(contact);
    }

    @Override
    public void removeLine(AbstractOrderLine line) {
        lines.remove(line);
        //    line.order = null;
    }

    @Override
    public void removeSalesRep(AbstractSalesRep salesRep) {
        salesReps.remove(salesRep);
        salesRep.removeOrder(this);
    }
}
