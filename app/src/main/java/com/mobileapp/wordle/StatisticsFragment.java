package com.mobileapp.wordle;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobileapp.wordle.databinding.FragmentStatisticsBinding;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        //read data
        SharedPreferences sharedPref = getActivity().getPreferences( Context.MODE_PRIVATE);

        int defaultValueWon = getResources().getInteger(R.integer.saved_games_won);
        int gamesWon = sharedPref.getInt(getString(R.string.saved_games_won), defaultValueWon);

        binding.gamesWonText.setText(String.valueOf(gamesWon));

        View view = binding.getRoot();


        // Inflate the layout for this fragment
        return view;
    }


    //set binding to null
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}