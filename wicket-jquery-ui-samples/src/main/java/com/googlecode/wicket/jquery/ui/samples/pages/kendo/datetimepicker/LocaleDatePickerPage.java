package com.googlecode.wicket.jquery.ui.samples.pages.kendo.datetimepicker;

import java.util.Date;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.googlecode.wicket.jquery.ui.kendo.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.kendo.button.Button;
import com.googlecode.wicket.jquery.ui.kendo.datetime.DatePicker;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.samples.SampleApplication;

public class LocaleDatePickerPage extends AbstractTimePickerPage
{
	private static final long serialVersionUID = 1L;

	public LocaleDatePickerPage()
	{
		final Form<Date> form = new Form<Date>("form");
		this.add(form);

		// FeedbackPanel //
		form.add(new JQueryFeedbackPanel("feedback"));

		// Date Picker //
		final DatePicker datepicker = new DatePicker("datepicker", new Model<Date>(new Date()), Locale.FRENCH);
		form.add(datepicker);

		// Buttons //
		form.add(new Button("submit") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				this.info(datepicker.getModelObjectAsString());
			}
		});

		form.add(new AjaxButton("button") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				this.info(datepicker.getModelObjectAsString());
				target.add(form);
			}
		});
	}

	/**
	 * renderHead could be overridden directly in DatePicker if using wicket6+ (javascript dependencies priority are handled)
	 */
	@Override
	public void renderHead(IHeaderResponse response)
	{
		super.renderHead(response);

		response.renderJavaScriptReference(new JavaScriptResourceReference(SampleApplication.class, "kendo.culture.fr.min.js"));
	}
}
