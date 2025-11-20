/**
 * Auto‑documented class ElevatorView.
 */
package esiee.front;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ElevatorView {
    private final Rectangle shape;

    public ElevatorView(double x, double y, double w, double h) {
        shape = new Rectangle(x, y, w, h);
        shape.setFill(Color.LIGHTSTEELBLUE);
        shape.setStroke(Color.BLACK);
    }

    public Rectangle getShape() { return shape; }

    public TranslateTransition moveToY(double y) {
        TranslateTransition move = new TranslateTransition(Duration.seconds(1), shape);
        move.setToY(y - shape.getY());
        move.play();
        return move;
    }
}
