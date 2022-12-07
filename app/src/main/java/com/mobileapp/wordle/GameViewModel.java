package com.mobileapp.wordle;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;


public class GameViewModel extends ViewModel {
    public int lives;
    final public String winningWord;
    public String currentGuess;


    public int currentPosition;





    public GameViewModel (String WinWord){
        lives = 0;
        currentPosition = 0;
        currentGuess = "";
        winningWord = WinWord;
    }



    public void submitGuess(){
        //updateKeyboard UI in gameFrag
        //update textGrid;
        System.out.println("in view model current Guess word is " + currentGuess);
        System.out.println("in view model current win word is " + winningWord);

        //lives go up
        //clear current word
        currentGuess = "";
        lives++;

        //back to start pos
        currentPosition = 0;

    }


}
