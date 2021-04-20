package com.dgl114.colormatch;

import android.graphics.Color;
import java.util.Random;

//****************************************************************************************
//ColorMatchGame.java           Author: zakacat
//
//This is the model portion of the app and it contains most of the game logic.
//This class includes:
// - ColorMatchGame() A constructor
// - newGame()  A way to generate the 9 buttons and their respective colors
// - chooseWinningCombination()  A way to randomly(ish) choose a winning combination of
//                               from two of the buttons
// - toggleButtonSelection()  A way to toggle the button selection on/off which also
//                            includes code to inhibit selecting more than two buttons at a time
// - resetSelection()   A way to reset the selection at the start of each new game
// - isGameOver()  A way to check if the game is over or not by comparing the selected buttons
//                  with the winning combination
// - Setters for - Game State and Selected State
// - Getters for - Game State and Selected State
//               - Winning selection and their corresponding spots in the 2d array
//               - If button is selected.
//               - Color value of button to be accessed by Main Activity as this is the only
//                  class that interacts with Button.java
//****************************************************************************************

public class ColorMatchGame {

    public static final int NUM_ROWS = 3;
    public static final int NUM_COLS = 3;
    private final Button[][] mColors;
    private int winRow1, winCol1, winRow2, winCol2;

    public ColorMatchGame() {
        mColors = new Button[NUM_ROWS][NUM_COLS];
    }
    //****************************************************************************************
    //newGame() iterates through the 2d array that are button objects. It randomizes the values
    //for red, blue, and green values and combines them into one int value and then it assigns
    //it to the newly created button object.
    //****************************************************************************************
    public void newGame() {
        Random randomNumGenerator = new Random();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                int red, blue , green;
                red = randomNumGenerator.nextInt(255);
                blue = randomNumGenerator.nextInt(255);
                green = randomNumGenerator.nextInt(255);
                mColors[row][col] = new Button();
                mColors[row][col].setColor(Color.rgb(red, blue, green));
            }
        }
        chooseWinningCombination();
    }
    //****************************************************************************************
    //chooseWinningCombination() generates random numbers from 0 - 2 (inclusive) to represent
    //the column and row of the two winning selections. If the two selections match, one of the
    //selections is shifted one column over. (This will create a bias in the game.)
    //****************************************************************************************
    private void chooseWinningCombination(){
        Random randomNumGenerator = new Random();
        int tempRow, tempCol;

        winRow1 = randomNumGenerator.nextInt(2);
        winCol1 = randomNumGenerator.nextInt(2);
        tempRow = randomNumGenerator.nextInt(2);
        tempCol = randomNumGenerator.nextInt(2);

        if ((winRow1 == tempRow) && (winCol1 == tempCol)){
            if(tempCol == 2){
                winCol2 = tempCol - 1;
            }
            else{
                winCol2 = tempCol + 1;
            }
        }
        else {
            winRow2 = tempRow;
            winCol2 = tempCol;
        }
    }

    //****************************************************************************************
    //toggleButtonSelection() takes the row and column numbers as parameters and as long
    //as there is not two or more button selected, an unselected button can be selected. There
    //are no constraints for changing a selected button to unselected.
    //****************************************************************************************
    public void toggleButtonSelection(int row, int col) {
        int count = 0;

        for (int rowIndex = 0; rowIndex < NUM_ROWS; rowIndex++) {
            for (int colIndex = 0; colIndex < NUM_COLS; colIndex++) {
                if (mColors[rowIndex][colIndex].isSelected()) {
                    count ++;
                }
            }
        }
        if(mColors[row][col].isSelected()){
            mColors[row][col].setSelected(false);
        }

        else{
            if(count<2) {
                mColors[row][col].setSelected(true);
            }
        }

    }
    //****************************************************************************************
    //resetSelection() iterates through the buttons and deselects them all. This is used every
    //time a new game is started.
    //****************************************************************************************
    public void resetSelection() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                mColors[row][col].setSelected(false);
            }

        }
    }
    //****************************************************************************************
    //isGameOver() iterates through the buttons and checks to see if a selected button matches
    //one of the winning combinations. If a selection matches, gameOver increments by one. So,
    //if gameOver is equal to two, then isGameOver() returns true. If not, then isGameOver()
    //returns false.
    //****************************************************************************************
    public boolean isGameOver() {
        int gameOver = 0; //

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (mColors[row][col].isSelected()) {
                    if((row == winRow1) && (col == winCol1)){
                        gameOver ++;
                    }
                    if((row == winRow2) && (col == winCol2)){
                        gameOver ++;
                    }
                    if(gameOver == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //****************************************************************************************
    //Setters
    //****************************************************************************************
    public void setState(int[] nineColors) { //
        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                mColors[row][col] = new Button();
                mColors[row][col].setColor(nineColors[index]);
                index++;
            }
        }
    }

    public void setStateSelected(boolean[] selected){
        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                mColors[row][col].setSelected(selected[index]);
                index++;
            }
        }

    }
    //****************************************************************************************
    //Getters
    //****************************************************************************************
    //****************************************************************************************
    //getState() gathers all the color values from the buttons and adds them to an int array.
    //getState() returns the int array to be used for Save State.
    //****************************************************************************************
    public int[] getState() {
        int[] localColors = new int[9];
        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                localColors[index]= mColors[row][col].getColor();
                index++;
            }
        }
        return localColors;
    }
    //****************************************************************************************
    //getStateSelected() gathers all the selection info from the buttons and adds them to
    //boolean array.
    //getStateSelected() returns the boolean array to be used for Save State.
    //****************************************************************************************
    public boolean[] getStateSelected(){
        boolean[] localBoo = new boolean[9];
        int index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                localBoo[index] = isButtonSelected(row, col);
                index++;
            }
        }
        return localBoo;
    }

    public int getWinRow1() {
        return winRow1;
    }

    public int getWinCol1() {

        return winCol1;
    }

    public int getWinRow2()
    {
        return winRow2;
    }

    public int getWinCol2() {

        return winCol2;
    }

    public boolean isButtonSelected(int row, int col) {
        return mColors[row][col].isSelected();
    }

    public int getColor(int row, int col){
        return mColors[row][col].getColor();
    }
}
