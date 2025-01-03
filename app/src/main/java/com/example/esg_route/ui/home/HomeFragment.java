package com.example.esg_route.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.esg_route.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Get ViewModel
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflate layout with data binding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this); // Set LifecycleOwner for LiveData observation

        // Observe LiveData and bind data
        homeViewModel.getText().observe(getViewLifecycleOwner(), text -> {
            binding.textHome.setText(text);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}