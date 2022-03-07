package com.example.pathfinder;
import javafx.scene.control.Button;

public class infoButton {

    //The current button
    private Button b = new Button();
    //Location of current button
    private int x,y = 0;
    //Previous button in sequence
    infoButton previous = null;
    //Current button obstacle status
    private boolean isObstacle = false;

    //Main constructor
    infoButton(Button b, int x, int y, infoButton previous, boolean isObstacle){
        this.b = b;
        this.x = x;
        this.y = y;
        this.previous = previous;
        this.isObstacle = isObstacle;
    }

    //Set the previous button when we move forward in the pathfinding algorithm, this can be traced back later
    public void addPrevious(infoButton previous){
       this.previous = previous;
    }
    //Set the obstacle status
    public void setObstacle(boolean isObstacle){
        this.isObstacle = isObstacle;
    }
    //Get the previous button
    public infoButton getPrevious(){
        return this.previous;
    }
    //Get the x coordinate
    public int getX(){
        return x;
    }
    //Get the y corrdinate
    public int getY(){
        return y;
    }
    //Get the current button
    public Button getButton(){
        return b;
    }
    //Get the obstacle status of the current button
    public boolean getObstacle(){
        return isObstacle;
    }
}
