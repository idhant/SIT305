package com.example.academymanagement.ui.credits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.academymanagement.R;

public class CreditsFragment extends Fragment {

    private CreditsViewModel creditsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_credits, container, false);
        final TextView textView = root.findViewById(R.id.text_credits);
        textView.setText("Credits");
        return root;
    }
}
