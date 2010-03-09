package mockdesigner.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author Manabu Shibata
 */
public class Line extends Component {

    @Override
    public String getComponentName() {
        return "line";
    }

    @Override
    public void paint(Graphics g) {
        Color orgColor = g.getColor();
        
        g.setColor(borderColor);
        g.drawLine(x, y, width, height);
        g.setColor(orgColor);
    }

    @Override
    public boolean isOn(Point p) {
        Point startPoint = new Point(x, y);
        Point endPoint = new Point(width, height);

        // 線分の始点から終点へのベクトル
        Point v = new Point(endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        // 線分の始点から円中心へのベクトル
        Point c = new Point(p.x - startPoint.x, p.y - startPoint.y);
        // 内積
        int n1 = dotProduct(v, c);
        if (n1 < 0) {
            // cの長さが円の半径より小さい場合は交差している
            return distance(startPoint, p) < RADIUS ? true : false;
        }

        int n2 = dotProduct(v, v);
        if (n1 > n2) {
            // 線分の終点と円の中心の距離の２乗を求める
            double len = distance(endPoint, p);
            len *= len;
            // 円の半径の２乗よりも小さい場合は交差している
            return len < RADIUS * RADIUS ? true : false;
        } else {
            int n3 = dotProduct(c, c);
            return (n3 - (n1 / (double)n2) * n1 < RADIUS * RADIUS) ? true : false;
        }
    }

    @Override
    public void startMove(Point clickPoint) {
        super.startMove(clickPoint);
        if (distance(clickPoint, new Point(x,y)) < RADIUS) {
            moveType = MoveType.START;
        } else if (distance(clickPoint, new Point(width, height)) < RADIUS) {
            moveType = MoveType.END;
        } else {
            moveType = MoveType.ALL;
        }
    }

    @Override
    public void move(Point dist) {
        if (moveType == MoveType.START) {
            x += dist.x;
            y += dist.y;
        } else if (moveType == MoveType.END) {
            width += dist.x;
            height += dist.y;
        } else {
            x += dist.x;
            y += dist.y;
            width += dist.x;
            height += dist.y;
        }
    }

}
