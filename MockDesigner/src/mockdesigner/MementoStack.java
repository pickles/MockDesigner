package mockdesigner;

import java.util.ArrayList;
import java.util.List;
import mockdesigner.component.MementoGroup;

/**
 * @author Manabu Shibata
 */
public class MementoStack {

    private int max = 10;
    private List<MementoGroup> stack;

    public MementoStack(int max) {
        this.max = max;
        stack = new ArrayList<MementoGroup>();
    }

    public void push(MementoGroup memento) {
        stack.add(memento);
        System.out.println("Memento add " + stack.size());
        if (stack.size() > max) {
            stack.remove(0);
            System.out.println("Memento max over, so delete.");
        }
    }

    public MementoGroup pop() {
        if (stack.size() == 0) return null;
        System.out.println("Memento pop " + stack.size());
        return stack.remove(stack.size()-1);
    }

    void clearAll() {
        stack.clear();
    }
}
