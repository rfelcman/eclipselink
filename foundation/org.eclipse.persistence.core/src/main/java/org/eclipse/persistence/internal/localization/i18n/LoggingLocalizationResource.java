/*
 * Copyright (c) 1998, 2025 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2014, 2024 IBM Corporation and/or its affiliates. All rights reserved.
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
//     07/16/2009-2.0 Guy Pelletier
//       - 277039: JPA 2.0 Cache Usage Settings
//     06/30/2010-2.1.1 Michael O'Brien
//       - 316513: Enable JMX MBean functionality for JBoss, Glassfish and WebSphere in addition to WebLogic
//       Move JMX MBean generic registration code up from specific platforms
//       see <link>http://wiki.eclipse.org/EclipseLink/DesignDocs/316513</link>
//     10/15/2010-2.2 Guy Pelletier
//       - 322008: Improve usability of additional criteria applied to queries at the session/EM
//     10/28/2010-2.2 Guy Pelletier
//       - 3223850: Primary key metadata issues
//     03/24/2011-2.3 Guy Pelletier
//       - 337323: Multi-tenant with shared schema support (part 1)
//     04/05/2011-2.3 Guy Pelletier
//       - 337323: Multi-tenant with shared schema support (part 3)
//     03/24/2011-2.3 Guy Pelletier
//       - 337323: Multi-tenant with shared schema support (part 8)
//     07/11/2011-2.4 Guy Pelletier
//       - 343632: Can't map a compound constraint because of exception:
//                 The reference column name [y] mapped on the element [field x]
//                 does not correspond to a valid field on the mapping reference
//     14/05/2012-2.4 Guy Pelletier
//       - 376603: Provide for table per tenant support for multitenant applications
//     11/28/2012-2.5 Guy Pelletier
//       - 374688: JPA 2.1 Converter support
//     01/24/2013-2.5 Guy Pelletier
//       - 389090: JPA 2.1 DDL Generation Support
//     02/04/2013-2.5 Guy Pelletier
//       - 389090: JPA 2.1 DDL Generation Support
//     02/13/2013-2.5 Guy Pelletier
//       - 397772: JPA 2.1 Entity Graph Support (XML support)
//     09/24/2014-2.6 Rick Curtis
//       - 443762 : Misc message cleanup.
//     12/18/2014-2.6 Rick Curtis
//       - 454189 : Misc message cleanup.#2
//     01/05/2015 Rick Curtis
//       - 455683: Automatically detect target server
//     11/07/2017 - Dalia Abo Sheasha
//       - 526957 : Split the logging and trace messages
//     11/14/2017 - Dalia Abo Sheasha
//       - 527273 : Minor message improvements
//     12/05/2023: Tomas Kraus
//       - New Jakarta Persistence 3.2 Features
package org.eclipse.persistence.internal.localization.i18n;

import java.util.ListResourceBundle;

/**
 * English ResourceBundle for LoggingLocalization messages.
 *
 * @author Shannon Chen
 * @since TOPLink/Java 5.0
 * <p>
 * Internal change on 2006/04/24:
 * <p>
 * Message id is added for iAS 11 logging compliance.
 * Every message added into this file needs the message id entry as well.
 * Logs by AbstractSessionLog.getLog().log() when the level is below CONFIG=4 (FINE, FINER, FINEST, ALL)
 * should be moved to TraceLocalizationResource.
 *
 */
public class LoggingLocalizationResource extends ListResourceBundle {

