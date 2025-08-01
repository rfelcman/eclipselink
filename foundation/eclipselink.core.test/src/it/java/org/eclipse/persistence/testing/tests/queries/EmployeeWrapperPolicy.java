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
package org.eclipse.persistence.testing.tests.queries;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.WrapperPolicy;
import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.testing.models.employee.domain.Employee;

/**
 * <p><b>Purpose</b>: The wrapper policy can be used to wrap all objects read from the database in another object.
 * This allows for TopLink to utilize one version of the class for its purposes and allows for the
 * application to deal with another version of the object.
 * The wrapper policy is used for things such as EJB Entity Beans and is directly used by the
 * TopLink for WebLogic product for EJB Container Managed Persistence.
 *
 * It is assumed that relationships must be through the wrapper objects.
 * Object identity is not maintained on the wrapper objects, only the wrapped object.
 *
 * This implementation of the wrapper policy is used core testing.  It has been
 * added for bugs 2612366 and 2766379.
 * @author Stephen McRitchie
 */
public class EmployeeWrapperPolicy implements WrapperPolicy {
    public ClassDescriptor descriptor;
    public static int timesUnwrapCalled = 0;

    public EmployeeWrapperPolicy() {
    }

    /**
     * PUBLIC:
     * Required: Lets the policy perform initialization.
     * @param session the session to initialize against
     */
    @Override
    public void initialize(AbstractSession session) throws DescriptorException {
        // do nothing.
    }

    /**
     * PUBLIC:
     * Required: Return true if the wrapped value should be traversed.
     * Normally the wrapped value is looked after independently, it is not required to be traversed.
     */
    @Override
    public boolean isTraversable() {
        return false;
    }

    /**
     * PUBLIC:
     * Required: Return true if the object is already wrapped.
     */
    @Override
    public boolean isWrapped(Object object) {
        return (object instanceof WrappedEmployee);
    }

    /**
     * PUBLIC:
     * Required: Set the descriptor.
     * @param descriptor the descriptor for the object being wrapped
     */
    @Override
    public void setDescriptor(ClassDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * PUBLIC:
     * Required: Unwrap the object to return the implementation that is meant to be used by TopLink.
     * The object may already be unwrapped in which case the object should be returned.
     * @param proxy the wrapped object
     * @param session the session to unwrap into
     */
    @Override
    public Object unwrapObject(Object proxy, AbstractSession session) {
        if (proxy instanceof WrappedEmployee) {
            timesUnwrapCalled++;
            Object primaryKey = ((WrappedEmployee)proxy).getId();
            return session.getIdentityMapAccessor().getFromIdentityMap(primaryKey, Employee.class);
        } else {
            return proxy;
        }
    }

    /**
     * PUBLIC:
     * Required: Wrap the object to return the implementation that the application requires.
     * The object may already be wrapped in which case the object should be returned.
     * @param original, the object to be wrapped
     * @param session the session to wrap the object against.
     * @return java.lang.Object the wrapped object
     */
    @Override
    public Object wrapObject(Object original, AbstractSession session) {
        if (original instanceof WrappedEmployee) {
            return original;
        }
        WrappedEmployee wrapped = new WrappedEmployee();
        wrapped.setId(((Employee)original).getId());
        return wrapped;
    }
}
