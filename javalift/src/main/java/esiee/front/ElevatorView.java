package esiee.front;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ElevatorView {
    private final Rectangle shape;
    private final double baseX;
    private int currentFloor = 0;
    public ElevatorView(double x, double y, double w, double h) {
        shape = new Rectangle(x, y, w, h);
        shape.setFill(Color.LIGHTSTEELBLUE);
        shape.setStroke(Color.BLACK);
        baseX = x;
    }

    public Rectangle getShape() { return shape; }

    public TranslateTransition moveToY(double y) {
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.1), shape);
        move.setToY(y - shape.getY());
        move.play();
        return move;
    }
    public int getCurrentFloor() {
        return currentFloor;
    }
    public void setCurrentFloor(int floor) {
        this.currentFloor = floor;
    }
    public double getBaseX() {
        return baseX;
    }
}
