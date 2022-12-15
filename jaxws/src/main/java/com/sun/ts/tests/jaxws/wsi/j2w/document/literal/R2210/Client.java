/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package com.sun.ts.tests.jaxws.wsi.j2w.document.literal.R2210;

import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.doclitclient.*;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;

public class Client extends ServiceEETest
    implements DescriptionConstants, SOAPConstants {
  /**
   * The client.
   */
  private SOAPClient client;

  static J2WDLShared service = null;

  /**
   * The document.
   */
  private Document document;

  /**
   * Test entry point.
   * 
   * @param args
   *          the command-line arguments.
   */
  public static void main(String[] args) {
    Client test = new Client();
    Status status = test.run(args, System.out, System.err);
    status.exit();
  }

  /**
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client = ClientFactory.getClient(J2WDLSharedClient.class, properties, this,
        service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testPartlessBodies
   *
   * @assertion_ids: WSI:SPEC:R2210
   *
   * @test_Strategy: Retrieve the WSDL, generated by the Java-to-WSDL tool, and
   *                 examine all soap:body elements, in document-literal
   *                 bindings, ensuring that if they do not specify the parts
   *                 attribute, that the corresponding message defines exactly
   *                 one part.
   * 
   * @throws Fault
   */
  public void testPartlessBodies() throws Fault {
    document = client.getDocument();
    Element[] bindings = DescriptionUtils.getBindings(document);
    for (int i = 0; i < bindings.length; i++) {
      verifyBinding(bindings[i]);
    }
  }

  protected void verifyBinding(Element binding) throws Fault {
    Element soapBinding = DescriptionUtils.getChildElement(binding,
        SOAP_NAMESPACE_URI, SOAP_BINDING_LOCAL_NAME);
    if (soapBinding == null) {
      return;
    }
    String style = soapBinding.getAttribute(SOAP_STYLE_ATTR);
    if (!style.equals(SOAP_DOCUMENT)) {
      return;
    }
    Element[] operations = DescriptionUtils.getChildElements(binding,
        WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME);
    for (int i = 0; i < operations.length; i++) {
      verifyOperation(binding, operations[i]);
    }
  }

  protected void verifyOperation(Element binding, Element operation)
      throws Fault {
    Element[] children = DescriptionUtils.getChildElements(operation,
        WSDL_NAMESPACE_URI, null);
    for (int i = 0; i < children.length; i++) {
      String localName = children[i].getLocalName();
      if ((localName.equals(WSDL_INPUT_LOCAL_NAME))
          || (localName.equals(WSDL_OUTPUT_LOCAL_NAME))) {
        verifyInputOutput(binding, operation, children[i]);
      }
    }
  }

  protected void verifyInputOutput(Element binding, Element operation,
      Element io) throws Fault {
    Element soapBody = DescriptionUtils.getChildElement(io, SOAP_NAMESPACE_URI,
        SOAP_BODY_LOCAL_NAME);
    if (soapBody == null) {
      return;
    }
    String use = soapBody.getAttribute(SOAP_USE_ATTR);
    if (use.length() == 0) {
      use = SOAP_LITERAL;
    }
    if (!use.equals(SOAP_LITERAL)) {
      return;
    }
    String parts = soapBody.getAttribute(SOAP_PARTS_ATTR);
    if (parts.length() > 0) {
      return;
    }
    Element message = getMessage(binding, operation, io);
    verifyMessage(message);
  }

  protected void verifyMessage(Element message) throws Fault {
    Element[] parts = DescriptionUtils.getChildElements(message,
        WSDL_NAMESPACE_URI, WSDL_PART_LOCAL_NAME);
    if (parts.length > 1) {
      String name = message.getAttribute(WSDL_NAME_ATTR);
      throw new Fault("wsdl:message named '" + name
          + "' referenced without a soap:body 'parts' attribute defines more than one part (BP-R2210)");
    }
  }

  protected Element getMessage(Element binding, Element operation, Element io)
      throws Fault {
    String name = binding.getAttribute(WSDL_TYPE_ATTR);
    int index;
    index = name.indexOf(':');
    if (index > 0) {
      name = name.substring(index + 1);
    }
    Element portType = DescriptionUtils.getPortType(document, name);
    if (portType == null) {
      throw new Fault("Required wsdl:portType element named '" + name
          + "' not found (BP-R2210)");
    }
    name = operation.getAttribute(WSDL_NAME_ATTR);
    operation = DescriptionUtils.getNamedChildElement(portType,
        WSDL_NAMESPACE_URI, WSDL_OPERATION_LOCAL_NAME, name);
    if (name == null) {
      throw new Fault(
          "Required wsdl:operation element is named 'null' (BP-R2210)");
    }
    name = io.getAttribute(WSDL_NAME_ATTR);
    String localName = io.getLocalName();
    io = DescriptionUtils.getNamedChildElement(operation, WSDL_NAMESPACE_URI,
        localName, name);
    if (io == null) {
      throw new Fault("Required wsdl:" + localName + " element named '" + name
          + "' not found (BP-R2210)");
    }
    name = io.getAttribute(WSDL_MESSAGE_ATTR);
    index = name.indexOf(':');
    if (index > 0) {
      name = name.substring(index + 1);
    }
    Element message = DescriptionUtils.getMessage(document, name);
    if (message == null) {
      throw new Fault("Required wsdl:message element named '" + name
          + "' not found (BP-R2210)");
    }
    return message;
  }
}
