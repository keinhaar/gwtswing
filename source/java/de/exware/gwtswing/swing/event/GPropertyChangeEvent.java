package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.util.GEventObject;

public class GPropertyChangeEvent extends GEventObject
{
   private String property;
   private Object value;
   
   public GPropertyChangeEvent(Object source,String property, Object value)
   {
      super(source);
      this.property = property;
      this.value = value;
   }
   
   public String getProperty()
   {
      return property;
   }
   
   public Object getValue()
   {
      return value;
   }
}
