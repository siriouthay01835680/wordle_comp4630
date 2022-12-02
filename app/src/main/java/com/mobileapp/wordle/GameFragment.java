package com.mobileapp.wordle;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import androidx.fragment.app.Fragment;

import com.mobileapp.wordle.databinding.FragmentGameBinding;

public class GameFragment extends Fragment {
    private FragmentGameBinding binding;

    // keyboard vars
    //final private char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    final private String row1 = "qwertyuiop";
    final private String row2 = "asdfghjkl";
    final private String row3 = "zxcvbnm";
    final private String alphabet2 = row1 + row2 + row3;

    String currentWord = "";
    Button[] alphaButtons = new Button[26];
    Button submit;
    Button delete;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //binding
        binding = FragmentGameBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        addKeyboard();


        // Inflate the layout for this fragment
        return view;
    }


    public void addKeyboard(){
        View view = binding.getRoot();

        for(int i=0; i < alphabet2.length() ; i++) {
            //setting up button
            alphaButtons[i] = new Button(view.getContext());
            char l = alphabet2.charAt(i);
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
            if(currentWord.length() == 5) {
                for(int i = 0; i < alphabet2.length(); i++){
                    //work on the logic whether its contained and if its in right pos
                    //if not either turn gray
                    //for now it all turns green
                    if(currentWord.contains(alphaButtons[i].getText().toString())){
                        alphaButtons[i].setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    }

                }

                currentWord = "";
                binding.testingWord.setText(currentWord);

            }

        });


    }
}