    // MSGID and TOP-NNNNN pairs were removed in bug 259260, commit bc38112
    /**
     * Key and message pairs.
     */
    static final String[][] contents = {
        // CONFIG, INFO, WARNING and SEVERE level messages.
        { "topLink_version", "EclipseLink, version: {0}" },
        { "eclipselink_version_error", "File: {0} not exist or is corrupted." },
        { "application_server_name_and_version", "Server: {0}" },

        { "connected_user_database_driver", "Connected: {0}{6}User: {1}{6}Database: {2}  Version: {3}{6}Driver: {4}  Version: {5}" },
        { "connected_user_database", "Connected: {3}{4}User: {0}{3}{4}Database: {1}  Version: {2}" },
        { "JDBC_driver_does_not_support_meta_data", "Connected: unknown (JDBC Driver does not support meta data.)" },
        { "connected_sdk", "Connected: SDK" },

        { "no_session_found", "Could not find the session with the name [{0}] in the session.xml file [{1}]" },

        { "identitymap_for", "{0}{1} for: {2}" },
        { "includes", "(includes: " },
        { "key_object_null", "{0}Key: {1}{2}Object: null" },
        { "key_identity_hash_code_object", "{0}Key: {1}{2}Identity Hash Code: {3}{2}Object: {4}" },
        { "key_version_identity_hash_code_object", "{0}Key: {1}{2}Version: {5}{2}Identity Hash Code: {3}{2}Object: {4}" },
        { "elements", "{0}{1} elements" },
        { "unitofwork_identity_hashcode", "{0}UnitOfWork identity hashcode: {1}" },
        { "deleted_objects", "Deleted Objects:" },
        { "deleting_object", "The remove operation has been performed on: {0}"},
        { "register_new_for_persist", "PERSIST operation called on: {0}." },
        { "all_registered_clones", "All Registered Clones:" },
        { "new_objects", "New Objects:" },
        { "unit_of_work_thread_info", "Current unit of work in session ({0}) was created by another thread (id: {1} name: {2}), than current thread (id: {3} name: {4})" },
        { "unit_of_work_thread_info_thread_dump", """
Creation thread (id: {0} name: {1}) stack trace:
{2}

Current thread (id: {3} name: {4}) stack trace:
{5}"""},
        { "failed_to_propogate_to", "CacheSynchronization : Failed to propagate to {0}.  {1}" },
        { "exception_thrown_when_attempting_to_shutdown_cache_synch", "Exception thrown when attempting to shutdown cache synch: {0}" },
        { "corrupted_session_announcement", "SessionID: {0}  Discovery manager received corrupted session announcement - ignoring." },
        { "exception_thrown_when_attempting_to_close_listening_topic_connection", "Exception thrown when attempting to close listening topic connection: {0}" },
        { "retreived_unknown_message_type", "Retreived unknown message type: {0} from topic: {1}; ignoring" },
        { "retreived_null_message", "Retreived null message from topic: {0}; ignoring" },
        { "received_unexpected_message_type", "Received unexpected message type: {0} from topic: {1}; ignoring" },
        { "problem_adding_remote_connection", "Problem adding remote connection: {0}" },

        { "error_in_codegen", "Error during generation of concrete bean class." },
        { "error_during_PersistenceManager_setup_for_bean", "Error during PersistenceManager setup for bean: {0}" },
        { "error_in_create", "Error in create." },
        { "error_executing_ejbHome", "Error executing ejbHome: {0}" },
        { "error_in_remove", "Error in remove." },
        { "an_error_occured_trying_to_undeploy_bean", "An error occurred trying to undeploy bean (after deployment failure): {0}" },
        { "an_error_occured_executing_findByPrimaryKey", "An error occurred executing findByPrimaryKey: {0}" },
        { "an_error_occured_preparing_bean", "An error occurred preparing bean for invocation: {0}" },
        { "an_error_executing_finder", "An error occurred executing finder: {0}" },
        { "an_error_executing_ejbSelect", "An error occurred executing ejbSelect: {0}" },
        { "ejbSelect2", "EjbSelect: {0}" },
        { "error_getting_transaction_status", "Error getting transaction status.  {0}" },
        { "removeEJB_return", "removeEJB return: {0}" },
        { "failed_to_find_mbean_server", "Failed to find MBean Server: {0}" },
        { "problem_while_registering", "Problem while registering: {0}" },
        { "objectchangepolicy_turned_off", "Change tracking turned off for: {0}" },
        { "External_transaction_controller_not_defined_by_server_platform", "The DatabaseSession has an external transaction controller defined " + "by something other than the ServerPlatform. EclipseLink will permit the " + "override of the external transaction controller, but we recommend " + "you consider the alternative of subclassing " + "org.eclipse.persistence.platform.server.ServerPlatformBase " + "and override getExternalTransactionControllerClass()." },

        { "extra_cmp_field", "There is an abstract getter and/or setter defined on the [{0}] " + "abstract bean class but the corresponding cmp field [{1}] " + "is not declared in the ejb-jar.xml." },
        { "extra_ejb_select", "There is an abstract ejbSelect defined on the [{0}] " + "abstract bean class but the corresponding ejbSelect [{1}{2}] " + "entry is not declared in the ejb-jar.xml." },
        { "extra_finder", "There is a finder defined on the [{0}] " + "home interface(s) but the corresponding finder [{1}{2}] " + "entry is not declared in the ejb-jar.xml." },
        { "cmp_and_cmr_field", "The ejb-jar.xml entry for [{0}] contains both a <cmp-field> and <cmr-field> entry for the attribute [{1}].  The <cmp-field> entry will be ignored." },

        { "toplink_cmp_bean_name_xml_deprecated", "Support for toplink-cmp-bean_name.xml is deprecated. " +
                "Please refer to the documentation for the use of toplink-ejb-jar.xml" },

        { "drop_connection_on_error", "Warning: Dropping remote command connection to {0} on error {1}" },
        { "received_corrupt_announcement", "Warning: Discovery manager could not process service announcement due to {0} - ignoring announcement" },
        { "missing_converter", "Warning: Cannot convert command {0} due to missing CommandConverter - ignoring command" },
        { "failed_command_propagation", "Error: Failed trying to propagate command to {0} due to {1}" },
        { "exception_thrown_when_attempting_to_close_connection", "Warning: exception thrown when attempting to close connection" },
        { "error_executing_remote_command", "{0} command failed due to: {1}" },
        { "problem_adding_connection", "Could not add remote connection from {0} due to error: {1}" },
        { "problem_reconnect_to_jms", "Could not reconnect to JMS Topic name {0} due to error: {1}" },

        { "toplink_severe", "[EL Severe]: " },
        { "toplink_warning", "[EL Warning]: " },
        { "toplink_info", "[EL Info]: " },
        { "toplink_config", "[EL Config]: " },
        { "toplink_fine", "[EL Fine]: " },
        { "toplink_finer", "[EL Finer]: " },
        { "toplink_finest", "[EL Finest]: " },
        { "toplink", "[EL]: " },
        { "an_error_occured_initializing_dms_listener", "Exception thrown when initializing DMS embedded listener and the SPY Servlet" },

        { "input_minimum_arguments", "The command line input arguments must at least include -s, -a or -x, and -o." },
        { "validate_ejb_jar", "Validating ejb-jar.xml starts, it may take some times..."},
        { "must_define_migration_output_dir", "You must define an output directory for the migration tool" },
        { "migration_output_dir_not_valid", "The output directory ({0}) you defined is not valid" },
        { "migration_input_dir_not_valid", "The input directory ({0}) you defined is not valid" },
        { "input_and_output_dir_be_different", "You must define an output directory different from the input directory." },
        { "input_archive_format_not_supported", "Migration utility supports .ear and .jar and input archive format. The input file format as ({0}) is not supported." },
        { "archive_not_found_in_input", "The archive file ({0}) is not existed under input directory ({1})." },
        { "input_not_both_archive_and_xml", "You use either -e to specify the archive file name, or -x to signal that descriptor files under the input directory will be migrated, but not both." },
        { "input_at_least_either_archive_or_xml", "You use either -e to specify the migrated archive file name, or -x to signal that descriptor xml files under the input directory will be migrated, and you must specify and only specify one of them." },
        { "ejb_jar_xml_not_found_in_input", "The ejb-jar.xml is not present under input directory ({0})." },
        { "weblogic_ejb_jar_xml_not_found_in_input", "weblogic-ejb-jar.xml is not existed under input directory ({0}) you specified." },
        { "toplink_ejb_jar_xml_found_in_input", "The toplink-ejb-jar.xml is under input directory ({0}). You have to remove the toplink descriptor away from the input directory to process the migration." },
        { "migration_successful", "Migration Successful!" },
        { "migration_failed", "Migration Failed." },
        { "mw_project_generated_and_under", "The migrated EclipseLink Workbench project files are under ({0})." },
        { "log_file_under_output_dir", "There is a log file called ({0}) under output directory ({1})." },
        { "parse_ejb_jar_with_validation_fails", "Parsing ejb-jar.xml with validation fails with error ({0}). The migration tool will parse the xml file without validation."},
        { "jar_entry_not_migratable", "The jar entry ({0}) in the input EAR file ({1}) is not migratable." },
        { "jar_entry_has_been_migrated", "The native cmp descriptor file in the jar entry ({0}) from the input EAR file ({1}) has been migrated." },
        { "no_jar_entry_migratable_in_ear", "None of the jar entry in the input EAR file ({0}) is migratable." },
        { "invalid_command_line_argument", "The command line argument ({0}) is invalid" },
        { "persistence_unit_ignores_statments_cache_setting", "The statement cache cannot be enabled because no connection pool is configured." },
        { "column_size_not_migrated", "DB column size ({0}) is not migrated. See migration doc for details." },
        { "verifiy_columns_read_locking_not_supported", "Optimistic setting \"Read\" on \"verify-columns\" in entity ({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "verifiy_rows_read_locking_not_supported", "Optimistic setting \"Read\" on \"verify-rows\" in entity ({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "one_to_one_join_outer_migrated", "The one-to-one outer join defined for cmr field ({0}) of entity bean ({1}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "bacth_update_not_supported", "The Batch update setting batch-size with value ({0}) defined on entity bean ({1}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "data_sync_on_ejb_create_not_supported", "The data syncronization setting data-synchronization-option=\"ejbCreate\" defined on entity bean ({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "weblogic_ql_not_supported", "WebLogic-QL({0}) of the method({1} of the entity({2}) is not migrated as EclipseLink does not support WebLogic QL language." },
        { "create_default_dbms_tables_not_supported", "WLS native CMP setting \"create-default-dbms-tables\" is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "default_dbms_tables_ddl_not_supported", "WLS native CMP setting \"default-dbms-tables-ddl\" is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "enable_batch_operations_as_true_not_supported", "WLS native CMP setting \"enable-batch-operations-as-true\" is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "validate_db_schema_with_not_supported", "WLS native CMP setting \"validate-db-schema-with\" is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "automatic_key_generation_not_supported", "WLS native CMP setting \"automatic-key-generation\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "check_exist_on_method_as_true_not_supported", "WLS native CMP setting \"check-exists-on-method-as-true\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "delay_database_insert_until_ejb_create_not_supported", "WLS native CMP setting \"delay-database-insert-until-ejbCreate\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "delay_database_insert_until_ejb_post_create_not_supported", "WLS native CMP setting \"delay-database-insert-until-ejbPostCreate\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "field_group_not_supported", "WLS native CMP setting \"field-group\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "relationship_cacheing_not_supported", "WLS native CMP setting \"relationship-caching\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." },
        { "weblogic_query_not_supported", "WLS native CMP setting \"weblogic-query\" on entity({0}) is not directly supported in EclipseLink CMP. See migration doc for details." }, // unused
        { "sequence_cachekey_improper_format", "WLS native CMP setting \"key-cache-size\" on entity ({0}) is ill-formatted with value ({1})" },
        { "dir_cleaned_for_mw_files", "Files and sub-directories under directory {0} have been deleted in order to create a clean directory for the new generated EclipseLink Mapping Workbench project files" },
        { "mapping_not_supported_by_mw", "The EclipseLink mapping {0} is not supported by the mapping workbench" },
        { "toplink_ejb_jar_in_jar", "toplink-ejb-jar.xml is included in jar({0}) file, no migration therefore will be performed for this jar." },
        { "jta_cannot_be_disabled_in_cmp", "When using Container Managed Persistence (CMP), JTA cannot be disabled. EclipseLink will act as if JTA is enabled." },
        { "jta_tsr_lookup_failure", "Cannot look up TransactionSynchronizationRegistry instance: {0}"},
        { "jta_tm_lookup_failure", "Cannot look up TransactionManager instance: {0}"},
        { "jta_duplicate_ctrl_property", "JTA transaction controller class defined in both \"eclipselink.target-server\" and \"eclipselink.jta.controller\" properties. Using value from \"eclipselink.target-server\"." },
        { "descriptor_named_query_cannot_be_added", "Cannot add a descriptor named query whose name conflict with an existing query. Query To Be Added: [{0}] is named: [{1}] with arguments [{2}]." },
        { "metadata_access_type", "The access type for the persistent class [{1}] is set to [{0}]." },
        { "metadata_default_alias", "The alias name for the entity class [{0}] is being defaulted to: {1}." },
        { "metadata_default_map_key_attribute_name", "The map key attribute name for the mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_name", "The table name for entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_schema", "The table schema for entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_catalog", "The table catalog for entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_generator_name", "The table generator name defined within [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_generator_schema", "The table generator schema defined within [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_generator_catalog", "The table generator catalog defined within [{0}] is being defaulted to: {1}." },
        { "metadata_default_table_generator_pk_column_value", "The pk column value for the table generator named [{0}] defined on [{1}] from [{2}] is being defaulted to: {0}." },
        { "metadata_default_sequence_generator_sequence_name", "The sequence name for the sequence generator named [{0}] defined on [{1}] from [{2}] is being defaulted to: {0}." },
        { "metadata_default_sequence_generator_schema", "The sequence generator schema defined within [{0}] is being defaulted to: {1}." },
        { "metadata_default_sequence_generator_catalog", "The sequence generator catalog defined within [{0}] is being defaulted to: {1}." },
        { "metadata_default_join_table_name", "The join table name for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_join_schema", "The join table schema for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_join_catalog", "The join table catalog for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_secondary_table_name", "The secondary table name for the entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_secondary_schema", "The secondary table name for the entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_secondary_catalog", "The secondary table name for the entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_collection_table_name", "The collection table name for the basic collection/map mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_collection_schema", "The collection table name for the basic collection/map mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_collection_catalog", "The collection table name for the basic collection/map mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_converter_data_type", "The data type for the converter named [{2}] used with the element [{1}] in the entity [{0}] is being defaulted to [{3}]." },
        { "metadata_default_converter_object_type", "The object type for the converter named [{2}] used with the element [{1}] in the entity [{0}] is being defaulted to [{3}]." },
        { "metadata_default_entity_graph_name", "The name for the named EntityGroup specification on class [{1}] is being defaulted to: {0}" },
        { "metadata_default_column", "The column name for element [{0}] is being defaulted to: {1}." },
        { "metadata_default_order_column", "The order column name for element [{0}] is being defaulted to: {1}." },
        { "metadata_default_key_column", "The key column name for the basic map mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_value_column", "The value column name for the basic collection/map mapping element mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_pk_column", "The primary key column name for the mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_fk_column", "The foreign key column name for the mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_qk_column", "The query key name for the variable one to one mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_source_pk_column", "The source primary key column name for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_source_fk_column", "The source foreign key column name for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_target_pk_column", "The target primary key column name for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_target_fk_column", "The target foreign key column name for the many to many mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_variable_one_to_one_discriminator_column", "The discriminator column name for the variable one to one mapping [{0}] is being defaulted to: {1}." },
        { "metadata_default_inheritance_discriminator_column", "The discriminator column name for the root inheritance class [{0}] is being defaulted to: {1}." },
        { "metadata_default_inheritance_pk_column", "The primary key column name for the inheritance class [{0}] is being defaulted to: {1}." },
        { "metadata_default_inheritance_fk_column", "The foreign key column name for the inheritance class [{0}] is being defaulted to: {1}." },
        { "metadata_default_secondary_table_pk_column", "The secondary table primary key column name for element [{0}] is being defaulted to: {1}." },
        { "metadata_default_secondary_table_fk_column", "The secondary table foreign key column name for element [{0}] is being defaulted to: {1}." },
        { "metadata_default_tenant_discriminator_column", "The tenant discriminator column name for element [{0}] is being defaulted to: {1}." },
        { "metadata_default_tenant_discriminator_context_property", "The tenant discriminator context property for the tenant discriminator column [{1}] on the element [{0}] is being defaulted to: {2}." },
        { "metadata_default_tenant_table_discriminator_type", "The tenant table discriminator type for the entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_tenant_table_discriminator_context_property", "The tenant table discriminator context property for the entity [{0}] is being defaulted to: {1}." },
        { "metadata_default_one_to_one_mapping", "The element [{0}] is being defaulted to a one to one mapping." },
        { "metadata_default_one_to_many_mapping", "The element [{0}] is being defaulted to a one to many mapping." },
        { "metadata_default_variable_one_to_one_mapping", "The element [{0}] is being defaulted to a variable one to one mapping." },
        { "metadata_default_one_to_one_reference_class", "The target entity (reference) class for the one to one mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_one_to_many_reference_class", "The target entity (reference) class for the one to many mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_many_to_one_reference_class", "The target entity (reference) class for the many to one mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_many_to_many_reference_class", "The target entity (reference) class for the many to many mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_variable_one_to_one_reference_class", "The target interface (reference) class for the variable one to one mapping element [{0}] is being defaulted to: {1}." },
        { "metadata_default_element_collection_reference_class", "The target class (reference) class for the element collection mapping element [{0}] is being defaulted to: {1}." },

        { "metadata_warning_override_annotation_with_xml", "Ignoring the annotation [{0}] from [{1}] since an XML element was defined in the mapping file [{2}]." },
        { "metadata_warning_override_named_annotation_with_xml", "Ignoring the annotation [{0}] from [{2}] since an XML element with the same name [{1}] was defined in the mapping file [{3}]" },
        { "metadata_warning_override_xml_with_eclipselink_xml", "Ignoring the element [{0}] from [{1}] defined in the mapping file [{2}] since this element was defined in the eclipselink-orm mapping file [{3}]" },
        { "metadata_warning_override_named_xml_with_eclipselink_xml", "Ignoring the element [{0}] named [{1}] defined in the mapping file [{2}] since an element with the same name was defined in the eclipselink-orm mapping file [{3}]" },

        { "metadata_warning_ignore_lob", "Ignoring lob specification on element [{1}] within entity class [{0}] since EclipseLink convert metadata is specified." },
        { "metadata_warning_ignore_temporal", "Ignoring temporal specification on element [{1}] within entity class [{0}] since EclipseLink convert metadata is specified." },
        { "metadata_warning_ignore_serialized", "Ignoring default serialization on element [{1}] within entity class [{0}] since EclipseLink convert metadata is specified." },
        { "metadata_warning_ignore_enumerated", "Ignoring enumerated specification on element [{1}] within entity class [{0}] since EclipseLink convert metadata is specified." },
        { "metadata_warning_ignore_converts", "Ignoring Jakarta Persistence convert specification on element [{1}] within entity class [{0}] since EclipseLink convert metadata is specified." },
        { "metadata_warning_ignore_auto_apply_converter", "Ignoring the auto-apply converter for element [{1}] within entity class [{0}] since EclipseLink convert metadata is specified." },
        { "metadata_warning_ignore_version_locking", "Optimistic locking metadata is already defined on the descriptor for the entity [{0}]. Ignoring version specification on element [{1}]." },

        { "metadata_warning_ignore_cacheable_false", "Ignoring the explicit cacheable=false set for the entity class [{0}] since a caching type of ALL has been specified in the persistence.xml file." },
        { "metadata_warning_ignore_cacheable_true", "Ignoring the explicit cacheable=true set for the entity class [{0}] since a caching type of NONE has been specified in the persistence.xml file." },

        { "metadata_warning_ignore_mapped_superclass_additional_criteria", "Ignoring the additional criteria metadata on the mapped superclass [{1}] for the entity class [{0}] since additional criteria metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_attribute_override", "Ignoring the attribute override named [{0}] on the element [{1}] of the mapped superclass [{2}] since an attribute override with the same name has been specified on the entity class [{3}]." },
        { "metadata_warning_ignore_association_override", "Ignoring the association override named [{0}] on the element [{1}] of the mapped superclass [{2}] since an association override with the same name has been specified on the entity class [{3}]." },

        { "metadata_warning_ignore_inheritance_subclass_cache", "Ignoring the cache metadata on the inheritance subclass [{0}]. Cache metadata should only be specified on the root of the inheritance hierarchy and can not be overidden in an inheritance subclass." },
        { "metadata_warning_ignore_inheritance_subclass_cache_interceptor", "Ignoring the cache interceptor metadata on the inheritance subclass [{0}]. Cache interceptor metadata should only be specified on the root of the inheritance hierarchy and can not be overidden in an inheritance subclass." },
        { "metadata_warning_ignore_inheritance_subclass_default_redirectors", "Ignoring the default redirector metadata on the inheritance subclass [{0}]. Default redirector metadata should only be specified on the root of the inheritance hierarchy and can not be overidden in an inheritance subclass." },
        { "metadata_warning_ignore_inheritance_subclass_read_only", "Ignoring the read only setting on the inheritance subclass [{0}]. A read only setting should only be specified on the root of the inheritance hierarchy and can not be overridden in an inheritance subclass." },
        { "metadata_warning_ignore_inheritance_tenant_discriminator_column", "Ignoring the tenant discriminator column setting on the inheritance subclass [{0}]. Tenant discriminator column(s) should only be specified on the root of the inheritance hierarchy and can not be overridden and/or specified in an inheritance subclass." },
        { "metadata_warning_ignore_inheritance_tenant_table_discriminator", "Ignoring the tenant table discriminator setting on the inheritance subclass [{0}]. The tenant table discriminator should only be specified on the root of the inheritance hierarchy and can not be overridden and/or specified in an inheritance subclass." },

        { "metadata_warning_ignore_mapped_superclass_association_override", "Ignoring the association override named [{0}] defined on the mapped superclass [{1}] for the entity [{2}] since an association override with the same name was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_attribute_override", "Ignoring the attribute override named [{0}] defined on the mapped superclass [{1}] for the entity [{2}] since an attribute override override with the same name was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_copy_policy", "Ignoring the copy policy metadata on the mapped superclass [{1}] for the entity class [{0}] since copy policy metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_optimistic_locking", "Ignoring the optimistic locking metadata on the mapped superclass [{1}] for the entity class [{0}] since optimistic locking metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_cache", "Ignoring the cache metadata on the mapped superclass [{1}] for the entity class [{0}] since cache metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_cacheable", "Ignoring the cacheable metadata on the mapped superclass [{1}] for the entity class [{0}] since cacheable metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_cache_interceptor", "Ignoring the cache interceptor metadata on the mapped superclass [{1}] for the entity class [{0}] since cache interceptor metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_default_redirectors", "Ignoring the default redirector metadata on the mapped superclass [{1}] for the entity class [{0}] since default redirector metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_change_tracking", "Ignoring the change tracking metadata on the mapped superclass [{1}] for the entity class [{0}] since change tracking metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_customizer", "Ignoring the customizer on the mapped superclass [{1}] for the entity class [{0}] since customizer metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_id_class", "Ignoring the id class on the mapped superclass [{1}] for the entity class [{0}] since an id class was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_read_only", "Ignoring the read only setting on the mapped superclass [{1}] for the entity class [{0}] since read only metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_fetch_group", "Ignoring the fetch group named [{2}] on the mapped superclass [{1}] for the entity class [{0}] since a fetch group with the same name was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_existence_checking", "Ignoring the existence checking setting on the mapped superclass [{1}] for the entity class [{0}] since existence checking metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_primary_key", "Ignoring the primary key setting on the mapped superclass [{1}] for the entity class [{0}] since primary key metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_multitenant", "Ignoring the multitenant setting on the mapped superclass [{1}] for the entity class [{0}] since multitenant metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_mapped_superclass_annotation", "Ignoring the metadata [{0}] setting on the mapped superclass [{1}] for the entity class [{2}] since the metadata was previously discovered for that entity (either on the entity itself or another mapped-superclass)." },
        { "metadata_warning_ignore_lazy", "Reverting the lazy setting on the OneToOne or ManyToOne attribute [{0}] for the entity class [{1}] since weaving was not enabled or did not occur." },

        { "metadata_warning_ignore_fetch_group", "Ignoring the fetch groups specified on class [{0}] for the entity [{1}] since weaving is not enabled and the entity class does not implement the FetchGroupTracker interface." },
        { "metadata_warning_ignore_mapping_metadata", "Ignoring the jakarta.persistence metadata applied to the attribute [{0}] from class [{1}]. jakarta.persistence metadata is ignored on fields or properties that are transient, static or abstract." },

        { "metadata_warning_multiple_id_fields_without_id_class", "You have specified multiple ids for the entity class [{0}] without specifying an @IdClass. By doing this you may lose the ability to find by identity, distributed cache support etc. Note: You may however use EntityManager find operations by passing a list of primary key fields. Else, you will have to use JPQL queries to read your entities. For other id options see @PrimaryKey." },
        { "metadata_warning_inverse_access_type_mapping_override", "Overriding the {2} mapping attribute [{1}] with the {4} mapping attribute [{3}] from class [{0}]. To avoid this warning you should mark the attribute [{1}] as transient."},
        { "metadata_warning_partitioned_not_set", "@Partitioning found on the element {1} of class {0}, but no @Partitioned.  The @Partitioned annotation must be used to set the partitioning policy, @Partitioning just defines the policy, but does not set it."},
        { "metadata_warning_reference_column_not_found", "The reference column name [{0}] mapped on the element [{1}] does not correspond to a valid id or basic field/column on the mapping reference. Will use referenced column name as provided."},
        { "metadata_warning_ignore_is_null_allowed", "isNullAllowed is reset to false in {0} because the aggregate has a (possibly nested) target foreign key mapping"},
        { "non_jpa_allowed_type_used_for_collection_using_lazy_access", "Element [{1}] within entity class [{0}] uses a collection type [{2}] when the Jakarta Persistence specification only supports java.util.Collection, " +
            "java.util.Set, java.util.List, or java.util.Map.  This type is supported with eager loading; using lazy loading with this collection type requires additional configuration and an IndirectContainer implementation " +
            "that extends [{2}] or setting the mapping to use basic indirection and the type to be ValueholderInterface." },
        { "metadata_warning_integer_discriminator_could_not_be_built", "Class [{0}] specifies discriminatorType=INTEGER and uses [{1}] as the discriminatorValue.  That value cannot be converted to an integer.  We will attempt to use this value in String format." },

        { "annotation_warning_ignore_annotation", "Ignoring the annotation [{0}] on the element [{1}] because of an XML metadata-complete setting of true for this class." },
        { "annotation_warning_ignore_private_owned", "Ignoring @PrivateOwned on element [{1}] within entity class [{0}]. A @PrivateOwned can only be used with a @OneToOne, @OneToMany and @VariableOneToOne. Also note, private ownership is implied with a @BasicCollection and @BasicMap." },
        { "annotation_warning_ignore_return_insert", "Ignoring the @ReturnInsert on the element [{0}]. A @ReturnInsert is only supported with a basic mapping." },
        { "annotation_warning_ignore_return_update", "Ignoring the @ReturnUpdate on the element [{0}]. A @ReturnUpdate is only supported with a basic mapping." },

        { "weaver_null_project", "Weaver session''s project cannot be null"},
        { "weaver_disable_by_system_property", "Weaving disabled by system property {0}"},
        { "weaver_not_overwriting", "Weaver is not overwriting class {0} because it has not been set to overwrite."},
        { "weaver_could_not_write", "Weaver encountered an exception while trying to write class {0} to the file system.  The exception was: {1}"},
        { "exception_while_weaving", "Weaver encountered an exception while trying to weave class {0}. The exception was: {1}"},

        { "weaver_class_not_in_project", "Weaver found a class that is not part of the project: {0}."},
        { "cannot_weave_changetracking", "Class {0} could not be weaved for change tracking as it is not supported by its mappings."},
        { "cannot_weave_virtual_one_to_one", "Class {0} has attribute {1} that uses a OneToOne or ManyToOne mapping on a virtual attribute.  Weaving of these types of mappings is not supported.  Weaving will be disabled for {0}."},

        { "overriding_cache_isolation", "Parent Entity {0} has an isolation level of: {1} which is more protective then the subclass {2} with isolation: {3} so the subclass has been set to the isolation level {1}."},
        { "locking_required_for_database_change_notification", "Entity {0} is not using version locking, but has multiple tables or relationships, and is using Oracle database change notification, changes to relationships or secondary tables may not invalidate the cache."},

        { "field_type_set_to_java_lang_string", "The default table generator could not locate or convert a java type ({1}) into a database type for database field ({0}). The generator uses \"java.lang.String\" as default java type for the field." },
        { "relational_descriptor_support_only", "The default table generator currently only supports generating default table schema from a relational project."},
        { "default_tables_already_existed", "The table ({0}) is already in the database, and will not be created."},

        { "config_factory", "Config factory: ({0}) = ({1})"},
        { "class_list_created_by", "Class list created by ({0}).({1})() method."},
        { "jar_file_url_exception", "Exception while parsing persistence.xml.  Jar file location could not be found: {0}"},
        { "cannot_unwrap_connection", "Cannot unwrap the oracle connection wrapped by the application server because of the following exception.  {0}"},
        { "platform_specific_connection_property_exists", "Cannot add platform specific connection property {0}={1}. Property key {0} was already added to connection properties."},
        { "error_loading_xml_file", "Exception while loading ORM xml file: {0}: {1}"},
        // B5112171: XML AnyObject and AnyCollection throw NPE on null document root element
        { "exception_loading_entity_class", "An exception while trying to initialize persistence.  {1} occurred while trying to load entity class: {0}."},
        { "marshal_warning_null_document_root_element", "{0}: The undefined document root element of a referenced object [{1}] is ignored during marshalling with an any collection|object mapping." },

        { "update_all_query_cannot_use_binding_on_this_platform", "UpdateAllQuery cannot use binding on this database platform. Changed query setting to execute without binding." },

        { "broadcast_exception_thrown_when_attempting_to_close_connection", "Warning: {0}: attempt to close connection caused exception {1}" },
        { "broadcast_connection_already_closed", "Warning: {0}: attempt to close connection which has been already closed. Ignoring." },
        { "broadcast_connection_already_closing", "Warning: {0}: attempt to close connection which is currently closing. Ignoring." },
        { "broadcast_remote_command_is_null", "Warning: {0}: received message {1} containing null instead of RemoteCommand." },
        { "broadcast_remote_command_wrong_type", "Warning: {0}: received message {1} containing an object of type {2} instead of expected type RemoteCommand." },
        { "broadcast_ignored_command_while_closing_connection", "Warning: {0}: ignoring request to publish command while connection is closing." },
        { "broadcast_listening_sleep_on_error", "Warning: {0}: Exception {1} was thrown. The thread will sleep for {2} milliseconds before resuming listening." },
        { "dbPlatformHelper_defaultingPlatform", "Not able to detect platform for vendor name [{0}]. Defaulting to [{1}]. The database dialect used may not match with the database you are using. Please explicitly provide a platform using property \"eclipselink.target-database\"."},
        { "dbPlatformHelper_noMappingFound", "Can not load resource [{0}] that loads mapping from vendor name to database platform. Autodetection of database platform will not work."},
        { "pgsql10_platform_with_json_extension", "PostgreSQL10Platform with org.eclipse.persistence.pgsql module. JSON extension is enabled."},
        { "pgsql10_platform_without_json_extension", "PostgreSQL10Platform without org.eclipse.persistence.pgsql module. JSON extension is disabled."},

        { "sessions_xml_path_where_session_load_from", "The session info is loaded from [{0}]."},
        { "resource_local_persistence_init_info_ignores_jta_data_source", "PersistenceUnitInfo {0} has transactionType RESOURCE_LOCAL and therefore jtaDataSource will be ignored"},
        { "deprecated_property", "property {1} is deprecated, property {0} should be used instead."},
        { "persistence_unit_processor_error_loading_class", "{0}: {1} was thrown on attempt of PersistenceLoadProcessor to load class {2}. The class is ignored."},

        { "attempted_to_open_url_as_jar", "{1} was thrown on attempt to open {0} as a jar."},
        { "attempted_to_open_url_as_directory", "{1} was thrown on attempt to open {0} as a directory."},
        { "attempted_to_open_entry_in_url_as_jar", "{2} was thrown on attempt to open {0} as a jar and access entry: {1}."},
        { "attempted_to_open_file_url_as_directory", "{2} was thrown on attempt to open {0} as a directory and access entry: {1}."},
        { "invalid_datasource_property_value", "{1} is not a valid object to be passed in for property {0}.  Valid values are String or instances of javax.sql.DataSource."},
        { "invalid_property_value", "{1} is not a valid object to be passed in for property {0}."},
        // class name is preceeded by "class" - we need to hardcode the full package name
        { "sdo_type_generation_modified_function_naming_format_to", "{0}: Generated Type [{1}] java get/set method name changed to [{2}] to follow class naming conventions."},
        { "sdo_type_generation_modified_class_naming_format_to", "{0}: Generated Type [{1}] java class name changed to [{2}] to follow class naming conventions."},
        { "sdo_type_generation_warning_class_name_violates_java_spec", "{0}: Generated Type [{1}] conflicts with Java specification naming rules for [{2}] and should be renamed."},
        { "sdo_type_generation_warning_class_name_violates_sdo_spec", "{0}: Generated Type [{1}] conflicts with SDO specification naming rules for [{2}] and should be renamed."},

        { "sdo_classgenerator_exception", "{2} A [{0}] Exception occurred - message is [{1}]"},
        { "query_has_both_join_attributes_and_partial_attributes", "{0} named {1} has both join attributes and partial attributes. These two technologies were not designed to work together, result may be unpredictible."},
        { "sdo_missing_schemaLocation", "Referenced schema with uri {0} could not be processed because no schemaLocation attribute was specified."},
        { "sdo_invalid_schemaLocation", "Could not create schemaLocation [{0}] for import with uri [{1}]."},
        { "sdo_error_processing_referenced_schema", "An {0} occurred processing referenced schema with uri {1} with schemaLocation {2}."},
        { "sdo_error_deserialization", "Unauthorized deserialization attempt with class {0}."},
        { "ox_turn_global_logging_off", " {0} Turning global session logging off."},
        { "ox_lowering_global_logging_from_default_info_to_warning", " {0} Lowering global logging from default INFO to WARNING level."},
        { "ox_turn_session_logging_off", " {0} Turning session logging off."},
        { "ox_lowering_session_logging_from_default_info_to_warning", " {0} Lowering session logging from default INFO to WARNING level."},

        { "cannot_get_server_name_and_version", "Cannot get server name and version because of the following exception.  {0}"},
        { "communication_failure_attempting_query_retry", "Communication failure detected when attempting to perform read query outside of a transaction. Attempting to retry query. Error was: {0}."},
        { "communication_failure_attempting_begintransaction_retry", "Communication failure detected when attempting to create transaction on database.  Attempting to retry begin transaction. Error was: {0}."},
        { "persistence_unit_processor_error_loading_class_weaving_disabled", "The classLoader [{0}]: for PersistenceLoadProcessor [{1}] failed to load class [{2}]. Weaving has been disabled for this session. EclipseLink may be unable to get a spec mandated temporary class loader from the server, you may be able to use static weaving as an optional workaround. "},
        { "persistence_unit_processor_null_temp_classloader", "The classLoader for PersistenceLoadProcessor [{0}] is null. Weaving has been disabled for this session. EclipseLink may be unable to get a spec mandated temporary class loader from the server, you may be able to use static weaving as an optional workaround. "},
        { "persistence_unit_processor_npe_temp_classloader", "The classLoader [{0}] for PersistenceLoadProcessor [{1}] is causing a NPE on loadClass. Switching classLoader to [{2}].  Weaving has been disabled for this session. EclipseLink may be unable to get a spec mandated temporary class loader from the server, you may be able to use static weaving as an optional workaround. "},
        { "persistence_unit_processor_jboss_temp_classloader_bypassed", "The temporary classLoader for PersistenceLoadProcessor [{0}] is not available.  Switching classLoader to [{1}].  Weaving has been disabled for this session. EclipseLink may be unable to get a spec mandated temporary class loader from the server, you may be able to use static weaving as an optional workaround. "},
        { "persistence_unit_processor_sap_temp_classloader_bypassed", "The temporary classLoader for PersistenceLoadProcessor [{0}] is not available.  Switching classLoader to [{1}].  Weaving has been disabled for this session. EclipseLink may be unable to get a spec mandated temporary class loader from the server, you may be able to use static weaving as an optional workaround. "},
        { "persistence_unit_processor_error_in_class_forname_weaving_disabled", "The classLoader [{0}]: failed to load class [{1}]. Weaving has been disabled for this session. EclipseLink may be unable to get a spec mandated temporary class loader from the server, you may be able to use static weaving as an optional workaround. "},
        { "entity_manager_sets_property_while_context_is_active", "Property {0} is set into EntityManager when active persistence context already exists, it will be processed and take effect only when a new active persistence context is created. To create a new active persistence context the existing one should be removed - that could be done by calling clear method on the EntityManager."},
        { "osgi_initializer_failed", "Construction of environment specific OSGi initializer, [{0}] failed with message: [{1}]."},
        { "osgi_initializer", "Using OSGi initializer: [{0}]."},
        { "entity_manager_ignores_nonjta_data_source", "Persistence unit uses JTA, therefore the EntityManager ignores non jta data source. "},
        { "entity_manager_ignores_jta_data_source", "Persistence unit does not use JTA, therefore the EntityManager ignores jta data source. "},
        { "entity_manager_has_multiple_connections", "Persistence unit has multiple connections, returning the first one from the list."},
        { "problem_registering_mbean", "Problem while registering MBean: {0}" },
        { "problem_unregistering_mbean", "Problem while unregistering MBean: {0}" },
        { "session_key_for_mbean_name_is_null", "Session name used for the MBean registration cannot be null." },
        // Implemented by RuntimeServices implementing subclasses
        { "jmx_mbean_runtime_services_pool_name", "Pool Name = {0}" },
        { "jmx_mbean_runtime_services_identity_map_non_existent", "Identity Map [{0}] does not exist." },
        { "jmx_mbean_runtime_services_identity_map_empty", "Identity Map [{0}] is empty." },
        { "jmx_mbean_runtime_services_identity_map_class", "Identity Map [{0}] class = {1}" },
        { "jmx_mbean_runtime_services_no_identity_maps_in_session", "There are no Identity Maps in this session." },
        { "jmx_mbean_runtime_services_identity_map_initialized", "Identity Map [{0}] is initialized." },
        { "jmx_mbean_runtime_services_identity_map_invalidated", "Identity Map [{0}] is invalidated." },
        { "jmx_mbean_runtime_services_print_cache_key_value", "Key [{0}] => Value [{1}]" },
        { "jmx_mbean_runtime_services_no_classes_in_session", "No Classes in session." },
        { "jmx_mbean_runtime_services_statement_cache_cleared", "Statement cache cleared." },
        { "jmx_mbean_runtime_services_no_connection_pools_available", "No Connection Pools Available." },
        { "jmx_mbean_runtime_services_failed_toget_initial_context", "Failed to get InitialContext for MBean registration: {0}" },
        { "jmx_mbean_runtime_services_mbeanserver_lookup_failed", "Failed to get InitialContext for MBean registration: {0}" },
        { "jmx_mbean_runtime_services_threadpool_initialize_failed", "Failed to initialize MBean threadPoolRuntime: {0}" },
        { "jmx_mbean_runtime_services_get_executethreadruntime_object_failed", "Version of WebLogic does not support executeThreadRuntime - using ClassLoader: {0}" },
        { "nested_entity_manager_flush_not_executed_pre_query_changes_may_be_pending", "The class {0} is already flushing. The query will be executed without further changes being written to the database.  If the query is conditional upon changed data the changes may not be reflected in the results.  Users should issue a flush() call upon completion of the dependent changes and prior to this flush() to ensure correct results." },
        { "query_has_joined_attribute_outside_fetch_group", "{0}: joined attribute [{1}] is not included into the fetch group. The joined attribute data (though read from the database) will be ignored. A new sql will be executed to read again the object referenced by the joined attribute; and yet another sql to read the whole main object (because of setting the value to the attribute outside the fetch group). Either include the fetched attribute into the fetch group or remove it."},
        // JAXB Metadata Logging Messages
        { "jaxb_metadata_warning_ignoring_java_attribute", "Ignoring attribute [{0}] on class [{1}] as no Property was generated for it."},
        { "jaxb_metadata_warning_invalid_bound_type", "The bound type [{0}] for adapter class [{1}] is invalid, and will be ignored."},
        { "jaxb_metadata_warning_no_classes_to_process", "There are no classes to process for package [{0}]."},
        { "jaxb_metadata_warning_ignoring_type_on_map", "Ignoring the type attribute set on xml-element since xml-map is specified on property [{0}]."},
        { "jaxb_metadata_warning_invalid_java_attribute", "An unsupported JavaAttribute [{0}] was encountered and will be ignored."},
        { "jaxb_metadata_warning_invalid_package_level_xml_java_type_adapter", "An invalid XmlJavaTypeAdapter [{0}] was specified for package [{1}], and will be ignored."},
        { "jaxb_metadata_warning_invalid_type_level_xml_java_type_adapter", "An invalid XmlJavaTypeAdapter [{0}] was specified for class [{1}], and will be ignored."},
        { "jaxb_metadata_warning_invalid_property_level_xml_java_type_adapter", "An invalid XmlJavaTypeAdapter [{0}] was specified for field/property [{1}] on class [{2}], and will be ignored."},
        // 316513: JMX implementation for JBoss, WebSphere and Glassfish as well as WebLogic
        { "jmx_mbean_runtime_services_registration_encountered_multiple_mbeanserver_instances", "Multiple JMX MBeanServer instances [{0}] exist, we will use the server at index [{1}] : [{2}]." },
        { "jmx_mbean_runtime_services_registration_mbeanserver_print", "JMX MBeanServer instance found: [{0}], # of beans: [{1}], domain: [{2}] at index: [{3}]." },
        { "jmx_mbean_runtime_services_switching_to_alternate_mbeanserver", "JMX MBeanServer in use: [{0}] from index [{1}] " },
        { "metamodel_print_type_header", "Printed list of Metamodel [{0}] Types to follow:"},
        { "metamodel_print_type_value", "Metamodel Type: [{0}]}"},
        { "named_argument_not_found_in_query_parameters", "Missing Query parameter for named argument: {0} \"null\" will be substituted." },
        { "jmx_unregistered_mbean", "Unregistered MBean [{0}] from MBeanServer [{1}]." },
        { "jmx_unable_to_unregister_mbean", "Unable to unregister MBean [{0}] because the MBeanServer is null. Verify that your ServerPlatform is JMX enabled." },
        // 338837:
        { "metamodel_type_collection_empty", "The collection of metamodel types is empty. Model classes may not have been found during entity search for Java SE and some Java EE container managed persistence units.  Please verify that your entity classes are referenced in persistence.xml using either <class> elements or a global <exclude-unlisted-classes>false</exclude-unlisted-classes> element" },
        { "metamodel_type_collection_empty_during_lookup", "The collection of metamodel [{1}] types is empty. Model classes may not have been found during entity search for Java SE and some Java EE container managed persistence units.  Please verify that your entity classes are referenced in persistence.xml using either <class> elements or a global <exclude-unlisted-classes>false</exclude-unlisted-classes> element.  The lookup on [{0}] will return null." },
        { "jpars_could_not_find_session_bean", "A call is being made to a session bean with JNDI Name: [{0}].  That bean can not be found."},
        { "jpars_could_not_find_persistence_context", "A JPA-RS call is requesting persistence context: [{0}].  That persistence context is not found."},
        { "jpars_could_not_find_class_in_persistence_unit", "Type: [{0}] cannot be found in persistence unit: [{1}]."},
        { "jpars_could_bootstrap_persistence_context", "Persistence Context: [{0}] could not be bootstrapped."},
        { "entity_not_available_during_merge", "Max tries exceeded.  Unable to find value of locked cacheKey.  Class [{0}] ID:[{1}] This Thread: [{2}] Owning Thread:[{3}]"},
        { "ddl_generation_unknown_property_value", "Unknown {0} value provided [{1}] for the persistence unit [{2}]. Valid options are: [{3}]" },
        // SOP:
        { "sop_object_deserialze_failed", "Failed to deserialize sopObject from [{0}] in [{1}]"},
        { "sop_object_not_found", "Serialized sopObject is not found in [{0}] in [{1}]"},
        { "sop_object_wrong_version", "Removing serialized sopObject from the row because it has a wrong version [{0}] in [{1}] in [{2}]"},
        { "sop_object_wrong_pk", "Removing serialized sopObject from the row because it has a wrong primary key [{0}] in [{1}] in [{2}]"},
        // 282751
        { "removing_unique_constraint", "Removing UNIQUE constraint definition from [{0}] because it is also a primary key."},
        { "session_manager_no_partition", "No partition instance associated with current SessionManager instance."},
        { "cannot_get_nested_collection_type", "The nested collection type cannot be obtained."},
        // DBWS
        { "dbws_xml_schema_read_error", "The [{0}] XML schema could not be read."},
        { "dbws_orm_metadata_read_error", "The [{0}] ORM metadata could not be read."},
        { "dbws_oxm_metadata_read_error", "The [{0}] OXM metadata could not be read."},
        { "dbws_no_wsdl_inline_schema", "The [{0}] WSDL inline schema could not be read."},
        // JPA 3.2
        { "unknown_property_type", "Unknown {0} type of {1} persistence property"},
        { "error_queryTimeoutParse", "Cannot parse the {0} jakarta.persistence.query.timeout property value: {1}"},

        { "encryptor_script_usage", "Usage is `passwordUpdate.sh|.cmd -ip <old encrypted password>`"},
        { "encryptor_script_description", "This application has an old encrypted password that was used by a previous version of EclipseLink. Reencrypt it with the latest algorithm."},
        { "encryptor_script_output", "The reencrypted password is: {0}"},
        { "cache_key_null_read_lock_manager", "CacheKey instance: {0} , locked by cache read lock manager has null primary key." },
        { "cache_key_null_identity_map", "CacheKey instance: {0} , stored into identity map has null primary key." }
    };

    /**
     * Return the lookup table.
     */
    @Override
    protected Object[][] getContents() {
        return contents;
    }
}