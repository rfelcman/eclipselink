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
package org.eclipse.persistence.testing.tests.tableswithspacesmodel;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.descriptors.RelationalDescriptor;
import org.eclipse.persistence.descriptors.VersionLockingPolicy;
import org.eclipse.persistence.expressions.ExpressionBuilder;
import org.eclipse.persistence.mappings.AggregateObjectMapping;
import org.eclipse.persistence.mappings.DirectCollectionMapping;
import org.eclipse.persistence.mappings.DirectToFieldMapping;
import org.eclipse.persistence.mappings.ManyToManyMapping;
import org.eclipse.persistence.mappings.OneToManyMapping;
import org.eclipse.persistence.mappings.OneToOneMapping;
import org.eclipse.persistence.mappings.TransformationMapping;
import org.eclipse.persistence.mappings.converters.ObjectTypeConverter;
import org.eclipse.persistence.queries.InMemoryQueryIndirectionPolicy;
import org.eclipse.persistence.queries.ReadObjectQuery;
import org.eclipse.persistence.sessions.DatabaseLogin;

/**
 * This class was generated by the TopLink project class generator.
 * It stores the meta-data (descriptors) that define the TopLink mappings.
 * ## OracleAS TopLink - 10g (10.1.3 ) (Build 040713) ##
 * @see org.eclipse.persistence.sessions.factories.ProjectClassGenerator
 */
public class EmployeeWithSpacesProject extends org.eclipse.persistence.sessions.Project {

    public EmployeeWithSpacesProject() {
        setName("Employee");
        applyLogin();

        addDescriptor(buildAddressDescriptor());
        addDescriptor(buildEmployeeDescriptor());
        addDescriptor(buildEmploymentPeriodDescriptor());
        addDescriptor(buildLargeProjectDescriptor());
        addDescriptor(buildPhoneNumberDescriptor());
        addDescriptor(buildProjectDescriptor());
        addDescriptor(buildSmallProjectDescriptor());
    }

    @Override
    public void applyLogin() {
        DatabaseLogin login = new DatabaseLogin();
        setDatasourceLogin(login);
    }

