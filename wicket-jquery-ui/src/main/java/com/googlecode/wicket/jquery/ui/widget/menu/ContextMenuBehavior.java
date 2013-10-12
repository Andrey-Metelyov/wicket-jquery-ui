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
package com.googlecode.wicket.jquery.ui.widget.menu;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.util.lang.Args;

import com.googlecode.wicket.jquery.core.IJQueryWidget.JQueryWidget;
import com.googlecode.wicket.jquery.core.JQueryAbstractBehavior;
import com.googlecode.wicket.jquery.core.JQueryEvent;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxBehavior;

/**
 * Provides a jQuery context menu behavior.<br/>
 * A {@link ContextMenuBehavior} can be associated to <i>only one</i> {@link Component} (ie: The behavior cannot be reused).
 *
 * @author Sebastien Briquet - sebfz1
 * @since 6.2.8
 */
public class ContextMenuBehavior extends JQueryAbstractBehavior implements IJQueryAjaxAware
{
	private static final long serialVersionUID = 1L;

	/**
	 * CSS class used to identify the component that invoke the context menu, in order
	 * to avoid events conflict (with the registered click in {@link ContextMenu})
	 */
	public static final String COMPONENT_CSS = "context-menu-invoker";

	private final ContextMenu menu;
	private Component component = null;

	private JQueryAjaxBehavior onContextMenuEventBehavior;

	/**
	 * Constructor
	 *
	 * @param menu the {@link ContextMenu} that should be displayed
	 */
	public ContextMenuBehavior(ContextMenu menu)
	{
		super("context-menu");

		this.menu = Args.notNull(menu, "menu");
	}


	// Methods //

	@Override
	public void bind(Component component)
	{
		super.bind(component);

		if (this.component != null)
		{
			throw new WicketRuntimeException("Behavior is already bound to another component.");
		}

		this.component = component;
		this.component.add(AttributeModifier.append("class", COMPONENT_CSS));
		this.component.add(this.onContextMenuEventBehavior = this.newOnContextMenuBehavior());
	}

	@Override
	protected String $()
	{
		return String.format("jQuery(function() { jQuery('%s').on('contextmenu', %s); });", JQueryWidget.getSelector(this.component), this.onContextMenuEventBehavior.getCallbackFunction());
	}


	// Events //

	@Override
	public void onAjax(AjaxRequestTarget target, JQueryEvent event)
	{
		if (event instanceof ContextMenuEvent)
		{
			this.menu.fireOnContextMenu(target, this.component);
		}
	}


	// Factories //

	/**
	 * Gets the ajax behavior that will be triggered on context-menu click
	 *
	 * @return the {@link JQueryAjaxBehavior}
	 */
	private JQueryAjaxBehavior newOnContextMenuBehavior()
	{
		return new JQueryAjaxBehavior(this) {

			private static final long serialVersionUID = 1L;

			@Override
			protected CallbackParameter[] getCallbackParameters()
			{
				return new CallbackParameter[] { CallbackParameter.context("event") };
			}

			@Override
			public CharSequence getCallbackFunctionBody(CallbackParameter... parameters)
			{
				return super.getCallbackFunctionBody(parameters) + " return false;"; // stop event propagation
			}

			@Override
			protected JQueryEvent newEvent()
			{
				return new ContextMenuEvent();
			}
		};
	}


	// Event class //

	/**
	 * Provides an event object that will be broadcasted by the {@link JQueryAjaxBehavior} 'contextmenu' callback
	 */
	protected static class ContextMenuEvent extends JQueryEvent
	{
	}
}
