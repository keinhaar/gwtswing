package de.exware.gwtswing.swing;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.lang.StyleSheet;

public class GUIManager
{
    private static Map<Object, Object> resources = new HashMap<>();
    private static boolean isInitialized;
    
    static
    {
        RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL()
            + "localization/de.exware.gwtswing.properties");
        try
        {
            requestBuilder.sendRequest(null, new RequestCallback()
            {
                @Override
                public void onError(Request request, Throwable exception)
                {
                    Window.alert("" + exception);
                }

                @Override
                public void onResponseReceived(Request request, Response response)
                {
                    if (response.getStatusCode() == 200)
                    {
                        String text = response.getText();
                        String[] strings;
                        if(text.contains("\\r"))
                        {
                            strings = text.split("\\r");
                        }
                        else
                        {
                            strings = text.split("\\n");
                        }
                        for (int i = 0; i < strings.length; i++)
                        {
                            String[] prop = strings[i].trim().split("=");
                            if (prop.length == 2)
                            {
                                String value = prop[1];
                                while (value.endsWith("\\"))
                                {
                                    value = value.substring(0, value.length() - 1) + strings[++i];
                                }
                                resources.put(prop[0], value);
                            }
                        }
                        isInitialized = true;
                    }
                }
            });
        }
        catch (RequestException e)
        {
            Logger.getLogger(GUIManager.class.getName()).severe("Unable to load localization: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static GColor getColor(Object key)
    {
        GColor col = (GColor) resources.get(key);
        if(col == null)
        {
            String k = key.toString();
            int index = k.indexOf('/');
            String cssrule = k.substring(0, index);
            String property = k.substring(index+1);
            col = StyleSheet.getColor(cssrule, property);
            resources.put(key, col);
        }
        return col;
    }

    public static GFont getFont(Object key)
    {
        GFont font = (GFont) resources.get(key);
        if(font == null)
        {
            String k = key.toString();
            int index = k.indexOf('/');
            String cssrule = k.substring(0, index);
            int size = StyleSheet.getInt(cssrule, "font-size");
//            if(GUtilities.getDevicePixelRatio() != 1)
//            {
//                double scaling = GUtilities.getDevicePixelRatio();
//                size = (int) (size * scaling);
//                StyleSheet.setPixel(cssrule, "font-size", size);
//            }
            String family = StyleSheet.getProperty(cssrule,"font-family");
            font = new GFont(family, size, GFont.PLAIN);
            resources.put(key, font);
        }
        return font;
    }

    public static boolean isInitialized(Object key)
    {
        return resources.get(key) != null;
    }
    
    public static GInsets getPadding(Class<?> clazz)
    {
        String key = ".gwts-" + clazz.getSimpleName() + "/padding";
        GInsets padding = (GInsets) resources.get(key);
        if(padding == null && resources.containsKey(key) == false)
        {
            Integer pad = getInt(".gwts-", clazz, "padding");
            Integer pl = getInt(".gwts-", clazz, "padding-left");
            Integer pr = getInt(".gwts-", clazz, "padding-right");
            Integer pb = getInt(".gwts-", clazz, "padding-bottom");
            Integer pt = getInt(".gwts-", clazz, "padding-top");
            pad = pad == null ? 0 : pad;
            pl = pl == null ? pad : pl;
            pr = pr == null ? pad : pr;
            pt = pt == null ? pad : pt;
            pb = pb == null ? pad : pb;
            
            if(pl != null && pr != null && pt != null && pb != null)
            {
                padding = new GInsets(pt, pl, pb, pr);
                resources.put(key, padding);
            }
            else
            {
                resources.put(key, null);
            }
        }
        return padding;
    }

    public static GInsets getBorderSize(Class<?> clazz)
    {
        String key = ".gwts-" + clazz.getSimpleName() + "/border-width";
        GInsets border = (GInsets) resources.get(key);
        if(border == null && resources.containsKey(key) == false)
        {
            Integer bord = getInt(".gwts-", clazz, "border-width");
            Integer pl = getInt(".gwts-", clazz, "border-left");
            Integer pr = getInt(".gwts-", clazz, "border-right");
            Integer pb = getInt(".gwts-", clazz, "border-bottom");
            Integer pt = getInt(".gwts-", clazz, "border-top");
            bord = bord == null ? 0 : bord;
            pl = pl == null ? bord : pl;
            pr = pr == null ? bord : pr;
            pt = pt == null ? bord : pt;
            pb = pb == null ? bord : pb;
            
            if(pl != null && pr != null && pt != null && pb != null)
            {
                border = new GInsets(pt, pl, pb, pr);
                resources.put(key, border);
            }
            else
            {
                resources.put(key, null);
            }
        }
        return border;
    }

    protected static Integer getInt(String prefix, Class<?> clazz, String rule)
    {
        Integer i = null;
        while(i == null && clazz != null)
        {
            String cssrule = prefix + clazz.getSimpleName();        
            i = StyleSheet.getInt(cssrule, rule);
            clazz = clazz.getSuperclass();
        }
        return i;
    }
    
    /**
     * Liefert einen internen lokalisierungs String für GWTSwing.
     * Wird z. B. für OK und Cancel Texte verwendet. 
     * @param string
     * @return
     */
    public static String getString(String string)
    {
        return (String) resources.get(string);
    }

    public static boolean isInitialized()
    {
        return isInitialized;
    }
}
