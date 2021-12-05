package de.exware.gwtswing.awt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

public class GImage
{
    private ImageElement bitmap;
    private boolean loaded;
    private boolean error;
    
    public GImage(String url)
    {
        this.bitmap = Document.get().createImageElement();
        Event.setEventListener(bitmap, new EventListener()
        {
            @Override
            public void onBrowserEvent(Event event)
            {
                if(event.getTypeInt() == Event.ONLOAD)
                {
                    loaded = true;
                }
                else if(event.getTypeInt() == Event.ONERROR)
                {
                    error = true;
                }
                Event.setEventListener(bitmap, null);
            }
        });
        Event.sinkEvents(bitmap, Event.ONLOAD | Event.ONERROR);
        bitmap.setSrc(url);
    }

    public int getWidth()
    {
        int nw = bitmap.getPropertyInt("naturalWidth");
        return nw;
    }

    public int getHeight()
    {
        int nh = bitmap.getPropertyInt("naturalHeight");
        return nh;
    }

    public ImageElement getImageElement()
    {
        return bitmap;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public boolean isError()
    {
        return error;
    }
    
}
