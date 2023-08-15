package com.example.quiz_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextView AlreadyHaveAccount;
    EditText Name,Email,Passwd,Phone;
    AppCompatButton btnRegister;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        AlreadyHaveAccount=findViewById(R.id.tv_HaveAnAccount);
        Name=findViewById(R.id.inputName);
        Email=findViewById(R.id.et_login_email);
        Passwd=findViewById(R.id.et_login_password);
        Phone=findViewById(R.id.inputPhoneNumber);
        btnRegister=findViewById(R.id.btnRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        fStore=FirebaseFirestore.getInstance();


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference=FirebaseDatabase.getInstance().getReference("Users");
                String name=Name.getText().toString();
                String email=Email.getText().toString();
                String passwd=Passwd.getText().toString();
                String phone=Phone.getText().toString();

                if (name.isEmpty()){
                    Name.setError("Fill Your name");
                }
                else{
                    if (email.isEmpty()){
                        Email.setError("Fill Your email");
                    }
                    else{
                        if (passwd.isEmpty()){
                            Passwd.setError("Fill Your password");
                        }
                        else{
                            if (phone.isEmpty() || Phone.length()!=10){
                                Phone.setError("Fill Your Number");
                            }
                            else{
                                firebaseAuth.createUserWithEmailAndPassword(email,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                          String  userId= task.getResult().getUser().getUid();
                                            DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users")
                                                    .document(userId);

                                            Map<String,Object> user = new HashMap<>();
                                            user.put("Name", name);
                                            user.put("Email", email);
                                            user.put("is_Admin", false);
                                            user.put("Phone Number", phone);

                                            userRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "User data saved successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Error saving user data", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "not added", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }

                    }
                }
            }
        });


        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }

}