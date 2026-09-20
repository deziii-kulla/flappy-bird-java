/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;

public class Flappybird extends Application { 
    
        int boardwidth=500;
        int boardheight=640;
        
        
        double velocityy=0;
        double gravity=0.35;
         boolean gameOver=false;
         boolean gameStarted=false;
         boolean paused=false;
         
        int bestscore=0;
        int score=0;
        int lastbestscore=0;
        Label scorel=new Label("Score: 0");
         Label pausel=new Label("Paused");
       
        //bird 
        ImageView bird;
        //pipes
        ArrayList<ImageView>pipes=new ArrayList<>();
        Pane gamepane=new Pane();
        GridPane startMenu=new GridPane();
        
        GridPane gameoverMenu= new GridPane();
        
        
         @Override
                 public void start(Stage stage){  
                     
                     ImageView bg=new ImageView();
                     
                     try{
                     bg.setImage(new Image(getClass().getResource("/javaproject/images/background.png").toExternalForm()));
                     }catch(Exception e){
                         System.out.println("Background image not found!!!");
                     }
                     bg.setFitWidth(boardwidth);
                     bg.setFitHeight(boardheight);
                        
                     bird=new ImageView();
                     try{
                         bird.setImage(new Image(getClass().getResource("/javaproject/images/bird.png").toExternalForm()));
                     }catch(Exception e){
                         System.out.println("Bird image not found!!!");
                     }
                    
                       bird.setFitWidth(42);
                     bird.setFitHeight(32);
                     bird.setX(100);  
                     bird.setY(boardheight/2.0); //vertically center the bird
                     
                     scorel.setStyle("-fx-font-size:30px; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding:6 14 6 14;");
                     
                     scorel.setLayoutX(20);
                     scorel.setLayoutY(20);
                     scorel.setVisible(false);
                     
                      pausel.setStyle("-fx-font-size:30px; -fx-text-fill: white;-fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.6);-fx-padding:15,-fx-background-radius:12;");
                      pausel.setLayoutX(boardwidth/2.0-70);
                    pausel.setLayoutY(boardheight/2.0-100);
                     pausel.setVisible(false);
                     
                     Button pausebt=new Button("Pause");
                     pausebt.setLayoutX(boardwidth-90); //top right
                     pausebt.setLayoutY(20);
                     
                     pausebt.setOnAction(e->{
                         if(!gameStarted || gameOver)return;
                         paused=!paused;
                         pausel.setVisible(paused);
                         pausebt.setText(paused?"Resume":"Pause");
                     });
                     gamepane.getChildren().addAll(bg,bird,scorel,pausel);
                     startMenu.setAlignment(Pos.CENTER);
                     startMenu.setHgap(10);
                     startMenu.setVgap(15);
                       startMenu.setStyle("-fx-background-color: rgba(0,0,0,0.6);-fx-padding:30; -fx-background-radius:15;");
                     Label title=new Label("Flappy bird");
                     title.setStyle("-fx-font-size:38px; -fx-text-fill: white; -fx-font-weight: bold;");
                     
                     Button startbt=new Button("Start");
                     startbt.setStyle("=fx-font-size:30px; -fx-padding: 10 25 10 25; -fx-font-weight: bold; -fx-text-fill: white; ");
                      startbt.setOnAction(e->{ gamepane.getChildren().remove(startMenu);
                      gameStarted=true;
                      scorel.setVisible(true);
                      });
                        startMenu.add(title,0,0);
                        startMenu.add(startbt,0,1);
                        GridPane.setHalignment(title,HPos.CENTER);
                        GridPane.setHalignment(startbt,HPos.CENTER); 
                         startMenu.setLayoutX((boardwidth-250)/2);
                         startMenu.setLayoutY((boardheight-150)/2);
                       
                        gamepane.getChildren().add(startMenu);
                  
                      
                     Scene scene=new Scene(gamepane,boardwidth,boardheight);
                     scene.setOnKeyPressed(e ->{
                         if (e.getCode()==KeyCode.SPACE && !gameOver&&gameStarted&&!paused){
                         velocityy=-6; // the bird jumps upward
                     }
                     });
                     stage.setScene(scene);
                     stage.setTitle("FLAPPY BIRD");
                     stage.show();    //display window
                     
                     AnimationTimer timer=new AnimationTimer(){  //runs 60 times per second; keeps the game alive and updated
                         long lastpipe=0;  //store the time when the lastpipe was created
                         
                         @Override
                         public void handle(long now){  //called every frame
                             if (!gameStarted||gameOver||paused)return;
                             
                             velocityy +=gravity;
                             bird.setY(bird.getY()+velocityy);
                              if (now-lastpipe > 1_500_000_000){
                                  addPipes();
                                  lastpipe=now; //reset timer for next pipes
                              }
                              
                              Iterator<ImageView>it=pipes.iterator(); //iterator to loop througth the pipes
                              while(it.hasNext()){ //
                                  ImageView p=it.next(); //current
                                  p.setX(p.getX()-3); //left by 3px,scrolling effect
                                  
                                  if (bird .getBoundsInParent().intersects(p.getBoundsInParent())){
                                      endGame();
                                  }
                                  
                                  if (p.getX()+80<0){
                                      it.remove();
                                      gamepane.getChildren().remove(p);//screen
                                      score++;
                                      scorel.setText("Score: "+score);
                                  }
                              }
                              if (bird.getY()>boardheight){
                                  endGame();
                              }
                         }
                     };
                     timer.start(); //starts the ganme loop
                 
                 }
                 void addPipes(){         
                     
                     int gap=160; 
                     try{
                     int topheight=(int)(Math.random()*200)+80;
                     
                     if (topheight<=0 || topheight>=boardheight-gap){
                         throw new IllegalArgumentException("Invalid pipe height: "+topheight);
                     }
                      ImageView toppipe=new ImageView();
                     try{
                         toppipe.setImage(new Image(getClass().getResource("/javaproject/images/toppipe.png").toExternalForm()));
                     }catch(Exception e){
                         System.out.println("Top pipe image not found!!!");
                     }
                   
                     toppipe.setFitWidth(80);
                     toppipe.setFitHeight(topheight);
                     toppipe.setX(boardwidth);
                     toppipe.setY(0);
                     
                     ImageView bottompipe=new ImageView();
                     try{
                         bottompipe.setImage(new Image(getClass().getResource("/javaproject/images/bottompipe.jfif").toExternalForm()));
                     }catch(Exception e){
                         System.out.println("Bottom pipe image not found!!!");
                     }
                
                     bottompipe.setFitWidth(80);
                     bottompipe.setFitHeight(boardheight-topheight-gap);
                     bottompipe.setX(boardwidth);
                     bottompipe.setY(topheight+gap);
                     
                     pipes.add(toppipe);
                     pipes.add(bottompipe);
                     gamepane.getChildren().addAll(toppipe,bottompipe);
                 
                 }catch(IllegalArgumentException e){
                     System.out.println("Error in creating pipes: "+e.getMessage());
                 }
                 }
                 
