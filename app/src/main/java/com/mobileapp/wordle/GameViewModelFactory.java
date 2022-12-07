package com.mobileapp.wordle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class GameViewModelFactory implements ViewModelProvider.Factory {

    private final String winWord;

    public GameViewModelFactory(String winWord){
        this.winWord = winWord;
    }


    @Override
    public <T extends ViewModel> T create(@NonNull Class <T> modelClass) {

        if (modelClass == GameViewModel.class){
            return (T) new GameViewModel(winWord);
        }

        return null;
    }


}
