package mockdesigner.component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manabu Shibata
 */
public class MementoGroup {

    private List<Memento> mementos = new ArrayList<Memento>();
    
    public MementoGroup(Memento memento) {
        this.mementos.add(memento);
    }
    
    public MementoGroup(List<Memento> mementos) {
        this.mementos = mementos;
    }

    public void addMemento(Memento memento) {
        mementos.add(memento);
    }

    public List<Memento> getMementos() {
        return mementos;
    }

}
