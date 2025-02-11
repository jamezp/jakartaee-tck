/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/*
 * $Id$
 */

/*
 * @(#)ClientSignatureServletTest.java
 */

package com.sun.ts.tests.signaturetest;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("signaturetest")
@Tag("platform")
@Tag("web")
public class ClientSignatureServletTest extends JakartaEESigTest implements Serializable {
    static final String VEHICLE_ARCHIVE = "signaturetest_ClientSignatureServletTest_servlet_vehicle.war";

    /**
     * Servlet container test
     * <p>
     * Only generate the war for this test since it needs to deploy to both Platform + Web Profile.
     * <p>
     * EE 10 JavaEESigTest_servlet_vehicle.ear contents
     * <p>
     * META-INF/MANIFEST.MF
     * JavaEESigTest_servlet_vehicle_web.war
     * <p>
     * JavaEESigTest_servlet_vehicle_web.war contents
     * <p>
     * META-INF/MANIFEST.MF
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/javaee/JavaEESigTest$Containers.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/javaee/JavaEESigTest.class
     * WEB-INF/classes/com/sun/ts/lib/harness/EETest$Fault.class
     * WEB-INF/classes/com/sun/ts/lib/harness/EETest$SetupException.class
     * WEB-INF/classes/com/sun/ts/lib/harness/EETest.class
     * WEB-INF/classes/com/sun/ts/lib/harness/ServiceEETest.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleClient.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleRunnable.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleRunnerFactory.class
     * WEB-INF/classes/com/sun/ts/tests/common/vehicle/servlet/ServletVehicle.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/ApiCheckDriver.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/PackageList.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTest.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestData.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestDriver.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestEE.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SigTestResult.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SignatureTestDriver$SignatureFileInfo.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SignatureTestDriver.class
     * WEB-INF/classes/com/sun/ts/tests/signaturetest/SignatureTestDriverFactory.class
     * WEB-INF/lib/sigtest.jar
     * WEB-INF/web.xml
     */

    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive signaturetest_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, VEHICLE_ARCHIVE);
        // The class files
        signaturetest_servlet_vehicle_web.addClasses(
                ClientSignatureServletTest.class,
                JakartaEESigTest.class,
                SigTestEE.class,

                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,

                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.Status.class,
                com.sun.ts.lib.util.TestUtil.class

        );
        // The web.xml descriptor
        URL warResURL = ClientSignatureServletTest.class.getResource("/com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml");
        if (warResURL != null) {
            signaturetest_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        } else {
            throw new IllegalStateException("missing web.xml");
        }

        // add jakarta.tck:sigtest-maven-plugin jar to the war
        // Import Maven runtime dependencies
        File[] files = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .importRuntimeDependencies()
                .resolve()
                .withTransitivity()
                .asFile();
        // add signature test artifact
        final int[] addedSigtestCount = {0};
        Arrays.stream(files).filter(file -> file.getName().contains("sigtest-maven-plugin"))
                .forEach(file -> {
                    signaturetest_servlet_vehicle_web.addAsLibrary(file);
                    if (addedSigtestCount[0] > 0) {
                        throw new IllegalStateException("multiple sigtest-maven-plugin libraries found: " + file.getName());
                    }
                    addedSigtestCount[0]++;
                });
        if (addedSigtestCount[0] == 0) {
            throw new IllegalStateException("Missing sigtest-maven-plugin library");
        }
        // Call the archive processor
        archiveProcessor.processWebArchive(signaturetest_servlet_vehicle_web, ClientSignatureServletTest.class, warResURL);
        return signaturetest_servlet_vehicle_web;
    }


    /* Test setup: */

    /*
     * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
     *
     * @class.testArgs: -ap tssql.stmt
     */
    public void setup(String args[], Properties p) throws Exception {
        super.setup(args, p);
        TestUtil.logMsg("Setup signature tests");
    }

    /* Test cleanup */

    public void cleanup() throws Exception {
        TestUtil.logMsg("Cleanup ok");
    }

    @Test
    @Override
    @TargetVehicle("servlet")
    public void signatureTest() throws java.lang.Exception {
        super.signatureTest();
    }

}
