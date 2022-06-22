package com.example.demo;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

import static javafx.application.Platform.exit;

public class HelloController {

    Random random = new Random();

    @FXML
    private ImageView diceImage;

    @FXML
    private Button Roll;

    @FXML
    private TextField textField;

    @FXML
    private ImageView board;

    @FXML
    private ImageView player1;

    @FXML
    private ImageView player2;

    @FXML
    private Label Final;

    @FXML
    private TextField T;

    @FXML
    private ImageView background;

    Turn turn ;

    @FXML
    public void roll() throws InterruptedException {

            int x = random.nextInt(6) + 1;
            Thread thread = new Thread(){
            public void run(){
            try{

            RotateTransition rotate = new RotateTransition();
            rotate.setNode(diceImage);
            rotate.setDuration(Duration.millis(1000));
            rotate.setByAngle(1080);
            rotate.play();
            File file = new File("src/main/Dice" + x + ".png");
            diceImage.setImage(new Image(file.toURI().toString()));}catch (Exception e){}}};
            thread.start();
            turn.playerTurn(x);
            turn.show();

            Thread t = new Thread(){
            public void run(){
            try {
                Thread.sleep(1000);
            turn.run();}catch (Exception e){}}};t.start();

            Thread T = new Thread(){
                public void run(){
                try{
                    Thread.sleep(1000);
                    turn.text();
                }catch (Exception e){}}
            };T.start();
    }

    public void initialize(){
        turn = new Turn(textField,Final,player1, player2,Roll);
        File fil = new File("src/main/Snake and Ladder.jpg");
        board.setImage(new Image(fil.toURI().toString()));
        File file = new File("src/main/Dice6.png");
        diceImage.setImage(new Image(file.toURI().toString()));
        File f1 = new File("src/main/Token1.png");
        player1.setImage(new Image(f1.toURI().toString()));
        File f2 = new File("src/main/Token2.png");
        player2.setImage(new Image(f2.toURI().toString()));
        File back = new File("src/main/Background.jpeg");
        background.setImage(new Image(back.toURI().toString()));
    }
}

class Turn {

    int Player_1 = 0;
    int old_P1;
    int Player_2 = 0;
    int old_P2;
    boolean start1 = false;
    boolean start2 = false;
    TextField player;
    ImageView I1;
    ImageView I2;
    Label End;
    Button button;

    int [] LadderStart = {4,13,33,42,50,62,74};
    int [] LadderEnd = {25,46,49,63,69,81,92};

    int [] SnakeHead = {40,27,43,54,66,76,89,99};
    int [] SnakeTail = {3,5,18,31,45,58,53,41};

    Turn(TextField player, Label End, ImageView I1, ImageView I2,Button button){
         this.player = player;
         this.I1 = I1;
         this.I2 = I2;
         this.End = End;
         this.button = button;
     }
    int playerNo = 1;


    public void playerTurn(int x){

        if(playerNo % 2 == 0){
            old_P1 = Player_1;
            if(x == 1){
                start2 = true;
            }
            if(start2 == true && (Player_2+x) <= 100){

                old_P2 = Player_2;
                Player_2+=x;
            }
        }

        else {
            old_P2 = Player_2;
            if(x == 1){
                start1 = true;
            }
            if(start1 == true && (Player_1+x) <= 100){
                old_P1 = Player_1;

                Player_1+=x;
            }
        }
        playerNo++;
    }

    public void show() throws InterruptedException {

        player.setText("Wait...");
        plot();
    }

    public  void plot(){

        int [][] Board = new int [10][10];
        int k = 0;
        for (int i = 100; i > 1 ; i = i - 10) {
            for (int j = 0; j < 10; j++) {

                if(i % 20 == 0){
                    Board[k][j] = i-j;
                }
                else {
                    Board[k][j] = i + (j-9);
                }
            }
            k++;
        }

        int [] c1 = {9,-1}; int [] d1 = new int [2];
        int [] c2 = {9,-1}; int [] d2 = new int [2];

        for (int i = 0; i < 10; i++) {

            for (int j = 0; j < 10; j++) {

                if (Board[i][j] == old_P1){
                    c1[0] = i; c1[1] = j;
                }

                if (Board[i][j] == old_P2){
                    c2[0] = i; c2[1] = j;
                }

                if (Board[i][j] == Player_1){
                    d1[0] = i; d1[1] = j;
                }

                if (Board[i][j] == Player_2){
                    d2[0] = i; d2[1] = j;
                }
            }
        }

        File f1 = new File("src/main/Token1.png");
        I1.setImage(new Image(f1.toURI().toString()));

        File f2 = new File("src/main/Token2.png");
        I2.setImage(new Image(f2.toURI().toString()));

        if(Player_1 != 0){
            TranslateTransition transition = new TranslateTransition();
            transition.setNode(I1);
            transition.setByY((d1[0]-c1[0])*60);
            transition.setByX((d1[1]-c1[1])*60);
            transition.play();
        }
        if(Player_2 != 0){
            TranslateTransition translate = new TranslateTransition();
            translate.setNode(I2);
            translate.setByY((d2[0]-c2[0])*60);
            translate.setByX((d2[1]-c2[1])*60);
            translate.play();
        }
        if(Player_1 == 100){
            End.setText("Player 1 wins");
            button.setDisable(true);
        }
        if(Player_2 == 100){
            End.setText("Player 2 wins");
            button.setDisable(true);
        }
    }

    public void run(){

        for (int i = 0; i < LadderStart.length; i++) {
            if(LadderStart[i] == Player_1){

                old_P1 = Player_1;
                Player_1 = LadderEnd[i];
                plot();
                break;
            }
            if(LadderStart[i] == Player_2) {
                old_P2 = Player_2;
                Player_2 = LadderEnd[i];
                plot();
                break;
            }
        }

        for (int i = 0; i < SnakeHead.length; i++) {
            if(SnakeHead[i] == Player_1){
                old_P1 = Player_1;
                Player_1 = SnakeTail[i];
                plot();
                break;
            }
            if(SnakeHead[i] == Player_2) {
                old_P2 = Player_2;
                Player_2 = SnakeTail[i];
                plot();
                break;
            }
        }
    }

    public void text(){

        if(playerNo % 2 == 0){
            player.setText("Player 2's Turn");
        }
        else {
            player.setText("Player 1's Turn");
        }
    }
}

