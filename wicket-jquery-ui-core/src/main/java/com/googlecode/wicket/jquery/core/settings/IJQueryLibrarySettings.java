/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.wicket.jquery.core.settings;

import org.apache.wicket.request.resource.ResourceReference;

/**
 * Provides the definition to the JQuery/jQuery UI backing library resource reference.
 *
 * @author Sebastien Briquet - sebfz1
 *
 */
public interface IJQueryLibrarySettings
{
	/**
	 * Gets the JQuery backing library resource reference
	 *
	 * @return the {@link ResourceReference}
	 */
	ResourceReference getJQueryReference();

	/**
	 * Sets the JQuery backing library resource reference
	 *
	 * @param reference the {@link ResourceReference}
	 */
	void setJQueryReference(ResourceReference reference);

	/**
	 * Gets the JQuery UI backing library resource reference
	 *
	 * @return the {@link ResourceReference}
	 */
	ResourceReference getJQueryUIReference();

	/**
	 * Sets the JQuery UI backing library resource reference
	 *
	 * @param reference the {@link ResourceReference}
	 */
	void setJQueryUIReference(ResourceReference reference);

	/**
	 * Gets the JQuery globalize library resource reference
	 *
	 * @return the {@link ResourceReference}
	 */
	ResourceReference getJQueryGlobalizeReference();

	/**
	 * Sets the JQuery globalize library resource reference
	 *
	 * @param reference the {@link ResourceReference}
	 */
	void setJQueryGlobalizeReference(ResourceReference reference);
}