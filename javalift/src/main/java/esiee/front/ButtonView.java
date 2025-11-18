package esiee.front;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ButtonView {
    private final Rectangle shape;

    public ButtonView(double x, double y, double width, double height) {
        shape = new Rectangle(x, y, width, height);
        shape.setFill(Color.LIGHTGRAY);
        shape.setStroke(Color.BLACK);
    }

    public Rectangle getShape() { return shape; }

    public void setOnAction(javafx.event.EventHandler<javafx.scene.input.MouseEvent> handler) {
        shape.setOnMouseClicked(handler);
        System.out.println("Action set on button");
    }
}
