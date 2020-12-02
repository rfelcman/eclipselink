#!/bin/bash -e
#
# Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause

#
# Arguments:
#  N/A

echo '-[ EclipseLink Init ]-----------------------------------------------------------'

. /etc/profile

export http_proxy=http://${PROXY_HOST}:${PROXY_PORT}
export https_proxy=http://${PROXY_HOST}:${PROXY_PORT}
export no_proxy='eclipselink.uk.oracle.com'

mkdir -p $HOME/extension.lib.external/mavenant
mkdir -p $HOME/.m2/
echo '<settings
    xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
    https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <localRepository>/cache/.m2</localRepository>
    <proxies>
    <proxy>
    <id>http-proxy</id>
    <active>true</active>
    <protocol>'$PROXY_PROTOCOL'</protocol>
    <host>'$PROXY_HOST'</host>
    <port>'$PROXY_PORT'</port>
    </proxy>
    <proxy>
    <id>https-proxy</id>
    <active>true</active>
    <protocol>'$PROXY_PROTOCOL_SSL'</protocol>
    <host>'$PROXY_HOST'</host>
    <port>'$PROXY_PORT'</port>
    </proxy>
    </proxies>
    <profiles>
    <profile>
    <activation>
    <activeByDefault>true</activeByDefault>
    </activation>
    <repositories>
    <repository>
    <id>1_central</id>
    <name>Central Repository</name>
    <url>https://repo.maven.apache.org/maven2</url>
    <layout>default</layout>
    <snapshots>
    <enabled>false</enabled>
    </snapshots>
    </repository>
    </repositories>
    <pluginRepositories>
    <pluginRepository>
    <id>1_central</id>
    <name>Central Repository</name>
    <url>https://repo.maven.apache.org/maven2</url>
    <layout>default</layout>
    <snapshots>
    <enabled>false</enabled>
    </snapshots>
    <releases>
    <updatePolicy>never</updatePolicy>
    </releases>
    </pluginRepository>
    </pluginRepositories>
    </profile>
    </profiles>
    </settings>' > $HOME/.m2/settings.xml

echo 'org.eclipse.core.net/proxyData/HTTP/host='$PROXY_HOST'
      org.eclipse.core.net/proxyData/HTTPS/host='$PROXY_HOST'
      org.eclipse.core.net/proxyData/HTTPS/hasAuth=false
      org.eclipse.core.net/proxyData/HTTP/port='$PROXY_PORT'
      org.eclipse.core.net/proxyData/HTTPS/port='$PROXY_PORT'
      org.eclipse.core.net/org.eclipse.core.net.hasMigrated=true
      org.eclipse.core.net/nonProxiedHosts=*.oracle.com|*.oraclecorp.com|localhost|127.0.0.1
      org.eclipse.core.net/systemProxiesEnabled=false
      org.eclipse.core.net/proxyData/HTTP/hasAuth=false' > $HOME/proxy.ini


ln -s /cache $HOME/extension.lib.external

