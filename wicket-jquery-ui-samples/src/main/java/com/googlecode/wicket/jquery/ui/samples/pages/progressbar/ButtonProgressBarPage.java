package com.googlecode.wicket.jquery.ui.samples.pages.progressbar;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.ProgressBar;

public class ButtonProgressBarPage extends AbstractProgressBarPage
{
	private static final long serialVersionUID = 1L;
	
	public ButtonProgressBarPage()
	{
		this.init();
	}

	private void init()
	{
		final Form<Void> form = new Form<Void>("form");
		this.add(form);

		// FeedbackPanel //
		final FeedbackPanel feedbackPanel = new JQueryFeedbackPanel("feedback");
		form.add(feedbackPanel.setOutputMarkupId(true));

		// ProgressBar //
		final ProgressBar progressBar = new ProgressBar("progress", new Model<Integer>(90)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onValueChanged(AjaxRequestTarget target)
			{
				info("value: " + this.getDefaultModelObjectAsString());
				target.add(feedbackPanel);
			}
			
			@Override
			protected void onComplete(AjaxRequestTarget target)
			{
				info("completed!");
				target.add(feedbackPanel);
			}
		};
		
		form.add(progressBar);

		// Buttons //
		form.add(new AjaxButton("backward") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				progressBar.backward(target);
			}
		});
		
		form.add(new AjaxButton("forward") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				progressBar.forward(target);
			}
		});
	}
}
