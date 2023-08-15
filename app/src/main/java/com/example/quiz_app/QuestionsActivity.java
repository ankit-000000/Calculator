package com.example.quiz_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_app.Adapters.questionRecycleViewAdapter;
import com.example.quiz_app.Models.QuestionModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    ImageView add_image_view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<QuestionModel> list;
    questionRecycleViewAdapter adapter;
    RecyclerView recyclerView;
    String topic_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions");
        add_image_view = findViewById(R.id.Add_Questions);

        recyclerView = findViewById(R.id.rv_question);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new questionRecycleViewAdapter(this, list);
        recyclerView.setAdapter(adapter);

        topic_name = getIntent().getStringExtra("topic_name");

        add_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewQuestionActivity.class);
                intent.putExtra("topic", topic_name);
                startActivity(intent);
            }
        });

        retrieveQuestions();
    }

    private void retrieveQuestions() {
        databaseReference.child(topic_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    QuestionModel questionModel = dataSnapshot.getValue(QuestionModel.class);
                    list.add(questionModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if necessary
            }
        });
    }
}
