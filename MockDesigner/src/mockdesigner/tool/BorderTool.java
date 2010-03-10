package mockdesigner.tool;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import mockdesigner.MementoManager;
import mockdesigner.component.Box;
import mockdesigner.component.Line;
import mockdesigner.component.Memento.Command;

/**
 * @author Manabu Shibata
 */
public class BorderTool extends DrawingTool {

    @Override
    public void drawCandidate(Graphics g) {
        if (!isDragging) return;
        
        int cx, cy, cw, ch;
        if (currentPoint.x < dragStartPoint.x) {
            cx = currentPoint.x;
            cw = dragStartPoint.x - currentPoint.x;
        } else {
            cx = dragStartPoint.x;
            cw = currentPoint.x - dragStartPoint.x;
        }

        if (currentPoint.y < dragStartPoint.y) {
            cy = currentPoint.y;
            ch = dragStartPoint.y - currentPoint.y;
        } else {
            cy = dragStartPoint.y;
            ch = currentPoint.y - dragStartPoint.y;
        }

        g.drawRect(cx, cy, cw, ch);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isLeftClick(e)) {
            isDragging = true;
            dragStartPoint = new Point(e.getPoint());
        } else if (isRightClick(e) && isDragging) {
            isDragging = false;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isLeftClick(e) && isDragging) {
            Box box = createBox();
            box.isSelect = true;
            manager.addComponent(box);
            MementoManager.getInstance().push(box.createMemento(Command.Create));
            manager.notifyValueChanged();

            notifyComponentCreated(box);
        }
    }

    protected Box createBox() {
        Box box = new Box();
        if (currentPoint.x < dragStartPoint.x) {
            box.x = currentPoint.x;
            box.width = dragStartPoint.x - currentPoint.x;
        } else {
            box.x = dragStartPoint.x;
            box.width = currentPoint.x - dragStartPoint.x;
        }

        if (currentPoint.y < dragStartPoint.y) {
            box.y = currentPoint.y;
            box.height = dragStartPoint.y - currentPoint.y;
        } else {
            box.y = dragStartPoint.y;
            box.height = currentPoint.y - dragStartPoint.y;
        }

        box.z = currentZ++;

        box.borderColor = color1;
        box.backgroundColor = null;

        return box;
    }

}
