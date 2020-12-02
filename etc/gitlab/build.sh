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

echo '-[ EclipseLink Build ]-----------------------------------------------------------'
. /etc/profile
export M2_HOME=${HOME}/extension.lib.external/apache-maven-3.6.0
ant -f antbuild.xml -Dp2.director.additionalArgs="-pluginCustomization ${HOME}/proxy.ini" clean build
