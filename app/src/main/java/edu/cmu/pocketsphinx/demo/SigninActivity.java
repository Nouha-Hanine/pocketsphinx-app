package edu.cmu.pocketsphinx.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity {
    EditText email;
    EditText username;
    EditText password;
    Button signinButton;
    DatabaseHelper DBhelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signinButton = findViewById(R.id.signinButton);
        DBhelper= new DatabaseHelper(this);
        signinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String uname, mail, pwd;
                uname= username.getText().toString();
                mail= email.getText().toString();
                pwd=password.getText().toString();
                if(uname == null || pwd == null || mail ==null){
                    Toast.makeText(SigninActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    if(DBhelper.checkEmail(mail)){
                        Toast.makeText(SigninActivity.this, "Email already used!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                   boolean SigninSuccess= DBhelper.insertUser(mail, uname, pwd);
                   if(SigninSuccess){
                       Toast.makeText(SigninActivity.this, "Signin done successfully", Toast.LENGTH_SHORT).show();
                   }else{
                       Toast.makeText(SigninActivity.this, "Signin failed", Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });

    }

}