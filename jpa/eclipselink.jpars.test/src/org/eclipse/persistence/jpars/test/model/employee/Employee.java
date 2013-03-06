/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 ******************************************************************************/
package org.eclipse.persistence.jpars.test.model.employee;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Version;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;
import org.eclipse.persistence.annotations.PrivateOwned;

@NamedQueries({
        @NamedQuery(
                name = "Employee.getManager",
                query = "select u.firstName, u.lastName, u.manager from Employee u"),
        @NamedQuery(
                name = "Employee.getManagerById",
                query = "select u.firstName, u.lastName, u.manager from Employee u where u.id = :id"),
        @NamedQuery(
                name = "Employee.salaryMax",
                query = "SELECT e.id, max(e.salary) AS max_salary from Employee e GROUP BY e.id, e.salary"),
        @NamedQuery(
                name = "Employee.count",
                query = "SELECT count(e) FROM Employee e"),
        @NamedQuery(
                name = "Employee.getPhoneNumbers",
                query = "SELECT e.firstName, e.lastName, pn FROM Employee e JOIN e.phoneNumbers pn"),
        @NamedQuery(
                name = "Employee.findAll", 
                query = "SELECT e FROM Employee e ORDER BY e.id")
})
@Entity
@Table(name = "JPARS_EMPLOYEE")
@SecondaryTable(name = "JPARS_SALARY")
@ObjectTypeConverter(name = "gender", objectType = Gender.class, dataType = String.class, conversionValues = {
        @ConversionValue(dataValue = "M", objectValue = "Male"),
        @ConversionValue(dataValue = "F", objectValue = "Female") })
public class Employee {

    @Id
    @GeneratedValue
    @Column(name = "EMP_ID")
    private int id;

    @Column(name = "F_NAME")
    private String firstName;

    /**
     * Gender mapped using Basic with an ObjectTypeConverter to map between
     * single char code value in databse to enum. JPA only supports mapping to
     * the full name of the enum or its ordinal value.
     */
    @Basic
    @Column(name = "GENDER")
    @Convert("gender")
    private Gender gender = Gender.Male;

    @Column(name = "L_NAME")
    private String lastName;

    @Column(table = "JPARS_SALARY")
    private double salary;

    @Version
    private Long version;
    
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "EMP_ID"), inverseJoinColumns = @JoinColumn(name = "PROJ_ID"), name = "JPARS_PROJ_EMP")
    private List<Project> projects = new ArrayList<Project>();

    @ManyToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employee manager;

    @OneToMany(mappedBy = "manager")
    private List<Employee> managedEmployees = new ArrayList<Employee>();

    @OneToMany(mappedBy = "employee", cascade = ALL, fetch = LAZY)
    @PrivateOwned
    private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

    @OneToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "ADDR_ID")
    @PrivateOwned
    private EmployeeAddress address;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "START_DATE")),
            @AttributeOverride(name = "endDate", column = @Column(name = "END_DATE")) })
    private EmploymentPeriod period;

    @ElementCollection
    @CollectionTable(name = "JPARS_RESPONS")
    private List<String> responsibilities = new ArrayList<String>();

    @OneToMany(mappedBy="employee", cascade=CascadeType.ALL)
    private List<Expertise> expertiseAreas = new ArrayList<Expertise>();
    
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JoinColumn(name = "OFFICE_ID")
    private Office office;
    
    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int empId) {
        this.id = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projectList) {
        this.projects = projectList;
    }

    public Project addProject(Project project) {
        getProjects().add(project);
        return project;
    }

    public Project removeProject(Project project) {
        getProjects().remove(project);
        return project;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public List<Employee> getManagedEmployees() {
        return this.managedEmployees;
    }

    public void setManagedEmployees(List<Employee> employeeList) {
        this.managedEmployees = employeeList;
    }

    public Employee addManagedEmployee(Employee employee) {
        getManagedEmployees().add(employee);
        employee.setManager(this);
        return employee;
    }

    public Employee removeManagedEmployee(Employee employee) {
        getManagedEmployees().remove(employee);
        employee.setManager(null);
        return employee;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneNumber> phoneNumberList) {
        this.phoneNumbers = phoneNumberList;
    }

    public PhoneNumber addPhoneNumber(PhoneNumber phoneNumber) {
        getPhoneNumbers().add(phoneNumber);
        phoneNumber.setEmployee(this);
        return phoneNumber;
    }

    public PhoneNumber addPhoneNumber(String type, String areaCode,
            String number) {
        PhoneNumber phoneNumber = new PhoneNumber(type, areaCode, number);
        return addPhoneNumber(phoneNumber);
    }

    public PhoneNumber removePhoneNumber(PhoneNumber phoneNumber) {
        getPhoneNumbers().remove(phoneNumber);
        phoneNumber.setEmployee(null);
        return phoneNumber;
    }

    public void setAddress(EmployeeAddress address) {
        this.address = address;
    }

    public EmployeeAddress getAddress() {
        return address;
    }

    public void setPeriod(EmploymentPeriod period) {
        this.period = period;
    }

    public EmploymentPeriod getPeriod() {
        return period;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<String> getResponsibilities() {
        return this.responsibilities;
    }

    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public void addResponsibility(String responsibility) {
        getResponsibilities().add(responsibility);
    }

    public void removeResponsibility(String responsibility) {
        getResponsibilities().remove(responsibility);
    }

    public List<Expertise> getExpertiseAreas() {
        return expertiseAreas;
    }

    public void setExpertiseAreas(List<Expertise> expertiseAreas) {
        this.expertiseAreas = expertiseAreas;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String toString() {
        return "Employee(" + getId() + ": " + getLastName() + ", "
                + getFirstName() + ")";
    }
}