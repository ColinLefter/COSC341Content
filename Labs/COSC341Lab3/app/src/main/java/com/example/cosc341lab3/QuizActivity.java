package com.example.cosc341lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        String numberOfQuestions = getIntent().getStringExtra("numberOfQuestions");
        String category = getIntent().getStringExtra("category");

        loadQuestions(category, Integer.parseInt(numberOfQuestions));
    }
    private String[] getCategoryQuestions(String category) {
        String[] categories = getResources().getStringArray(R.array.category);

        String[] questions;
        if (category.equals(categories[0])) {  // Canadian Capital
            questions = getResources().getStringArray(R.array.canadian_capital_questions);
        } else if (category.equals(categories[1])) {  // About Canada
            questions = getResources().getStringArray(R.array.about_canada_questions);
        } else if (category.equals(categories[2])) {  // Canadian Mountains
            questions = getResources().getStringArray(R.array.canadian_mountains_questions);
        } else {
            questions = new String[0];  // Default case, no questions
        }
        return questions;
    }

    private String[][] getQuestionOptions(String category) {
        String[] categories = getResources().getStringArray(R.array.category);

        String[][] options;
        if (category.equals(categories[0])) {  // Canadian Capital
            options = new String[][]{
                getResources().getStringArray(R.array.canadian_capital_options_q1),
                getResources().getStringArray(R.array.canadian_capital_options_q2),
                getResources().getStringArray(R.array.canadian_capital_options_q3),
                getResources().getStringArray(R.array.canadian_capital_options_q4)
            };
        } else if (category.equals(categories[1])) {  // About Canada
            options = new String[][]{
                getResources().getStringArray(R.array.about_canada_options_q1),
                getResources().getStringArray(R.array.about_canada_options_q2),
                getResources().getStringArray(R.array.about_canada_options_q3),
                getResources().getStringArray(R.array.about_canada_options_q4)
            };
        } else if (category.equals(categories[2])) {  // Canadian Mountains
            options = new String[][]{
                getResources().getStringArray(R.array.canadian_mountains_options_q1),
                getResources().getStringArray(R.array.canadian_mountains_options_q2),
                getResources().getStringArray(R.array.canadian_mountains_options_q3),
                getResources().getStringArray(R.array.canadian_mountains_options_q4)
            };
        } else {
            options = new String[0][0];  // Default case, no options
        }
        return options;
    }

    private String[] getCorrectAnswersForQuestions(String category) {
        String[] categories = getResources().getStringArray(R.array.category);

        String[] correctAnswers;
        if (category.equals(categories[0])) {  // Canadian Capital
            correctAnswers = getResources().getStringArray(R.array.canadian_capital_correct_answers);
        } else if (category.equals(categories[1])) {  // About Canada
            correctAnswers = getResources().getStringArray(R.array.about_canada_correct_answers);
        } else if (category.equals(categories[2])) {  // Canadian Mountains
            correctAnswers = getResources().getStringArray(R.array.canadian_mountains_correct_answers);
        } else {
            correctAnswers = new String[0];  // Default case, no correct answers
        }
        return correctAnswers;
    }

    private void loadQuestions(String category, int numOfQuestions) {
        String[] questionsToAsk = getCategoryQuestions(category);
        String[][] categoryOptions = getQuestionOptions(category);
        String[] correctAnswers = getCorrectAnswersForQuestions(category);

        // this task requires us to dynamically set the layout display using the data above
        // the solution to this is to use a layout inflater to create views for each question
        // where the base template is the question_layout.xml template

        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < numOfQuestions; i++) { // only show numOfQuestions many views
            // Inflate the question layout
            View questionView = inflater.inflate(R.layout.question_layout, null);

            // Find and set the image for the question (if applicable)
            ImageView questionImage = questionView.findViewById(R.id.imageView);
            switch (category) {
                case "About Canada":
                    // Set the image resource based on the question number
                    @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(
                            "about_canada" + (i + 1),
                            "drawable", getPackageName()
                    );
                    questionImage.setImageResource(imageResId);
                    break;
                case "Canadian Capital":
                    @SuppressLint("DiscouragedApi") int imageResId2 = getResources().getIdentifier(
                            "capital" + (i + 1),
                            "drawable",
                            getPackageName());
                    questionImage.setImageResource(imageResId2);
                    break;
                case "Canadian Mountains":
                    @SuppressLint("DiscouragedApi") int imageResId3 = getResources().getIdentifier(
                            "mountain" + (i + 1),
                            "drawable",
                            getPackageName());
                    questionImage.setImageResource(imageResId3);
                    break;
            }

            // Find the RadioGroup and populate the options
            RadioGroup optionsGroup = questionView.findViewById(R.id.radioGroupOptions);

            for (String option : categoryOptions[i]) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                optionsGroup.addView(radioButton);
            }

            // Add the question view to your main layout
            // Assuming you have a LinearLayout or other ViewGroup in your activity_quiz.xml to add these views
            LinearLayout quizContainer = findViewById(R.id.quizContainer);
            quizContainer.addView(questionView);
        }
    }
}

class Question {
    private int imageResourceId;
    private List<String> options;

    public Question(int imageResourceId, List<String> options) {
        this.imageResourceId = imageResourceId;
        this.options = options;
    }

    // Getters
    public int getImageResourceId() { return imageResourceId; }
    public List<String> getOptions() { return options; }
}
