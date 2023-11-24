package com.example.cosc341firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class WriteData extends AppCompatActivity {
    private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_data);
        root = FirebaseDatabase.getInstance().getReference();

        boolean writeOnDatabase = getIntent().getBooleanExtra("WRITE_ON_DATABASE", false);

        EditText studentNumberIn = findViewById(R.id.studentNumberIn);
        EditText lastNameIn = findViewById(R.id.lastNameIn);
        EditText firstNameIn = findViewById(R.id.firstNameIn);
        RadioGroup genderGroup = findViewById(R.id.genderGroup);
        Spinner divisionDropdown = findViewById(R.id.divisionDropdown);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.subjects_array, android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionDropdown.setAdapter(adapter);

        Button submitButton = findViewById(R.id.writeDataSubmitButton);
        submitButton.setOnClickListener(v -> {
            int selectedId = genderGroup.getCheckedRadioButtonId();
            RadioButton selectedGender = findViewById(selectedId);

            String studentNumber = studentNumberIn.getText().toString();
            String lastName = lastNameIn.getText().toString();
            String firstName = firstNameIn.getText().toString();
            String gender = selectedGender.getText().toString();
            String division = divisionDropdown.getSelectedItem().toString();

            if (validateInput(studentNumber, firstName, lastName)) {
                String data = String.format(
                        "%s,%s,%s,%s,%s\n",
                        studentNumber,
                        lastName,
                        firstName,
                        gender,
                        division
                );
                if (writeOnDatabase) {
                    writeToDB(studentNumber, lastName, firstName, gender, division);
                } else {
                    writeToDataFile(data, this);
                }
                finish();
            }
        });
    }

    public void writeToDB(String studentNumber, String lastName, String firstName, String gender, String division) {
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("studentNumber", studentNumber);
        userData.put("lastName", lastName);
        userData.put("firstName", firstName);
        userData.put("gender", gender);
        userData.put("division", division);

        root.push().setValue(userData);
    }

    private void writeToDataFile(String data, Context context) {
        String filename = "data.txt";
        File file = new File(context.getFilesDir(), filename);

        try {
            // check if the file exists
            if (!file.exists()) {
                file.createNewFile();
            }

            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                OutputStreamWriter out = new OutputStreamWriter(fos);
                out.write(data);
            }
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }

    private boolean validateInput(String studentNumber, String firstName, String lastName) {
        if (studentNumber == null || firstName == null || lastName == null || studentNumber.length() < 8) {
            // show toast
            Toast.makeText(
                    this,
                    "Please enter a valid student ID, first name and last name",
                    Toast.LENGTH_SHORT
            ).show();
            return false;
        }
        return true;
    }
}