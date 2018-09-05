import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.event.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;

/*
 * An application that creates and runs a Space Invaders game
 *
 * @author Ingrid Pedersen and Connor McRae
 */

public class Main extends Application {
    //public Rectangle testBu///llet=new Rectangle();
    public boolean ingridDowns = false;
    public boolean left = false;
    public boolean hit;
    public int horizontal;
    public Rectangle tank = new Rectangle(30, 30);
    public Group tankGroup = new Group();
    public Group alienGroup = new Group();
    public Group mainGroup = new Group();
    public BorderPane border = new BorderPane();
    public VBox vbox = new VBox();
    public Text score = new Text();
    public Text lives = new Text();
    public Text level = new Text();
    public int scoreNumber = 0;
    public int life = 3;
    public int levelNumber = 1;
    public double difficultySpeed = .5;

    /*
     * The primary method that begins the game. Creates the
     * tank and bullet, and calls the other methods to make the
     * aliens and sets them in a Keyframe to make them move.
     * @param Stage stage the primary stage where the game is set
     *
     */

    @Override
    public void start(Stage stage) throws Exception {

        makeInvaders(50);
        tank.setX(125);
        tank.setY(250);
        tank.setFill(Color.GREEN);
        tankGroup.getChildren().addAll(tank);
        score.setFill(Color.GREEN);
        lives.setFill(Color.GREEN);
        level.setFill(Color.GREEN);

        //Making the tank respond to keystrokes
        tankGroup.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                if(tank.getX() < 10.0)
                    tank.setX(tank.getX());
                else
                    tank.setX(tank.getX() - 10.0);
            }
            if (e.getCode() == KeyCode.RIGHT) {
                if(tank.getX() > 260) {
                    tank.setX(tank.getX());
                }
                else tank.setX(tank.getX() + 10.0);
            }
            if(e.getCode() == KeyCode.UP) {
                Rectangle bullet = makeBullet();
                EventHandler<ActionEvent> handler = event -> {

                    //Making a bullet and checking intersections
                    bullet.setY(bullet.getY()-10);
                    for(Node n : alienGroup.getChildren()) {
                        if (bullet.intersects(n.getBoundsInLocal()) && !bullet.getFill().equals(Color.BLACK)) {
                            scoreNumber+=200;
                            score.setText("Score: " + Integer.toString(scoreNumber));
                            bullet.setFill(Color.BLACK);
                            alienGroup.getChildren().remove(n);

                            //Instantiating a new game if all invaders are cleared
                            if(alienGroup.getChildren().isEmpty()) {
                                levelNumber++;
                                level.setText("Level: " + Integer.toString(levelNumber));
                                System.out.println("Trying");
                                difficultySpeed /= 2;
                                makeInvaders(50);
                            }
                        }
                    }
                };
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(.1), handler);
                Timeline timeline = new Timeline();
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }
        });

        //Making the aliens move
        EventHandler<ActionEvent> handler = event -> {

            if(ingridDowns) {
                //alienBullet();
                alienGroup.setLayoutY(alienGroup.getLayoutY() + 10);
                ingridDowns = !ingridDowns;

                //Lose condition if the aliens reach the bottom or all lives are lost
                if(alienGroup.getLayoutY() >= 150 || life == 0) {
                        Text failText = new Text("GAME OVER");
                        failText.setFill(Color.GREEN);
                        VBox failBox = new VBox();
                        failBox.getChildren().addAll(failText, score);
                        Scene scene = new Scene(failBox, 200, 200);
                        scene.setFill(Color.BLACK);
                        stage.setScene(scene);
                        stage.show();
                    }
                }


            if(left) moveLeft();

            else moveRight();
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(difficultySpeed), handler);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        //Setting the text for score, lives and level difficulty
        score.setText("Score: " + Integer.toString(scoreNumber));
        lives.setText("Lives: " + Integer.toString(life));
        level.setText("Level: " + Integer.toString(levelNumber));

        //Setting scene and stage
        vbox.getChildren().addAll(lives, score, level);
        border.setBottom(vbox);
        mainGroup.getChildren().addAll(border, tankGroup, alienGroup);
        Scene scene = new Scene(mainGroup, 300, 300);
        scene.setFill(Color.BLACK);
        stage.setTitle("Space Invaders");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
        tankGroup.requestFocus();
    }

    /*
     * A method to shift the aliens left during execution.
     * The appropriate checks for bounds are performed in the
     * associated eventHandler in the start method.
     *
     */
    public void moveLeft(){

        for(Node n : alienGroup.getChildren()) {

            alienGroup.setLayoutX(alienGroup.getLayoutX()-10);
            horizontal-=10;
            if (horizontal==-40) {
                left=false; ingridDowns=true;
            }
            mainGroup.getChildren().add(alienGroup);
        }
    }

    /*
     * A method to shift the aliens right during execution.
     * The appropriate checks for bounds are performed in the
     * associated eventHandler in the start method.
     *
     */
    public void moveRight() {

        for(Node n : alienGroup.getChildren()) {
            alienGroup.setLayoutX(alienGroup.getLayoutX()+10);
            horizontal+=10;
            if(horizontal==50) {
                left = true;
                ingridDowns = true;
            }
            mainGroup.getChildren().add(alienGroup);
        }
    }

    /*
     * A method that makes the aliens in a loop and adds
     * them to the alienGroup group.
     * @param spot This determines the starting X value of the aliens
     */
    public void makeInvaders(int spot) {

        alienGroup.setLayoutY(60);

        for(int i = 0; i < 10; i++) {
            for(int i2 = 0; i2 < 5; i2++) {
                Rectangle alien = new Rectangle(15, 15, Color.BLUE);

                if(i2 < 1) alien.setFill(Color.RED);

                else if(i2 <=2) alien.setFill(Color.BLUE);

                else alien.setFill(Color.GREEN);

                alien.setX(spot + i*20);
                alien.setY(i2*20);
                alienGroup.getChildren().add(alien);
            }
        }
    }

    /*
     * Makes a rectangle bullet and sets its X and Y coordinates
     * before adding it to the tankGroup group to be added to the
     * main Group. This method is only called in the eventHandler
     * if the up arrow key is pressed.
     * @returns the rectangle bullet
     */
    public Rectangle makeBullet() {

        Rectangle bullet = new Rectangle(7, 7, Color.GREEN);
        bullet.setX(tank.getX() + 12.5);
        bullet.setY(tank.getY());
        tankGroup.getChildren().add(bullet);
        return bullet;
    }

    public void alienBullet() {

        Rectangle bullet = new Rectangle(7, 7, Color.RED);
        bullet.setX(Math.random() * (180));
        bullet.setY(alienGroup.getLayoutY());
        alienGroup.getChildren().add(bullet);

        EventHandler<ActionEvent> eventHandler = event -> {

            Rectangle alienBullet = new Rectangle(7, 7, Color.RED);
            alienBullet.setY(alienBullet.getY() + 10);
            for (Node n : tankGroup.getChildren()) {
                if (alienBullet.intersects(n.getBoundsInParent())) {
                    tank.setFill(Color.YELLOW);
                    life--;
                }
            }
        };
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(2), eventHandler);
        Timeline timeline2 = new Timeline();
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.getKeyFrames().add(keyFrame2);
        timeline2.play();

    }

    /*
     * Launches the application
     */
    public static void main(String[] args) {

        Application.launch(args);
    }
}
