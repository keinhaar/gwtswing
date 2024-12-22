package de.exware.gwtswing.swing;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.GPlatform.Callback;
import de.exware.gplatform.style.GPStyleSheet;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.plaf.ComponentUI;

public class GUIManager
{
    private static Map<Object, Object> resources = new HashMap<>();
    private static Map<Object, Object> uiResources = new HashMap<>();
    private static boolean isInitialized;
    private static GLookAndFeel lookAndFeel = null;
    
    static
    {
    	try
    	{
    		GPlatform.getInstance().loadData(GPlatform.getInstance().getModuleBaseURL()
    				+ "localization/de.exware.gwtswing.properties", new Callback()
	            {
	                @Override
	                public void onError(Throwable exception)
	                {
                        isInitialized = true;
	                    GPlatform.getInstance().alert("" + exception);
	                }
	
	                @Override
	                public void onSuccess(int statuscode, String data)
	                {
	                    if (statuscode == 200)
	                    {
	                        String text = data;
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
        catch (Exception e)
        {
            Logger.getLogger(GUIManager.class.getName()).severe("Unable to load localization: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static final void resetCache()
    {
        uiResources.clear();
    }
    
    public static GColor getColor(Object key)
    {
        GColor col = (GColor) uiResources.get(key);
        if(col == null)
        {
            String k = key.toString();
            int index = k.indexOf('/');
            String cssrule = k.substring(0, index);
            String property = k.substring(index+1);
            String c = GPStyleSheet.getColor(cssrule, property);
            col = GColor.decode(c);
            uiResources.put(key, col);
        }
        return col;
    }

    public static GFont getFont(Object key)
    {
        GFont font = (GFont) uiResources.get(key);
        if(font == null)
        {
            String k = key.toString();
            int index = k.indexOf('/');
            String cssrule = k.substring(0, index);
            int size = GPStyleSheet.getInt(cssrule, "font-size");
//            if(GUtilities.getDevicePixelRatio() != 1)
//            {
//                double scaling = GUtilities.getDevicePixelRatio();
//                size = (int) (size * scaling);
//                GwtGPStyleSheet.setPixel(cssrule, "font-size", size);
//            }
            String family = GPStyleSheet.getProperty(cssrule,"font-family");
            font = new GFont(family, GFont.PLAIN, size);
            uiResources.put(key, font);
        }
        return font;
    }

    public static boolean isInitialized(Object key)
    {
        return resources.get(key) != null;
    }
    
    public static GInsets getPadding(Class<?> clazz)
    {
        String key = ".gwts-" + clazz.getName() + "/padding";
        GInsets padding = (GInsets) uiResources.get(key);
        if(padding == null && uiResources.containsKey(key) == false)
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
                uiResources.put(key, padding);
            }
            else
            {
                uiResources.put(key, null);
            }
        }
        return padding;
    }

    public static GInsets getBorderSize(Class<?> clazz)
    {
        String key = ".gwts-" + clazz.getName() + "/border-width";
        GInsets border = (GInsets) uiResources.get(key);
        if(border == null && uiResources.containsKey(key) == false)
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
                uiResources.put(key, border);
            }
            else
            {
                uiResources.put(key, null);
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
            String key = cssrule + "/" + rule;
            i = (Integer) uiResources.get(key);
            if(i == null && uiResources.containsKey(key) == false)
            {
                i = GPStyleSheet.getInt(cssrule, rule);
                uiResources.put(key, i);
            }
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
    
    public static ComponentUI getUI(GComponent component)
    {
    	if(lookAndFeel != null)
    	{
    		return lookAndFeel.getDefaults().getUI(component);
    	}
    	
    	return null;
    }
    
    public static void setLookAndFeel(GLookAndFeel lookAndFeel)
    {
    	if(GUIManager.lookAndFeel != null)
    	{
    		GUIManager.lookAndFeel.uninitialize();
    	}
    	
    	lookAndFeel.initialize();
    	
    	GUIManager.lookAndFeel = lookAndFeel;
    }
}
