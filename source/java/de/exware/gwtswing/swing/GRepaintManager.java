package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GRepaintManager
{
    private static Set<GComponent> containerToLayout = new HashSet<>();
    private static boolean layoutScheduled;
    private static long start;
    
    public static void revalidate(GComponent container)
    {
        if(containerToLayout.contains(container) == false)
        {
            containerToLayout.add(container);
        }
        if(layoutScheduled == false)
        {
            layoutScheduled = true;
            GSwingUtilities.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    start = System.currentTimeMillis();
                    layoutScheduled = false;
                    List<GComponent> list = new ArrayList(containerToLayout);
                    containerToLayout.clear();
                    for(int i=0;i<list.size();i++)
                    {
                        GComponent cont = list.get(i);
                        if(cont.isVisible() && isParentContained(cont, list) == false)
                        {
                            cont.validate();
                        }
                    }
//                    Window.alert("" + (System.currentTimeMillis()-start));
                }
            });
        }
    }
    
    private static boolean isParentContained(GComponent comp, List<GComponent> list)
    {
        GComponent c = comp.getParent();
        while(c != null)
        {
            if(list.contains(c))
            {
                return true;
            }
            c = c.getParent();
        }
        return false;
    }
}
