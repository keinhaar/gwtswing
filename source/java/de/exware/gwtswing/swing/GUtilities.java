package de.exware.gwtswing.swing;

import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.exware.gwtswing.awt.GCanvas;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GPoint;

/**
 * Utilities, die nicht im GSwingUtilities Interface zu finden sind.
 * z.B. das einfügen in DOM Elemente erlauben. Das ermitteln einer URL Resource usw.  
 * @author martin
 *
 */
public class GUtilities
{
    public static final String CLIENT_PROPERTY_IGNORE_ENABLE_RECURSIVE = "CLIENT_PROPERTY_IGNORE_ENABLE_RECURSIVE";
    private static DivElement measureElement;
    private static GCanvas measureCanvas;
    

    static
    {
        measureElement = Document.get().createDivElement();
//        measureElement.getStyle().setHeight(0, Unit.PX);
        measureElement.getStyle().setLeft(0, Unit.PX);
        measureElement.getStyle().setTop(50, Unit.PX);
//        measureElement.getStyle().setOverflow(Overflow.HIDDEN);
        BodyElement body = Document.get().getBody();
        measureElement.getStyle().setZIndex(10000);
        body.appendChild(measureElement);
        measureCanvas = new GCanvas();
    }
    
    /**
     * Inserts a classname as first classname.
     * @param el
     * @param className
     */
    public static void insertClassNameBefore(Element el, String className)
    {
        String str = el.getClassName();
        str = className + " " + str;
        el.setClassName(str);
    }
    
    /**
     * Wraps 2 or more Components in a Horizontal Container to treat them as one.
     * @return
     */
    public static GComponent wrapComponents(GComponent ... comps )
    {
        GPanel panel = new GPanel();
        panel.setLayout(new GGridBagLayout());
        GGridBagConstraints gbc = new GGridBagConstraints();
        gbc.gridx = 1;
        gbc.insets.right = 3;
        for(int i=0;i<comps.length;i++)
        {
            panel.add(comps[i], gbc);
            gbc.gridx++;
        }
        return panel;
    }
    
    /**
     * Brückenmethode. Diese erlaubt das hinzufügen von Real Swing Komponenten zu normalen HTML Elementen.
     * @param w
     * @param gc
     */
    public static void addToWidget(Element el, GComponent gc)
    {
        Element peer = gc.getPeer();
        el.appendChild(peer);
        gc.revalidate();
//        gc.revalidate();
    }

    public static native Object getStyleSheetValue() /*-{
        if (true) {
            var st = $doc.styleSheets;            
            alert("STYLE: " + st[0].cssRules.item(0).selectorText);
            return st;
        } 
    }-*/;
    
    /**
     * This Method returns an URL for a resource file in the specified plugin.
     */
    public static final String getResource(String pluginid,String resourcename)
    {
       pluginid = pluginid.replace('.', '/');
       String res = GWT.getModuleBaseForStaticFiles() + pluginid + resourcename;      
       return res;
    }

    /**
     * Returns a complete URL for the given servlet. This will be created by 
     * retrieving the Applications Base URL, which will be concatenated with the servlet URL.
     */
    public static final String getServletURL(String servletPath)
    {
       String res = GWT.getModuleBaseURL() + servletPath;      
       return res;
    }

    /**
     * Liefert ein Objekt, mit dem die PreferredSize gemessen werden kann. Das Objekt ist
     * unsichtbar. Alle hinzugefügten Elemente müssen wieder entfernt werden, und alle
     * Änderungen an Attributen sollten Rückgängig gemacht werden.
     * @return
     */
    public static DivElement getMeasureElement()
    {
        return measureElement;
    }
    
    public static GCanvas getMeasureCanvas()
    {
        return measureCanvas;
    }
    
