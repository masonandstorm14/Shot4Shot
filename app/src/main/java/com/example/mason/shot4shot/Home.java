package com.example.mason.shot4shot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        database = FirebaseDatabase.getInstance().getReference();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    database.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if(user.newAccount == true){
                                mover(NewAccount.class);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else {
                    // User is signed out

                }
            }
        };


        final Button customLogin = (Button) findViewById(R.id.btn_EmailLogin);
        customLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mover(CustomLogin.class);
            }
        });
    }

    protected void mover(Class move){
        Intent i = new Intent(this, move);
        startActivity(i);
    }
}
