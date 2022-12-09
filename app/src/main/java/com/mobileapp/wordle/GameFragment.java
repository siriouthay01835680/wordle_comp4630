package com.mobileapp.wordle;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.mobileapp.wordle.databinding.FragmentGameBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            wordToGuess = savedInstanceState.getString("STATE_WORD");
        } else {
            //new game
            incrementGamesPlayed();
            try {
                wordList = readFromFileToList("wordfile.txt");
                wordToGuess = pickAWord(wordList);
                System.out.println("winning word is " + wordToGuess);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding
        binding = FragmentGameBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        //view model
        viewModelFactory = new GameViewModelFactory(wordToGuess);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(GameViewModel.class);

        buildGameGrid();
        addKeyboard();

        viewModel.isHintToggled = GameFragmentArgs.fromBundle(getArguments()).getIsHintToggled();
        if(viewModel.isHintToggled){
            binding.hintButton.setEnabled(true);
        }

        binding.hintButton.setOnClickListener(view1 -> {
            //when hint button is clicked, get rand char from guess word & put in grid
            binding.hintButton.setEnabled(false);
            String hint = viewModel.enableHints();
            int index = alphabet.indexOf(hint);
            gameGrid[viewModel.lives][viewModel.hintIndex].setText(String.valueOf(viewModel.hintChar));
            gameGrid[viewModel.lives][viewModel.hintIndex].setTextColor(Color.BLACK);
            gameGrid[viewModel.lives][viewModel.hintIndex].setBackgroundResource(R.color.green);

            //update keyboard for hint
            alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        });

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
                //gameGrid[i][j].setTextColor(Color.BLACK);
                gameGrid[i][j].setTextSize(35);
                gameGrid[i][j].setGravity(Gravity.CENTER);
                gameGrid[i][j].setPadding(30,30,30,30);
                gameGrid[i][j].setText(viewModel.gridText[i][j]);

                //setting layout params
                GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams();
                layoutParams.setMargins(5, 5, 5, 5);

                gameGrid[i][j].setHeight(180);
                gameGrid[i][j].setWidth(195);

                //gameGrid[i][j].setBackgroundResource(R.color.off_white);
                //get the current state of colors of colors for the grid position if already decided
                //if not default is off-white
                gridColorDecider(gameGrid[i][j], viewModel.gridColor[i][j]);
                //add newly created TextView to GridLayout
                gl.addView(gameGrid[i][j], layoutParams);
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
            keyboardColorDecider(alphaButtons[i], viewModel.keyboardColor[i]);


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
                    //if hint is enabled, make sure hint char cannot be changed
                    if (viewModel.isHintEnabled && (viewModel.currentPosition == viewModel.hintIndex)) {
                        gameGrid[viewModel.lives][viewModel.hintIndex].setText(String.valueOf(viewModel.hintChar));
                        gameGrid[viewModel.lives][viewModel.hintIndex].setTextColor(Color.BLACK);
                    }
                    else {
                        //add to grid
                        gameGrid[viewModel.lives][viewModel.currentPosition].setText(key);
                    }

                    //add to grid
                    gameGrid[viewModel.lives][viewModel.currentPosition].setText(key);
                    viewModel.saveGridText(key);

                    viewModel.currentPosition++;
                    viewModel.currentGuess += key;



                }
            });

        }

        //deletes last character in UI and viewModel
        delete.setOnClickListener(view1 -> {
            if(viewModel.currentGuess.length() > 0) {

                viewModel.currentGuess = viewModel.currentGuess.substring(0, viewModel.currentGuess.length() - 1);
                viewModel.currentPosition--;
                gameGrid[viewModel.lives][viewModel.currentPosition].setText(" ");
                viewModel.saveGridText(" ");


            }
        });

        //submits word to Game
        submit.setOnClickListener(view1 -> {
            //only accepts 5 letter submissions
            if(viewModel.currentGuess.length() != 5) {
                //error message that length is too short and leaves
                Toast.makeText(requireActivity().getApplicationContext(), "Word must have 5 letters", Toast.LENGTH_SHORT).show();
                return;
            }

            //only accepts valid words
            if(!wordList.contains(viewModel.currentGuess.toLowerCase())) {
                //error messaege to user than exits
                Toast.makeText(requireActivity().getApplicationContext(), "Word not in our word bank", Toast.LENGTH_SHORT).show();
                return;
            }

            checkGuess();
            viewModel.submitGuess();


            //would like a delay here so the UI can finish but didn't know how to
            if(viewModel.isGameOver()){

                //update games won count
                if(viewModel.isGameWon)
                    incrementGamesWon();

                //possible game is over and need to switch frags
                GameFragmentDirections.ActionGameFragmentToResultFragment action = GameFragmentDirections.actionGameFragmentToResultFragment();
                System.out.println("game over");
                action.setWord(viewModel.winningWord);
                action.setIsGameWon(viewModel.isGameWon);
                Navigation.findNavController(view).navigate(action);
            }//end of game over instructions

        }//end of submit button
        );

        }


