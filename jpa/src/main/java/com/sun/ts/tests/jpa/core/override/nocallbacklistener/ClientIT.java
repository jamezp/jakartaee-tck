/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.override.nocallbacklistener;

import java.util.List;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;
import com.sun.ts.tests.jpa.core.override.util.CallBackCounts;


public class ClientIT extends PMClientBase {

	private static final Long ID = 1L;

	public ClientIT() {
	}

	public static JavaArchive createDeployment() throws Exception {

		String pkgNameWithoutSuffix = ClientIT.class.getPackageName();
		String pkgName = ClientIT.class.getPackageName() + ".";
		String[] classes = { pkgName + "NoCallBackListener" };
		return createDeploymentJar("jpa_core_override_nocallbacklistener.jar", pkgNameWithoutSuffix, classes);

	}

	@BeforeAll
	public void setup() throws Exception {
		TestUtil.logTrace("setup");
		try {
			super.setup();
			removeTestData();
		} catch (Exception e) {
			TestUtil.logErr("Exception: ", e);
			throw new Exception("Setup failed:", e);
		}
	}

	/*
	 * @testName: testNoCallBackListener
	 * 
	 * @assertion_ids: PERSISTENCE:SPEC:694; PERSISTENCE:SPEC:695;
	 * PERSISTENCE:SPEC:696; PERSISTENCE:SPEC:697; PERSISTENCE:SPEC:698;
	 * PERSISTENCE:SPEC:698; PERSISTENCE:SPEC:699; PERSISTENCE:SPEC:700;
	 * PERSISTENCE:SPEC:701; PERSISTENCE:SPEC:702; PERSISTENCE:SPEC:703;
	 * PERSISTENCE:SPEC:704; PERSISTENCE:SPEC:707; PERSISTENCE:SPEC:708;
	 * PERSISTENCE:SPEC:709; PERSISTENCE:SPEC:710; PERSISTENCE:SPEC:711;
	 * PERSISTENCE:SPEC:712; PERSISTENCE:SPEC:713; PERSISTENCE:SPEC:716;
	 * PERSISTENCE:SPEC:719; PERSISTENCE:SPEC:720; PERSISTENCE:SPEC:722;
	 * PERSISTENCE:SPEC:723; PERSISTENCE:SPEC:724
	 * 
	 * @test_Strategy: CallBack methods are tested without using callback method's
	 * annotations.
	 * 
	 */
	@Test
	public void testNoCallBackListener() throws Exception {

		boolean pass1 = false;
		boolean pass2 = false;
		boolean pass3 = false;

		CallBackCounts.clearCountsMap();
		NoCallBackListener entity = new NoCallBackListener();
		entity.setId(ID);
		getEntityTransaction().begin();
		getEntityManager().persist(entity);
		TestUtil.logTrace("persisted entity" + entity);
		getEntityManager().flush();
		getEntityManager().refresh(entity);

		// perform query to induce postLoad() method
		//
		// The PostLoad method for an entity is invoked after the entity has
		// been loaded into the current persistence context from the database
		// or after the refresh operation has been applied to it. The PostLoad
		// method is invoked before a query result is returned or accessed or
		// before an association is traversed.
		//
		NoCallBackListener newEntity = null;
		List result = getEntityManager().createQuery("SELECT n FROM NoCallBackListener n").getResultList();
		if (result != null && result.size() > 0) {
			newEntity = (NoCallBackListener) result.get(0);
			if (newEntity.getId() == 1) {
				TestUtil.logTrace("Retrieved persisted entity");
			}
			getEntityManager().remove(entity);
			getEntityManager().flush();
			getEntityTransaction().commit();
			try {
				pass1 = checkPersistCallBacks();
				TestUtil.logTrace("pass1 = " + pass1);

				pass2 = checkRemoveCallBacks();
				TestUtil.logTrace("pass2 = " + pass2);

				pass3 = checkLoadCallBacks();
				TestUtil.logTrace("pass3 = " + pass3);
				if (pass1 && pass2 && pass3) {
					TestUtil.logTrace("testOverrideCallBackMethods Passed");
				} else {
					throw new Exception("Test failed while testing prePersist" + ", postPersist, preremove and "
							+ "postremove methods and also the post Load method");
				}
			} catch (Exception e) {
				throw new Exception("Exception thrown while testing testNoCallBackListener" + e);
			}
		} else {
			throw new Exception("getResultList returned a null list");
		}
	}

	private boolean checkPersistCallBacks() throws Exception {
		boolean result = false;
		if (test("prePersistFromXML", 1) && (test("postPersist", 1))) {
			result = true;
		}
		return result;
	}

	private boolean checkRemoveCallBacks() throws Exception {
		boolean result = false;
		if (test("preRemoveFromXML", 1) && (test("postRemoveFromXML", 1))) {
			result = true;
		}
		return result;
	}

	private boolean checkLoadCallBacks() throws Exception {
		boolean result = false;
		if (test("postLoadFromXML", 1)) {
			return true;
		}
		return result;
	}

	private boolean test(final String callBackName, final int expectedCount) throws Exception {

		boolean pass = false;

		int count = CallBackCounts.getCount(callBackName);
		if (count == expectedCount) {
			TestUtil.logTrace("test passed in test method" + callBackName);
			pass = true;
		} else {
			TestUtil.logTrace("test not passed as the count and the" + " expected count are not same" + callBackName);
		}

		return pass;
	}

	@AfterAll
	public void cleanup() throws Exception {
		TestUtil.logTrace("Cleanup data");
		removeTestData();
		TestUtil.logTrace("cleanup complete, calling super.cleanup");
		super.cleanup();
	}

	private void removeTestData() {
		TestUtil.logTrace("removeTestData");
		if (getEntityTransaction().isActive()) {
			getEntityTransaction().rollback();
		}
		try {
			getEntityTransaction().begin();
			getEntityManager().createNativeQuery("DELETE FROM NOENTITYLISTENER_TABLE").executeUpdate();
			getEntityTransaction().commit();
		} catch (Exception e) {
			TestUtil.logErr("Exception encountered while removing entities:", e);
		} finally {
			try {
				if (getEntityTransaction().isActive()) {
					getEntityTransaction().rollback();
				}
			} catch (Exception re) {
				TestUtil.logErr("Unexpected Exception in removeTestData:", re);
			}
		}
	}
}
