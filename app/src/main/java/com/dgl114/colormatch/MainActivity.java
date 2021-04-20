package com.dgl114.colormatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

//****************************************************************************************
//MainActivity.java         Author: zakacat
//
//This is the controller portion of the app and it contains methods and code for the
//interaction of the View with the Model.
//This class includes:
// - onCreate() Aligns the Model information with the view fields in View. Also has a
//              condition for new game or restarting from a saved state.
// - startGame() Calls methods from both the Controller and Model needed to start game.
// - onColorButtonClick() addresses a click event by calling methods needed to select the
//                          buttons.
// - buttonDelineater() delineates an 'X' on the a button that is selected.
// - setButtonColors() assigns the Button object color attribute as the background of the
//                      button in layout.
// - blendButtonColors() get the colors associated with the winning combination, blends
//                      them together, and sets that blend to the viewButton.
// - onPlayClick() Calls startGame()
// - onCheckClick() Calls mGame.isGameOver() to verify if the round has been won. Increments
//                  winning and losing guesses.
// - setOutputText() updates the output text in layout with the current amount of winning
//                   and losing guesses.
// - onSaveInstanceState() Calls static outState methods and saves game information
//                          (somewhere magically)
//****************************************************************************************

public class MainActivity extends AppCompatActivity {

      private ColorMatchGame mGame;
      private Button[][] mButtons;
      private Button viewButton;
      private TextView correct, failure;
      private int mCorrect, mFailure, mBlendedColor;
      private final String GAME_STATE = "game state";
      private final String CORRECT_STATE = "correct state";
      private final String FAIL_STATE = "fail stare";
      private final String SELECT_STATE = "select state";
      private final String BLEND_STATE = "blended state";

    //****************************************************************************************
    //onCreate() is called every time the app is booted up for the first time, or if the
    // screen is rotated and the layout has changes.
    // It aligns the View information with the Model information. Object types must match.
    //This method also includes a conditional for starting. If there is no save state, a new game
    //is started. If there is a Save State, the method will load and re-assign that data
    //accordingly.
    //****************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        correct= findViewById(R.id.correct_guesses);
        failure= findViewById(R.id.absolute_failures);
        viewButton = findViewById(R.id.blended_view);
        mButtons = new Button[ColorMatchGame.NUM_ROWS][ColorMatchGame.NUM_COLS];

        GridLayout gridLayout = findViewById(R.id.color_selection_grid);
        int childIndex = 0;
        for (int row = 0; row < ColorMatchGame.NUM_ROWS; row++) {
            for (int col = 0; col < ColorMatchGame.NUM_COLS; col++) {
                mButtons[row][col] = (Button) gridLayout.getChildAt(childIndex);
                childIndex++;
            }
        }

        mGame = new ColorMatchGame();

