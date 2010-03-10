package mockdesigner.tool;

import mockdesigner.component.Box;

/**
 * @author Manabu Shibata
 */
public class BoxTool extends BorderTool {

    @Override
    public Box createBox() {
        Box box = super.createBox();
        box.backgroundColor = color2;
        return box;
    }
}
