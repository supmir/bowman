package sample;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

class Sprite extends Rectangle {
    boolean visible = true;
    int id;


    Sprite(int x, int y, int w, int h, Color color) {
        super(w, h, color);
        setTranslateX(x);
        setTranslateY(y);
    }

    void setId(int id) {
        this.id = id;
    }

    int getSpriteId() {
        return this.id;
    }

    void moveDown() {
        setTranslateY(getTranslateY() + 5);

    }

    void moveLeft() {
        setTranslateX(getTranslateX() - 5);

    }

    void moveRight() {
        setTranslateX(getTranslateX() + 5);

    }

    void moveUp() {
        setTranslateY(getTranslateY() - 5);

    }


}

class Player extends Sprite {
    private int playerAngle;
    private double xDragStart, yDragStart, xDragEnd, yDragEnd;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private final ArrayList<Bullet> hiddenBullets = new ArrayList<>();
    private final Floaties floaty;
    private final Text status = new Text();
    private final Rotate rotate = new Rotate();
    private final double gravity = 9.8 * 0.005;
    private final double friction = 0.001;
    private double xVelocity, yVelocity;
    private boolean isTouchingFloor = false;


    Player(int x, int y, int w, int h, Color color, int playerAngle, int Id) {
        super(x, y, w, h, color);
        super.setId(Id);
        this.playerAngle = playerAngle;
        this.xVelocity = 0;
        this.yVelocity = 0;
//        System.out.println(this.getTranslateX() + this.getWidth());
//        this.floaty = new Floaties((int) (this.getTranslateX() ), (int) this.getTranslateY(), 0, 3, Color.DARKRED);
        this.floaty = new Floaties((int) (this.getTranslateX() + this.getWidth()), (int) this.getTranslateY());

        this.status.setFill(Color.BLACK);
        this.status.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        this.status.setTextOrigin(VPos.TOP);
        this.status.setY(0);

        super.setOnMousePressed(event -> {
            this.floaty.setColor(Color.DARKRED);

            this.floaty.setTranslateX(this.getTranslateX() + (this.getWidth() / 2));
            this.floaty.setTranslateY(this.getTranslateY() + (this.getHeight() / 2));

            System.out.println("Drag started");
            xDragStart = event.getX();
            yDragStart = event.getY();
        });
        super.setOnMouseDragged(event -> {


//            this.floaty.setTranslateX(getTranslateX() + (xDragStart - event.getX()));
//            this.floaty.setTranslateY(getTranslateY() + (yDragStart - event.getY()));

            this.floaty.setTranslateX(this.getTranslateX() + (this.getWidth() / 2));
            this.floaty.setTranslateY(this.getTranslateY() + (this.getHeight() / 2));


            double power = (
                    Math.sqrt(
                            Math.pow(event.getX() - xDragStart, 2) +
                                    Math.pow(event.getY() - yDragStart, 2)
                    )
            ) / 5;
            power = power > 20 ? 20 : power;

            this.floaty.setWidth((power * 5) + (getWidth() / 2));
            double angle = -Math.atan2((event.getY() - yDragStart), -(event.getX() - xDragStart));
            this.floaty.rotate(Math.toDegrees(angle));
            this.status.setText("Power : " + power + "\nAngle : " + -Math.toDegrees(angle));

        });
        super.setOnMouseReleased(event -> {

            System.out.println("Drag ended");
            xDragEnd = event.getX();
            yDragEnd = event.getY();
            double power = (Math.sqrt(Math.pow(xDragEnd - xDragStart, 2) + Math.pow(yDragEnd - yDragStart, 2))) / 10;
            power = power > 20 ? 20 : power;
            //                double angle = Math.atan((yDragEnd - yDragStart) / (xDragEnd - xDragStart));
            double angle = -Math.atan2((yDragEnd - yDragStart), -(xDragEnd - xDragStart));

            System.out.println("Power: " + power);
            System.out.println("Angle: " + angle);

            shoot(angle, power);
            this.floaty.setColor(Color.WHITE);
        });

    }

