package mockdesigner.tool;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import mockdesigner.MementoManager;
import mockdesigner.component.Memento.Command;
import mockdesigner.component.Picture;

/**
 * @author Manabu Shibata
 */
public class PictureTool extends DrawingTool {

    @Override
    public void drawCandidate(Graphics g) {
        g.drawRect(currentPoint.x, currentPoint.y, 48, 48);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isLeftClick(e)) {
            Picture picture = createPicture();
            picture.isSelect = true;
            manager.addComponent(picture);
            MementoManager.getInstance().push(picture.createMemento(Command.Create));
            manager.notifyValueChanged();

            notifyComponentCreated(picture);
        }
    }

    private Picture createPicture() {
        Picture picture = new Picture();
        picture.x = currentPoint.x;
        picture.y = currentPoint.y;
        picture.z = currentZ++;
        return picture;
    }
}
