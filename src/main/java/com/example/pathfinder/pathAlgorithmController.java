package com.example.pathfinder;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.event.*;
import java.net.URL;
import java.util.Random;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class pathAlgorithmController implements Initializable {

    //Scene builder variables
    public Label infoText;
    public GridPane gridPane;

    //Private storage variables
    //2D array of info buttons, represents the grid users see on screen
    private infoButton buttonArray[][] = new infoButton[10][10];
    //Start and finish buttons
    private infoButton start;
    private infoButton finish;
    //Is the pathfinding complete?
    private boolean done;
    //Storage of obstacles
    private ArrayList<infoButton> obstacles = new ArrayList<infoButton>();

    //Load UI on startup
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshMap();
    }

    //Load UI on repeated home screen loads
    public void OnStart() {
        refreshMap();
    }

    //Main pathfinding algorithm
    public void pathFind(){
        //Repaint all buttons except start and finish
        resetButtons();
        //Set the status of pathfinding to incomplete
        done = false;
        //Set up arraylists for the queue of buttons to be checked, and a list of chosen buttons, which will later become the main path
        ArrayList<infoButton> queue = new ArrayList<infoButton>();
        ArrayList<infoButton> chosen = new ArrayList<infoButton>();
        //Add the start button to the chosen array
        chosen.add(start);
        //Queue the start button for pathfinding
        queue.add(start);
        //Loop through the queue list
        for (int m = 0; m < queue.size(); m++) {
            //Loop through the surrounding tiles, up, down, left, and right
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    //Make sure the program does not check sideways movement options
                    if ((j == 0 && i != 0) || (j != 0 && i == 0)) {
                        //Use a try loop to make sure the buttons checked are not null, AKA off the edge of the map
                        try {
                            //Create a new temporary infoButton for reference later
                            infoButton temp = buttonArray[queue.get(m).getX() + j][queue.get(m).getY() + i];
                            //Check to make sure the button is not chosen, and it is not an obstacle
                            if (!chosen(chosen, temp) && !temp.getObstacle()) {
                                //Add the previous button
                                temp.addPrevious(queue.get(m));
                                //Add the button to the queue
                                queue.add(temp);
                                //Add the button to the chosen array
                                chosen.add(buttonArray[queue.get(m).getX() + j][queue.get(m).getY() + i]);
                            }
                            //If the current button is the finish, stop the program and display the path
                            if (temp == finish) {
                                //Set the completion status to complete
                                done = true;
                                //Loop through each previous button and change the color to clearly display the path
                                while (temp.getPrevious() != null) {
                                    temp.getPrevious().getButton().setStyle("-fx-background-color: #0000FF");
                                    temp = temp.getPrevious();
                                }
                                //Update UI and exit pathfinding
                                infoText.setText("Path found!");
                                return;
                            }
                        } catch (IndexOutOfBoundsException e) {
                        }
                    }
                }
            }
        }
        //Check to see if a path has been found, if not, we know that no path exists from start to finish, due to obstacles
        if (!done){
            infoText.setText("No possible paths exist.");
            done = false;
        }
    }

    //Loop through a given list to see if a button has already been chosen in pathfinding
    public boolean chosen(ArrayList<infoButton> list, infoButton b){
        boolean chosen = false;
        for (infoButton but : list){
            if (but.getButton() == b.getButton()){
                chosen = true;
            }
        }
        return chosen;
    }

    //When we press any grid button, what happens?
    public void onButtonPress (ActionEvent event){
        //Retrieve the button that was pressed
        Button b = (Button)event.getSource();
        //Temp variables for location
        int x = 0;
        int y = 0;
        //Locate the pressed button in our buttonArray
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                if (buttonArray[j][i].getButton() == b){
                    x = j;
                    y = i;
                }
            }
        }
        //Setting obstacles on button press
        if (b != start.getButton() && b != finish.getButton()) {
            //Check to make sure the pressed button is not already an obstacle, if so we reset it
            for (infoButton but : obstacles) {
                //Removing an obstacle if we re-click it
                if (but.getButton() == b) {
                    obstacles.remove(but);
                    but.getButton().setStyle("-fx-background-color:");
                    return;
                }
            }
            //Set the current button to an obstacle, and add it to the array, updating its obstacle status
            b.setStyle("-fx-background-color: #000000");
            obstacles.add(buttonArray[x][y]);
            buttonArray[x][y].setObstacle(true);
        }
    }

    //Recolor all map tiles except the start and finish buttons
    public void resetButtons(){
        //Loop through the rows
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            //Loop through the columns
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                //Check to make sure we don't undo obstacles
                if (!buttonArray[i][j].getObstacle()) {
                    //Set each button to an invalid color because I can't find the default one :)
                    buttonArray[i][j].getButton().setStyle("-fx-background-color: #0000000");
                }
            }
        }
        //Recolor the start and finish buttons
        start.getButton().setStyle("-fx-background-color: #00ff00");
        finish.getButton().setStyle("-fx-background-color: #FF0000");
    }

    //Reset all map tiles
    public void refreshMap(){
        Random r = new Random();
        //Reset all UI elements
        infoText.setText("Click any square to create obstacles, then click pathfind to begin!");
        //Clear the grid
        gridPane.getChildren().clear();
        //Current button number storage since we loop through a 2D number system
        int f = 0;
        //Loop through rows
        for (int i = 0; i < gridPane.getRowCount(); i++) {
            //Loop through columns
            for (int j = 0; j < gridPane.getColumnCount(); j++) {
                //Create a new button element with a number
                Button b = new Button("" + f);
                //Increment the number counter
                f++;
                //What happens when the buttons are clicked?
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        onButtonPress(event);
                    }
                });
                //Add the button to the grid, in the specified location
                gridPane.add(b, j, i);
                //Add an infobutton, containing more info than a button, to our grid for later reference
                buttonArray[j][i] = new infoButton(b, j, i, start, false);
            }
        }

        //Choose the start button
        start = buttonArray[r.nextInt(10)][r.nextInt(10)];
        start.getButton().setStyle("-fx-background-color: #00ff00");

        //Choose the finish button, make sure it is not the start
        do {
            finish = buttonArray[r.nextInt(10)][r.nextInt(10)];
        } while (finish == start);
        finish.getButton().setStyle("-fx-background-color: #FF0000");
    }
}