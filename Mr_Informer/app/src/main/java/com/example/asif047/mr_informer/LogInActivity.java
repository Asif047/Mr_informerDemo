package com.example.asif047.mr_informer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailET;
    private EditText passwordET;

    private Button logInBtn;
    private Button registerBtn;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        emailET= (EditText) findViewById(R.id.edit_text_email);
        passwordET= (EditText) findViewById(R.id.edit_text_password);

        logInBtn= (Button) findViewById(R.id.button_log_in);
        registerBtn= (Button) findViewById(R.id.button_register);

        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null)
        {
            //profile activity here



            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }



        logInBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);


    }


    private void userLogin()
    {
        String email=emailET.getText().toString().trim();
        String password=passwordET.getText().toString().trim();


        if(TextUtils.isEmpty(email))
        {

            //email is empty
            Toast.makeText(this,"Please enter the email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this,"Please enter the password",Toast.LENGTH_SHORT).show();
            return;
        }



        progressDialog.setMessage("Registering user....");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful())
                        {
                            //start the profile activity

                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }

                        else
                        {
                            Toast.makeText(LogInActivity.this,"Your email or password doesn't match",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {

        if(view==logInBtn)
        {
            userLogin();
        }

        if(view==registerBtn)
        {

            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }

    }
}
