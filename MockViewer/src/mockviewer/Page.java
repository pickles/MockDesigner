package mockviewer;

import java.util.Vector;

import com.docomostar.ui.Graphics;

import mockviewer.component.Component;

public class Page {

    Vector components = new Vector();
	
    public void add(Component component) {
	components.addElement(component);
    }
	
    public void render(Graphics g) {
	for (int i = 0; i < components.size(); i++) {
	    Component component = (Component) components.elementAt(i);
	    component.paint(g);
	}
    }
}
