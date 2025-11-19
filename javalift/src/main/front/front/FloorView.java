/**
 * Auto‑documented class FloorView.
 */
package esiee.front;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FloorView {
    private final Rectangle shape;

    public FloorView(double x, double y, double width, double height) {
        shape = new Rectangle(x, y, width, height);
        shape.setFill(Color.DARKGRAY);
        shape.setStroke(Color.BLACK);
    }

    public Rectangle getShape() {
        return shape;
    }
}
