/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0,
 * or the Eclipse Distribution License v. 1.0 which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 */

/**
 * {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} works together with {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper}.
 * This pair of classes allows to intercept calls to {@linkplain java.sql.Driver} and {@linkplain java.sql.Connection} methods.
 * {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} can imitate both the db going down (or losing network connection to it),
 * and coming back up (or network connection restored).
 * {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper} have the same functionality, but applied to a single connection.
 * <p>
 * There's an example of {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper} usage in EntityManagerJUnitTestSuite:
 * testEMCloseAndOpen and testEMFactoryCloseAndOpen.
 * <p>
 * To use {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} in jpa,
 * initialize {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} - all methods are static - with the original driver name.
 * <p>
 * Then create EMF, using PersistenceUnit properties, substitute:
 * the original driver class for {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} (optional) and
 * the original url for "coded" (':' substituted for'*') url (otherwise {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} would not be called - the original driver would).
 * If created EMF uses {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} it will print out connection string that looks like "jdbc*oracle*thin*@localhost*1521*orcl".
 * <p>
 * {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} just passes all the calls to the wrapped driver (the one passed to initialize method):
 * connect method wraps the created connection into {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper} and caches them.
 * <p>
 * Unless any of "break" methods called it should function in exactly the same way as original driver, the same for connections, too.
 * <p>
 * But of course real fun is in breaking:
 * <ul>
 * <li>{@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakDriver()} breaks all the methods
 * (that throw {@linkplain java.sql.SQLException}) of the {@linkplain java.sql.Driver};
 * <li>{@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakOldConnections()} breaks all connections produced so far;
 * <li>{@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakNewConnections()} ensures that all newly produced connections are broken.
 * </ul>
 * <p>
 * Any method called on broken connection results in {@linkplain java.sql.SQLException}.
 * <p>
 * The simple scenarios used in both EntityManagerJUnitTestSuite tests is imitation of db going down, then coming back:<br>
 * going down: call {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakDriver()}
 * and {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakOldConnections()}
 * (calling {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakNewConnections()}
 * is possible but won't add anything - as long as driver is broken there will be no new connections);<br>
 * coming back: call {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#repairDriver()} - now new functional
 * new connections could be created, but all old connections are still broken.
 * <p>
 * Also you can also break / repair individual connection.
 * If, say {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#breakOldConnections()}
 * was performed on {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} and repair on {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper}
 * the chronologically last call wins. There's no harm in breaking (or repairing) several times in a row.
 * <p>
 * You can pass custom exception string to each break method, otherwise defaults used (the string will be in {@linkplain java.sql.SQLException}, also visible in debugger).
 * <p>
 * Another usage that seems useful: stepping through the code in debugger you can trigger {@linkplain java.sql.SQLException}
 * to be thrown by any {@linkplain java.sql.Connection} method at will
 * be setting broken flag on {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper} to true.
 * <p>
 * After the EMF using {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper} is closed,
 * call {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.DriverWrapper#clear()} to forget the wrapped driver
 * and clear all the cached {@linkplain org.eclipse.persistence.testing.dbdriver.wrapper.ConnectionWrapper}s.
 */
package org.eclipse.persistence.testing.dbdriver.wrapper;