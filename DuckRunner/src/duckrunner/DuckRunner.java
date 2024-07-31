
package duckrunner;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

public class DuckRunner extends PApplet {
    PImage duck;
    PImage background;
    PImage obstacle;

    int duckX = 70;
    int duckY = 150;

    int bgX = 0;
    int bgY = 0;

    int obstacleX;
    int obstacleY = 330;

    float speed = 8;
    int jumpHeight;

    int startTime;
    int timer;

    float speedIncrease = 3f;
    float rateOfDifficulty = 5;
    int difficultyTime = 0;
    int score = 0;
    int highScore = 0;
    Random thing = new Random();


    enum GameState {
        GAMEOVER, RUNNING
    }

    static GameState currentState;

    public static void main(String[] args) {
        PApplet.main("duckrunner.DuckRunner");

    }

    public void settings() {
        size(700, 400);
    }
    public void increaseDifficulty() {
        if (timer % rateOfDifficulty == 0 && difficultyTime != timer) {
            speed += speedIncrease;
            difficultyTime = timer;
        }

    }

    public void setup() {
        duck = loadImage("Images/dragon.png");
        background = loadImage("Images/Sky.png");
        obstacle = loadImage("Images/Hydrant.png");
        obstacle.resize(50, 95);
        startTime = millis();
        currentState = GameState.RUNNING;
    }

    public void drawScore() {
        fill(255, 255, 255);
        textAlign(CENTER);
        text("Score: " + timer, width - 70, 30);

    }
    public void draw() {
        switch (currentState) {
            case RUNNING:
                drawBackground();
                drawDuck();
                createObstacles();
                timer = (millis() - startTime) / 1000;
                drawScore();
                increaseDifficulty();
                break;
            case GAMEOVER:
                drawGameOver();
                break;
        }
    }

    public void drawDuck() {
        imageMode(CENTER);
        image(duck, duckX, duckY);
        if (duckY <= 300) {
            jumpHeight += 1;
            duckY += jumpHeight;
        }
    }

    public void drawBackground() {
        imageMode(CORNER);
        image(background, bgX, bgY);
        image(background, bgX + background.width, 0);
        bgX -= speed;
        if (bgX <= (background.width * -1)) {
            bgX = 0;
        }
    }
    public void mousePressed() {
        if (currentState == GameState.RUNNING) {
            if (duckY >= 0) {
                jumpHeight = -15;
                duckY += jumpHeight;
            }
        }
        if (currentState == GameState.GAMEOVER) {
            obstacleX = 0;
            bgX = 0;
            startTime = millis();
            speed = 8;
            currentState = GameState.RUNNING;
        }

    }
    public void drawGameOver() {
        fill(255, 190, 190);
        noStroke();
        rect(width / 2 - 125, height / 2 - 80, 250, 160);
        fill(255, 100, 100);
        textAlign(CENTER);
        text("Game Over!", width / 2, height / 2 -50);
        text("Your Score " + score, width / 2, height / 2 - 30);
        text("High Score " + highScore, width / 2, height / 2 - 10);
    }
    public void createObstacles() {
        imageMode(CENTER);
        image(obstacle, obstacleX, obstacleY);
        obstacleX -= speed;
        if(obstacleX < 0) {
            obstacleX = width;
            obstacleY = thing.nextInt(330);

        }
        if((abs(duckX-obstacleX) < 40 && abs(duckY-obstacleY)< 80 )) {
            score = timer;
            if(score > highScore) {
                highScore = score;
            }
            currentState = GameState.GAMEOVER;
        }
    }
}
