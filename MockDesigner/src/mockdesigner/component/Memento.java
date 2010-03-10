package mockdesigner.component;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Manabu Shibata
 */
public class Memento {

    public static enum Command { Create, Update, Delete };


    private Component component;
    private Map<String, Object> properties;
    private Command command;

    public Memento(Command command){
        this.command = command;
    }
    
    public Memento(Component component, Command command) {
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

    public Command getCommand() {
        return command;
    }

}
