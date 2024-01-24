package de.exware.gwtswing.swing;

import de.exware.gwtswing.swing.plaf.GUIDefaults;

public abstract class GLookAndFeel 
{
	abstract public GUIDefaults getDefaults();
	abstract public void initialize();
	abstract public void uninitialize();
	abstract public String getName();
}