    public native static double getDevicePixelRatio()
    /*-{
        return window.devicePixelRatio;
    }-*/;

    
//    public static PixelStyle getComputedStyle(Element element) 
//    {
//        PixelStyle style = new PixelStyle();
//        int paddingLeft = getPixelValue(getComputedStyleProperty(element, "padding-left"));
//        style.setPaddingLeft(paddingLeft);
//        int paddingRight = getPixelValue(getComputedStyleProperty(element, "padding-right"));
//        style.setPaddingRight(paddingRight);
//        int paddingTop = getPixelValue(getComputedStyleProperty(element, "padding-top"));
//        style.setPaddingTop(paddingTop);
//        int paddingBottom = getPixelValue(getComputedStyleProperty(element, "padding-bottom"));
//        style.setPaddingBottom(paddingBottom);
//        int borderLeft = getPixelValue(getComputedStyleProperty(element, "border-left-width"));
//        style.setBorderLeft(borderLeft);
//        int borderRight = getPixelValue(getComputedStyleProperty(element, "border-right-width"));
//        style.setBorderRight(borderRight);
//        int borderTop = getPixelValue(getComputedStyleProperty(element, "border-top-width"));
//        style.setBorderTop(borderTop);
//        int borderBottom = getPixelValue(getComputedStyleProperty(element, "border-bottom-width"));
//        style.setBorderBottom(borderBottom);
//        double fontSize = getPixelValue(getComputedStyleProperty(element, "font-size"));
//        style.setFontSize(fontSize);
//        return style;
//    }
//    
//    static class PixelStyle
//    {
//        private int borderRight;
//        private int borderLeft;
//        private int paddingLeft;
//        private int paddingRight;
//        private int borderTop;
//        private int borderBottom;
//        private int paddingTop;
//        private int paddingBottom;
//        private double fontSize;
//        
//        public void setPaddingLeft(int paddingLeft)
//        {
//            this.paddingLeft = paddingLeft;
//        }
//        
//        public void setFontSize(double fontSize)
//        {
//            this.fontSize = fontSize;
//        }
//
//        public void setPaddingRight(int paddingRight)
//        {
//            this.paddingRight = paddingRight;
//        }
//
//        public int getPaddingLeft()
//        {
//            return paddingLeft;
//        }
//
//        public int getPaddingRight()
//        {
//            return paddingRight;
//        }
//
//        protected int getBorderRight()
//        {
//            return borderRight;
//        }
//
//        protected void setBorderRight(int borderRight)
//        {
//            this.borderRight = borderRight;
//        }
//
//        protected int getBorderLeft()
//        {
//            return borderLeft;
//        }
//
//        protected void setBorderLeft(int borderLeft)
//        {
//            this.borderLeft = borderLeft;
//        }
//
//        protected int getBorderTop()
//        {
//            return borderTop;
//        }
//
//        protected void setBorderTop(int borderTop)
//        {
//            this.borderTop = borderTop;
//        }
//
//        protected int getBorderBottom()
//        {
//            return borderBottom;
//        }
//
//        protected void setBorderBottom(int borderBottom)
//        {
//            this.borderBottom = borderBottom;
//        }
//
//        protected int getPaddingTop()
//        {
//            return paddingTop;
//        }
//
//        protected void setPaddingTop(int paddingTop)
//        {
//            this.paddingTop = paddingTop;
//        }
//
//        protected int getPaddingBottom()
//        {
//            return paddingBottom;
//        }
//
//        protected void setPaddingBottom(int paddingBottom)
//        {
//            this.paddingBottom = paddingBottom;
//        }
//
//        public double getFontSize()
//        {
//            return fontSize;
//        }
//    }
//    
    /**
     * Liefert von einer CSS Property den Wert in Pixeln.
     * @param value
     * @return
     */
    public static int getPixelValue(String value)
    {
        String str = value;
        str = str.replace("px", "");
        int val = 0;
        try
        {
            val = Integer.parseInt(str);
        }
        catch(NumberFormatException ex)
        {
            
        }
        return val;
    }
    
    /**
     * Liefert von einer CSS Property den Wert in Pixeln.
     * @param value
     * @return
     */
    public static double getPixelValueAsDouble(String value)
    {
        String str = value;
        str = str.replace("px", "");
        double val = 0;
        try
        {
            val = Double.parseDouble(str);
        }
        catch(NumberFormatException ex)
        {
            
        }
        return val;
    }
    
    public static native String getComputedStyleProperty(Element element, String propName) /*-{
        if (element.currentStyle) {
            return element.currentStyle[style];
        } else if (window.getComputedStyle) {
            return window.getComputedStyle(element, null).getPropertyValue(
                    propName);
        }
    }-*/;    
    
    public static native void clearSelection() /*-{
        $wnd.getSelection().removeAllRanges();
    }-*/;

    public static void focusFirstField(GComponent focusRoot)
    {
        Stack<GComponent> comps = new Stack<>();
        comps.add(focusRoot);
        while(comps.isEmpty() == false)
        {
            GComponent comp = comps.pop();
            GComponent[] c = comp.getComponents();
            for(int i=c.length-1;i>=0;i--)
            {
                comps.push(c[i]);
            }
            if(comp instanceof GTextField)
            {
                comp.requestFocus();
                break;
            }
        }
    }    
    
