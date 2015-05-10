import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.CallbackParameter;
import org.apache.wicket.markup.html.basic.Label;

import com.googlecode.wicket.jquery.core.IJQueryWidget;
import com.googlecode.wicket.jquery.core.JQueryBehavior;
import com.googlecode.wicket.jquery.core.JQueryEvent;
import com.googlecode.wicket.jquery.core.ajax.IJQueryAjaxAware;
import com.googlecode.wicket.jquery.core.ajax.JQueryAjaxBehavior;
import com.googlecode.wicket.jquery.ui.JQueryUIBehavior;

interface IMyJQueryListener
{
	void onMyEvent(AjaxRequestTarget target);
}

public class MyJQueryLabel extends Label implements IJQueryWidget, IMyJQueryListener
{
	private static final long serialVersionUID = 1L;

	public MyJQueryLabel(String id)
	{
		super(id);
	}

	// Events //
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(JQueryWidget.newWidgetBehavior(this));
	}

	@Override
	public void onConfigure(JQueryBehavior behavior)
	{
	}

	@Override
	public void onBeforeRender(JQueryBehavior behavior)
	{
	}

	@Override
	public void onMyEvent(AjaxRequestTarget target)
	{
		// do something here
	}

	@Override
	public JQueryBehavior newWidgetBehavior(String selector)
	{
		return new MyJQueryBehavior(selector, "jquerymethod") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onMyEvent(AjaxRequestTarget target)
			{
				MyJQueryLabel.this.onMyEvent(target);
			}
		};
	}

	static abstract class MyJQueryBehavior extends JQueryUIBehavior implements IJQueryAjaxAware, IMyJQueryListener
	{
		private static final long serialVersionUID = 1L;
		private JQueryAjaxBehavior onMyEventBehavior;

		public MyJQueryBehavior(String selector, String method)
		{
			super(selector, method);
		}

		@Override
		public void bind(Component component)
		{
			super.bind(component);

			this.onMyEventBehavior = this.newJQueryAjaxBehavior();
			component.add(this.onMyEventBehavior);
		}

		// Events //
		@Override
		public void onConfigure(Component component)
		{
			super.onConfigure(component);

			this.setOption("jqueryevent", this.onMyEventBehavior.getCallbackFunction());
		}

		@Override
		public void onAjax(AjaxRequestTarget target, JQueryEvent event)
		{
			if (event instanceof MyEvent)
			{
				this.onMyEvent(target);
			}
		}

		// Factory //
		protected JQueryAjaxBehavior newJQueryAjaxBehavior()
		{
			return new JQueryAjaxBehavior(this) {

				private static final long serialVersionUID = 1L;

				@Override
				protected CallbackParameter[] getCallbackParameters()
				{
					return new CallbackParameter[] { CallbackParameter.context("event"), CallbackParameter.context("ui") };
				}

				@Override
				protected JQueryEvent newEvent()
				{
					return new MyEvent();
				}
			};
		}

		// Event Class //
		protected static class MyEvent extends JQueryEvent
		{
		}
	}
}
