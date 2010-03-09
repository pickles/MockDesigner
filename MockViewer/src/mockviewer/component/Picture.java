package mockviewer.component;

import com.docomostar.media.ImageReference;
import com.docomostar.ui.Graphics;

public class Picture extends Component {

    public String imagePath;

    public ImageReference image;

    public void init() {
	if(imagePath.equalsIgnoreCase("")) imagePath = "default.gif";
	int index = imagePath.lastIndexOf('\\');
	System.out.println(imagePath + ":" + index);
	String filename = imagePath;
	if (index >= 0)
	    filename = imagePath.substring(index+1);
	System.out.println(filename);
	image = ImageReference.createImageReference("resource:///" + filename);
    }

    public void paint(Graphics g) {
	g.drawImageReference(image, x, y, true);
    }

}
