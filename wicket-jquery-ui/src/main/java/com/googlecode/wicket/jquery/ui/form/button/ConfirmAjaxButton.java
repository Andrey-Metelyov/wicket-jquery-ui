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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.core.IJQueryWidget.JQueryWidget;
import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.form.button.IndicatingAjaxButton.Position;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogBehavior;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.widget.dialog.MessageFormDialog;

/**
 * Provides a {@link AjaxButton} which pop-ups an OK-Cancel confirmation dialog when clicked. In case of confirmation, the form is sent via an ajax post.<br/>
 * <br/>
 * <b>Note:</b> this component is not an {@link AjaxButton} itself but a Panel, it should not be attached to a &lt;button /&gt;; it can be attached on a &lt;div /&gt; or a &lt;span /&gt; for instance.<br/>
 * <br/>
 * <b>Warning:</b> it is not possible to get a form component value - that is going to be changed - to be displayed in the dialog box message. The reason is that in order to get a form component (updated) model object, the form component
 * should be validated. The dialog does not proceed to a (whole) form validation while being opened, because the form validation will occur when the user will confirm (by clicking on OK button). This the intended behavior.
 *
 * @author Sebastien Briquet - sebfz1
 *
 */
public abstract class ConfirmAjaxButton extends GenericPanel<String>
{
	private static final long serialVersionUID = 1L;

	private final IModel<String> labelModel;
	private final IModel<String> titleModel;

	private AjaxButton button;
	private AbstractFormDialog<?> dialog;

	/**
	 * Constructor
	 *
	 * @param id markup id
	 * @param label the button text
	 * @param title the dialog title
	 * @param message the dialog message
	 */
	public ConfirmAjaxButton(String id, String label, String title, String message)
	{
		this(id, Model.of(label), Model.of(title), Model.of(message));
	}

	/**
	 * Constructor
	 *
	 * @param id markup id
	 * @param label the button text
	 * @param title the dialog title
	 * @param message the dialog message
	 */
	public ConfirmAjaxButton(String id, IModel<String> label, IModel<String> title, IModel<String> message)
	{
		super(id, message);

		this.labelModel = label;
		this.titleModel = title;
	}

	// Events //

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.dialog = this.newDialog("dialog", this.titleModel, this.getModel());
		this.add(this.dialog);

		this.button = this.newAjaxButton("button", dialog);
		this.add(this.button);

		this.button.add(new Label("label", this.labelModel).setRenderBodyOnly(true));

		// behaviors //
		if (ConfirmAjaxButton.this.isIndicating())
		{
			this.add(this.newAjaxIndicatingButtonBehavior());
		}
	}

	/**
	 * Triggered when the form is submitted, but the validation failed
	 *
	 * @param target the {@link AjaxRequestTarget}
	 * @param form the {@link Form}
	 */
	protected abstract void onError(AjaxRequestTarget target, Form<?> form);

	/**
	 * Triggered when the form is submitted, and the validation succeed
	 *
	 * @param target the {@link AjaxRequestTarget}
	 * @param form the {@link Form}
	 */
	protected abstract void onSubmit(AjaxRequestTarget target, Form<?> form);

	@Override
	protected void onDetach()
	{
		super.onDetach();

		this.labelModel.detach();
		this.titleModel.detach();
	}

	// Properties //

	/**
	 * Gets the icon being displayed in the button
	 *
	 * @return the {@link JQueryIcon}
	 */
	protected String getIcon()
	{
		return JQueryIcon.ALERT;
	}

	/**
	 * Indicates whether the button will display the ajax-indicator on submit<br/>
	 * if {@code true}, the button will also be disabled on click
	 *
	 * @return false by default
	 */
	protected boolean isIndicating()
	{
		return false;
	}

	// Factories //

	/**
	 * Creates the dialog instance<br/>
	 * <b>Warning:</b> to be overridden with care!
	 *
	 * @param id the markupId
	 * @param title the title of the dialog
	 * @param message the message to be displayed
	 * @return the dialog instance
	 */
	protected AbstractFormDialog<?> newDialog(String id, IModel<String> title, IModel<String> message)
	{
		return new MessageFormDialog(id, title, message, DialogButtons.OK_CANCEL, DialogIcon.WARN) {

			private static final long serialVersionUID = 1L;

			@Override
			public DialogButton getSubmitButton()
			{
				return this.findButton(OK);
			}

			@Override
			public Form<?> getForm()
			{
				return Form.findForm(ConfirmAjaxButton.this);
			}

			@Override
			protected void onError(AjaxRequestTarget target)
			{
				super.close(target, null); // closes the dialog on error.

				ConfirmAjaxButton.this.onError(target, this.getForm());
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target)
			{
				ConfirmAjaxButton.this.onSubmit(target, this.getForm());
			}
		};
	}

	/**
	 * Gets the new {@link AjaxButton} that will open the supplied dialog
	 * 
	 * @param id the markupId
	 * @param dialog the {@link AbstractDialog}
	 * @return the new {@code AjaxButton}
	 */
	protected AjaxButton newAjaxButton(String id, final AbstractDialog<?> dialog)
	{
		return new AjaxButton(id) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onInitialize()
			{
				super.onInitialize();

				// does not validate the form before the window is being displayed
				this.setDefaultFormProcessing(false);
			}

			@Override
			protected String getIcon()
			{
				return ConfirmAjaxButton.this.getIcon();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				dialog.open(target);
			}
		};
	}

	/**
	 * Gets a new {@link AjaxIndicatingButtonBehavior} for this confirm-button
	 * 
	 * @return a new {@code AjaxIndicatingButtonBehavior}
	 */
	protected AjaxIndicatingButtonBehavior newAjaxIndicatingButtonBehavior()
	{
		final String buttonSelector = JQueryWidget.getSelector(this.button);
		final String dialogSelector = JQueryWidget.getSelector(this.dialog);
		final String submitSelector = "#" + this.dialog.getSubmitButton().getMarkupId();

		return new AjaxIndicatingButtonBehavior(buttonSelector, this.getIcon(), Position.LEFT) {

			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(Component component, IHeaderResponse response)
			{
				super.renderHead(component, response);

				// close statement //
				String closeStatement = "";
				closeStatement += String.format("jQuery('%s').click(function() {", submitSelector);
				closeStatement += String.format("jQuery('%s').%s('close');", dialogSelector, DialogBehavior.METHOD);
				closeStatement += "});";

				this.renderOnDomReadyScript(closeStatement, response);
			}

			@Override
			public String getSelector()
			{
				return submitSelector;
			}

			@Override
			protected Options newOnClickOptions()
			{
				Options options = super.newOnClickOptions();
				options.set("disabled", true);

				return options;
			}

			@Override
			protected Options newOnAjaxStopOptions()
			{
				Options options = super.newOnAjaxStopOptions();
				options.set("disabled", !ConfirmAjaxButton.this.isEnabledInHierarchy());

				return options;
			}
		};
	}
}
