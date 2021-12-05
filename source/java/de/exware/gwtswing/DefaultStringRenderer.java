package de.exware.gwtswing;


public class DefaultStringRenderer<T>
    implements StringRenderer<T>
{
    public String toString(T object)
    {
        if(object != null)
        {
            return object.toString();
        }
        return null;
    }
}
