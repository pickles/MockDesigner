package mockdesigner.tool;

import mockdesigner.component.Box;

/**
 * @author Manabu Shibata
 */
public class FillTool extends BorderTool {

    @Override
    public Box createBox() {
        Box box = super.createBox();
        box.borderColor = null;
        box.backgroundColor = color2;
        return box;
    }
}
