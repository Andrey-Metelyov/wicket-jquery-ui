package com.googlecode.wicket.jquery.ui.samples.pages.kendo.treeview;

import java.util.Arrays;
import java.util.List;

import com.googlecode.wicket.jquery.ui.samples.SamplePage;



abstract class AbstractTreeViewPage extends SamplePage
{
	private static final long serialVersionUID = 1L;

	public AbstractTreeViewPage()
	{
	}

	@Override
	protected List<DemoLink> getDemoLinks()
	{
		return Arrays.asList(
				new DemoLink(DefaultTreeViewPage.class, "Behavior (static html)"),
				new DemoLink(StringTreeViewPage.class, "TreeView (simple content)"),
				new DemoLink(CustomTreeViewPage.class, "AjaxTreeView (custom beans)")
			);
	}
}
