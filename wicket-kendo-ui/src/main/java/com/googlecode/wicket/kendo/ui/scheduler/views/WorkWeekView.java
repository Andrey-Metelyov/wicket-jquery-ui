package com.googlecode.wicket.kendo.ui.scheduler.views;

/**
 * Defines the 'workWeek' scheduler's view
 *
 * @author Patrick Davids - Patrick1701
 *
 */
public final class WorkWeekView extends SchedulerView
{
	private static final long serialVersionUID = 1L;

	/**
	 * Gets a new instance of <tt>WorkWeekView</tt>
	 *
	 * @return a new {@link WorkWeekView}
	 */
	public static WorkWeekView newInstance()
	{
		return new WorkWeekView();
	}

	/**
	 * Constructor
	 */
	private WorkWeekView()
	{
		super(SchedulerViewType.workWeek);
	}
}
