/*
 * Copyright (c) 1998, 2024 Oracle and/or its affiliates. All rights reserved.
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
package org.eclipse.persistence.testing.oxm.mappings.compositeobject.singleelement;

public class Employee  {

  private int id;
  private EmailAddress emailAddress;

  public Employee() {
    super();
  }

  public int getID() {
    return id;
  }

  public void setID(int newId) {
    id = newId;
  }

  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(EmailAddress newEmailAddress) {
    emailAddress = newEmailAddress;
  }

  public String toString()
  {
        String returnString =  "Employee: " + this.getID() + " ";
        if(getEmailAddress() != null)
            returnString += getEmailAddress().toString();

        return returnString;
  }

  public boolean equals(Object object)
  {
    if(!(object instanceof Employee employeeObject))
      return false;
      return (this.getID() == employeeObject.getID()) &&
              ((this.getEmailAddress() == null && employeeObject.getEmailAddress() == null) || (this.getEmailAddress().equals(employeeObject.getEmailAddress())));
  }
}
