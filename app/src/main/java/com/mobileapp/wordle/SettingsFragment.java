package com.mobileapp.wordle;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.mobileapp.wordle.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;

    //save state variables
//    private boolean lightModeChecked = false, musicChecked = false;

    //save the state of the switches
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean("lightModeChecked", lightModeChecked);
//        savedInstanceState.putBoolean("musicChecked", musicChecked);
//        super.onSaveInstanceState(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //for music switch
        Intent intent = new Intent(getActivity(), BackgroundSoundService.class);

//        if (savedInstanceState != null){
//            onSaveInstanceState(savedInstanceState);
//            lightModeChecked = savedInstanceState.getBoolean("lightModeChecked");
//            binding.lightModeSwitch.setChecked(lightModeChecked);
//
//        }

        binding.lightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    lightModeChecked = binding.lightModeSwitch.isChecked();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    lightModeChecked = binding.lightModeSwitch.isChecked();
                }
            }
        });

        binding.musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getActivity().startService(intent);
                }
                else{
                    getActivity().stopService(intent);
                }
            }
        });
        binding.hintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SettingsFragmentDirections.ActionSettingsFragmentToGameFragment action = SettingsFragmentDirections.actionSettingsFragmentToGameFragment();
                    action.setIsHintToggled(true);
                    Navigation.findNavController(view).navigate(action);
                }
            }
        });
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