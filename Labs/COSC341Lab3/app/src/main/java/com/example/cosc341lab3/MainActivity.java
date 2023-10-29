package com.example.cosc341lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> questionsSpinnerValues = new ArrayList<>();
    ArrayList<String> categorySpinnerValues = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner numberOfQuestions = findViewById(R.id.noOfQuestions);
        Spinner category = findViewById(R.id.category);

        // ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> questionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_questions, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        // Specifying the layout to use when the list of choices appears
        questionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to the spinner
        numberOfQuestions.setAdapter(questionsAdapter);
        questionsSpinnerValues.add(numberOfQuestions.getSelectedItem().toString());

        category.setAdapter(categoryAdapter);
        categorySpinnerValues.add(category.getSelectedItem().toString());

//        Intent intent = new Intent(this, DisplayInformation.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("name", nameStr);
//        bundle.putString("email", emailStr);
//        bundle.putString("gender", genderStr);
//        bundle.putString("dob", dateOfBirthStr);
//        bundle.putString("password", passwordStr);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }
}