                 void endGame(){ 
                     gameOver=true;
                     paused=false;
                     
                     int previousbest=bestscore;
                     boolean newRecord=false;
                     if(score>bestscore){
                         bestscore=score;
                         newRecord=true;
                     }
                     gameoverMenu.getChildren().clear();
                     gameoverMenu.setPrefWidth(400);
                     gameoverMenu.setPrefHeight(300);
                      gameoverMenu.setHgap(10);
                      gameoverMenu.setVgap(10);
                      gameoverMenu.setAlignment(Pos.CENTER);
                   
                     gameoverMenu.setStyle("-fx-background-color:rgba(0,0,0,0.7);-fx-padding:30;-fx-background-radius:15;");
                     Label over=new Label("GAME OVER");
                     over.setStyle("-fx-font-size: 28px; -fx-text-fill:white; -fx-font-weight: bold;-fx-padding:30;");
                     
                     Label scoretext=new Label("SCORE: "+ score);
                     scoretext.setStyle("-fx-font-size:24px ; -fx-text-fill:white;");
                     
                     Label lastrecord=new Label("LAST BEST RECORD: "+ previousbest);
                     lastrecord.setStyle("-fx-font-size:24px ; -fx-text-fill:gold;"); 
                     
                     gameoverMenu.add(over,0,0);
                     gameoverMenu.add(scoretext,0,1);
                     gameoverMenu.add(lastrecord,0,2);
                     GridPane.setHalignment(scoretext,HPos.CENTER);
                     GridPane.setHalignment(lastrecord,HPos.CENTER);
                     
                     
                     if(newRecord){
                         Label newrecord=new Label("NEW RECORD: "+ score);
                         newrecord.setStyle("-fx-font-size:24px; -fx-text-fill:green;-fx-font-weight:bold;");
                           gameoverMenu.add(newrecord,0,3);
                           GridPane.setHalignment(newrecord,HPos.CENTER);
                     }
                     
                     
                     Button retry=new Button("Retry");
                     retry.setStyle("-fx-font-size:24px; -fx-padding: 8 20;");
                     
                     Button exit=new Button("Exit");
                     exit.setStyle("-fx-font-size:24px; -fx-padding: 8 20;");
                     
                     retry.setOnAction(e->restart());
                     exit.setOnAction(e->System.exit(0));
                     gameoverMenu.add(retry,0,4);
                     gameoverMenu.add(exit,0,5);
                      GridPane.setHalignment(retry,HPos.CENTER);
                     GridPane.setHalignment(exit,HPos.CENTER);
                     
                      gameoverMenu.setLayoutX((boardwidth-400)/2);
                      gameoverMenu.setLayoutY((boardheight-300)/2);
                      
                          gamepane.getChildren().add(gameoverMenu);
                      }
                 


                 
                 void restart(){ 
                     try{
                     gamepane.getChildren().remove(gameoverMenu);
                     pipes.forEach(p->gamepane.getChildren().remove(p));
                     pipes.clear();
                     
                     bird.setY(boardheight/2.0);
                     velocityy=0;
                     score=0;
                     scorel.setText("Score: 0");
                     gameOver=false; 
                     gameStarted=true;
                     
                 }catch(Exception e){
                     System.out.println("Error while restaring the game"+ e.getMessage());
                 }
                 }
                 
                 public static void main(String[]args){
                     launch(args);
 
        
    }
}
