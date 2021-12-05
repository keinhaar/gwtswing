package de.exware.gwtswing.util;

/**
 * GEventObject Baseclass like Swings GEventObject
 */
abstract public class GEventObject
{
   private Object source;
   
   public GEventObject(Object source)
   {
      this.source = source;
   }
   
   /**
    * Source of the GEventObject
    */
   public Object getSource()
   {
      return source;
   }

   public void setSource(Object source)
   {
       this.source = source;
   }
}
