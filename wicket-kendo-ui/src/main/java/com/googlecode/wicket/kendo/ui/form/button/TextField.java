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
package com.googlecode.wicket.kendo.ui.form.button;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Provides a {@link org.apache.wicket.markup.html.form.TextField} with the Kendo-ui style
 *
 * @author Sebastien Briquet - sebfz1
 *
 */
public class TextField<T> extends org.apache.wicket.markup.html.form.TextField<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 */
	public TextField(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 */
	public TextField(String id, IModel<T> model)
	{
		super(id, model);
	}

	//TODO: add other ctors

	// Events //

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		this.add(AttributeModifier.append("class", Model.of("k-textbox")));
	}
}
