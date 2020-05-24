package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application {

    private Pane root;

    private Player player, player2;
    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            update();
        }
    };

    private double t = 0;

    private void setRoot() {
        root = new Pane();
        Floor floor = new Floor(0, 400, 1200, 100, Color.rgb(0, 0, 0));
        setPlayers();
        root.setPrefSize(1200, 500);
//        root.getChildren().add(new AnimatedPerson());
        root.getChildren().add(floor);
        root.getChildren().add(player.getFloaty());
        root.getChildren().add(player2.getFloaty());
        root.getChildren().add(player);
        root.getChildren().add(player2);


    }

    private Parent createContent() {
        setRoot();

        Pane lower = new Pane();
        lower.setPrefSize(1200, 300);

        player.getStatusBox().setX(0);
        player2.getStatusBox().setTextAlignment(TextAlignment.RIGHT);
        lower.getChildren().add(player.getStatusBox());
        lower.getChildren().add(player2.getStatusBox());


        timer.start();

        return new VBox(root, lower);
    }

    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());

//        return root.getChildren().stream().filter(e -> e.getClass().getEnclosingClass() == (new Sprite(0, 0, 0, 0, null)).getClass().getEnclosingClass()).map(n -> (Sprite) n).collect(Collectors.toList());
    }

    private void update() {
        t += 0.016;
        root.getChildren().addAll(player.getBullets());
        root.getChildren().addAll(player2.getBullets());
        player.removeBullets();
        player2.removeBullets();
        player2.getStatusBox().setX(800);
//        double temp =
//        player2.getStatusBox().setX(player2.getStatusBox().getPref);
//        System.out.println(temp);


        sprites().forEach(sprite -> {
            switch (sprite.getClass().getSimpleName()) {
                case "Player":
                    sprites().stream().filter(e -> e.getClass().getSimpleName().equals("Floor")).forEach(floor -> {
                        if (sprite.getBoundsInParent().intersects(floor.getBoundsInParent())) {
                            ((Player) sprite).stopGravity();
                        } else {
                            ((Player) sprite).startGravity();
                        }
                    });
                    ((Player) sprite).move();
                    break;
                case "Bullet":
                    ((Bullet) sprite).bulletMove();
                    sprites().stream().filter(e -> e.getClass().getSimpleName().equals("Enemy")).forEach(enemy -> {
                        if (sprite.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            enemy.visible = false;
                            sprite.visible = false;
                        }
                    });
                    sprites().stream().filter(e -> e.getClass().getSimpleName().equals("Floor")).forEach(floor -> {
                        if (sprite.getBoundsInParent().intersects(floor.getBoundsInParent())) {
                            ((Bullet) sprite).stopMoving();
                        }
                    });
                    sprites().stream().filter(e -> e.getClass().getSimpleName().equals("Player")).forEach(player -> {
                        if (sprite.getBoundsInParent().intersects(player.getBoundsInParent()) && sprite.getSpriteId() != player.getSpriteId()) {
                            System.out.println("BulletSprite = " + sprite.getSpriteId() + "PlayerSprite = " + player.getSpriteId());
                            player.visible = false;
                        }
                    });
//                    sprites().stream().filter(e -> e.getSpriteId() == 2).forEach(player1 -> {
//                        if (sprite.getBoundsInParent().intersects(player1.getBoundsInParent()) && sprite.getSpriteId()!=2) {
//                            player1.visible=false;
//                        }
//                    });

                    break;
//                case "enemy":
//                    if (t > 2) {
//                        Math.random();
//                    }//                        shoot((Player) sprite);
//                    break;
                default:
                    break;
            }
        });

        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            return !s.visible;
        });
        if (t > 2) {
            t = 0;
        }
    }


    private void setPlayers() {
        player = new Player(100, 300, 30, 100, Color.rgb(100, 100, 100), 0, 1);
        player2 = new Player(900, 300, 30, 100, Color.rgb(100, 100, 100), 180, 2);
    }

    @Override
    public void start(Stage primaryStage) {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(createContent(), 1200, 800);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case D:
                    player.moveLeft();
                    break;
                case A:
                    player.moveRight();
                    break;
                case W:
                    player.moveUp();
                    break;
                case S:
                    player.moveDown();
                    break;
                case Z:
                    player.rotate(-1);
                    break;
                case X:
                    player.rotate(1);
                    break;
                case C:
                    System.out.println(Math.toRadians(player.getPlayerAngle()));
                    player.shoot(Math.toRadians(player.getPlayerAngle()), 10);
                    break;
                case J:
                    player2.moveLeft();
                    break;
                case L:
                    player2.moveRight();
                    break;
                case I:
                    player2.moveUp();
                    break;
                case K:
                    player2.moveDown();
                    break;
                case M:
                    player2.rotate(-1);
                    break;
                case COMMA:
                    player2.rotate(1);
                    break;
                case N:

                    System.out.println(player2.getPlayerAngle());
                    System.out.println(Math.toRadians(player2.getPlayerAngle()));
                    player2.shoot(Math.toRadians(player2.getPlayerAngle()), 10);
                    break;
                case SPACE:

                    try {
                        start(primaryStage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