    public ClassDescriptor buildAddressDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.Address.class);
        descriptor.addTableName("ADDRESS TABLE");
        descriptor.addPrimaryKeyFieldName("ADDRESS TABLE.ADDRESS_ID");

        // Descriptor Properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.useRemoteSoftCacheWeakIdentityMap();
        descriptor.setRemoteIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("ADDRESS TABLE.ADDRESS_ID");
        descriptor.setSequenceNumberName("ADDRESS_SEQ");
        descriptor.setAlias("Address");

        // Cache Invalidation Policy

        // Query Manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        descriptor.getQueryManager().setQueryTimeout(0);
        // Named Queries.


        // Event Manager.

        // Mappings.
        DirectToFieldMapping cityMapping = new DirectToFieldMapping();
        cityMapping.setAttributeName("city");
        cityMapping.setFieldName("ADDRESS TABLE.CITY");
        descriptor.addMapping(cityMapping);

        DirectToFieldMapping countryMapping = new DirectToFieldMapping();
        countryMapping.setAttributeName("country");
        countryMapping.setFieldName("ADDRESS TABLE.COUNTRY");
        descriptor.addMapping(countryMapping);

        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("ADDRESS TABLE.ADDRESS_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping postalCodeMapping = new DirectToFieldMapping();
        postalCodeMapping.setAttributeName("postalCode");
        postalCodeMapping.setFieldName("ADDRESS TABLE.P_CODE");
        descriptor.addMapping(postalCodeMapping);

        DirectToFieldMapping provinceMapping = new DirectToFieldMapping();
        provinceMapping.setAttributeName("province");
        provinceMapping.setFieldName("ADDRESS TABLE.PROVINCE");
        descriptor.addMapping(provinceMapping);

        DirectToFieldMapping streetMapping = new DirectToFieldMapping();
        streetMapping.setAttributeName("street");
        streetMapping.setFieldName("ADDRESS TABLE.STREET");
        descriptor.addMapping(streetMapping);

        return descriptor;
    }

    public ClassDescriptor buildEmployeeDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.Employee.class);
        descriptor.addTableName("EMPLOYEE TABLE");
        descriptor.addTableName("SALARY TABLE");
        descriptor.addPrimaryKeyFieldName("EMPLOYEE TABLE.EMP_ID");

        // Interface Properties.
        descriptor.getInterfacePolicy().addParentInterface(org.eclipse.persistence.testing.models.employee.interfaces.Employee.class);

        // Descriptor Properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.useRemoteSoftCacheWeakIdentityMap();
        descriptor.setRemoteIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("EMPLOYEE TABLE.EMP_ID");
        descriptor.setSequenceNumberName("EMP_SEQ");
        VersionLockingPolicy lockingPolicy = new VersionLockingPolicy();
        lockingPolicy.setWriteLockFieldName("EMPLOYEE TABLE.VERSION");
        descriptor.setOptimisticLockingPolicy(lockingPolicy);
        descriptor.setAlias("Employee");

        // Cache Invalidation Policy

        // Query Manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        descriptor.getQueryManager().setQueryTimeout(0);
        // Named Queries.


        // Event Manager.

        // Mappings.
        DirectToFieldMapping firstNameMapping = new DirectToFieldMapping();
        firstNameMapping.setAttributeName("firstName");
        firstNameMapping.setFieldName("EMPLOYEE TABLE.F_NAME");
        firstNameMapping.setNullValue("");
        descriptor.addMapping(firstNameMapping);

        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping lastNameMapping = new DirectToFieldMapping();
        lastNameMapping.setAttributeName("lastName");
        lastNameMapping.setFieldName("EMPLOYEE TABLE.L_NAME");
        lastNameMapping.setNullValue("");
        descriptor.addMapping(lastNameMapping);

        DirectToFieldMapping salaryMapping = new DirectToFieldMapping();
        salaryMapping.setAttributeName("salary");
        salaryMapping.setFieldName("SALARY TABLE.SALARY");
        descriptor.addMapping(salaryMapping);

        DirectToFieldMapping genderMapping = new DirectToFieldMapping();
        genderMapping.setAttributeName("gender");
        genderMapping.setFieldName("EMPLOYEE TABLE.GENDER");
        ObjectTypeConverter genderMappingConverter = new ObjectTypeConverter();
        genderMappingConverter.addConversionValue("F", "Female");
        genderMappingConverter.addConversionValue("M", "Male");
        genderMapping.setConverter(genderMappingConverter);
        descriptor.addMapping(genderMapping);

        TransformationMapping normalHoursMapping = new TransformationMapping();
        normalHoursMapping.setAttributeName("normalHours");
        normalHoursMapping.setAttributeTransformation("buildNormalHours");
        normalHoursMapping.addFieldTransformation("EMPLOYEE TABLE.START_TIME", "getStartTime");
        normalHoursMapping.addFieldTransformation("EMPLOYEE TABLE.END_TIME", "getEndTime");
        normalHoursMapping.setIsMutable(true);
        descriptor.addMapping(normalHoursMapping);

        AggregateObjectMapping periodMapping = new AggregateObjectMapping();
        periodMapping.setAttributeName("period");
        periodMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.EmploymentPeriod.class);
        periodMapping.setIsNullAllowed(true);
        periodMapping.addFieldNameTranslation("EMPLOYEE TABLE.END_DATE", "endDate->DIRECT");
        periodMapping.addFieldNameTranslation("EMPLOYEE TABLE.START_DATE", "startDate->DIRECT");
        descriptor.addMapping(periodMapping);

        DirectCollectionMapping responsibilitiesListMapping = new DirectCollectionMapping();
        responsibilitiesListMapping.setAttributeName("responsibilitiesList");
        responsibilitiesListMapping.useBasicIndirection();
        responsibilitiesListMapping.setReferenceTableName("RESPONS TABLE");
        responsibilitiesListMapping.setDirectFieldName("RESPONS TABLE.DESCRIP");
        responsibilitiesListMapping.addReferenceKeyFieldName("RESPONS TABLE.EMP_ID", "EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(responsibilitiesListMapping);

        OneToOneMapping addressMapping = new OneToOneMapping();
        addressMapping.setAttributeName("address");
        addressMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Address.class);
        addressMapping.useBasicIndirection();
        addressMapping.privateOwnedRelationship();
        addressMapping.addForeignKeyFieldName("EMPLOYEE TABLE.ADDR_ID", "ADDRESS TABLE.ADDRESS_ID");
        descriptor.addMapping(addressMapping);

        //Joel:EJBQLTesting

        OneToOneMapping managerMapping = new OneToOneMapping();
        managerMapping.setAttributeName("manager");
        managerMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Employee.class);
        managerMapping.useBasicIndirection();
        managerMapping.addForeignKeyFieldName("EMPLOYEE TABLE.MANAGER_ID", "EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(managerMapping);

        OneToManyMapping managedEmployeesMapping = new OneToManyMapping();
        managedEmployeesMapping.setAttributeName("managedEmployees");
        managedEmployeesMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Employee.class);
        managedEmployeesMapping.useBasicIndirection();
        managedEmployeesMapping.addTargetForeignKeyFieldName("EMPLOYEE TABLE.MANAGER_ID", "EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(managedEmployeesMapping);

        OneToManyMapping phoneNumbersMapping = new OneToManyMapping();
        phoneNumbersMapping.setAttributeName("phoneNumbers");
        phoneNumbersMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.PhoneNumber.class);
        phoneNumbersMapping.useBasicIndirection();
        phoneNumbersMapping.privateOwnedRelationship();
        phoneNumbersMapping.addTargetForeignKeyFieldName("PHONE NUM.EMP_ID", "EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(phoneNumbersMapping);

        ManyToManyMapping projectsMapping = new ManyToManyMapping();
        projectsMapping.setAttributeName("projects");
        projectsMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Project.class);
        projectsMapping.useBasicIndirection();
        projectsMapping.setRelationTableName("PROJ EMP");
        projectsMapping.addSourceRelationKeyFieldName("PROJ EMP.EMP_ID", "EMPLOYEE TABLE.EMP_ID");
        projectsMapping.addTargetRelationKeyFieldName("PROJ EMP.PROJ_ID", "PROJECT TABLE.PROJ_ID");
        descriptor.addMapping(projectsMapping);

        return descriptor;
    }

    public ClassDescriptor buildEmploymentPeriodDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.descriptorIsAggregate();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.EmploymentPeriod.class);

        // Descriptor Properties.
        descriptor.setAlias("EmploymentPeriod");

        // Cache Invalidation Policy

        // Query Manager.
        // Named Queries.


        // Event Manager.

        // Mappings.
        DirectToFieldMapping endDateMapping = new DirectToFieldMapping();
        endDateMapping.setAttributeName("endDate");
        endDateMapping.setFieldName("endDate->DIRECT");
        descriptor.addMapping(endDateMapping);

        DirectToFieldMapping startDateMapping = new DirectToFieldMapping();
        startDateMapping.setAttributeName("startDate");
        startDateMapping.setFieldName("startDate->DIRECT");
        descriptor.addMapping(startDateMapping);

        return descriptor;
    }

    public ClassDescriptor buildLargeProjectDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.LargeProject.class);
        descriptor.addTableName("L PROJECT TABLE");

        // Inheritance Properties.
        descriptor.getInheritancePolicy().setParentClass(org.eclipse.persistence.testing.models.employee.domain.Project.class);
        descriptor.getInheritancePolicy().dontReadSubclassesOnQueries();

        // Interface Properties.
        descriptor.getInterfacePolicy().addParentInterface(org.eclipse.persistence.testing.models.employee.interfaces.LargeProject.class);

        // Descriptor Properties.
        descriptor.setAlias("LargeProject");

        // Cache Invalidation Policy

        // Query Manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        descriptor.getQueryManager().setQueryTimeout(0);
        // Named Queries.


        // Event Manager.

        // Mappings.
        DirectToFieldMapping budgetMapping = new DirectToFieldMapping();
        budgetMapping.setAttributeName("budget");
        budgetMapping.setFieldName("L PROJECT TABLE.BUDGET");
        descriptor.addMapping(budgetMapping);

        DirectToFieldMapping milestoneVersionMapping = new DirectToFieldMapping();
        milestoneVersionMapping.setAttributeName("milestoneVersion");
        milestoneVersionMapping.setFieldName("L PROJECT TABLE.MILESTONE");
        descriptor.addMapping(milestoneVersionMapping);

        return descriptor;
    }

    public ClassDescriptor buildPhoneNumberDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.PhoneNumber.class);
        descriptor.addTableName("PHONE NUM");
        descriptor.addPrimaryKeyFieldName("PHONE NUM.EMP_ID");
        descriptor.addPrimaryKeyFieldName("PHONE NUM.TYPE");

        // Descriptor Properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.useRemoteSoftCacheWeakIdentityMap();
        descriptor.setRemoteIdentityMapSize(100);
        descriptor.setAlias("PhoneNumber");

        // Cache Invalidation Policy

        // Query Manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        descriptor.getQueryManager().setQueryTimeout(0);
        // Named Queries.
        //Named Query -- localNumbers
        ReadObjectQuery namedQuery0 = new ReadObjectQuery(org.eclipse.persistence.testing.models.employee.domain.PhoneNumber.class);
        namedQuery0.setName("localNumbers");
        namedQuery0.setCascadePolicy(1);
        namedQuery0.setQueryTimeout(-1);
        namedQuery0.setShouldUseWrapperPolicy(true);
        namedQuery0.setShouldMaintainCache(true);
        namedQuery0.setShouldPrepare(true);
        namedQuery0.setMaxRows(0);
        namedQuery0.setShouldRefreshIdentityMapResult(false);
        namedQuery0.setCacheUsage(2);
        namedQuery0.setLockMode((short)0);
        namedQuery0.setShouldRefreshRemoteIdentityMapResult(false);
        namedQuery0.setDistinctState((short)0);
        namedQuery0.setInMemoryQueryIndirectionPolicy(new InMemoryQueryIndirectionPolicy(0));
        ExpressionBuilder expBuilder0 = namedQuery0.getExpressionBuilder();
        namedQuery0.setSelectionCriteria(expBuilder0.get("id").equal(expBuilder0.getParameter("ID")).and(expBuilder0.get("areaCode").equal("613")));
        namedQuery0.addArgument("ID", java.lang.Number.class);
        descriptor.getQueryManager().addQuery("localNumbers", namedQuery0);


        // Event Manager.

        // Query keys.
        descriptor.addDirectQueryKey("id", "PHONE NUM.EMP_ID");

        // Mappings.
        DirectToFieldMapping areaCodeMapping = new DirectToFieldMapping();
        areaCodeMapping.setAttributeName("areaCode");
        areaCodeMapping.setFieldName("PHONE NUM.AREA_CODE");
        descriptor.addMapping(areaCodeMapping);

        DirectToFieldMapping numberMapping = new DirectToFieldMapping();
        numberMapping.setAttributeName("number");
        numberMapping.setFieldName("PHONE NUM.P_NUMBER");
        descriptor.addMapping(numberMapping);

        DirectToFieldMapping typeMapping = new DirectToFieldMapping();
        typeMapping.setAttributeName("type");
        typeMapping.setFieldName("PHONE NUM.TYPE");
        descriptor.addMapping(typeMapping);

        OneToOneMapping ownerMapping = new OneToOneMapping();
        ownerMapping.setAttributeName("owner");
        ownerMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Employee.class);
        ownerMapping.useBasicIndirection();
        ownerMapping.addForeignKeyFieldName("PHONE NUM.EMP_ID", "EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(ownerMapping);

        return descriptor;
    }

    public ClassDescriptor buildProjectDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.Project.class);
        descriptor.addTableName("PROJECT TABLE");
        descriptor.addPrimaryKeyFieldName("PROJECT TABLE.PROJ_ID");

        // Inheritance Properties.
        descriptor.getInheritancePolicy().setClassIndicatorFieldName("PROJECT TABLE.PROJ_TYPE");
        descriptor.getInheritancePolicy().addClassIndicator(org.eclipse.persistence.testing.models.employee.domain.SmallProject.class, "S");
        descriptor.getInheritancePolicy().addClassIndicator(org.eclipse.persistence.testing.models.employee.domain.LargeProject.class, "L");

        // Interface Properties.
        descriptor.getInterfacePolicy().addParentInterface(org.eclipse.persistence.testing.models.employee.interfaces.Project.class);

        // Descriptor Properties.
        descriptor.useSoftCacheWeakIdentityMap();
        descriptor.setIdentityMapSize(100);
        descriptor.useRemoteSoftCacheWeakIdentityMap();
        descriptor.setRemoteIdentityMapSize(100);
        descriptor.setSequenceNumberFieldName("PROJECT TABLE.PROJ_ID");
        descriptor.setSequenceNumberName("PROJ_SEQ");
        VersionLockingPolicy lockingPolicy = new VersionLockingPolicy();
        lockingPolicy.setWriteLockFieldName("PROJECT TABLE.VERSION");
        descriptor.setOptimisticLockingPolicy(lockingPolicy);
        descriptor.setAlias("Project");

        // Cache Invalidation Policy

        // Query Manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        descriptor.getQueryManager().setQueryTimeout(0);
        // Named Queries.


        // Event Manager.

        // Mappings.
        DirectToFieldMapping descriptionMapping = new DirectToFieldMapping();
        descriptionMapping.setAttributeName("description");
        descriptionMapping.setFieldName("PROJECT TABLE.DESCRIP");
        descriptionMapping.setNullValue("");
        descriptor.addMapping(descriptionMapping);

        DirectToFieldMapping idMapping = new DirectToFieldMapping();
        idMapping.setAttributeName("id");
        idMapping.setFieldName("PROJECT TABLE.PROJ_ID");
        descriptor.addMapping(idMapping);

        DirectToFieldMapping nameMapping = new DirectToFieldMapping();
        nameMapping.setAttributeName("name");
        nameMapping.setFieldName("PROJECT TABLE.PROJ_NAME");
        nameMapping.setNullValue("");
        descriptor.addMapping(nameMapping);

        OneToOneMapping teamLeaderMapping = new OneToOneMapping();
        teamLeaderMapping.setAttributeName("teamLeader");
        teamLeaderMapping.setReferenceClass(org.eclipse.persistence.testing.models.employee.domain.Employee.class);
        teamLeaderMapping.useBasicIndirection();
        teamLeaderMapping.addForeignKeyFieldName("PROJECT TABLE.LEADER_ID", "EMPLOYEE TABLE.EMP_ID");
        descriptor.addMapping(teamLeaderMapping);

        return descriptor;
    }

    public ClassDescriptor buildSmallProjectDescriptor() {
        RelationalDescriptor descriptor = new RelationalDescriptor();
        descriptor.setJavaClass(org.eclipse.persistence.testing.models.employee.domain.SmallProject.class);
        descriptor.addTableName("PROJECT TABLE");

        // Inheritance Properties.
        descriptor.getInheritancePolicy().setParentClass(org.eclipse.persistence.testing.models.employee.domain.Project.class);
        descriptor.getInheritancePolicy().dontReadSubclassesOnQueries();

        // Interface Properties.
        descriptor.getInterfacePolicy().addParentInterface(org.eclipse.persistence.testing.models.employee.interfaces.SmallProject.class);

        // Descriptor Properties.
        descriptor.setAlias("SmallProject");

        // Cache Invalidation Policy

        // Query Manager.
        descriptor.getQueryManager().checkCacheForDoesExist();
        descriptor.getQueryManager().setQueryTimeout(0);
        // Named Queries.


        // Event Manager.

        return descriptor;
    }

}
