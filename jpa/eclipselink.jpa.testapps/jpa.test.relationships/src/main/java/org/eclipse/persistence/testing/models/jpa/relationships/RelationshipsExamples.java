/*
 * Copyright (c) 1998, 2025 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2025 IBM Corporation. All rights reserved.
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

package org.eclipse.persistence.testing.models.jpa.relationships;

import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;

import java.util.ArrayList;
import java.util.List;

public class RelationshipsExamples {
    public void buildExamples(Session session) {
        List<Object> allObjects = new ArrayList<>();
        Order orderExample1 = orderExample1();
        Customer customerExample1 = customerExample1();
        Customer customerExample2 = customerExample2();
        SalesPerson salesPerson1 = salesPersonExample1();
        ServiceCall call = new ServiceCall();
        call.setDescription("Trade from Habs.");
        CustomerServiceRepresentative rep = new CustomerServiceRepresentative();
        rep.setName("Brian");
        customerExample1.addCSInteraction(call, rep);
        ServiceCall call2 = new ServiceCall();
        call.setDescription("Trade from Leafs.");
        CustomerServiceRepresentative rep2 = new CustomerServiceRepresentative();
        rep.setName("Darrel");
        customerExample1.addCSInteraction(call2, rep2);
        customerExample1.addCCustomer(customerExample2);
        customerExample1.addCCustomer2(customerExample2);

        /*
         * Set relationships: Order1 has customer1 as the billed customer Order1
         * has customer2 as the customer OrdersList contains Order1 and is set
         * as customer2's orders SalesPerson1 is set as salesPerson to Order1
         */
        orderExample1.setBilledCustomer(customerExample1);
        orderExample1.setCustomer(customerExample2);
        List<Order> ordersList = new ArrayList<>();
        ordersList.add(orderExample1);
        customerExample2.setOrders(ordersList);
        orderExample1.setSalesPerson(salesPerson1);

        Order orderExample2 = orderExample2();
        Customer customerExample3 = customerExample3();
        Customer customerExample4 = customerExample4();
        SalesPerson salesPerson2 = salesPersonExample2();
        /*
         * Set relationships: Order2 has customer3 as the billed customer Order2
         * has customer4 as the customer Order2 is added to the OrdersList and
         * is set as customer2's orders SalesPerson2 is set as salesPerson to
         * Order2
         */
        orderExample2.setBilledCustomer(customerExample3);
        orderExample2.setCustomer(customerExample4);
        ordersList.add(orderExample2);
        customerExample4.setOrders(ordersList);
        orderExample2.setSalesPerson(salesPerson2);

        ShippingAddress shippingAddressExample1 = shippingAddressExample1();

        allObjects.add(customerExample1);
        allObjects.add(customerExample2);
        allObjects.add(orderExample1);
        allObjects.add(customerExample3);
        allObjects.add(customerExample4);
        allObjects.add(orderExample2);

        allObjects.add(itemExample1());
        allObjects.add(itemExample2());
        allObjects.add(itemExample3());
        allObjects.add(itemExample4());

        allObjects.add(orderExample3());
        allObjects.add(orderExample4());
        allObjects.add(salesPerson1);
        allObjects.add(salesPerson2);

        allObjects.add(call);
        allObjects.add(call2);
        allObjects.add(rep);
        allObjects.add(rep2);

        allObjects.add(shippingAddressExample1);

        UnitOfWork unitOfWork = session.acquireUnitOfWork();
        unitOfWork.registerAllObjects(allObjects);
        unitOfWork.commit();
    }

    public static Customer customerExample1() {
        Customer customer1 = new Customer();
        customer1.setName("John Smith");
        customer1.setCity("Ottawa");
        return customer1;
    }

    public static Customer customerExample2() {
        Customer customer2 = new Customer();
        customer2.setName("Jane Smith");
        customer2.setCity("Orleans");
        return customer2;
    }

    public static Customer customerExample3() {
        Customer customer3 = new Customer();
        customer3.setName("Karen McDonald");
        customer3.setCity("Nepean");
        return customer3;
    }

    public static Customer customerExample4() {
        Customer customer4 = new Customer();
        customer4.setName("Robert Sampson");
        customer4.setCity("Manotick");
        return customer4;
    }

    public static Item itemExample1() {
        Item item = new Item();
        item.setName("item1");
        item.setDescription("Item1 description");
        return item;
    }

    public static Item itemExample2() {
        Item item = new Item();
        item.setName("item2");
        item.setDescription("Item2 description");
        return item;
    }

    public static Item itemExample3() {
        Item item = new Item();
        item.setName("item3");
        item.setDescription("Item3 description");
        return item;
    }

    public static Item itemExample4() {
        Item item = new Item();
        item.setName("item4");
        item.setDescription("Item4 description");
        return item;
    }

    public static Order orderExample1() {
        Order order = new Order();
        order.setQuantity(70);
        order.setShippingAddress("100 Argyle Street");
        return order;
    }

    public static Order orderExample2() {
        Order order = new Order();
        order.setQuantity(680);
        order.setShippingAddress("500 Oracle Parkway");
        return order;
    }

    public static Order orderExample3() {
        Order order = new Order();
        order.setQuantity(22);
        order.setShippingAddress("240 Queen Street");
        return order;
    }

    public static Order orderExample4() {
        Order order = new Order();
        order.setQuantity(1);
        order.setShippingAddress("50 O'Connor");
        return order;
    }

    public static SalesPerson salesPersonExample1() {
        SalesPerson salesPerson = new SalesPerson();
        salesPerson.setName("Sales Person 1");
        return salesPerson;
    }

    public static SalesPerson salesPersonExample2() {
        SalesPerson salesPerson = new SalesPerson();
        salesPerson.setName("Sales Person 2");
        return salesPerson;
    }

    public static ShippingAddress shippingAddressExample1() {
        return new ShippingAddress(1001L,
                                   "Rochester",
                                   "Minnesota",
                                   55901,
                                   List.of("Peter", "Parker"),
                                   new StreetAddress(2800,
                                                     "37th St NW",
                                                     List.of("Receiving Dock", "Building 040-1")));
    }

}
