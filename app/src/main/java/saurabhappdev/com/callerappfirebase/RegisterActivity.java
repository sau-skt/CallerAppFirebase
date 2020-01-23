package saurabhappdev.com.callerappfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import saurabhappdev.com.callerappfirebase.Models.User;

public class RegisterActivity extends AppCompatActivity {
    EditText edName, edEmail, edPassword;
    Button RegBtn;
    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edName = (EditText) findViewById(R.id.Regname);
        edEmail = (EditText) findViewById(R.id.Regemail);
        edPassword = (EditText) findViewById(R.id.RegPassword);
        RegBtn = (Button) findViewById(R.id.RegBtn);
        auth=FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void register(){
                final String name = edName.getText().toString();
                final String email = edEmail.getText().toString();
                final String password = edPassword.getText().toString();

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    User user = new User(name,email,password,firebaseUser.getUid());
                                    reference.child(firebaseUser.getUid()).setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        finish();
                                                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(RegisterActivity.this, "User could not be registered", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
    }
}
