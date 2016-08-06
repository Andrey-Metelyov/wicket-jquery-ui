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
package com.googlecode.wicket.kendo.ui.form.datetime.local;

import java.util.Locale;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.core.utils.DateUtils;
import com.googlecode.wicket.jquery.core.utils.LocaleUtils;

/**
 * Provides a datetime-picker based on a {@link DatePicker} and a {@link TimePicker}
 *
 * @author Sebastien Briquet - sebfz1
 */
public class DateTimePicker extends FormComponentPanel<LocalDateTime> implements ITextFormatProvider
{
	private static final long serialVersionUID = 1L;

	private static final String ERROR_NOT_INITIALIZED = "Internal timePicker is not initialized (#onInitialize() has not yet been called).";

	DatePicker datePicker;
	TimePicker timePicker;

	private final Locale locale;
	private final String datePattern;
	private final String timePattern;

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 */
	public DateTimePicker(String id)
	{
		this(id, null, null, DateUtils.LOCAL_DATE_PATTERN, DateUtils.LOCAL_TIME_PATTERN);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param locale the {@code Locale}
	 */
	public DateTimePicker(String id, Locale locale)
	{
		this(id, null, locale, LocaleUtils.getLocaleDatePattern(locale, DateUtils.LOCAL_DATE_PATTERN), LocaleUtils.getLocaleTimePattern(locale, DateUtils.LOCAL_TIME_PATTERN));
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public DateTimePicker(String id, String datePattern, String timePattern)
	{
		this(id, null, null, datePattern, timePattern);
	}

	/**
	 * constructor
	 *
	 * @param id the markup id
	 * @param locale the {@code Locale}
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public DateTimePicker(String id, Locale locale, String datePattern, String timePattern)
	{
		this(id, null, locale, datePattern, timePattern);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 */
	public DateTimePicker(String id, IModel<LocalDateTime> model)
	{
		this(id, model, null, DateUtils.LOCAL_DATE_PATTERN, DateUtils.LOCAL_TIME_PATTERN);
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 * @param locale the {@code LocalDate}
	 */
	public DateTimePicker(String id, IModel<LocalDateTime> model, Locale locale)
	{
		this(id, model, locale, LocaleUtils.getLocaleDatePattern(locale, DateUtils.LOCAL_DATE_PATTERN), LocaleUtils.getLocaleTimePattern(locale, DateUtils.LOCAL_TIME_PATTERN));
	}

	/**
	 * Constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public DateTimePicker(String id, IModel<LocalDateTime> model, String datePattern, String timePattern)
	{
		this(id, model, null, datePattern, timePattern);
	}

	/**
	 * Main constructor
	 *
	 * @param id the markup id
	 * @param model the date {@code IModel}
	 * @param locale the {@code Locale}
	 * @param datePattern the SimpleDateFormat pattern for the date
	 * @param timePattern the SimpleDateFormat pattern for the time
	 */
	public DateTimePicker(String id, IModel<LocalDateTime> model, Locale locale, String datePattern, String timePattern)
	{
		super(id, model);

		this.locale = locale;
		this.datePattern = datePattern;
		this.timePattern = timePattern;
	}

	// Methods //

	@Override
	public void convertInput()
	{
		LocalDate date = this.datePicker.getConvertedInput();
		LocalTime time = this.timePicker.getConvertedInput();

		this.setConvertedInput(LocalDateTime.of(date, time != null ? time : LocalTime.MIDNIGHT));
	}

	// Properties //

	@Override
	public Locale getLocale()
	{
		if (this.locale != null)
		{
			return this.locale;
		}

		return super.getLocale();
	}

	/**
	 * Returns the date-time pattern.
	 *
	 * @see org.apache.wicket.markup.html.form.AbstractTextComponent.ITextFormatProvider#getTextFormat()
	 */
	@Override
	public final String getTextFormat()
	{
		if (this.isTimePickerEnabled())
		{
			return String.format("%s %s", this.getDatePattern(), this.getTimePattern());
		}

		return this.getDatePattern();
	}

	/**
	 * Gets a (localized) string representation of the model object, given the date-time pattern in use.
	 *
	 * @return the model object as string
	 */
	public String getModelObjectAsString()
	{
		LocalDateTime date = this.getModelObject();

		if (date != null)
		{
			return date.format(DateTimeFormatter.ofPattern(this.getTextFormat(), this.getLocale()));
		}

		return "";
	}

	/**
	 * Gets the date pattern in use
	 *
	 * @return the pattern
	 */
	public final String getDatePattern()
	{
		return this.datePicker.getTextFormat(); // let throw a NPE if #getDatePattern() is called before #onConfigure()
	}

	/**
	 * Gets the time pattern in use
	 *
	 * @return the pattern
	 */
	public final String getTimePattern()
	{
		return this.timePicker.getTextFormat(); // let throw a NPE if #getTimePattern() is called before #onConfigure()
	}

	/**
	 * Indicates whether the time-picker is enabled.<br/>
	 * This method is marked final because an override will not change the time-picker 'enable' flag
	 *
	 * @return the enabled flag
	 */
	public final boolean isTimePickerEnabled()
	{
		if (this.timePicker != null)
		{
			return this.timePicker.isEnabled();
		}

		throw new WicketRuntimeException(ERROR_NOT_INITIALIZED);
	}

	/**
	 * Sets the time-picker enabled flag
	 *
	 * @param enabled the enabled flag
	 */
	public final void setTimePickerEnabled(boolean enabled)
	{
		if (this.timePicker != null)
		{
			this.timePicker.setEnabled(enabled);
		}
		else
		{
			throw new WicketRuntimeException(ERROR_NOT_INITIALIZED); // fixes #61
		}
	}

	/**
	 * Sets the time-picker enabled flag
	 *
	 * @param target the {@link AjaxRequestTarget}
	 * @param enabled the enabled flag
	 */
	public final void setTimePickerEnabled(AjaxRequestTarget target, boolean enabled)
	{
		this.setTimePickerEnabled(enabled);

		target.add(this.timePicker);
	}

	// Events //

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		this.datePicker = this.newDatePicker("datepicker", new PropertyModel<LocalDate>(this.getModel(), "date"), this.locale, this.datePattern, new Options());
		this.timePicker = this.newTimePicker("timepicker", new PropertyModel<LocalTime>(this.getModel(), "time"), this.locale, this.timePattern, new Options());

		this.add(this.datePicker.setConvertEmptyInputStringToNull(false)); // will force to use the converter (null bypasses)
		this.add(this.timePicker.setConvertEmptyInputStringToNull(false)); // will force to use the converter (null bypasses)
	}

	// Factories //

	/**
	 * Gets a new {@link DatePicker}
	 *
	 * @param id the markup id
	 * @param model the {@link IModel}
	 * @param locale the {@link Locale}
	 * @param datePattern the date pattern to be used
	 * @param options the {@code Options}
	 * @return the {@link DatePicker}
	 */
	protected DatePicker newDatePicker(String id, IModel<LocalDate> model, Locale locale, String datePattern, Options options)
	{
		return new DatePicker(id, model, locale, datePattern, options);
	}

	/**
	 * Gets a new {@link TimePicker}
	 *
	 * @param id the markup id
	 * @param locale the {@link Locale}
	 * @param model the {@link IModel}
	 * @param timePattern the date pattern to be used
	 * @param options the {@code Options}
	 * @return the {@link TimePicker}
	 */
	protected TimePicker newTimePicker(String id, IModel<LocalTime> model, Locale locale, String timePattern, Options options)
	{
		return new TimePicker(id, model, locale, timePattern, options);
	}
}
