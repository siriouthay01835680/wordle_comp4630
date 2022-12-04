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

import com.mobileapp.wordle.databinding.FragmentGameBinding;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;

    // keyboard vars
    //final private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    final private String row1 = "qwertyuiop";
    final private String row2 = "asdfghjkl";
    final private String row3 = "zxcvbnm";
    final private String alphabet = row1 + row2 + row3;

    String currentWord = "";
    Button[] alphaButtons = new Button[26];
    Button submit;
    Button delete;

    //grid variables
    TextView[][] gameGrid = new TextView[6][5];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //binding
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        buildGameGrid();
        addKeyboard();


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
                gameGrid[i][j].setText("A");
                gameGrid[i][j].setTextSize(14);
                gameGrid[i][j].setGravity(Gravity.CENTER);
                gameGrid[i][j].setPadding(30,30,30,30);

                //setting layout params
                GridLayout.LayoutParams layoutParams=new GridLayout.LayoutParams();
                layoutParams.setMargins(5, 5, 5, 5);

                gameGrid[i][j].setHeight(200);
                gameGrid[i][j].setWidth(195);

                gameGrid[i][j].setBackgroundResource(R.color.off_white);
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


            //setting up buttons position
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.MATCH_PARENT,1f);
            if(row1.contains(letter))
                binding.keyboardRow1.addView( alphaButtons[i], lp);
            if(row2.contains(letter))
                binding.keyboardRow2.addView( alphaButtons[i], lp);
            if(row3.contains(letter))
                binding.keyboardRow3.addView( alphaButtons[i],lp);


            //after the l letter the submit button follows
            if(l == 'l'){
                lp = new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.MATCH_PARENT,2f);
                submit = new Button(view.getContext());
                submit.setText("Enter");
                binding.keyboardRow3.addView(submit,lp);
            }

            //after the last letter of keyboard the m letter follows
            if(l == 'm'){
                lp = new TableRow.LayoutParams(0,android.widget.TableRow.LayoutParams.MATCH_PARENT,1.5f);
                delete = new Button(view.getContext());
                delete.setText("<-");
                binding.keyboardRow3.addView(delete,lp);
            }

            //setting event handlers for button
            alphaButtons[i].setOnClickListener(view1 -> {
                if(currentWord.length() < 5) {
                    Button b = (Button) view1;
                    String key = b.getText().toString();
                    currentWord += key;
                    binding.testingWord.setText(currentWord);
                }

            });

        }

        //deletes last character
        delete.setOnClickListener(view1 -> {
            if(currentWord.length() > 0) {
                currentWord = currentWord.substring(0, currentWord.length() - 1);
                binding.testingWord.setText(currentWord);
            }

        });

        //submits words
        submit.setOnClickListener(view1 -> {

            //can only submit if all length of the word is 5
            if(currentWord.length() == 5) {
                //iterate through the current 5 letter word
                for(int i = 0; i < 5; i++){
                    //gets letter
                   char letter =  currentWord.charAt(i);

                    //looking for the index in our array of buttons with just the letter
                   int index = alphabet.indexOf(letter);
                    //work on the logic whether its contained and if its in right position
                    //if not either turn gray
                    //for now it all turns green
                    if(currentWord.contains(alphaButtons[index].getText().toString())){
                        alphaButtons[index].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    }

                }

                currentWord = "";
                binding.testingWord.setText(currentWord);

            }

        });


    }
}