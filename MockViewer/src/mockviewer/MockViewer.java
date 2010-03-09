package mockviewer;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;

import com.docomostar.StarApplication;
import com.docomostar.ui.Canvas;
import com.docomostar.ui.Display;
import com.docomostar.ui.Graphics;

public class MockViewer extends StarApplication {

    ComponentManager cm = new ComponentManager();
	
    public void started(int arg0) {
	try {
	    init();
	} catch (Exception e) {
	    e.printStackTrace();
	    this.terminate();
	}
	Display.setCurrent(new MockCanvas());
    }

    private void init() throws IOException {
	InputStream in = null;
	try {
	    in = Connector.openInputStream("resource:///sample.xml");
	    cm.parseConfigurationFile(in);
	} finally {
	    if (in != null) {
		in.close();
	    }
	}
    }
	
    class MockCanvas extends Canvas {
	public void paint(Graphics g) {
	    cm.currentPage.render(g);
	}
    }
}
