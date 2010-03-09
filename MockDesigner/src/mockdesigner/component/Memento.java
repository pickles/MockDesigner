package mockdesigner.component;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Manabu Shibata
 */
public class Memento {

    private Component component;
    private Map<String, Object> properties;
    private String command;

    public Memento(Component component, String command) {
        this.command = command;
        this.component = component;
        properties = new TreeMap<String, Object>();
    }

    public void addProperty(String name, Object value) {
        properties.put(name, value);
    }

    public Component getComponent() {
        return component;
    }
    
    public Component getRestoredComponent() {
        component.restore(this);
        return component;
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getCommand() {
        return command;
    }

}
