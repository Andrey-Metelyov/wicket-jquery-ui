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
package com.googlecode.wicket.kendo.ui.widget.accordion;

import org.apache.wicket.Component;

import com.googlecode.wicket.jquery.core.IJQueryWidget;
import com.googlecode.wicket.kendo.ui.KendoUIBehavior;

/**
 * Provides the ability to expands (without selecting) panes
 *
 * @author Sebastien Briquet - sebfz1
 * @since 6.19.0
 * @since 7.0.0
 */
public class ExpandBehavior extends KendoUIBehavior
{
	private static final long serialVersionUID = 1L;

	/** all children selector */
	public static final String ALL_CHILDREN = "li";

	/** first child selector */
	public static final String FIRST_CHILD = "li:first-child";

	/** children selector */
	private final String children;

	/**
	 * Constructor
	 *
	 * @param selector the html selector (ie: "#myId")
	 */
	public ExpandBehavior(String selector)
	{
		this(selector, ALL_CHILDREN);
	}

	/**
	 * Constructor
	 *
	 * @param selector the html selector (ie: "#myId")
	 * @param children the children selector
	 */
	public ExpandBehavior(String selector, String children)
	{
		super(selector, AccordionBehavior.METHOD);
		
		this.children = children;
	}

	/**
	 * Constructor
	 *
	 * @param component the {@link Component}
	 */
	public ExpandBehavior(Component component)
	{
		this(component, ALL_CHILDREN);
	}

	/**
	 * Constructor
	 *
	 * @param component the {@link Component}
	 * @param children the children selector
	 */
	public ExpandBehavior(Component component, String children)
	{
		super(IJQueryWidget.getSelector(component), AccordionBehavior.METHOD);
		
		this.children = children;
	}

	// Properties //

	@Override
	public boolean isTemporary(Component component)
	{
		return true;
	}

	// Methods //

	@Override
	public String $()
	{
		return String.format("%s.expand(jQuery('%s'), false);", this.widget(), this.children);
	}
}
