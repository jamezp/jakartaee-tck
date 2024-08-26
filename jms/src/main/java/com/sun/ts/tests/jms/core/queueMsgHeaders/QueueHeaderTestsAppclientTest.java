package com.sun.ts.tests.jms.core.queueMsgHeaders;

import com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTests;
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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueueHeaderTestsAppclientTest extends com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTests {
    static final String VEHICLE_ARCHIVE = "queueMsgHeaders_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        queueMsgHeaders_appclient_vehicle: 
        queueMsgHeaders_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgHeaders_ejb_vehicle: 
        queueMsgHeaders_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgHeaders_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queueMsgHeaders_jsp_vehicle: 
        queueMsgHeaders_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queueMsgHeaders_servlet_vehicle: 
        queueMsgHeaders_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/queueMsgHeaders/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive queueMsgHeaders_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "queueMsgHeaders_appclient_vehicle_client.jar");
            // The class files
            queueMsgHeaders_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = QueueHeaderTests.class.getResource("/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml");
            if(resURL != null) {
              queueMsgHeaders_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = QueueHeaderTests.class.getResource("//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              queueMsgHeaders_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            queueMsgHeaders_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + QueueHeaderTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(queueMsgHeaders_appclient_vehicle_client, QueueHeaderTests.class, resURL);

        // Ear
            EnterpriseArchive queueMsgHeaders_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queueMsgHeaders_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queueMsgHeaders_appclient_vehicle_ear.addAsModule(queueMsgHeaders_appclient_vehicle_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = QueueHeaderTests.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              queueMsgHeaders_appclient_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(queueMsgHeaders_appclient_vehicle_ear, QueueHeaderTests.class, earResURL);
        return queueMsgHeaders_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrIDQTest() throws java.lang.Exception {
            super.msgHdrIDQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrTimeStampQTest() throws java.lang.Exception {
            super.msgHdrTimeStampQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrCorlIdQTest() throws java.lang.Exception {
            super.msgHdrCorlIdQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrReplyToQTest() throws java.lang.Exception {
            super.msgHdrReplyToQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSTypeQTest() throws java.lang.Exception {
            super.msgHdrJMSTypeQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSPriorityQTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSExpirationQueueTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSDestinationQTest() throws java.lang.Exception {
            super.msgHdrJMSDestinationQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSDeliveryModeQTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSRedeliveredTest() throws java.lang.Exception {
            super.msgHdrJMSRedeliveredTest();
        }


}