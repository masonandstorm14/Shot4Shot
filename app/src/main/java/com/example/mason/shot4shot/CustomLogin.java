package com.example.mason.shot4shot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomLogin extends AppCompatActivity {

    private FirebaseAuth auth;

    private String email;
    private String password;
    private DatabaseReference database;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_login);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        TextView createAccount = (TextView) findViewById(R.id.textView_makeAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mover(CreateAccount.class);
            }
        });

        final EditText emailText = (EditText) findViewById(R.id.editText_createEmail);
        final EditText passwordText = (EditText) findViewById(R.id.editText_createPassword);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("sign", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("sign", "onAuthStateChanged:signed_out");
                }
            }
        };


        Button Login = (Button) findViewById(R.id.btn_createAccount);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = emailText.getText().toString();
                password = passwordText.getText().toString();


                if(email != null && password !=null && !email.equals("") && !password.equals("")) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.toString();
                                }
                            })
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    database.child("users").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User user = dataSnapshot.getValue(User.class);
                                            if (user.newAccount == true) {
                                                mover(NewAccount.class);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }

    protected void mover(Class move){
        Intent i = new Intent(this, move);
        startActivity(i);
    }
}
