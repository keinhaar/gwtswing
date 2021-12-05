package de.exware.gwtswing.lang;

import com.google.gwt.core.client.JavaScriptObject;

import de.exware.gwtswing.awt.GColor;

public final class StyleSheet extends JavaScriptObject
{
    protected StyleSheet()
    {
    }

    public static GColor getColor(String selector, String property)
    {
        GColor col = null;
        CSSRule rule = null;
        for(int i=count()-1;col == null && i>=0;i--)
        {
            try
            {
                StyleSheet st = get(i);
                rule = st.getCSSRule(selector);
                if(rule != null)
                {
                    col = rule.getColor(property);
                }
            }
            catch(Exception ex)
            {
//                ex.printStackTrace();
            }
        }
        return col;
    }

    public static String getProperty(String selector, String property)
    {
        String prop = null;
        CSSRule rule = null;
        for(int i=count()-1;prop == null && i>=0;i--)
        {
            try
            {
                StyleSheet st = get(i);
                rule = st.getCSSRule(selector);
                if(rule != null)
                {
                    String href = st.getHref();
                    prop = rule.getProperty(property);
                }
            }
            catch(Exception ex)
            {
            }
        }
        return prop;
    }

    public static Integer getInt(String selector, String property)
    {
        Integer prop = null;
        CSSRule rule = null;
        for(int i=count()-1;prop == null && i>=0;i--)
        {
            try
            {
                StyleSheet st = get(i);
                String href = st.getHref();
                rule = st.getCSSRule(selector);
                if(rule != null)
                {
                    prop = rule.getInt(property);
                }
            }
            catch(Exception ex)
            {
            }
        }
        return prop;
    }

    public CSSRule getCSSRule(String selector)
    {
        CSSRule rule = null;
        String href = getHref();
        int c = getRuleCount();
        for(int i=0;i<c;i++)
        {
            CSSRule r = getCSSRule(i);
            String sel = r.getSelector();
            if(sel.equals(selector))
            {
                rule = r;
                break;
            }
        }
        return rule;
    }
    
    protected native CSSRule getCSSRule(int i) /*-{
        return this.cssRules[i];
    }-*/;
    
    protected native int getRuleCount() /*-{
        return this.cssRules.length;
    }-*/;
    
    public static native StyleSheet get(int index) /*-{
        var st = $doc.styleSheets[index];
        return st;                
    }-*/;

    public native String getHref() /*-{
        var st = this.href;
        return st;                
    }-*/;

    protected static native int count() /*-{
        var st = $doc.styleSheets.length;
        return st;            
    }-*/;
}
