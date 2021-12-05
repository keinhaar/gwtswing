package de.exware.gwtswing.upload;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Java Wrapper around the JavaScript FileList Object.
 * @author martin
 *
 */
public class BrowserFileList extends JavaScriptObject
{
    protected BrowserFileList()
    {
    }

    private final native BrowserFile get(int i)
    /*-{
		return this.item(i);
    }-*/;

    private final native int size()
    /*-{
		return this.length;
    }-*/;

    /**
     * Get a List of BrowserFile Objects.
     * @return
     */
    public final List<BrowserFile> getFiles()
    {
        List<BrowserFile> files = new ArrayList<BrowserFile>();
        for (int i = 0; i < size(); i++)
        {
            BrowserFile file = get(i);
            files.add(file);
        }
        return files;
    }
}
