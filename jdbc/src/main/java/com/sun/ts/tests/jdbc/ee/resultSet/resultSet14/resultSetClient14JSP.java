/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * @(#)resultSetClient14.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet14;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The resultSetClient14 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")

public class resultSetClient14JSP extends resultSetClient14 implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet14";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet14_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
		archive.addClasses(resultSetClient14JSP.class, resultSetClient14.class);
		
	       // The jsp descriptor
  URL jspUrl = resultSetClient14JSP.class.getResource("jsp_vehicle_web.xml");
  if(jspUrl != null) {
  	archive.addAsWebInfResource(jspUrl, "web.xml");
  }
  // The sun jsp descriptor
  URL sunJSPUrl = resultSetClient14JSP.class.getResource("resultSet14_jsp_vehicle_web.war.sun-web.xml");
  if(sunJSPUrl != null) {
  	archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
  }
  // Call the archive processor
  archiveProcessor.processWebArchive(archive, resultSetClient14JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(resultSetClient14JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		
		return archive;
	};

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient14JSP theTests = new resultSetClient14JSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testGetShort04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Smallint_Tab.Call the getShort(int columnIndex)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Smallint datatype.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort04() throws Exception {
		super.testGetShort04();
  }

  /*
   * @testName: testGetShort05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Smallint_Tab.Call the getShort(int columnIndex)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the minimum Value of JDBC Smallint datatype.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort05() throws Exception {
		super.testGetShort05();
  }

  /*
   * @testName: testGetShort06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Smallint_Tab.Call the getShort(int columnIndex)
   * method.Check if it returns the value zero.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort06() throws Exception {
		super.testGetShort06();
  }

  /*
   * @testName: testGetShort76
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
   * JDBC:JAVADOC:411; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Smallint_Tab.Call the getShort(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Smallint datatype.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort76() throws Exception {
		super.testGetShort76();
  }

  /*
   * @testName: testGetShort77
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
   * JDBC:JAVADOC:411; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Smallint_Tab.Call the getShort(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Minimum Value of JDBC Smallint datatype.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort77() throws Exception {
		super.testGetShort77();
  }

  /*
   * @testName: testGetShort78
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
   * JDBC:JAVADOC:411; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Smallint_Tab.Call the getShort(String columnName)
   * method.Check if the value returned is zero.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort78() throws Exception {
		super.testGetShort78();
  }

  /*
   * @testName: testGetShort07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Integer_Tab with the
   * maximum value of table Smallint_Tab.Now execute a query to get the maximum
   * value of Integer_Tab table and retrieve the result of the query using the
   * getShort(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Smallint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort07() throws Exception {
		super.testGetShort07();
  }

  /*
   * @testName: testGetShort08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Integer_Tab with the
   * minimum value of table Smallint_Tab.Now execute a query to get the minimum
   * value of Integer_Tab table and retrieve the result of the query using the
   * getShort(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Smallint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort08() throws Exception {
		super.testGetShort08();
  }

  /*
   * @testName: testGetShort09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Integer_Tab.Call the getShort(int columnIndex)
   * method.Check if it returns the value zero.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort09() throws Exception {
		super.testGetShort09();
  }

  /*
   * @testName: testGetShort10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Real_Tab with the
   * maximum value of table Smallint_Tab.Now execute a query to get the maximum
   * value of Real_Tab table and retrieve the result of the query using the
   * getShort(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Smallint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort10() throws Exception {
		super.testGetShort10();
  }

  /*
   * @testName: testGetShort11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Real_Tab with the
   * minimum value of table Smallint_Tab.Now execute a query to get the minimum
   * value of Real_Tab table and retrieve the result of the query using the
   * getShort(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Smallint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort11() throws Exception {
		super.testGetShort11();
  }

  /*
   * @testName: testGetShort12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Real_Tab.Call the getShort(int columnIndex)
   * method.Check if it returns the value zero.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort12() throws Exception {
		super.testGetShort12();
  }

  /*
   * @testName: testGetShort16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Float_Tab with the
   * maximum value of table Smallint_Tab.Now execute a query to get the maximum
   * value of Float_Tab table and retrieve the result of the query using the
   * getShort(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Smallint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort16() throws Exception {
		super.testGetShort16();
  }

  /*
   * @testName: testGetShort17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Float_Tab with the
   * minimum value of table Smallint_Tab.Now execute a query to get the minimum
   * value of Float_Tab table and retrieve the result of the query using the
   * getShort(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Smallint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetShort17() throws Exception {
		super.testGetShort17();
  }

  /*
   * @testName: testGetShort18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:378;
   * JDBC:JAVADOC:379; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Float_Tab.Call the getShort(int columnIndex)
   * method.Check if it returns the value zero.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetShort18() throws Exception {
		super.testGetShort18();
  }
}