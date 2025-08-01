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
package org.eclipse.persistence.testing.models.interfaces;

import org.eclipse.persistence.indirection.ValueHolder;
import org.eclipse.persistence.indirection.ValueHolderInterface;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;

import java.math.BigInteger;
import java.util.Vector;

public class ProductManager implements ManagerialJob, VIP, java.io.Serializable {
    public Number jobCode;
    public Float salary;
    public String product;
    public ValueHolderInterface managedEmployees;
    public BigInteger goldCardNumber;

    public ProductManager() {
        this.managedEmployees = new ValueHolder();
    }

    public static ProductManager example1() {
        ProductManager example = new ProductManager();

        example.setProduct("Quake 2 Maps");
        example.setSalary(88000.00f);
        example.setGoldCardNumber(new BigInteger("482122381872"));

        Vector employees = new Vector(1);
        employees.add(ProductDeveloper.example1());

        example.setManagedEmployees(employees);

        return example;
    }

    public static ProductManager example2() {
        ProductManager example = new ProductManager();

        example.setProduct("Trinity Operating System");
        example.setSalary(84000.00f);
        example.setGoldCardNumber(new BigInteger("998128762878"));

        Vector employees = new Vector(1);
        employees.add(ProductDeveloper.example2());

        example.setManagedEmployees(employees);

        return example;
    }

    public static ProductManager example3() {
        ProductManager example = new ProductManager();

        example.setProduct("Ada For Dummies");
        example.setSalary(84000.00f);
        example.setGoldCardNumber(new BigInteger("144173389267"));

        Vector employees = new Vector(1);
        employees.add(ProductDeveloper.example3());

        example.setManagedEmployees(employees);

        return example;
    }

    @Override
    public BigInteger getGoldCardNumber() {
        return goldCardNumber;
    }

    @Override
    public Number getJobCode() {
        return jobCode;
    }

    @Override
    public Vector getManagedEmployees() {
        return (Vector)managedEmployees.getValue();
    }

    public String getProduct() {
        return product;
    }

    @Override
    public Float getSalary() {
        return salary;
    }

    public static TableDefinition productManagerTable() {
        TableDefinition table = new TableDefinition();

        table.setName("PRD_MGR");
        table.addField("CODE", java.math.BigDecimal.class, 15);
        table.addField("SALARY", Float.class);
        table.addField("PRODUCT", String.class, 30);
        table.addField("GOLD_CARD", BigInteger.class);

        return table;
    }

    public void setGoldCardNumber(BigInteger goldCardNumber) {
        this.goldCardNumber = goldCardNumber;
    }

    @Override
    public void setJobCode(Number jobCode) {
        this.jobCode = jobCode;
    }

    public void setManagedEmployees(Vector employees) {
        managedEmployees.setValue(employees);
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String toString() {
        return "Product Manager: " + getJobCode();
    }
}
