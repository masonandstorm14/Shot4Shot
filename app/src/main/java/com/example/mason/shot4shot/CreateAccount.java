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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth Auth;
    private DatabaseReference database;

    private String email;
    private String password;
    private String username;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Firebase
        Auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("users");

        //All layout items
        final EditText Email = (EditText) findViewById(R.id.editText_createEmail);
        final EditText Password = (EditText) findViewById(R.id.editText_createPassword);
        final EditText Username = (EditText) findViewById(R.id.editText_userName);
        final TextView errorTextPassword = (TextView) findViewById(R.id.errorText_Password);
        final EditText confirmPassword = (EditText) findViewById(R.id.editText_confirmPassword);

        //Called when new account button is used
        Button createAccount = (Button) findViewById(R.id.btn_createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gets the values from each view
                email = Email.getText().toString();
                password = Password.getText().toString();
                username = Username.getText().toString();

                //initalizes the IsusernameTaken method
                IsUserNameTaken checker = new IsUserNameTaken();

                //test if each view has any data and if the username is not taken
                if (email != null && password != null && username != null && !email.equals("") && !password.equals("") && !username.equals("") && !checker.IsUserNameTaken(database, username)) {
                    //test if password and confirm password match
                    if (password.equals(confirmPassword.getText().toString())) {
                        Auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                    }
                                })

                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        //creats a new user in the database
                                        User user = new User(username, email, true);
                                        database.child("events").child(Auth.getCurrentUser().getUid()).setValue(user);
                                        //moves back to the login screen
                                        mover(CustomLogin.class);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //error text for failure to create account
                                        toast = Toast.makeText(CreateAccount.this, e.getMessage(), Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                    } else {
                        //error text for password and confirm password field not matching
                        toast = Toast.makeText(CreateAccount.this, "Passwords do not match", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else if (checker.IsUserNameTaken(database, username)) {
                    toast = Toast.makeText(CreateAccount.this, "Username Is Taken", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    //used to move classes
    protected void mover(Class move) {
        Intent i = new Intent(this, move);
        startActivity(i);
    }
}
