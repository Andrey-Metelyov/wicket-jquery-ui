package com.googlecode.wicket.jquery.ui.samples.pages.button;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.form.button.ConfirmAjaxButton;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;

public class ConfirmAjaxButtonPage extends AbstractButtonPage
{
	private static final long serialVersionUID = 1L;
	
	public ConfirmAjaxButtonPage()
	{
		this.init();
	}
	
	private void init()
	{
		final Form<Void> form = new Form<Void>("form") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				info("Form submitted");
			}
		};

		this.add(form);

		// FeedbackPanel //
		final FeedbackPanel feedbackPanel = new JQueryFeedbackPanel("feedback");
		form.add(feedbackPanel.setOutputMarkupId(true));

		// TextField //
		final RequiredTextField<String> textField = new RequiredTextField<String>("text", new Model<String>());
		form.add(textField);

		// Buttons //
		form.add(new ConfirmAjaxButton("button", "Submit", "Please confirm", "Do you confirm the new value?") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				this.error("Validation failed!");
				target.add(feedbackPanel);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				this.info("Model object: " + textField.getModelObject());
				target.add(feedbackPanel);
			}
		});
	}
}
