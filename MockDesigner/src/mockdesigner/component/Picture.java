package mockdesigner.component;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import javax.imageio.ImageIO;
import mockdesigner.anotation.Property;

/**
 * @author Manabu Shibata
 */
public class Picture extends Component {

    // GIF / JPG / BMP
    @Property("ImagePath")
    public String imagePath;
    
    private static Image defaultImage;

    static {
        try {
            defaultImage = ImageIO.read(Picture.class.getClassLoader().getResourceAsStream("default.gif"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Picture() {
        width = defaultImage.getWidth(null);
        height = defaultImage.getHeight(null);
    }

    private Image image = null;
    
    @Override
    public void paint(Graphics g) {
        if (image != null)
            g.drawImage(image, x, y, null);
        else
            g.drawImage(defaultImage, x, y, null);
    }

    @Override
    public boolean isOn(Point p) {
        if (x <= p.x && p.x <= x + width && y <= p.y && p.y <= y + height)
            return true;

        return false;
    }

    @Override
    public void afterBuild() {
        updateImage();
    }

    @Override
    public void updateProperty(String name, String value) {
        super.updateProperty(name, value);
        updateImage();
    }

    protected void updateImage() {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                image = ImageIO.read(new File(imagePath));
                width = image.getWidth(null);
                height = image.getHeight(null);
            } catch (Exception e) {
                e.printStackTrace();
                imagePath = "";
            }
        }
    }
    
    @Override
    public void move(Point dist) {
        x += dist.x;
        y += dist.y;
    }

    @Override
    public String getComponentName() {
        return "image";
    }

}
