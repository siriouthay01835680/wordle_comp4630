package com.mobileapp.wordle;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.mobileapp.wordle.databinding.FragmentGameBinding;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;

    // keyboard vars
    final private String row1 = "QWERTYUIOP";
    final private String row2 = "ASDFGHJKL";
    final private String row3 = "ZXCVBNM";
    final private String alphabet = row1 + row2 + row3;



    Button[] alphaButtons = new Button[26];
    Button submit;
    Button delete;

    //grid variables
    TextView[][] gameGrid = new TextView[6][5];

    //test variables for checkGuess() prototype; can delete along with checkGuess()
    private String wordToGuess = "";

    //var to hold words from text file
    private List<String> wordList = null;

    // view model
    private GameViewModelFactory viewModelFactory;
    private GameViewModel viewModel;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //binding
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        try {
            wordList = readFromFileToList("wordfile.txt");
            wordToGuess = pickAWord(wordList);
            System.out.println("winning word is" + wordToGuess);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //view model
        viewModelFactory = new GameViewModelFactory(wordToGuess);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(GameViewModel.class);

        buildGameGrid();
        addKeyboard();



        //prototype can be deleted later if necessary
        /*
        checkGuess("PEARL", 0);
        checkGuess("WORLD", 1);
        checkGuess("LDROW", 2);

        checkGuess("WORDL", 3);
        */

        //checkGuess(wordToGuess, 3);


        // Inflate the layout for this fragment
        return view;
    }

    public void buildGameGrid(){
        View view = binding.getRoot();
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 5; j++){
                //access the GridLayout
                GridLayout gl = binding.gameGridLayout;
                //create new TextViews and set default attributes
                gameGrid[i][j] = new TextView(view.getContext());
                gameGrid[i][j].setTextColor(Color.BLACK);
                gameGrid[i][j].setTextSize(35);
                gameGrid[i][j].setGravity(Gravity.CENTER);
                gameGrid[i][j].setPadding(30,30,30,30);

                //setting layout params
                GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams();
                layoutParams.setMargins(5, 5, 5, 5);

                gameGrid[i][j].setHeight(180);
                gameGrid[i][j].setWidth(195);

                gameGrid[i][j].setBackgroundResource(R.color.off_white);
                //add newly created TextView to GridLayout
                gl.addView(gameGrid[i][j], layoutParams);
            }
        }
    }

    /** BEGIN: Prototype for checking guess against the word to be guessed; can be deleted **/
    /*
    public void checkGuess(String guess, int lives){
        for(int i = 0; i < 5; i++){
            guess = guess.toUpperCase();
            if(guess.charAt(i) == wordToGuess.charAt(i)){
            gameGrid[lives][i].setBackgroundResource(R.color.green);
            gameGrid[lives][i].setText(String.valueOf(guess.charAt(i)));
            gameGrid[lives][i].setTextColor(Color.WHITE);
            }

            else if(wordToGuess.contains(String.valueOf(guess.charAt(i)))){
                gameGrid[lives][i].setBackgroundResource(R.color.yellow);
                gameGrid[lives][i].setText(String.valueOf(guess.charAt(i)));
                gameGrid[lives][i].setTextColor(Color.WHITE);
            }
            else {
                gameGrid[lives][i].setBackgroundResource(R.color.gray);
                gameGrid[lives][i].setText(String.valueOf(guess.charAt(i)));
                gameGrid[lives][i].setTextColor(Color.WHITE);
            }
        }
    }*/
    /** END: Prototype for checking guess against the word to be guessed **/


    public void checkGuess(){
        for(int i = 0; i < 5; i++){

            char letter = viewModel.currentGuess.charAt(i);

            //looking for the index in our array of buttons with just the letter
            int index = alphabet.indexOf(letter);

            if(viewModel.currentGuess.charAt(i) == viewModel.winningWord.charAt(i)){
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.green);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.currentGuess.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);

                //update keyboard
                alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            }

            else if(viewModel.winningWord.contains(String.valueOf(viewModel.currentGuess.charAt(i)))){
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.yellow);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.currentGuess.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);

                //update Keyboard
                alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
            }
            else{
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.gray);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.currentGuess.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);

                //update keyboard
                alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
            }
        }

    }


    public void addKeyboard(){
        View view = binding.getRoot();

        for(int i=0; i < alphabet.length() ; i++) {
            //setting up button
            alphaButtons[i] = new Button(view.getContext());
            char l = alphabet.charAt(i);
            String letter = Character.toString(l);
            alphaButtons[i].setText(letter);


            //setting up buttons position
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.MATCH_PARENT,1f);
            if(row1.contains(letter))
                binding.keyboardRow1.addView(alphaButtons[i], lp);
            if(row2.contains(letter))
                binding.keyboardRow2.addView(alphaButtons[i], lp);
            if(row3.contains(letter))
                binding.keyboardRow3.addView(alphaButtons[i],lp);


            //after the l letter the submit button follows
            if(l == 'L'){
                lp = new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.MATCH_PARENT,2f);
                submit = new Button(view.getContext());
                submit.setText("Enter");
                binding.keyboardRow3.addView(submit,lp);
            }

            //after the last letter of keyboard the m letter follows
            if(l == 'M'){
                lp = new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.MATCH_PARENT,1.5f);
                delete = new Button(view.getContext());
                delete.setText("<-");
                binding.keyboardRow3.addView(delete,lp);
            }

            //setting event handlers for button
            alphaButtons[i].setOnClickListener(view1 -> {
                if(viewModel.currentGuess.length() < 5) {
                    Button b = (Button) view1;
                    String key = b.getText().toString().toUpperCase();

                    //add to grid
                    gameGrid[viewModel.lives][viewModel.currentPosition].setText(key);

                    viewModel.currentPosition++;
                    viewModel.currentGuess += key;

                    //delete this
                    //currentWord += key;
                    binding.testingWord.setText(viewModel.currentGuess);
                }

            });

        }

        //deletes last character
        delete.setOnClickListener(view1 -> {
            if(viewModel.currentGuess.length() > 0) {

                viewModel.currentGuess = viewModel.currentGuess.substring(0, viewModel.currentGuess.length() - 1);
                viewModel.currentPosition--;
                gameGrid[viewModel.lives][viewModel.currentPosition].setText(" ");

                //code below needs to be delete
                binding.testingWord.setText(viewModel.currentGuess);
            }

        });

        //submits words
        submit.setOnClickListener(view1 -> {
            //this is where we can check if a word is valid

            //can only submit if all length of the word is 5
            if(viewModel.currentGuess.length() == 5) {
                checkGuess();
                viewModel.submitGuess();
                binding.testingWord.setText(viewModel.currentGuess);

                //would like a delay here so the UI can finish but didnt know how to
                if(viewModel.isGameOver()){
                    //possible game is over and need to switch frags
                    GameFragmentDirections.ActionGameFragmentToResultFragment action = GameFragmentDirections.actionGameFragmentToResultFragment();
                    System.out.println("game over");
                    action.setWord(viewModel.winningWord);
                    action.setIsGameWon(viewModel.isGameWon);
                    Navigation.findNavController(view).navigate(action);

                }


            }

        }

        );
    }




    private List <String> readFromFileToList(String fileName) throws IOException{
        InputStream inputStream = getContext().getAssets().open(fileName);
        List <String> myList = new ArrayList<String>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            String line;
            while((line = reader.readLine()) != null){
                //System.out.println(line);
                myList.add(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return myList;
    }

    private String pickAWord (List wordList){
        Random rand = new Random();
        int index = rand.nextInt(wordList.size());
        return wordList.get(index).toString().toUpperCase();
    }


//set binding to null

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}