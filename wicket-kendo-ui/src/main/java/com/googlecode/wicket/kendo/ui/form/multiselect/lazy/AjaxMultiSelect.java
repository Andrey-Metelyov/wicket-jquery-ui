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
package com.googlecode.wicket.kendo.ui.form.multiselect.lazy;

import java.util.Collection;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import com.googlecode.wicket.jquery.core.JQueryBehavior;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxPostBehavior;
import com.googlecode.wicket.jquery.core.event.ISelectionChangedListener;
import com.googlecode.wicket.jquery.core.renderer.IChoiceRenderer;
import com.googlecode.wicket.kendo.ui.form.dropdown.lazy.DropDownListBehavior;
import com.googlecode.wicket.kendo.ui.renderer.ChoiceRenderer;

/**
 * Provides a Kendo UI MultiSelect widget.<br/>
 * This ajax version will post the {@link Component}, using a {@link JQueryAjaxPostBehavior}, when the 'change' javascript method is called.
 * 
 * @author Sebastien Briquet - sebfz1
 */
public abstract class AjaxMultiSelect<T> extends MultiSelect<T> implements ISelectionChangedListener
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 */
	public AjaxMultiSelect(String id)
	{
		super(id);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 */
	public AjaxMultiSelect(String id, IModel<? extends Collection<T>> model)
	{
		super(id, model);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param renderer the {@link ChoiceRenderer}
	 */
	public AjaxMultiSelect(String id, IChoiceRenderer<? super T> renderer)
	{
		super(id, renderer);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param renderer the {@link ChoiceRenderer}
	 */
	public AjaxMultiSelect(String id, IModel<? extends Collection<T>> model, IChoiceRenderer<? super T> renderer)
	{
		super(id, model, renderer);
	}

	// Properties //

	@Override
	public boolean isSelectionChangedEventEnabled()
	{
		return true;
	}

	// Events //

	/**
	 * Triggers when the selection has changed
	 */
	public final void onSelectionChanged()
	{
		this.convertInput();
		this.updateModel();
	}

	@Override
	public void onSelectionChanged(AjaxRequestTarget target)
	{
		// noop
	}

	// IJQueryWidget //

	@Override
	public final JQueryBehavior newWidgetBehavior(String selector)
	{
		final ISelectionChangedListener listener = new SelectionChangedListenerWrapper(this) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSelectionChanged(AjaxRequestTarget target)
			{
				AjaxMultiSelect.this.onSelectionChanged(); // updates the model
				AjaxMultiSelect.this.onSelectionChanged(target);
			}
		};

		return new DropDownListBehavior(selector, listener) {

			private static final long serialVersionUID = 1L;

			@Override
			protected CharSequence getDataSourceUrl()
			{
				return AjaxMultiSelect.this.getCallbackUrl();
			}
		};
	}
}
