package mockdesigner;

import java.util.List;
import mockdesigner.component.Component;
import mockdesigner.component.Memento;
import mockdesigner.component.MementoGroup;

/**
 * @author Manabu Shibata
 */
public class MementoManager {

    private static MementoManager theInstance = new MementoManager();

    MementoStack mementoStack = new MementoStack(10);
    protected MementoManager() {}

    public static MementoManager getInstance() {
        return theInstance;
    }

    public void push(Memento memento) {
        mementoStack.push(new MementoGroup(memento));
    }

    public MementoGroup pop() {
        return mementoStack.pop();
    }

    public void undo() {
        MementoGroup mementos = MementoManager.getInstance().pop();
        ComponentManager manager = ComponentManager.getInstance();
        
        if (mementos == null) {
            return;
        }
        for (Memento memento : mementos.getMementos()) {
            Component component = memento.getComponent();

            if (memento.getCommand() == Memento.Command.Delete) {
                component.restore(memento);
                manager.addComponent(component);
            } else if (memento.getCommand() == Memento.Command.Update) {
                component.restore(memento);
            } else if (memento.getCommand() == Memento.Command.Create) {
                manager.removeComponent(component);
            }
        }
        manager.notifyValueChanged();
    }

    public void push(List<Memento> mementos) {
        mementoStack.push(new MementoGroup(mementos));
    }

    public void clearAll() {
        mementoStack.clearAll();
    }

}