#DOWNLOAD SOME DEPENDENCIES
wget -nc -q https://repo1.maven.org/maven2/junit/junit/4.12/junit-4.12.jar -O $HOME/extension.lib.external/junit-4.12.jar
wget -nc -q https://repo1.maven.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar -O $HOME/extension.lib.external/hamcrest-core-1.3.jar
wget -nc -q https://repo1.maven.org/maven2/org/jmockit/jmockit/1.35/jmockit-1.35.jar -O $HOME/extension.lib.external/jmockit-1.35.jar
wget -nc -q https://repo1.maven.org/maven2/org/hibernate/validator/hibernate-validator/6.0.7.Final/hibernate-validator-6.0.7.Final.jar -O $HOME/extension.lib.external/hibernate-validator-6.0.7.Final.jar
wget -nc -q https://repo1.maven.org/maven2/org/jboss/logging/jboss-logging/3.3.0.Final/jboss-logging-3.3.0.Final.jar -O $HOME/extension.lib.external/jboss-logging-3.3.0.Final.jar
wget -nc -q https://repo1.maven.org/maven2/org/glassfish/javax.el/3.0.1-b08/javax.el-3.0.1-b08.jar -O $HOME/extension.lib.external/javax.el-3.0.1-b08.jar
wget -nc -q https://repo1.maven.org/maven2/com/fasterxml/classmate/1.3.1/classmate-1.3.1.jar -O $HOME/extension.lib.external/classmate-1.3.1.jar
wget -nc -q https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.48/mysql-connector-java-5.1.48.jar -O $HOME/extension.lib.external/mysql-connector-java-5.1.48.jar
wget -nc -q https://archive.apache.org/dist/ant/binaries/apache-ant-1.10.7-bin.tar.gz -O $HOME/extension.lib.external/apache-ant-1.10.7-bin.tar.gz
wget -nc -q https://archive.apache.org/dist/maven/ant-tasks/2.1.3/binaries/maven-ant-tasks-2.1.3.jar -O $HOME/extension.lib.external/mavenant/maven-ant-tasks-2.1.3.jar
wget -nc -q https://download.jboss.org/wildfly/15.0.1.Final/wildfly-15.0.1.Final.tar.gz -O $HOME/extension.lib.external/wildfly-15.0.1.Final.tar.gz
#wget -nc -q https://download.eclipse.org/eclipse/downloads/drops4/R-4.10-201812060815/eclipse-SDK-4.10-linux-gtk-x86_64.tar.gz -O $HOME/extension.lib.external/eclipse-SDK-4.10-linux-gtk-x86_64.tar.gz
wget -nc -q http://eclipselink.uk.oracle.com/eclipse-SDK-4.10-linux-gtk-x86_64.tar.gz -O $HOME/extension.lib.external/eclipse-SDK-4.10-linux-gtk-x86_64.tar.gz
wget -nc -q https://archive.apache.org/dist/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz -O $HOME/extension.lib.external/apache-maven-3.6.0-bin.tar.gz

#UNPACK SOME  DEPENDENCIES
tar -x -z -C $HOME/extension.lib.external -f $HOME/extension.lib.external/wildfly-15.0.1.Final.tar.gz
tar -x -z -C $HOME/extension.lib.external -f $HOME/extension.lib.external/eclipse-SDK-4.10-linux-gtk-x86_64.tar.gz
tar -x -z -C $HOME/extension.lib.external -f $HOME/extension.lib.external/apache-maven-3.6.0-bin.tar.gz

#PREPARE build.properties FILE
echo "extensions.depend.dir=$HOME/extension.lib.external" >> $HOME/build.properties
echo "junit.lib=$HOME/extension.lib.external/junit-4.12.jar:$HOME/extension.lib.external/hamcrest-core-1.3.jar" >> $HOME/build.properties
echo 'jdbc.driver.jar=$HOME/extension.lib.external/mysql-connector-java-5.1.48.jar' >> $HOME/build.properties
echo 'db.driver=com.mysql.jdbc.Driver' >> $HOME/build.properties
echo "db.url=$TEST_DB_URL" >> $HOME/build.properties
echo "db.user=$TEST_DB_USERNAME" >> $HOME/build.properties
echo "db.pwd=$TEST_DB_PASSWORD" >> $HOME/build.properties
echo 'db.platform=org.eclipse.persistence.platform.database.MySQLPlatform' >> $HOME/build.properties
echo "eclipse.install.dir=$HOME/extension.lib.external/eclipse" >> $HOME/build.properties
echo 'server.name=wildfly' >> $HOME/build.properties
echo "wildfly.home=$HOME/extension.lib.external/wildfly-15.0.1.Final" >> $HOME/build.properties
echo 'wildfly.config=standalone-full.xml' >> $HOME/build.properties
