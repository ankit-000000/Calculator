package com.example.quiz_app;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_app.Adapters.TopicRecycleViewAdapter;
import com.example.quiz_app.Models.Topic;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity {

    private CircleImageView imageAdd;
    private AlertDialog addTopicDialog;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ArrayList<Topic> list;
    TopicRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        imageAdd = findViewById(R.id.iv_Add_Topics);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Topics");

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTopicDialog();
            }
        });
        list=new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.Admin_Topic_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicRecycleViewAdapter(this,list);
        recyclerView.setAdapter(adapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Topic topic=dataSnapshot.getValue(Topic.class);
                        list.add(topic);
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(AdminActivity.this, "topic doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private int getTopicCounter() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getInt("topic_counter", 0);
    }

    private void incrementTopicCounter() {
        int currentCounter = getTopicCounter();
        int newCounter = currentCounter + 1;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("topic_counter", newCounter);
        editor.apply();
    }

    private void showAddTopicDialog() {
        EditText topic_name;
        EditText Questions_No;
        Button save;

        // Inflate the dialog layout and find its views
        View dialogView = LayoutInflater.from(AdminActivity.this).inflate(R.layout.layout_add_topic, null);
        topic_name = dialogView.findViewById(R.id.etTopicName);
        Questions_No = dialogView.findViewById(R.id.etQuestionNo);
        save = dialogView.findViewById(R.id.btnSaveTopic);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        addTopicDialog = builder.create();
        addTopicDialog.show();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = topic_name.getText().toString();
                String questions = Questions_No.getText().toString();
               Topic topic=new Topic();
               topic.setTopicName(name);
               topic.setQuestion(questions);

                databaseReference.child("Topic"+ getTopicCounter()).setValue(topic);
                adapter.notifyDataSetChanged();
                addTopicDialog.dismiss();
                incrementTopicCounter();
            }
        });
    }
}
