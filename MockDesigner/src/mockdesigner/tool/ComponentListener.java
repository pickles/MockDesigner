package mockdesigner.tool;

import java.util.List;
import mockdesigner.component.Component;

/**
 * @author Manabu Shibata
 */
public interface ComponentListener {

    public void notifyValueChanged();

    public void notifySelectionChanged(List<Component> components);
    
}
