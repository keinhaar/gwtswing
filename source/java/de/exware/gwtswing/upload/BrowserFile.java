package de.exware.gwtswing.upload;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Java Wrapper around the JavaScript File Object.
 * @author martin
 *
 */
public class BrowserFile extends JavaScriptObject
{
    protected BrowserFile()
    {
    }

    public final native String getName()
    /*-{
		return this.name;
    }-*/;

}
