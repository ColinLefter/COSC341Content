package com.example.cosc341firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ReadData extends AppCompatActivity {
    private DatabaseReference root;
    private int userDataIdx = 0; // we start from the first captured data
    private int numData = 0;
    private ArrayList<String[]> userData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);
        root = FirebaseDatabase.getInstance().getReference();

        boolean readFromDatabase = getIntent().getBooleanExtra("WRITE_ON_DATABASE", false);
        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);
        Button mainScreenButton = findViewById(R.id.mainScreenButton);

        nextButton.setOnClickListener(v -> {
            userDataIdx = (userDataIdx + 1) % numData;
            getData(readFromDatabase);
        });

        previousButton.setOnClickListener(v -> {
            userDataIdx = (userDataIdx - 1) % numData;
            if (userDataIdx < 0) {
                userDataIdx = numData-1;
            }
            getData(readFromDatabase);
        });

        mainScreenButton.setOnClickListener(v -> {
            finish();
        });

        getData(readFromDatabase); // if it is the first time
    }

    public void updateUI() {
        if (!userData.isEmpty() && userDataIdx < userData.size()) {
            String[] globalData = userData.get(userDataIdx);

            String studentNumber = globalData[0];
            String lastName = globalData[1];
            String firstName = globalData[2];
            String gender = globalData[3];
            String division = globalData[4];
            String fullName = firstName + " " + lastName;

            TextView studentNumberView = findViewById(R.id.SIDOut);
            TextView nameView = findViewById(R.id.nameOut);
            TextView genderView = findViewById(R.id.genderOut);
            TextView divisionView = findViewById(R.id.divisionOut);

            studentNumberView.setText(studentNumber);
            nameView.setText(fullName);
            genderView.setText(gender);
            divisionView.setText(division);
        }
    }
    public void getData(boolean readFromDatabase) {
        if (readFromDatabase) { // controls where the data is being read from
            readFromFirebase();
        } else {
            readDataFromFile();
        }
        updateUI(); // to ensure that asynchronous calls are completed in time
    }

    public void readFromFirebase() {
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userData.clear(); // just in case
                for (DataSnapshot node : dataSnapshot.getChildren()) {
                    String studentNumber = node.getKey();
                    String lastName = node.child("LastName").getValue(String.class);
                    String firstName = node.child("FirstName").getValue(String.class);
                    String gender = node.child("Gender").getValue(String.class);
                    String division = node.child("Division").getValue(String.class);

                    String[] dataPoints = {studentNumber, lastName, firstName, gender, division};
                    userData.add(dataPoints);
                }
                numData = userData.size(); // update now that we have all the data
                updateUI(); // to deal with the asynchronous calls
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("firebase", "Error fetching data", databaseError.toException());
            }
        });
    }

    private void readDataFromFile() {
        String filename = "data.txt";
        String line;

        try (
            FileInputStream fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr)
        ) {
            while ((line = br.readLine()) != null) {
                String[] dataPoints = line.split(",");
                userData.add(dataPoints);
            }
            numData = userData.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}