        if (savedInstanceState == null) {
            startGame();
        }
        else {
            int [] gameState = savedInstanceState.getIntArray(GAME_STATE);
            mGame.setState(gameState);
            mCorrect = savedInstanceState.getInt(CORRECT_STATE);
            mFailure = savedInstanceState.getInt(FAIL_STATE);
            boolean[] selectState = savedInstanceState.getBooleanArray(SELECT_STATE);
            mGame.setStateSelected(selectState);
            buttonDelineater();
            setButtonColors();
            mBlendedColor = savedInstanceState.getInt(BLEND_STATE);
            viewButton.setBackgroundColor(mBlendedColor);
            setOutputText();
        }
    }

    //****************************************************************************************
    //startGame() calls the methods needed for a new game to appear on screen.
    //****************************************************************************************
    private void startGame() {
        mGame.newGame();
        setOutputText();
        setButtonColors();
        buttonDelineater();
        blendButtonColors();
        mGame.resetSelection();
    }
    //****************************************************************************************
    //OnColorButtonClick() iterates through Button array and identifies which button matches
    //with the button that was selected by the user then it calls buttonDelineater() to mark
    // the button as selected or not. (It looks like the only thing that the buttonFound does
    // is stop the loop from continuing once the button is found. This probably doesn't matter
    //for this small of a loop, but on larger numbers of objects or variables, this might save
    //some processing power)
    //****************************************************************************************
    public void onColorButtonClick(View gameButton) {
        boolean buttonFound = false;
        for (int row = 0; row < ColorMatchGame.NUM_ROWS && !buttonFound; row++) {
            for (int col = 0; col < ColorMatchGame.NUM_COLS && !buttonFound; col++) {
                if (gameButton == mButtons[row][col]) {
                    mGame.toggleButtonSelection(row, col);
                    buttonFound = true;
                }
            }
        }
        buttonDelineater();
        }
    //****************************************************************************************
    //buttonDelineater() iterates through the buttons and if any are selected, it will delinieate
    //the button visually with a large letter 'X'.
    //****************************************************************************************
    private void buttonDelineater(){
        for (int row = 0; row < ColorMatchGame.NUM_ROWS; row++) {
            for (int col = 0; col < ColorMatchGame.NUM_COLS; col++) {
                if (mGame.isButtonSelected(row, col)) {
                    mButtons[row][col].setText("X");
                }
                else{
                    mButtons[row][col].setText("");
                }
            }
        }
    }
    //****************************************************************************************
    //setButtonColor() iterates through the buttons and assigns the background color of the
    //View button with the color value of the Button.java object (thru mGame.getColor()).
    //****************************************************************************************
    private void setButtonColors() {
        for (int row = 0; row < ColorMatchGame.NUM_ROWS; row++) {
            for (int col = 0; col < ColorMatchGame.NUM_COLS; col++) {
                    mButtons[row][col].setBackgroundColor(mGame.getColor(row,col));
            }
        }
    }
    //****************************************************************************************
    //blendButtonColors() retrieves the color values as ints from the buttons of the
    //winning combination and uses the static method from ColorUtilies.java to blend them
    //together. It then assigns that new color to the background of viewButton.
    //****************************************************************************************
    private void blendButtonColors(){
        int local1 = mGame.getColor(mGame.getWinRow1(), mGame.getWinCol1());
        int local2 = mGame.getColor(mGame.getWinRow2(), mGame.getWinCol2());
        mBlendedColor = ColorUtilities.blend(Color.valueOf(local1), Color.valueOf(local2));
        viewButton.setBackgroundColor(mBlendedColor);
    }
    //****************************************************************************************
    //onPlayClick() calls startGame() and starts a new game.
    //****************************************************************************************
    public void onPlayClick(View button_new_game) {
        startGame();
    }

    //****************************************************************************************
    //onCheckClick() calls mGame.isGameOver() and if true is returned, then a toast is made and
    //mCorrect is incremented by one. If false is returned, then a toast is made and
    //mFailure is incremented by one. setOutputText() is then called to update the text output
    //with the correct number of correct and incorrect guesses.
    //****************************************************************************************
    public void onCheckClick(View button_check) {
        if (mGame.isGameOver()) {
            Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show();
            mCorrect++;
        }
        else{//
            Toast.makeText(this, R.string.failure, Toast.LENGTH_SHORT).show();
            mFailure++;
        }
        setOutputText();
    }
    //****************************************************************************************
    //setOutputText() updates the text field views with the current amount of correct and
    //incorrect guesses. (I tried to refer to strings.xml and concatenate the number of guesses,
    //but it was adding that value to the string reference and calling different resources... or
    //it would crash the app.)
    //****************************************************************************************
    private void setOutputText() {
        correct.setText(getString(R.string.correctGuesses, mCorrect));
        failure.setText(getString(R.string.absoluteFailures, mFailure));
    }
    //****************************************************************************************
    //onSaveInstanceState() stores the various information needed to restore the
    //game in outState.
    //****************************************************************************************
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CORRECT_STATE, mCorrect);
        outState.putInt(FAIL_STATE, mFailure);
        outState.putIntArray(GAME_STATE, mGame.getState());
        outState.putBooleanArray(SELECT_STATE, mGame.getStateSelected());
        outState.putInt(BLEND_STATE, mBlendedColor);
    }
}