package com.example.quiz_app;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz_app.Models.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserQuestionsActivity extends AppCompatActivity {

    private ArrayList<QuestionModel> list;
    private int position = 0;
    private int score = 0;
    private int right=0;
    private int wrong=0;

    private int selectedOptionIndex = -1;
    LinearLayout linearLayout;
    TextView tvQuestionNo, tvQuestion;
    Button btnOption1, btnOption2, btnOption3, btnOption4, btnNext;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_questions);

        // Initialize views
        tvQuestionNo = findViewById(R.id.tv_user_questionNo);
        tvQuestion = findViewById(R.id.tv_user_question);
        btnOption1 = findViewById(R.id.btn_Option1);
        btnOption2 = findViewById(R.id.btn_Option2);
        btnOption3 = findViewById(R.id.btn_Option3);
        btnOption4 = findViewById(R.id.btn_Option4);
        btnNext = findViewById(R.id.btnNext);

        linearLayout = findViewById(R.id.option_layout);
        btnNext.setEnabled(false);

        list = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions");

        String topic = getIntent().getStringExtra("topic_name");
        databaseReference.child(topic).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    QuestionModel questionModel = dataSnapshot.getValue(QuestionModel.class);
                    list.add(questionModel);
                }
                if (list.size() > 0) {
                    showQuestion(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });
    }

    private void showQuestion(int position) {
        QuestionModel question = list.get(position);
        tvQuestionNo.setText((position + 1) + "/" + list.size());
        tvQuestion.setText(question.getQuestion());
        btnOption1.setText(question.getOptionA());
        btnOption2.setText(question.getOptionB());
        btnOption3.setText(question.getOptionC());
        btnOption4.setText(question.getOptionD());

        // Set onClickListener for the next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedOptionIndex >= 0) {
                    checkAnswer(selectedOptionIndex);
                    for (int i = 0; i < 4; i++) {
                        Button button = (Button) linearLayout.getChildAt(i);
                        button.setAlpha(1f);
                        button.setBackgroundResource(R.drawable.option_btn); // Use the selector drawable
                    }

                } else {
                    Toast.makeText(UserQuestionsActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClickListeners for each option button
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            Button optionButton = (Button) linearLayout.getChildAt(i);
            optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(optionIndex);
                }
            });
        }
    }

    private void selectOption(int optionIndex) {
        // Reset the background color of all option buttons using the selector drawable
        for (int i = 0; i < 4; i++) {
            Button button = (Button) linearLayout.getChildAt(i);
            button.setAlpha(0.5f);// Use the selector drawable
        }

        // Change the background color of the selected option
        Button selectedButton = (Button) linearLayout.getChildAt(optionIndex);
        selectedButton.setAlpha(1f);
        selectedButton.setBackgroundResource(R.drawable.selected_option_btn); // Use the selector drawable

        // Update the selected option index
        selectedOptionIndex = optionIndex;

        // Enable the next button after selecting an option
        btnNext.setEnabled(true);
    }

    private void checkAnswer(int optionIndex) {
        String selectedOption = getSelectedOption(optionIndex);
        QuestionModel currentQuestion = list.get(position);
        String correctAnswer = currentQuestion.getCorrectAns();
        Log.d(TAG, "checkAnswer: "+selectedOption);
        Log.d(TAG, "checkAnswer: "+currentQuestion.getCorrectAns());
        if (selectedOption.equals(correctAnswer)) {
            Log.d(TAG, "hello");
            score++;
            right++;
        }
        position++;
        if (position < list.size()) {
            showQuestion(position);
        } else {
            wrong=(list.size()) - right;
            // Transfer user to the next page to see the score
            Toast.makeText(this, ""+score, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ScoreActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("rightAns",right);
            intent.putExtra("wrongAns",wrong);
            intent.putExtra("totalQuestions", list.size());
            startActivity(intent);
        }
    }

    private String getSelectedOption(int optionIndex) {
        switch (optionIndex) {
            case 0:
                return "A";
            case 1:
                return "B";
            case 2:
                return "C";
            case 3:
                return "D";
            default:
                return "";
        }
    }
}
