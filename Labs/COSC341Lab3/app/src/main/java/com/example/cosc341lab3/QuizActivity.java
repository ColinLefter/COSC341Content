package com.example.cosc341lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    private int currentQuestionIdx = 0;
    private int numQuestions;
    private int numCorrect = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        numQuestions = Integer.parseInt(getIntent().getStringExtra("numberOfQuestions"));
        String category = getIntent().getStringExtra("category");

        loadQuestions(category);
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
            questions = new String[0];  // No questions (just a fallback)
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
            options = new String[0][0];  // No options (just a fallback)
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
            correctAnswers = new String[0];  // No correct answers (just a fallback)
        }
        return correctAnswers;
    }

    private void loadQuestions(String category) {
        String[] questionsToAsk = getCategoryQuestions(category);
        String[][] categoryOptions = getQuestionOptions(category);
        String[] correctAnswers = getCorrectAnswersForQuestions(category);

        // this task requires us to dynamically set the layout display using the data above
        // the solution to this is to use a layout inflater to create views for each question
        // where the base template is the question_layout.xml template
        // all we have to do is replace the template image and text view with the right content
        // and then append radio buttons to the empty radio group

        if (currentQuestionIdx <= numQuestions) {
            loadQuestionView(questionsToAsk, categoryOptions, correctAnswers, category);
        } else { // Now we need to show the results

            // Now we need to display the results page by inflating it to the main quiz container
            LayoutInflater inflater = LayoutInflater.from(this);
            ConstraintLayout quizContainer = findViewById(R.id.quizContainer);
            View resultsView = inflater.inflate(R.layout.results_page_layout, quizContainer, false);
            setContentView(resultsView);

            TextView resultsPlaceholder = findViewById(R.id.results_placeholder);
            resultsPlaceholder.setText(String.format(
                    Locale.getDefault(), // Just to suppress the error
                    "%d/%d",
                    numCorrect,
                    numQuestions
            ));
        }
    }

    private void loadQuestionView(String[] questionsToAsk, String[][] categoryOptions, String[] correctAnswers, String category) {
        LayoutInflater inflater = LayoutInflater.from(this);
        ConstraintLayout quizContainer = findViewById(R.id.quizContainer);
        View questionView = inflater.inflate(R.layout.question_layout, quizContainer, false);

        // Locate the image and dynamically update the image depending on the current question
        ImageView questionImage = questionView.findViewById(R.id.imageView);
        switch (category) {
            case "About Canada":
                // Set the image resource based on the question number
                @SuppressLint("DiscouragedApi") int imageResId = getResources().getIdentifier(
                        "about_canada" + (currentQuestionIdx + 1),
                        "drawable", getPackageName()
                );
                questionImage.setImageResource(imageResId);
                break;
            case "Canadian Capital":
                @SuppressLint("DiscouragedApi") int imageResId2 = getResources().getIdentifier(
                        "capital" + (currentQuestionIdx + 1),
                        "drawable",
                        getPackageName());
                questionImage.setImageResource(imageResId2);
                break;
            case "Canadian Mountains":
                @SuppressLint("DiscouragedApi") int imageResId3 = getResources().getIdentifier(
                        "mountain" + (currentQuestionIdx + 1),
                        "drawable",
                        getPackageName());
                questionImage.setImageResource(imageResId3);
                break;
        }

        TextView questionText = questionView.findViewById(R.id.textView);
        questionText.setText(questionsToAsk[currentQuestionIdx]);

        // Locate the RadioGroup and iterate through the list of options, appending individual radio buttons
        RadioGroup optionsGroup = questionView.findViewById(R.id.radioGroupOptions);

        for (String option : categoryOptions[currentQuestionIdx]) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            optionsGroup.addView(radioButton);
        }

        // Locating the next button
        Button nextButton = questionView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            // Incrementing the question index counter and loading the next view
            // We need to process the user input right at this point before we clear the view
            // We do so by obtaining the selected option from the radio group
            String correctAnswer = correctAnswers[currentQuestionIdx++]; // Post-increment
            int selectedId = optionsGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = questionView.findViewById(selectedId);
            if (selectedRadioButton != null && selectedRadioButton.getText().equals(correctAnswer)) {
                numCorrect++;
            }

            quizContainer.removeAllViews();
            loadQuestions(category);
        });
        // Adding the question view to the main quiz view
        quizContainer.addView(questionView);
    }
}