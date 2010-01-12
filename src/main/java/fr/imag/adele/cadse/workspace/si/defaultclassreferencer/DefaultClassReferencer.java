/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package fr.imag.adele.cadse.workspace.si.defaultclassreferencer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.internal.registry.osgi.OSGIUtils;
import org.osgi.framework.Bundle;

import fr.imag.adele.cadse.workspace.as.classreferencer.IClassReferencer;

/**
 * @generated
 */
public class DefaultClassReferencer implements IClassReferencer {
	/** The m logger. */
	static Logger	mLogger	= Logger.getLogger("SI.Workspace.DefaultClassReferencer");

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.imag.adele.cadse.workspace.as.classreferencer.IClassReferencer#loadClass(java.lang.String,
	 *      java.lang.String)
	 */
	public <T> Class<T> loadClass(String defaultContributorName, String qualifiedClassName) {
		if (qualifiedClassName == null || qualifiedClassName.length() == 0) {
			return null;
		}
		int i = qualifiedClassName.indexOf('/');
		String contributorName = null;
		String className;
		if (i != -1) {
			contributorName = qualifiedClassName.substring(0, i).trim();
			className = qualifiedClassName.substring(i + 1).trim();
		} else {
			className = qualifiedClassName;
			contributorName = defaultContributorName;
		}

		Bundle contributingBundle;
		contributingBundle = OSGIUtils.getDefault().getBundle(contributorName);

		if (contributingBundle == null) {
			mLogger.severe("cannot find Bundle " + contributorName);
			return null;
		}

		// load the requested class from this bundle
		Class<T> classInstance = null;
		try {
			classInstance = contributingBundle.loadClass(className);
		} catch (Exception e) {
			mLogger.log(Level.SEVERE, "Cannot load " + className + " from " + contributorName, e);
			return null;
		} catch (LinkageError e) {
			mLogger.log(Level.SEVERE, "Cannot load " + className + " from " + contributorName, e);
			return null;
		}
		return classInstance;
	}

}
