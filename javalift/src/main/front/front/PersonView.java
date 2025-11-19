/**
 * Auto‑documented class PersonView.
 */
package esiee.front;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class PersonView {
    private final Circle shape;
    private int floor;
    private final int floorDest;

    public PersonView(double x, double y, Color color, int floor, int floorDest) {
        this.floor = floor;
        this.floorDest = floorDest;
        shape = new Circle(x, y, 8, color);
    }

    public Circle getShape() { return shape; }
    public int getFloor() { return floor; }
    public void setFloor(int floor) {
        this.floor = floor;  
    } 
        
    public int getFloorDest() { return floorDest; }
    public TranslateTransition moveTo(double x, double y) {
        TranslateTransition move = new TranslateTransition(Duration.seconds(1), shape);
        move.setToX(x - shape.getCenterX());
        move.setToY(y - shape.getCenterY());
        move.play();
        return move;
    }
}
