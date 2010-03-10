package mockdesigner.tool;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import mockdesigner.ComponentManager;
import mockdesigner.MementoManager;
import mockdesigner.component.Component;
import mockdesigner.component.Memento.Command;

/**
 * @author Manabu Shibata
 */
public class SelectTool extends Tool {
 
    private boolean isCtrlPressing = false;
    private boolean isSelectionMoving = false;
    private boolean cursorMoving = false;

    public void drawCandidate(Graphics g) {
        if (isSelectionMoving)
            g.drawOval(currentPoint.x - 4, currentPoint.y - 4, 8, 8);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (isLeftClick(e)) {
            select(e);
        } else {
            unselect();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isLeftClick(e)) {
            Component c = manager.getComponent(e.getPoint());
            if (c != null) {
                if (!c.isSelect && !isCtrlPressing) manager.unselectAll();
                c.isSelect = true;
                manager.notifySelectionChanged();
                
                isSelectionMoving = true;
                dragStartPoint = new Point(e.getPoint());
                MementoManager.getInstance().push(manager.getSelectedMementos(Command.Update));
                manager.selectionStartMove(dragStartPoint);
            }
        } else if (isRightClick(e) && isSelectionMoving) {
            // キャンセルされたので履歴を削除
            MementoManager.getInstance().pop();
            isSelectionMoving = false;
            manager.selectionCancelMove();
            manager.notifyValueChanged();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentPoint = new Point(e.getPoint());
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        Point prevPoint = new Point(currentPoint);
        currentPoint = new Point(e.getPoint());
        
        if (isSelectionMoving) {
            Point dist = new Point(currentPoint.x - prevPoint.x, currentPoint.y - prevPoint.y);
            manager.moveSelections(dist);
            manager.notifyValueChanged();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentPoint = new Point(e.getPoint());
        if (isSelectionMoving) {
            if (isNotMoveFromDragStartPoint(currentPoint))
                MementoManager.getInstance().pop();
            isSelectionMoving = false;
            manager.selectionEndMove();
            manager.notifyValueChanged();
        } else {
            if (isLeftClick(e)) {
                select(e);
            } else {
                unselect();
            }
        }
    }

    private boolean isNotMoveFromDragStartPoint(Point p) {
        return p.x == dragStartPoint.x && p.y == dragStartPoint.y;
    }

    private void select(MouseEvent e) {
        Component c = manager.getComponent(e.getPoint());
        if (c != null) {
            if (isCtrlPressing) {
                c.flipSelection();
            } else {
                manager.unselectAll();
                c.isSelect = true;
            }
        } else {
            manager.unselectAll();
        }
        manager.notifySelectionChanged();
    }

    private void unselect() {
        manager.unselectAll();
        manager.notifySelectionChanged();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_CONTROL) {
            isCtrlPressing = true;
        } else if (isCursorKey(e)) {
            moveByCursorKey(e);
        }
    }

    private void moveByCursorKey(KeyEvent e) {
        int code = e.getKeyCode();
        if (!cursorMoving) {
            cursorMoving = true;
            MementoManager.getInstance().push(manager.getSelectedMementos(Command.Update));
        }
        
        switch (code) {
            case KeyEvent.VK_UP:
                manager.moveSelections(new Point(0, -1));
                break;
            case KeyEvent.VK_DOWN:
                manager.moveSelections(new Point(0, 1));
                break;
            case KeyEvent.VK_LEFT:
                manager.moveSelections(new Point(-1, 0));
                break;
            case KeyEvent.VK_RIGHT:
                manager.moveSelections(new Point(1, 0));
                break;
        }
        manager.notifyValueChanged();
    }

    private boolean isCursorKey(KeyEvent e) {
        int code = e.getKeyCode();

        return code == KeyEvent.VK_UP ||
               code == KeyEvent.VK_DOWN ||
               code == KeyEvent.VK_LEFT ||
               code == KeyEvent.VK_RIGHT;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            isCtrlPressing = false;
        } else if (isCursorKey(e)) {
            cursorMoving = false;
        }
    }

}
