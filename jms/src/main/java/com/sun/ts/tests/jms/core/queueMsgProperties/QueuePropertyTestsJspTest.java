package com.sun.ts.tests.jms.core.queueMsgProperties;

import com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueuePropertyTestsJspTest extends com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests {
    static final String VEHICLE_ARCHIVE = "queueMsgProperties_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        queueMsgProperties_appclient_vehicle: 
        queueMsgProperties_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgProperties_ejb_vehicle: 
        queueMsgProperties_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgProperties_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queueMsgProperties_jsp_vehicle: 
        queueMsgProperties_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queueMsgProperties_servlet_vehicle: 
        queueMsgProperties_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/queueMsgProperties/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive queueMsgProperties_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "queueMsgProperties_jsp_vehicle_web.war");
            // The class files
            queueMsgProperties_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml");
            if(warResURL != null) {
              queueMsgProperties_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = QueuePropertyTests.class.getResource("//com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              queueMsgProperties_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml");
            if(warResURL != null) {
              queueMsgProperties_jsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/jsp_vehicle_web.xml");
            }
            warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/jms/core/queueMsgProperties/jsp_vehicle_web.xml");
            if(warResURL != null) {
              queueMsgProperties_jsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/jsp_vehicle_web.xml");
            }
            warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              queueMsgProperties_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              queueMsgProperties_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(queueMsgProperties_jsp_vehicle_web, QueuePropertyTests.class, warResURL);

        // Ear
            EnterpriseArchive queueMsgProperties_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queueMsgProperties_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queueMsgProperties_jsp_vehicle_ear.addAsModule(queueMsgProperties_jsp_vehicle_web);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = QueuePropertyTests.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              queueMsgProperties_jsp_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(queueMsgProperties_jsp_vehicle_ear, QueuePropertyTests.class, earResURL);
        return queueMsgProperties_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgPropertiesQTest() throws java.lang.Exception {
            super.msgPropertiesQTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgPropertiesConversionQTest() throws java.lang.Exception {
            super.msgPropertiesConversionQTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void msgPropertyExistTest() throws java.lang.Exception {
            super.msgPropertyExistTest();
        }


}