package de.exware.gwtswing.swing.plaf;

import de.exware.gwtswing.swing.GComponent;

public abstract class ComponentUI
{
	
	static public ComponentUI createUI(GComponent component)
	{
		throw new Error("Not implemented.");
	}
	
	protected ComponentUI() {};
	
	abstract public void installUI(GComponent c);

	abstract public void uninstallUI(GComponent c);
}
