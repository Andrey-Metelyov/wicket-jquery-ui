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
package com.googlecode.wicket.jquery.core.ajax;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallThrottlingDecorator;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.util.time.Duration;

import com.googlecode.wicket.jquery.core.JQueryBehavior;
import com.googlecode.wicket.jquery.core.JQueryEvent;

/**
 * Base class for implementing AJAX GET calls to a {@link IJQueryAjaxAware} source, which is usually a {@link JQueryBehavior}<br />
 * <br />
 * <b>Example</b>
 * <pre>
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
	protected void onInitialize()
	{
		super.onInitialize();

		this.add(JQueryWidget.newWidgetBehavior(this));
	}

	public void onMyEvent(AjaxRequestTarget target)
	{
		// do something here
	}

	public JQueryBehavior newWidgetBehavior(String selector)
	{
		return new MyJQueryBehavior(selector, "jquerymethod") {

			private static final long serialVersionUID = 1L;

			public void onMyEvent(AjaxRequestTarget target)
			{
				MyJQueryLabel.this.onMyEvent(target);
			}
		};
	}

	static abstract class MyJQueryBehavior extends JQueryBehavior implements IJQueryAjaxAware, IMyJQueryListener
	{
		private static final long serialVersionUID = 1L;
		private JQueryAjaxBehavior onMyEventBehavior;

		public MyJQueryBehavior(String selector, String method)
		{
			super(selector, method);
		}

		public void bind(Component component)
		{
			super.bind(component);

			component.add(this.onMyEventBehavior = this.newJQueryAjaxBehavior());
		}

		// Events //
		public void onConfigure(Component component)
		{
			super.onConfigure(component);

			this.setOption("jqueryevent", this.onMyEventBehavior.getCallbackFunction());
		}

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

				public String getCallbackFunction()
				{
					return "function(event, ui) { " + this.getCallbackScript() + " }";
				}

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
 * </pre>
 *
 * @author Sebastien Briquet - sebfz1
 *
 */
public abstract class JQueryAjaxBehavior extends AbstractDefaultAjaxBehavior
{
	private static final long serialVersionUID = 1L;

	private final IJQueryAjaxAware source;
	private final Duration duration;


	/**
	 * Constructor
	 * @param source {@link Behavior} to which the event - returned by {@link #newEvent()} - will be broadcasted.
	 */
	public JQueryAjaxBehavior(IJQueryAjaxAware source)
	{
		this(source, Duration.NONE);
	}

	/**
	 * Constructor
	 * @param source {@link Behavior} to which the event - returned by {@link #newEvent()} - will be broadcasted.
	 * @param duration {@link Duration}. If different than {@link Duration#NONE}, an {@link AjaxCallThrottlingDecorator} will be added with the specified {@link Duration}.
	 */
	public JQueryAjaxBehavior(IJQueryAjaxAware source, Duration duration)
	{
		this.source = source;
		this.duration = duration;
	}

	@Override
	protected void respond(AjaxRequestTarget target)
	{
		if (this.source != null)
		{
			this.source.onAjax(target, this.newEvent());
		}
	}

	/**
	 * Gets the {@link JQueryEvent} to be broadcasted to the {@link IJQueryAjaxAware} source when the behavior will respond
	 * @return the {@link JQueryEvent}
	 */
	protected abstract JQueryEvent newEvent();

	// wicket 1.5.x //
	/**
	 * Gets the jQuery callback function<br/>
	 * Implementation is typically like:<br/>
	 * <code>return "function( event, ui ) { " + this.getCallbackScript() + "}";</code>
	 *
	 * @return the jQuery callback function
	 */
	public String getCallbackFunction()
	{
		return "function() { " + this.getCallbackScript() + " }";
	}

	/**
	 * Promotes visibility
	 */
	@Override
	public CharSequence getCallbackScript()
	{
		return super.getCallbackScript();
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator()
	{
		if (this.duration != Duration.NONE)
		{
			return new AjaxCallThrottlingDecorator("throttle", this.duration);
		}

		return super.getAjaxCallDecorator();
	}

}
