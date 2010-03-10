package mockdesigner.tool;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import mockdesigner.MementoManager;
import mockdesigner.component.Line;
import mockdesigner.component.Memento.Command;

/**
 * @author Manabu Shibata
 */
public class LineTool extends DrawingTool {

    @Override
    public void drawCandidate(Graphics g) {
        if (isDragging)
            g.drawLine(dragStartPoint.x, dragStartPoint.y, currentPoint.x, currentPoint.y);
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
            Line line = createLine();
            line.isSelect = true;
            manager.addComponent(line);
            MementoManager.getInstance().push(line.createMemento(Command.Create));
            manager.notifyValueChanged();

            notifyComponentCreated(line);
        }
    }

    protected Line createLine() {
        Line line = new Line();
        line.x = dragStartPoint.x;
        line.y = dragStartPoint.y;
        line.width = currentPoint.x;
        line.height = currentPoint.y;
        line.z = currentZ++;
        line.borderColor = color1;
        return line;
    }
}
