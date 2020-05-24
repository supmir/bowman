package sample;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class AnimatedPerson extends Group {
    AnimatedPerson(){
        super();
        Rectangle body = new Rectangle(3,80, Color.BLACK);
        this.getChildren().add(body);
    }
}
