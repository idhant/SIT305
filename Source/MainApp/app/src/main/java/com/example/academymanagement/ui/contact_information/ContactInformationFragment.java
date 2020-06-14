package com.example.academymanagement.ui.contact_information;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.academymanagement.R;

public class ContactInformationFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contact_information, container, false);
        final TextView textView = root.findViewById(R.id.text_contact_information);
        textView.setText("Contact Us");

        final ImageButton phoneButton = root.findViewById(R.id.fragment_contact_information_phone);
        final ImageButton emailButton = root.findViewById(R.id.fragment_contact_information_email);
        final ImageButton mapButton = root.findViewById(R.id.fragment_contact_information_map);

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:470287377"));
                startActivity(callIntent);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude = -37.8475775;
                double longitude = 145.1145573;
                String label = "Academy Name";
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mailAddress = "ibhambri@deakin.edu.au";
                String mailSubject = "Clicked mail Button on your app";
                String mailBody = "Hi Developer," + "\n" + "I clicked on the mail button on your app to contact you." + "\n" + "Regards," + "\n" +"My name";
                //TODO: Open only mail apps.
                //https://developer.android.com/guide/components/intents-common#Email
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, mailAddress);
                intent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, mailBody);
                startActivity(intent);
            }
        });

        return root;
    }
}
