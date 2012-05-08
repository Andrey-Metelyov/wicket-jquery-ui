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
package com.googlecode.wicket.jquery.ui.form.button;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;

import com.googlecode.wicket.jquery.ui.IJQuerySecurityProvider;
import com.googlecode.wicket.jquery.ui.IJQueryWidget;
import com.googlecode.wicket.jquery.ui.JQueryBehavior;
import com.googlecode.wicket.jquery.ui.JQueryIcon;

/**
 * Provides a jQuery button based on the built-in AjaxButton, protected by roles. Roles are checked against an {@link IJQuerySecurityProvider}<br/>
 * Assuming the {@link WebSession} is implementing {@link IJQuerySecurityProvider} if not provided.
 *  
 * @author Sebastien Briquet - sebastien@7thweb.net
 *
 */
public abstract class SecuredButton extends Button implements IJQueryWidget
{
	private static final long serialVersionUID = 1L;

	private final IJQuerySecurityProvider provider;
	private String[] roles;

	
	/**
	 * Constructor
	 * @param id the markup id
	 * @param roles list of roles allowed to enable the button
	 */
	public SecuredButton(String id, String... roles)
	{
		this(id, (IJQuerySecurityProvider) WebSession.get(), roles);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param provider the {@link IJQuerySecurityProvider} that will check roles
	 * @param roles list of roles allowed to enable the button
	 */
	public SecuredButton(String id, IJQuerySecurityProvider provider, String... roles)
	{
		super(id);

		this.roles = roles;
		this.provider = provider;
	}

	public SecuredButton(String id, IModel<String> model, String... roles)
	{
		this(id, model, (IJQuerySecurityProvider) WebSession.get(), roles);
	}

	/**
	 * Constructor
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param roles list of roles allowed to enable the button
	 */
	public SecuredButton(String id, IModel<String> model, IJQuerySecurityProvider provider, String... roles)
	{
		super(id, model);

		this.roles = roles;
		this.provider = provider;
	}


	public void setRoles(String[] roles)
	{
		this.roles = roles;
	}

//	@Override
//	public boolean isEnabled()
//	{
//		return super.isEnabled() && !this.isLocked(); //moved to onConfigure
//	}

	/**
	 * Indicates whether the button is locked.
	 * @return the result of {@link IJQuerySecurityProvider#hasRole(String...)}
	 */
	public final boolean isLocked()
	{
		return !this.provider.hasRole(this.roles);
	}

	// IJQueryWidget //
	@Override
	public JQueryBehavior newWidgetBehavior(String selector)
	{
		return new JQueryBehavior(selector, "button") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onConfigure(Component component)
			{
				this.setOption("icons", String.format("{ primary: '%s' }", isLocked() ? JQueryIcon.Locked : JQueryIcon.Unlocked));
			}
		};
	}

	// Events //
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		this.add(JQueryWidget.newWidgetBehavior(this)); //cannot be in ctor as the markupId may be set manually afterward
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		this.setEnabled(!this.isLocked());
	}
}