/* checkGuess checks the guess against the winning word
    and updates the GridView and Keyboard UI
    and updates state of word, grid, and keyboard is also saved in view model
* */

    public void checkGuess(){
        for(int i = 0; i < 5; i++){
                //check hint if enabaled
            if(viewModel.isHintEnabled && i == viewModel.hintIndex){
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.green);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.winningWord.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);
                continue;
            }

            char letter = viewModel.currentGuess.charAt(i);

            //looking for the index in our array of buttons with just the letter
            int index = alphabet.indexOf(letter);

            if(viewModel.currentGuess.charAt(i) == viewModel.winningWord.charAt(i)){
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.green);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.currentGuess.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);

                //update keyboard
                alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

                //update State:
                viewModel.gridColor[viewModel.lives][i] = "GREEN";
                viewModel.keyboardColor[index] = "GREEN";
            }

            else if(viewModel.winningWord.contains(String.valueOf(viewModel.currentGuess.charAt(i)))){
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.yellow);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.currentGuess.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);

                //update Keyboard
                alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));


                //update State in ViewModel:
                viewModel.gridColor[viewModel.lives][i] = "YELLOW";
                viewModel.keyboardColor[index] = "YELLOW";
            }
            else{
                gameGrid[viewModel.lives][i].setBackgroundResource(R.color.gray);
                gameGrid[viewModel.lives][i].setText(String.valueOf(viewModel.currentGuess.charAt(i)));
                gameGrid[viewModel.lives][i].setTextColor(Color.WHITE);

                //update keyboard
                alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));

                //update State:
                viewModel.gridColor[viewModel.lives][i] = "GRAY";
                viewModel.keyboardColor[index] = "GRAY";
            }
        }

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


    @Override
    public void onSaveInstanceState(Bundle savedInstances){
        savedInstances.putString("STATE_WORD", viewModel.winningWord);
        super.onSaveInstanceState(savedInstances);
    }

    /* helper function that decided the color for each color in Grid
    * takes a textView and value of a color*/
    private void gridColorDecider(TextView t, String color){
        switch(color){
            case "GREEN":
                t.setTextColor(Color.WHITE);
                t.setBackgroundResource(R.color.green);
                break;
            case "YELLOW":
                t.setTextColor(Color.WHITE);
                t.setBackgroundResource(R.color.yellow);
                break;
            case "GRAY":
                t.setTextColor(Color.WHITE);
                t.setBackgroundResource(R.color.gray);
                break;
            default:
                t.setTextColor(Color.BLACK);
                t.setBackgroundResource(R.color.off_white);

        }
    }

    /* Helper function that decided the color for each keyboard key
    *  that takes a button and string of color  */

    public void keyboardColorDecider(Button b, String color){
        switch(color){
            case "GREEN":
                b.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                break;
            case "YELLOW":
                b.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
                break;
            case "GRAY":
                b.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gray)));
                break;

        }

    }



    /** BEGIN: Prototype for checking guess against the word to be guessed; can be deleted

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
    }
     END: Prototype for checking guess against the word to be guessed **/


private void incrementGamesPlayed(){
    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    int defaultValue = getResources().getInteger(R.integer.saved_total_games);
    int totalGames= sharedPref.getInt(getString(R.string.saved_total_games), defaultValue);
    totalGames++;

    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(getString(R.string.saved_total_games), totalGames);
    editor.apply();

}

private void incrementGamesWon(){
    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
    int defaultValue = getResources().getInteger(R.integer.saved_games_won);
    int gameswon = sharedPref.getInt(getString(R.string.saved_games_won), defaultValue);
    gameswon++;

    SharedPreferences.Editor editor = sharedPref.edit();
    editor.putInt(getString(R.string.saved_games_won), gameswon);
    editor.apply();

}

//set binding to null

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}