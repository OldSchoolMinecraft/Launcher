package me.moderator_man.osml;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Arrays;
import java.util.List;

public class SimpleFocusTraversalPolicy extends FocusTraversalPolicy
{
	private final List<? extends Component> order;

	public SimpleFocusTraversalPolicy(Component... arrays)
	{
		super();
		this.order = Arrays.asList(arrays);
	}

	@Override
	public Component getFirstComponent(Container focusCycleRoot)
	{
		return order.get(0);
	}

	@Override
	public Component getLastComponent(Container focusCycleRoot)
	{
		return order.get(order.size() - 1);
	}

	@Override
	public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
	{
		int idx = order.indexOf(aComponent) + 1;
		if (idx >= order.size() && !focusCycleRoot.isFocusCycleRoot())
		{
			return null;
		} else
		{
			return order.get(idx % order.size());
		}
	}

	@Override
	public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
	{
		int idx = order.indexOf(aComponent) - 1;
		if (idx < 0 && !focusCycleRoot.isFocusCycleRoot())
		{
			return null;
		} else
		{
			return order.get((idx + order.size()) % order.size());
		}
	}

	@Override
	public Component getDefaultComponent(Container focusCycleRoot)
	{
		return order.get(0);
	}
}