    void move() {
        xVelocity = xVelocity - (isTouchingFloor ? friction * 10 : friction) * xVelocity;
        if (isTouchingFloor) {

            setTranslateY(getTranslateY() - (yVelocity));
            yVelocity = 0;

        } else {
            yVelocity = yVelocity + gravity;
            setTranslateY(getTranslateY() + yVelocity);
        }


        setTranslateX(getTranslateX() + xVelocity);
    }


    void moveLeft() {
        xVelocity += 1;
        xVelocity = xVelocity > 3 ? 3 : xVelocity + 1;


    }

    void moveRight() {
        xVelocity = xVelocity < -3 ? -3 : xVelocity - 1;

    }

    void moveUp() {
        if (isTouchingFloor) {
            yVelocity += -3;
            setTranslateY(getTranslateY() + yVelocity);
        }
    }

    void shoot(double angle, double power) {
        for (Bullet bullet :
                hiddenBullets) {
            bullet.setColor(Color.BLACK);
        }
        Bullet s = new Bullet(
                (int) (this.getTranslateX()),
                (int) (this.getTranslateY() + (this.getHeight() / 2)),
                angle, power, this.id);
        bullets.add(s);
    }

    double getPlayerAngle() {
        return playerAngle;
    }

    List<Bullet> getBullets() {
        return bullets;
    }


    void rotate(int clockwise) {
        playerAngle += clockwise;
        rotate.setAngle(playerAngle);
        System.out.println(playerAngle);
        rotate.setPivotX(super.getX() + (super.getWidth() / 2));
        rotate.setPivotY(super.getY() + (super.getHeight() / 2));
        super.getTransforms().setAll(rotate);
//        super.getTransforms().addAll(rotate);

    }


    public void removeBullets() {
        this.hiddenBullets.addAll(bullets);
        this.bullets = new ArrayList<>();
    }

    Floaties getFloaty() {
        return floaty;
    }

    public void stopGravity() {
        isTouchingFloor = true;
        move();
    }

    public void startGravity() {
        isTouchingFloor = false;
        move();
    }

    public Text getStatusBox() {
        return this.status;
    }

}

class Bullet extends Sprite {
    private double xVelocity;
    private double yVelocity;
    private double gravity = 9.8 * 0.01;
    private final Rotate rotate = new Rotate();


    Bullet(int x, int y, double angle, double power, int Id) {
        super(x, y, 30, 5, Color.GREEN);
        super.setId(Id);
        xVelocity = power * Math.cos(angle);

        yVelocity = power * Math.sin(angle);
        System.out.println("Angle: " + angle);
        System.out.println("x: " + xVelocity + " y: " + yVelocity);

    }

    void bulletMove() {
        if (xVelocity == 0 && yVelocity == 0 && gravity == 0) {
            return;
        }

        rotate.setAngle(Math.toDegrees(Math.atan2(yVelocity, xVelocity)));

        rotate.setPivotX(super.getX() + (super.getWidth() / 2));
        rotate.setPivotY(super.getY() + (super.getHeight() / 2));
        super.getTransforms().setAll(rotate);

        yVelocity += gravity;
        setTranslateX(getTranslateX() + xVelocity);
        setTranslateY(getTranslateY() + yVelocity);
    }

    public void stopMoving() {
        xVelocity = 0;
        yVelocity = 0;
        gravity = 0;
    }


    public void setColor(Color color) {
        super.setFill(color);
    }
}

class Floaties extends Sprite {
    private final Rotate rotate = new Rotate();

    Floaties(int x, int y) {
        super(x, y, 0, 3, Color.DARKRED);

    }

    public void setColor(Color color) {
        super.setFill(color);
    }

    public void rotate(double angle) {

        rotate.setAngle(angle);
        super.getTransforms().setAll(rotate);


    }
}

class Floor extends Sprite {
    Floor(int x, int y, int w, int h, Color color) {
        super(x, y, w, h, color);

    }
}
