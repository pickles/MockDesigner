/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mockdesigner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mockdesigner.component.Component;

/**
 *
 * @author Manabu Shibata
 */
public class ComponentManager {

    public List<Component> components = new ArrayList<Component>();

    public void addComponent(Component c) {
        components.add(c);
        updateOrder();
    }

    public void removeComponent(Component c) {
        components.remove(c);
    }

    public List<Component> getComponents() {
        return components;
    }

    public Component getComponent(Point coordinate) {
        for (int i = components.size()-1; i >= 0; i--) {
            Component c = components.get(i);
            if (c.isOn(coordinate))  {
                System.out.println("Hit!");
                return c;
            }
        }
        return null;
    }

    public void updateOrder() {
        Collections.sort(components, new Comparator<Component>() {

            public int compare(Component c1, Component c2) {
                return c1.z - c2.z;
            }
        });
    }

    public void clearAll() {
        components.clear();
    }
    
}
