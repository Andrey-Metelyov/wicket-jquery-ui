package com.googlecode.wicket.jquery.ui.samples.kendoui.chart;

import java.util.Arrays;
import java.util.List;

import com.googlecode.wicket.jquery.ui.samples.KendoSamplePage;

abstract class AbstractChartPage extends KendoSamplePage // NOSONAR
{
	private static final long serialVersionUID = 1L;

	@Override
	protected List<DemoLink> getDemoLinks()
	{
		return Arrays.asList( // lf
				new DemoLink(LineChartPage.class, "Line Chart"), // lf
				new DemoLink(ColumnChartPage.class, "Column Chart") // lf
		);
	}
}