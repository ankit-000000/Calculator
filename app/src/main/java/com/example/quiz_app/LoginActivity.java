package com.example.quiz_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    TextView create_newAccount;
    EditText Email, Passwd;
    Button btnLogin;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        Email = findViewById(R.id.et_login_email);
        Passwd = findViewById(R.id.et_login_password);
        create_newAccount = findViewById(R.id.tv_HaveAnAccount);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String passwd = Passwd.getText().toString();

                if (email.isEmpty()) {
                    Email.setError("Fill Email");
                } else if (passwd.isEmpty()) {
                    Passwd.setError("fill password");
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               String userId=firebaseAuth.getCurrentUser().getUid();
                                check_User_Role(userId);
                            } else {
                                Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        create_newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }
        });
    }

    private void check_User_Role(String userId) {
        if (userId != null) {
                DocumentReference userRef = fStore.collection("Users").document(userId);
                userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                   if (documentSnapshot.exists()){
                       if (documentSnapshot.getBoolean("is_Admin")){
                           startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                           finish();
                       }
                       else {
                           startActivity(new Intent(getApplicationContext(),Home.class));
                           finish();
                       }
                   }
                    }
                });
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
        }
    }
}
