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
package org.eclipse.persistence.testing.tests.transactions;

import org.eclipse.persistence.expressions.Expression;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.testing.framework.AutoVerifyTestCase;
import org.eclipse.persistence.testing.framework.TestErrorException;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 * This class tests if the commit transaction feature
 * works for database updates.
 */
public class UpdateCommitTransactionTest extends AutoVerifyTestCase {
    Employee employee;
    Expression searchExpression;
    String oldFirstName;
    String oldLastName;

    public UpdateCommitTransactionTest() {
        super();
        employee = createEmployee();
        searchExpression = createSearchExpression(employee.getFirstName(), employee.getLastName());
    }

    private Employee createEmployee() {
        // Create the example employee
        employee = (org.eclipse.persistence.testing.models.employee.domain.Employee)new org.eclipse.persistence.testing.models.employee.domain.EmployeePopulator().basicEmployeeExample1();
        employee.setFirstName("Timugen");
        employee.setLastName("Singaera");
        employee.addResponsibility("Answer the phones.");

        oldFirstName = employee.getFirstName();
        oldLastName = employee.getLastName();

        return employee;
    }

    private Expression createSearchExpression(String firstName, String lastName) {
        // Create an expression to retreive the employee from the database
        ExpressionBuilder expressionBuilder = new ExpressionBuilder();
        Expression exp1;
        Expression exp2;
        Expression expression;

        exp1 = expressionBuilder.get("firstName").equal(firstName);
        exp2 = expressionBuilder.get("lastName").equal(lastName);

        return exp1.or(exp2);
    }

    @Override
    public String getDescription() {
        return "This test verifies that the commit transaction feature works for database inserts.";
    }

    private Employee getEmployee() {
        return employee;
    }

    private Expression getSearchExpression() {
        return searchExpression;
    }

    @Override
    public void reset() {
        DatabaseSession session = getDatabaseSession();

        Employee databaseEmployee = (Employee)session.readObject(Employee.class, getSearchExpression());

        if (databaseEmployee != null) {
            session.deleteObject(databaseEmployee);
        }

        Expression newSearchExpression = createSearchExpression(oldFirstName, oldLastName);

        databaseEmployee = (Employee)session.readObject(Employee.class, newSearchExpression);

        if (databaseEmployee != null) {
            session.deleteObject(databaseEmployee);
        }

        // Re-create the example employee and search expression
        employee = createEmployee();
        searchExpression = createSearchExpression(employee.getFirstName(), employee.getLastName());
    }

    @Override
    protected void resetVerify() {
        Session session = getSession();

        Employee databaseEmployee = (Employee)session.readObject(Employee.class, getSearchExpression());

        if (databaseEmployee != null) {
            throw new TestErrorException("Employee object has not been removed from the database");
        }

        Expression newSearchExpression = createSearchExpression("Alex", "Runparts");

        databaseEmployee = (Employee)session.readObject(Employee.class, newSearchExpression);

        if (databaseEmployee != null) {
            throw new TestErrorException("Employee object has not been removed from the database");
        }
    }

    @Override
    protected void setup() {
        // Add an example employee to the database
        getDatabaseSession().insertObject(getEmployee());
    }

    @Override
    protected void test() {
        DatabaseSession session = getDatabaseSession();

        // Read the object from the database
        Employee databaseEmployee = (Employee)session.readObject(Employee.class, getSearchExpression());

        session.beginTransaction();
        databaseEmployee.setFirstName("Alex");
        databaseEmployee.setLastName("Runparts");
        employee = databaseEmployee;
        session.updateObject(databaseEmployee);
        session.commitTransaction();
    }

    @Override
    protected void verify() {
        Session session = getSession();

        // Read the employee from the database, search using the original first and last name.
        Employee databaseEmployee = (Employee)session.readObject(Employee.class, getSearchExpression());

        // If the employee object IS in the database then there is a problem.
        if (databaseEmployee != null) {
            throw new TestErrorException("Employee object should have been updated after commit transaction");
        }

        searchExpression = createSearchExpression(employee.getFirstName(), employee.getLastName());

        // Read the employee from the database, search using the new first and last name.
        databaseEmployee = (Employee)session.readObject(Employee.class, searchExpression);

        // If an employee with the new first and last name is found then success.
        if (databaseEmployee == null) {
            throw new TestErrorException("Database should have updated employee after commit transaction");
        }
    }
}
