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
 * $Id$
 */

package com.sun.ts.tests.jpa.ee.packaging.appclient.annotation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;

public class ClientIT {

	private static final Coffee cRef[] = new Coffee[5];

	@PersistenceUnit(unitName = "CTS-APPCLIENT-ANNOTATED")
	private static EntityManagerFactory emf;

	private EntityManager em;

	private EntityTransaction et;

	/*
	 * @class.setup_props:
	 */

	@BeforeAll
	public void setup() throws Exception {
		try {
			if (emf != null) {
				em = emf.createEntityManager();
			} else {
				TestUtil.logErr("EMF is null");
				throw new Exception("Setup Failed!");
			}
			if (em != null) {
				et = em.getTransaction();
			} else {
				TestUtil.logErr("EM is null");
				throw new Exception("Setup Failed!");
			}
			if (et == null) {
				TestUtil.logErr("ET is null");
				throw new Exception("Setup Failed!");
			}
			removeTestData();
		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:906; PERSISTENCE:SPEC:937;
	 * PERSISTENCE:SPEC:843; JavaEE:SPEC:10054; JavaEE:SPEC:10055;
	 * PERSISTENCE:SPEC:848; PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:912;
	 * 
	 * @test_Strategy: In JavaEE application client containers, only
	 * application-managed entity managers are required to be used. [JTA is not
	 * required to be supported in application client containers.]
	 *
	 * In JavaEE environment, the root of a persistence unit may be an application
	 * client jar file The persistence.xml resides in the META-INF directory of the
	 * client.jar
	 *
	 * RESOURCE_LOCAL Transaction Type Defined
	 *
	 * The EntityManagerFactory is obtained via dependency injection.
	 *
	 * Deploy the client.jar to the application server with the above content.
	 * Create entities, persist them, then find.
	 *
	 */
	@Test
	public void test1() throws Exception {

		TestUtil.logTrace("Begin test1");
		boolean pass = true;

		try {

			TestUtil.logTrace("createTestData");

			et.begin();
			TestUtil.logTrace("Create 5 Coffees");
			cRef[0] = new Coffee(1, "hazelnut", 1.0F);
			cRef[1] = new Coffee(2, "vanilla creme", 2.0F);
			cRef[2] = new Coffee(3, "decaf", 3.0F);
			cRef[3] = new Coffee(4, "breakfast blend", 4.0F);
			cRef[4] = new Coffee(5, "mocha", 5.0F);

			TestUtil.logTrace("Start to persist coffees ");
			for (Coffee c : cRef) {
				if (c != null) {
					em.persist(c);
					TestUtil.logTrace("persisted coffee " + c);
				}
			}
			et.commit();

			TestUtil.logTrace("Clearing the persistence context");
			em.clear();

			et.begin();
			for (Coffee c : cRef) {
				if (c != null) {
					Coffee newcoffee = em.find(Coffee.class, c.getId());
					if (newcoffee != null) {
						em.remove(newcoffee);
						TestUtil.logTrace("removed coffee " + newcoffee);
					} else {
						TestUtil.logErr("find of coffee[" + c.getId() + "] returned null");
						pass = false;
					}
				}
			}
			et.commit();
		} catch (Exception e) {
			TestUtil.logErr("Unexpected while creating test data:", e);
			pass = false;
		} finally {
			try {
				if (et.isActive()) {
					et.rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception in rollback:", re);
			}
		}
		if (!pass) {
			throw new Exception("test1 failed");
		}
	}

	@AfterAll
	public void cleanup() throws Exception {
		TestUtil.logTrace("cleanup");
		removeTestData();
		// clear the cache if the provider supports caching otherwise
		// the evictAll is ignored.
		TestUtil.logTrace("Clearing cache");
		emf.getCache().evictAll();

	}

	private void removeTestData() {
		TestUtil.logTrace("removeTestData");
		if (et.isActive()) {
			et.rollback();
		}
		try {
			et.begin();
			em.createNativeQuery("DELETE FROM COFFEE").executeUpdate();
			et.commit();
		} catch (Exception e) {
			TestUtil.logErr("Exception encountered while removing entities:", e);
		} finally {
			try {
				if (et.isActive()) {
					et.rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
