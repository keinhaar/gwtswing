package de.exware.gwtswing;

/**
 * Implementing classes should know how an Object should be represented as String.
 * Using this interface will allow us to use different representations in VRadioButtonGroups
 * as it is possible with Renderes on Tables and Lists. 
 * @author martin
 */
public interface StringRenderer<T>
{
    public String toString(T object);
}