    public static void loadTextResource(String url, AsyncCallback<String> callback)
    {
        try
        {
            RequestBuilder req = new RequestBuilder(RequestBuilder.GET, url);
            req.sendRequest(null, new RequestCallback()
            {
                
                @Override
                public void onResponseReceived(Request request, Response response)
                {
                    int status = response.getStatusCode();
                    if(status == 200  //OK
                        || status == 0 && response.getText().length() > 0) //is zero on iphone or ipad
                    {
                        String text = response.getText();
                        callback.onSuccess(text);
                    }
                    else
                    {
                        String text = "Unable to load resource " + url;
                        callback.onFailure(new RuntimeException(text));
                    }
                }
                
                @Override
                public void onError(Request request, Throwable exception)
                {
                    callback.onFailure(exception);
                }
            });
        }
        catch(RequestException ex)
        {
        }
    }

    /**
     * 
     * @param cont
     * @param enabled
     * @param forced if true, then all Components will be disabled. Otherwise some Components
     * like ETabPane tabs are excluded.
     */
    public static void setEnabledRecursive(GComponent cont,boolean enabled, boolean forced)
    {
        Stack<GComponent> comps = new Stack<>();
        comps.push(cont);
        while(comps.isEmpty() == false)
        {
            GComponent comp = comps.pop();
//            if(comp instanceof JScrollPane)
//            {
//                GScrollPane sp = (GScrollPane) comp;
//                comps.push(sp.getViewport().getView());
//                continue;
//            }
            if(comp instanceof GTextComponent)
            {
//                ((GTextComponent)comp).setEditable(enabled);
                ((GTextComponent)comp).setEnabled(enabled);
            }
            else if(comp instanceof GTabbedPane)
            {
                GTabbedPane tpane = (GTabbedPane) comp;
                Boolean ignore = (Boolean) comp.getClientProperty(CLIENT_PROPERTY_IGNORE_ENABLE_RECURSIVE);
                if(ignore == null || ignore == false || forced)
                {
                    comp.setEnabled(enabled);
                    for(int i=0;i<tpane.getTabCount();i++)
                    {
                        comps.push(tpane.getTabbedComponent(i));
                    }
                }
            }
            else if(comp instanceof GComponent)
            {
                GComponent jcomp = comp;
                Boolean ignore = (Boolean) jcomp.getClientProperty(CLIENT_PROPERTY_IGNORE_ENABLE_RECURSIVE);
                if(ignore == null || ignore == false || forced)
                {
                    comp.setEnabled(enabled);
                    GComponent con = comp;
                    for(int i=0;i<con.getComponentCount();i++)
                    {
                        comps.push(con.getComponent(i));
                    }
                }
            }
        }
    }

    public static void setTooltipRecursive(GComponent cont,String tooltip)
    {
        Stack<GComponent> comps = new Stack<>();
        comps.push(cont);
        while(comps.isEmpty() == false)
        {
            GComponent comp = comps.pop();
            for(int i=0;i<comp.getComponentCount();i++)
            {
                comps.push(comp.getComponent(i));
            }
            if(comp instanceof GComponent)
            {
                comp.setToolTipText(tooltip);
            }
        }
    }

    public static void setEnabledRecursive(GComponent cont,boolean enabled)
    {
        GUtilities.setEnabledRecursive(cont, enabled, false);
    }
    
    /**
     * Liefert die Entfernung zwischen 2 Punkten.
     * @param a
     * @param b
     * @return
     */
    public static double getDistance(GPoint a, GPoint b)
    {
        double distance = 0;
        double x = b.getX() - a.getX();
        double x2 = x * x;
        double y = b.getY() - a.getY();
        double y2 = y * y;
        distance = x2 + y2;
        distance = Math.sqrt(distance);
        return distance;
    }
    
    /**
     * Liefert die Richtung in der der Punkt relativ zu Punkt a liegt.
     * Liegt Punkt b genau rechts von Punkt a, dann ist der Winkel 0.
     * Der Winkel steigt im Uhrzeigersinn bis 2 * PI an.
     * @param a
     * @param b
     * @return Winkel in RAD.
     */
    public static double getAngle(GPoint a, GPoint b)
    {        
        double angle = 0;
        if(a.equals(b) == false)
        {
            double x = b.getX() - a.getX();
            double y = b.getY() - a.getY();
            angle = Math.atan(y/x);
            if(x < 0)
            {
                angle += Math.PI;
            }
            if(x >= 0 && y < 0)
            {
                angle += Math.PI * 2;
            }
        }
        return angle;
    }
    
    public static void main(String[] args)
    {
        double angle = getAngle(new GPoint(100,100), new GPoint(151,100));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(150,101));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(150,150));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(100,150));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(50,150));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(50,100));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(50,50));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(100,50));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(150,50));
        System.out.println(angle * 180 / Math.PI);
        angle = getAngle(new GPoint(100,100), new GPoint(150,99));
        System.out.println(angle * 180 / Math.PI);
    }
}
