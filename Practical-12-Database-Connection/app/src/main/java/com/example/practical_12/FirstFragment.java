package com.example.practical_12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.practical_12.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private DatabaseManager dbManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize database manager
        dbManager = new DatabaseManager(requireContext());
        dbManager.open();

        binding.textviewResult.setVisibility(View.GONE);

        // Insert button click listener
        binding.buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });

        // View all users button (navigate to second fragment)
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private void insertUser() {
        String name = binding.edittextName.getText().toString().trim();
        String email = binding.edittextEmail.getText().toString().trim();
        String ageStr = binding.edittextAge.getText().toString().trim();

        // Validation
        if (name.isEmpty() || email.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            
            // Insert user
            long userId = dbManager.insertUser(name, email, age);
            
            if (userId > 0) {
                Toast.makeText(requireContext(), "User inserted successfully (ID: " + userId + ")", Toast.LENGTH_SHORT).show();
                binding.edittextName.setText("");
                binding.edittextEmail.setText("");
                binding.edittextAge.setText("");
                
                // Show success message
                binding.textviewResult.setText("✓ User inserted successfully!\nID: " + userId + 
                        "\nName: " + name + "\nEmail: " + email + "\nAge: " + age);
                binding.textviewResult.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(requireContext(), "Failed to insert user", Toast.LENGTH_SHORT).show();
                binding.textviewResult.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Invalid age format", Toast.LENGTH_SHORT).show();
            binding.textviewResult.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbManager != null) {
            dbManager.close();
        }
        binding = null;
    }

}