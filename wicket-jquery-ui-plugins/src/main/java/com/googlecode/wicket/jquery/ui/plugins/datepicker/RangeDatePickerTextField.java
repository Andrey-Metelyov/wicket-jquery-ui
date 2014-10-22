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
package com.googlecode.wicket.jquery.ui.plugins.datepicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import com.googlecode.wicket.jquery.core.JQueryAbstractBehavior;
import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.event.IValueChangedListener;
import com.googlecode.wicket.jquery.core.utils.DateUtils;

/**
 * Provides a {@link FormComponentPanel} based on a {@link TextField} and a {@link RangeDatePicker}
 *
 * @author Sebastien Briquet - sebfz1
 *
 */
public class RangeDatePickerTextField extends FormComponentPanel<DateRange> implements IValueChangedListener
{
	private static final long serialVersionUID = 1L;
	private static final String NULL = "?";
	private static final String SEPARATOR = " - ";

	private final Options options;
	private TextField<DateRange> input;
	private RangeDatePicker datepicker;

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 */
	public RangeDatePickerTextField(String id)
	{
		this(id, new Options("calendars", 3));
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param options {@link Options}
	 */
	public RangeDatePickerTextField(String id, final Options options)
	{
		super(id);

		this.options = options;
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 */
	public RangeDatePickerTextField(String id, IModel<DateRange> model)
	{
		this(id, model, new Options("calendars", 3));
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param options {@link Options}
	 */
	public RangeDatePickerTextField(String id, IModel<DateRange> model, final Options options)
	{
		super(id, model);

		this.options = options;
	}

	// Methods //
	@Override
	public void convertInput()
	{
		this.setConvertedInput(this.input.getConvertedInput());
	}

	// Properties //
	/**
	 * Gets the separator to be displayed in the {@link TextField}, between the two dates.
	 *
	 * @return the text separator. Default to {@link #SEPARATOR}
	 */
	protected String getSeparator()
	{
		return SEPARATOR;
	}

	/**
	 * Gets the text to be displayed in the {@link TextField}, in place of a date which is null.
	 *
	 * @return the null representation. Default to {@link #NULL}
	 */
	protected String getNullString()
	{
		return NULL;
	}

	// Events //

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		// TextField //
		this.input = this.newTextField("text", this.getModel());
		this.input.setOutputMarkupId(true);
		this.input.add(this.newToggleBehavior());
		this.add(this.input);

		// DatePicker //
		this.datepicker = new RangeDatePicker("datepicker", this.getModel(), this.options) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onValueChanged(AjaxRequestTarget target)
			{
				RangeDatePickerTextField.this.input.modelChanged();
				target.add(RangeDatePickerTextField.this.input);

				RangeDatePickerTextField.this.onValueChanged(target);
			}
		};

		this.add(this.datepicker);
	}

	@Override
	public void onValueChanged(AjaxRequestTarget target)
	{
		// noop
	}

	// Factories //
	/**
	 * Gets a new {@link DateFormat} to be used by the {@link TextField}'s {@link IConverter}
	 *
	 * @param locale the {@link Locale}
	 * @return the {@link DateFormat}
	 */
	protected DateFormat newDateFormat(Locale locale)
	{
		DateFormat df = new SimpleDateFormat("dd MMM yyyy", locale);
		df.setTimeZone(DateUtils.UTC);

		return df;
	}

	/**
	 * Gets a new {@link TextField}.
	 *
	 * @param iModel
	 * @return the {@link TextField}
	 */
	private TextField<DateRange> newTextField(String id, IModel<DateRange> model)
	{
		return new TextField<DateRange>(id, model, DateRange.class) {

			private static final long serialVersionUID = 1L;

			@Override
			public <C> IConverter<C> getConverter(Class<C> type)
			{
				if (DateRange.class.isAssignableFrom(type))
				{
					return newConverter();
				}

				return super.getConverter(type);
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected <C> IConverter<C> newConverter()
	{
		return (IConverter<C>) new IConverter<DateRange>() {

			private static final long serialVersionUID = 1L;

			@Override
			public DateRange convertToObject(String value, Locale locale)
			{
				DateFormat df = RangeDatePickerTextField.this.newDateFormat(locale);
				String[] dates = value.split(RangeDatePickerTextField.this.getSeparator());

				try
				{
					return new DateRange(df.parse(dates[0]), df.parse(dates[1]));
				}
				catch (ParseException e)
				{
					throw new ConversionException(e.getMessage(), e);
				}
				catch (IndexOutOfBoundsException e)
				{
					throw new ConversionException(e.getMessage(), e);
				}
			}

			@Override
			public String convertToString(DateRange value, Locale locale)
			{
				DateFormat df = RangeDatePickerTextField.this.newDateFormat(locale);
				Date start = value.getStart();
				Date end = value.getEnd();

				return String.format("%s%s%s", start != null ? df.format(start) : getNullString(), RangeDatePickerTextField.this.getSeparator(), end != null ? df.format(end) : getNullString());
			}
		};
	}

	/**
	 * Gets a new {@link JQueryAbstractBehavior} to show the {@link RangeDatePicker} on {@link TextField}'s click event.
	 *
	 * @return the {@link JQueryAbstractBehavior}
	 */
	private JQueryAbstractBehavior newToggleBehavior()
	{
		return new JQueryAbstractBehavior("datepicker-toggle") {

			private static final long serialVersionUID = 1L;

			@Override
			protected String $()
			{
				StringBuilder statements = new StringBuilder();

				statements.append("jQuery('#").append(input.getMarkupId()).append("').on('click', function() { ");
				statements.append("		jQuery('#").append(datepicker.getMarkupId()).append("').DatePickerShow(); ");
				statements.append("} );");

				return statements.toString();
			}
		};
	}
}
