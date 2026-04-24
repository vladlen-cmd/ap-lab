package com.example.practical_12;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.practical_12.databinding.FragmentSecondBinding;
import java.util.List;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private DatabaseManager dbManager;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize database manager
        dbManager = new DatabaseManager(requireContext());
        dbManager.open();

        // Refresh button click listener
        binding.buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAllUsers();
            }
        });

        // Delete all button click listener
        binding.buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbManager.deleteAllUsers();
                Toast.makeText(requireContext(), "All users deleted", Toast.LENGTH_SHORT).show();
                displayAllUsers();
            }
        });

        // Back to first fragment button
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        // Display users on fragment load
        displayAllUsers();
    }

    private void displayAllUsers() {
        List<User> users = dbManager.getAllUsers();
        int count = dbManager.getUserCount();

        // Update count
        binding.textviewCount.setText("Total Users: " + count);

        if (users.isEmpty()) {
            binding.textviewUsersList.setText("No users found in database");
        } else {
            StringBuilder sb = new StringBuilder();
            for (User user : users) {
                sb.append("ID: ").append(user.getId())
                        .append("\n");
                sb.append("Name: ").append(user.getName())
                        .append("\n");
                sb.append("Email: ").append(user.getEmail())
                        .append("\n");
                sb.append("Age: ").append(user.getAge())
                        .append("\n");
                sb.append("Created: ").append(user.getCreatedAt())
                        .append("\n");
                sb.append("─────────────────\n");
            }
            binding.textviewUsersList.setText(sb.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment resumes
        displayAllUsers();
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