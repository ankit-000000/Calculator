package com.example.quiz_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.quiz_app.Adapters.questionRecycleViewAdapter;
import com.example.quiz_app.Models.QuestionModel;
import com.example.quiz_app.R;
import com.example.quiz_app.R.id;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNewQuestionActivity extends AppCompatActivity {

    private TextView tvCorrectAnswer;
    private EditText etQuestion;
    private EditText etOptionA;
    private EditText etOptionB;
    private EditText etOptionC;
    private EditText etOptionD;
    private RadioGroup rgOptions;
    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;
    private RadioButton radioButtonD;
    private Button btnUpload;
    questionRecycleViewAdapter adapter;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);

        databaseReference = FirebaseDatabase.getInstance().getReference("Questions");

        etQuestion = findViewById(R.id.et_input_questions);
        etOptionA = findViewById(R.id.editTextText2);
        etOptionB = findViewById(R.id.editTextText3);
        etOptionC = findViewById(R.id.editTextText4);
        etOptionD = findViewById(R.id.editTextText5);
        rgOptions = findViewById(R.id.rg_options);
        radioButtonA = findViewById(R.id.radioButton);
        radioButtonB = findViewById(R.id.radioButton2);
        radioButtonC = findViewById(R.id.radioButton3);
        radioButtonD = findViewById(R.id.radioButton4);
        btnUpload = findViewById(R.id.btn_upload);
        tvCorrectAnswer=findViewById(id.tv_CorrectAnswer);

        tvCorrectAnswer.setText(getSelectedOption());


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveQuestionToFirebase();
            }
        });
    }

    private void saveQuestionToFirebase() {
        String question = etQuestion.getText().toString();
        String optionA = etOptionA.getText().toString();
        String optionB = etOptionB.getText().toString();
        String optionC = etOptionC.getText().toString();
        String optionD = etOptionD.getText().toString();
        String correctAns = getSelectedOption();

        // Generate a unique key for the question using push() method
        String key = databaseReference.push().getKey();

        QuestionModel questionModel = new QuestionModel(question, optionA, optionB, optionC, optionD, correctAns, key);

        // Save the question data to Firebase with the unique key
        String name= getIntent().getStringExtra("topic");
        databaseReference.child(name).child(key).setValue(questionModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                etQuestion.setText("");
                etOptionA.setText("");
                etOptionB.setText("");
                etOptionC.setText("");
                etOptionD.setText("");
            }
        });
    }

    private String getSelectedOption() {
        int selectedOptionId = rgOptions.getCheckedRadioButtonId();
        if (selectedOptionId == R.id.radioButton) {
            return "A";
        } else if (selectedOptionId == R.id.radioButton2) {
            return "B";
        } else if (selectedOptionId == R.id.radioButton3) {
            return "C";
        } else if (selectedOptionId == R.id.radioButton4) {
            return "D";
        }
        return "";
    }
}
