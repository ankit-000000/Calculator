package com.example.quiz_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScoreActivity extends AppCompatActivity {
    TextView username,userScore,correctAns,wrongAns;
    Button retry,quit;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        username=findViewById(R.id.usernameText);
        userScore=findViewById(R.id.scoreText);
        correctAns=findViewById(R.id.rightAnswersText);
        wrongAns=findViewById(R.id.wrongAnswersText);
        firebaseAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        String userId=firebaseAuth.getCurrentUser().getUid();
        DocumentReference userRef = fStore.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                   username.setText( documentSnapshot.getString("Name"));
                }
                else {
                    Toast.makeText(ScoreActivity.this, "document empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        retry=findViewById(R.id.retryButton);
        quit=findViewById(R.id.quitButton);

        userScore.setText("Total Score: "+getIntent().getIntExtra("score",0));
        correctAns.setText("Correct Answers:"+getIntent().getIntExtra("rightAns",0));
        wrongAns.setText("Wrong Answers: "+getIntent().getIntExtra("wrongAns",0));

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserQuestionsActivity.class));
                finish();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Home.class));
                finish();
            }
        });

    }
}