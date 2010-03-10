package mockdesigner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mockdesigner.component.Component;
import mockdesigner.component.Memento;
import mockdesigner.tool.ComponentListener;

/**
 * @author Manabu Shibata
 */
public class ComponentManager {
    
    private List<ComponentListener> listeners
            = new ArrayList<ComponentListener>();
    
    private static ComponentManager theInstance = new ComponentManager();
    private List<Component> components = new ArrayList<Component>();
    
    protected ComponentManager() {
    }

    public static ComponentManager getInstance() {
        return theInstance;
    }

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

    public void unselectAll() {
        for (Component c : components) {
            c.isSelect = false;
        }
    }

    public List<Component> getSelectedComponents() {
        List<Component> results = new ArrayList<Component>();
        for (Component candidate : components) {
            if (candidate.isSelect) results.add(candidate);
        }
        return results;
    }

    public List<Memento> getSelectedMementos(Memento.Command command) {
        List<Memento> results = new ArrayList<Memento>();
        for (Component c : getSelectedComponents()) {
            results.add(c.createMemento(command));
        }
        return results;
    }

    public void selectionStartMove(Point p) {
        for (Component c : getSelectedComponents()) {
            c.startMove(p);
        }
    }
    
    public void moveSelections(Point p) {
        for (Component c : getSelectedComponents()) {
            c.move(p);
        }
    }

    public void selectionEndMove() {
        for (Component c : getSelectedComponents()) {
            c.endMove();
        }
    }

    public void selectionCancelMove() {
        for (Component c : getSelectedComponents()) {
            c.cancelMove();
        }
    }

    public void cancelAction(List<Memento> mementos) {
        for (Memento memento : mementos) {
            Component target = memento.getComponent();
            if (memento.getCommand() == Memento.Command.Create) {
                components.remove(target);
            } else if (memento.getCommand() == Memento.Command.Update) {
                target.restore(memento);
            } else if (memento.getCommand() == Memento.Command.Delete) {
                target.restore(memento);
                components.add(target);
            }
        }
    }

    public void notifyValueChanged() {
        for (ComponentListener listener : listeners) {
            listener.notifyValueChanged();
        }
    }

    public void notifySelectionChanged() {
        ComponentManager manager = ComponentManager.getInstance();
        List<Component> selectedComponents = manager.getSelectedComponents();
        for (ComponentListener listener : listeners) {
            listener.notifySelectionChanged(selectedComponents);
        }
    }

    public void addComponentListener(ComponentListener listener) {
        listeners.add(listener);
    }

    public void removeComponentListener(ComponentListener listener) {
        listeners.remove(listener);
    }

    public List<Component> deleteSelectedComponents() {
        List<Component> targets = getSelectedComponents();
        
        for (Component c : targets) {
            c.isSelect = false;
            components.remove(c);
        }

        return targets;
    }

    public List<Component> copySelectedComponents() {
        List<Component> result = new ArrayList<Component>();
        for (Component c : getSelectedComponents()) {
            result.add(c.copy());
        }
        return result;
    }

    public void pasteComponents(List<Component> copiedComponent) {
        for (Component c : copiedComponent ) {
            components.add(c.copy());
        }
    }

}
