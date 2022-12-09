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


        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int defaultValueWon = getResources().getInteger(R.integer.saved_games_won);
        int gamesWon = sharedPref.getInt(getString(R.string.saved_games_won), defaultValueWon);

        int defaultValueGames = getResources().getInteger(R.integer.saved_total_games);
        float totalGames = sharedPref.getInt(getString(R.string.saved_total_games), defaultValueGames);


        float winRate = gamesWon/totalGames* 100;
        String displayRate = String.valueOf(winRate + '%');

        System.out.println("games won in stats"+ gamesWon);

        binding.gamesWonText.setText(String.valueOf(gamesWon));
        binding.winRateText.setText(displayRate);


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