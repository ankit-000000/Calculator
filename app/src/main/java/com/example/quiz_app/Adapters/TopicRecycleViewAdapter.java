package com.example.quiz_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz_app.AdminActivity;
import com.example.quiz_app.Home;
import com.example.quiz_app.Models.Topic;
import com.example.quiz_app.QuestionsActivity;
import com.example.quiz_app.R;
import com.example.quiz_app.UserQuestionsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TopicRecycleViewAdapter extends RecyclerView.Adapter<TopicRecycleViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Topic> list;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    public TopicRecycleViewAdapter(Context context, ArrayList<Topic> list) {
        this.context = context;
        this.list = list;
        firebaseAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        fStore = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore
    }


    @NonNull
    @Override
    public TopicRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.topic_layout,parent,false);
       ViewHolder viewHolder=new ViewHolder(view);
       return viewHolder;
    }
    public void deleteTopic(int position) {
        if (position >= 0 && position < list.size()) {
            // Construct the key based on the position
            String key = "Topic" + position;

            // Delete the topic from Firebase using the constructed key
            DatabaseReference topicRef = FirebaseDatabase.getInstance().getReference("Topics").child(key);
            topicRef.removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Remove the topic from the list and notify the adapter
                                list.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                // Handle error if the deletion fails
                                Log.e("TopicRecycleViewAdapter", "Error deleting topic from Firebase: " + task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TopicRecycleViewAdapter.ViewHolder holder, int position) {
        Topic topic=list.get(position);
        holder.textView.setText(topic.getTopicName());
        holder.textView1.setText(topic.getQuestion());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId=firebaseAuth.getCurrentUser().getUid();
                check_User_Role(userId,topic);

            }
        });
    }
    private void check_User_Role(String userId,Topic topic) {
        if (userId != null) {
            DocumentReference userRef = fStore.collection("Users").document(userId);
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        if (documentSnapshot.getBoolean("is_Admin")){
                            Intent intent=new Intent(context, QuestionsActivity.class);
                            intent.putExtra("topic_name",topic.getTopicName());
                            context.startActivity(intent);
                        }
                        else {
                            Intent intent=new Intent(context, UserQuestionsActivity.class);
                            intent.putExtra("topic_name",topic.getTopicName());
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             textView=itemView.findViewById(R.id.tv_topic_name);
            textView1=itemView.findViewById(R.id.tv_no_of_questions);
        }
    }
}
