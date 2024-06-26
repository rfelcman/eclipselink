/*
 * Copyright (c) 2006, 2021 Oracle and/or its affiliates. All rights reserved.
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
//     Oracle - initial API and implementation
//
package org.eclipse.persistence.jpa.jpql.tools.utility.iterator;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A <code>SingleElementListIterator</code> holds a single element and returns it with the first
 * call to {@link #next()}, at which point it will return <code>false</code> to any subsequent call
 * to {@link #hasNext()}. Likewise, it will return <code>false</code> to a call to {@link #hasPrevious()}
 * until a call to {@link #next()}, at which point a call to {@link #previous()} will return the
 * single element.
 *
 * @version 2.4
 * @since 2.4
 */
@SuppressWarnings("nls")
public class SingleElementListIterator<E> implements ListIterator<E> {

    /**
     * The only element of this <code>Iterator</code>.
     */
    private E element;

    /**
     * The element that can be returned by {@link #next()}.
     */
    private E next;

    /**
     * A constant used to determine if we are at the end of the iteration.
     */
    private static final Object END = new Object();

    /**
     * Creates a new <code>SingleElementListIterator</code> that returns only the specified element.
     *
     * @param element The only element of this <code>Iterator</code>
     */
    public SingleElementListIterator(E element) {
        super();
        this.next    = element;
        this.element = element;
    }

    @Override
    public void add(E item) {
        throw new UnsupportedOperationException("This SingleElementListIterator is read-only.");
    }

    @Override
    public boolean hasNext() {
        return next == element;
    }

    @Override
    public boolean hasPrevious() {
        return next == END;
    }

    public ListIterator<E> iterator() {
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E next() {

        if (next == END) {
            throw new NoSuchElementException("No more elements can be retrieved.");
        }

        next = (E) END;
        return element;
    }

    @Override
    public int nextIndex() {
        return (next == element) ? 0 : 1;
    }

    @Override
    public E previous() {

        if (next == element) {
            throw new NoSuchElementException("No more elements can be retrieved.");
        }

        next = element;
        return element;
    }

    @Override
    public int previousIndex() {
        return (next == END) ? 0 : -1;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("This SingleElementListIterator is read-only.");
    }

    @Override
    public void set(E item) {
        throw new UnsupportedOperationException("This SingleElementListIterator is read-only.");
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(");
        sb.append(element);
        sb.append(")");
        return sb.toString();
    }
}
