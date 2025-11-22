package esiee.front;

import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Represents a person in the tower simulation.
 * A person is defined by:
 * - a graphical circular shape
 * - a current floor
 * - a destination floor
 * - coordinates inside the scene
 *
 * This class also provides a movement animation method allowing
 * the person to move toward a given (x, y) position using a TranslateTransition.
 *
 * @see TowerView
 * @see ElevatorView
 */
public class PersonView {

    /** The graphical shape representing the person. */
    private final Circle shape;

    /** The current floor where the person is located. */
    private int floor;

    /** The destination floor that the person wants to reach. */
    private final int floorDest;
    private final int id;

    public PersonView(int id ,double x, double y, Color color, int floor, int floorDest) {
        this.id = id;
        this.floor = floor;
        this.floorDest = floorDest;
        shape = new Circle(x, y, 8, color);
    }

    /**
     * Returns the graphical shape of the person.
     *
     * @return the Circle shape
     */
    public Circle getShape() {
        return shape;
    }

    /**
     * Returns the current floor index.
     *
     * @return the floor where the person is located
     */
    public int getFloor() {
        return floor;
    }

    /**
     * Sets the current floor index. This does not move the graphical position.
     *
     * @param floor new floor value
     */
    public void setFloor(int floor) {
        this.floor = floor;  
    } 
    public int getId() { return id; }
    public int getFloorDest() { return floorDest; }
    public TranslateTransition moveTo(double x, double y) {
        TranslateTransition move = new TranslateTransition(Duration.seconds(0.1), shape);
        move.setToX(x - shape.getCenterX());
        move.setToY(y - shape.getCenterY());
        move.play();
        return move;
    }
}
