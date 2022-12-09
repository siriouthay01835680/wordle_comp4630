package com.mobileapp.wordle;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import java.util.Random;


public class GameViewModel extends ViewModel {
    public int lives;
    final public String winningWord;
    public String currentGuess;



    //save state of current grid and keyboard
    public String[][] gridText = new String[6][5];
    public String[][] gridColor = new String[6][5];
    public String[] keyboardColor = new String[26];
    public boolean isGameWon;


    public Boolean isHintEnabled;
    public Integer hintIndex;
    public int currentPosition;
    public char hintChar;
    public Boolean isHintToggled;

    public GameViewModel (String WinWord){
        lives = 0;
        currentPosition = 0;
        currentGuess = "";
        winningWord = WinWord;
        isGameWon = false;
        clearGrid();
        clearKeyBoard();


        isHintEnabled = false;
        hintChar = ' ';
        isHintToggled = false;

    }

    public void saveGridText( String letter){
        gridText[lives][currentPosition] = letter;
    }


    public void clearGrid(){
        for(int i = 0; i< 6; i++){
            for(int j = 0; j < 5; j++){
                gridText[i][j] = " ";
                gridColor[i][j] = " ";
            }

        }
    }

    public void clearKeyBoard(){
        for(int i = 0; i < 26; i++){
            keyboardColor[i] = " ";
        }

    }



    public void submitGuess(){
        //updateKeyboard UI in gameFrag
        //update textGrid;
        System.out.println("in view model current Guess word is " + currentGuess);
        System.out.println("in view model current win word is " + winningWord);




        if(currentGuess.equals(winningWord))
            isGameWon = true;

        //lives go up
        //clear current word
        currentGuess = "";
        lives++;

        //back to start pos
        currentPosition = 0;

    }

    public String enableHints(){
        Random rand = new Random();
        isHintEnabled = true;
        int randomNum = rand.nextInt(5);
//        System.out.println(randomNum);
//        System.out.println(String.valueOf(winningWord.charAt(randomNum)));
        hintChar = winningWord.charAt(randomNum);
        hintIndex = winningWord.indexOf(hintChar);
        return String.valueOf(hintChar);
    }

    public boolean isGameOver(){
        if(isGameWon || lives > 5)
            return true;

        return false;
    }


}
