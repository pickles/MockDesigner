package mockdesigner.tool;

import mockdesigner.component.Component;

/**
 * @author Manabu Shibata
 */
public interface DrawingListener {

    public void componentCreated(Component component);

    public void shouldRepaint();
    
}
