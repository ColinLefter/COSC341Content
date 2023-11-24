package com.example.cosc341firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadData extends AppCompatActivity {
    private DatabaseReference root;
    private int lineToRead = 0;

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
            lineToRead++;
        });

        previousButton.setOnClickListener(v -> {
            lineToRead--;
        });

        mainScreenButton.setOnClickListener(v -> {
            finish();
        });
    }
    public void getDate(boolean readFromDatabase) {
        if (lineToRead < 0) {

        }

        if (readFromDatabase) {
            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // this is where we read the data from the user
                    if (snapshot.hasChildren()) {
                        DataSnapshot current = snapshot.getChildren().iterator().next();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println(error.getDetails());
                }
            });
        } else {
            String line = readLineFromFile(lineToRead, this);
            displayData(line);
        }
    }

    private void displayData(String dataLine) {
        if (dataLine != null && !dataLine.isEmpty()) {
            String[] dataParts = dataLine.split(",");
            if (dataParts.length >= 5) {
                String studentNumber = dataParts[0];
                String lastName = dataParts[1];
                String firstName = dataParts[2];
                String gender = dataParts[3];
                String division = dataParts[4];
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
        } else {
            Toast.makeText(
                    this,
                    "No data yet. Please write some data first.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private String readLineFromFile(int lineNumber, Context context) {
        try {
            InputStream inputStream = context.openFileInput("data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            int currentLine = 0;
            while ((line = reader.readLine()) != null) {
                if (currentLine == lineNumber) {
                    reader.close();
                    return line;
                }
                currentLine++;
            }
            reader.close();
        } catch (IOException e) {
            Log.e("ReadData", "Error reading file", e);
        }
        return null;
    }

}