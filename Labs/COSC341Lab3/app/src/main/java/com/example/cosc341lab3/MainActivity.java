package com.example.cosc341lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSpinners();
        setupButtonListener();
    }

    private void initializeSpinners() {
        Spinner numberOfQuestions = findViewById(R.id.noOfQuestions);
        Spinner category = findViewById(R.id.category);

        ArrayAdapter<CharSequence> questionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.number_of_questions, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);

        questionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        numberOfQuestions.setAdapter(questionsAdapter);
        category.setAdapter(categoryAdapter);
    }

    private void setupButtonListener() {
        Button loadQuizButton = findViewById(R.id.button);
        loadQuizButton.setOnClickListener(v -> loadQuiz());
    }

    private void loadQuiz() {
        Spinner numberOfQuestions = findViewById(R.id.noOfQuestions);
        Spinner category = findViewById(R.id.category);

        String selectedNumberOfQuestions = numberOfQuestions.getSelectedItem().toString();
        String selectedCategory = category.getSelectedItem().toString();

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra("numberOfQuestions", selectedNumberOfQuestions);
        intent.putExtra("category", selectedCategory);
        startActivity(intent);
    }
}