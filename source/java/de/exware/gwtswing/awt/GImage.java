package de.exware.gwtswing.awt;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPImageElement;
import de.exware.gplatform.event.GPEvent;
import de.exware.gplatform.event.GPEventListener;

public class GImage
{
    private GPImageElement bitmap;
    private boolean loaded;
    private boolean error;
    
    public GImage(String url)
    {
        this.bitmap = GPlatform.getDoc().createImageElement();
        bitmap.enabledEvents(GPEvent.Type.ONLOAD, GPEvent.Type.ONERROR);
        bitmap.setEventListener(new GPEventListener()
        {
            
            @Override
            public void onBrowserEvent(GPEvent event)
            {
                if(event.getType() == GPEvent.Type.ONLOAD)
                {
                    loaded = true;
                }
                else if(event.getType() == GPEvent.Type.ONERROR)
                {
                    error = true;
                }
                bitmap.setEventListener(null);
            }
        });
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

    public GPImageElement getImageElement()
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
