package com.mobileapp.wordle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobileapp.wordle.databinding.FragmentGameBinding;
import com.mobileapp.wordle.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String word = ResultFragmentArgs.fromBundle(getArguments()).getWord();
        boolean isWon = ResultFragmentArgs.fromBundle(getArguments()).getIsGameWon();

        if(isWon){
            String winMessage = getResources().getString(R.string.wonMessage);
            winMessage += word;
            binding.resultMessage.setText(winMessage);
        }else{
            String lostMessage = getResources().getString(R.string.lostMessage);
            lostMessage += word;
            binding.resultMessage.setText(lostMessage);
            binding.imageTrophy.setVisibility(View.INVISIBLE);
        }


        //button that goes to a new Game
        binding.newGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_resultFragment_to_gameFragment);
            }
        });


        return view;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}