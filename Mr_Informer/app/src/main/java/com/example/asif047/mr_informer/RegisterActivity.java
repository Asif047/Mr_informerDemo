package com.example.asif047.mr_informer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{



    private EditText nameET;
    private EditText emailET;
    private EditText passwordET;

    private Button registerBtn;
    private Button signinBtn;

    private String temp_name;

    private FirebaseAuth firebaseAuth;


    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);


        if(!isConnected(RegisterActivity.this))buildDialog(RegisterActivity.this).show();
        else
        {

        }




        //new starts

        progressDialog=new ProgressDialog(this);

        nameET= (EditText) findViewById(R.id.edit_text_name);
        emailET= (EditText) findViewById(R.id.edit_text_email);
        passwordET= (EditText) findViewById(R.id.edit_text_password);


        registerBtn= (Button) findViewById(R.id.button_register_user);
        signinBtn= (Button) findViewById(R.id.button_signin_user);
        firebaseAuth=FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser()!=null)
        {
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }



        registerBtn.setOnClickListener(this);
        signinBtn.setOnClickListener(this);


        //new ends

    }



    public boolean isConnected(Context context)
    {
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo=cm.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnectedOrConnecting())
        {
            android.net.NetworkInfo wifi=cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile=cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile!=null && mobile.isConnectedOrConnecting())||(wifi!=null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;

        }
        else
            return false;
    }




    public AlertDialog.Builder buildDialog(Context c)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have mobile data or wifi to access this.Press ok to exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        return builder;
    }







    //new starts


    private void registerUser()
    {
        String name=nameET.getText().toString().trim();
        String email=emailET.getText().toString().trim();
        String password=passwordET.getText().toString().trim();


        if(TextUtils.isEmpty(name))
        {

            //email is empty
            Toast.makeText(this,"Please enter the name",Toast.LENGTH_SHORT).show();
            return;
        }


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

        if(TextUtils.getTrimmedLength(password)<6)
        {
            Toast.makeText(this,"password should be at least 6 characters",Toast.LENGTH_SHORT).show();
            return;
        }



        progressDialog.setMessage("Registering user....");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            userProfile();

                            //profile activity here
                            finish();

                            temp_name=nameET.getText().toString().trim();
                            //Toast.makeText(MainActivity.this,temp_name, Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(RegisterActivity.this, ProfileActivity.class);
                            i.putExtra("user_name",temp_name);

                            startActivity(i);


                        }

                        else
                        {
                            Toast.makeText(RegisterActivity.this,"Your email is not corrent",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                });

    }



    private void userProfile()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();

        if(user!=null)
        {
            UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameET.getText().toString().trim()).build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Log.d("TESTING","User profile updated");
                                Toast.makeText(RegisterActivity.this,"User profile updated",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }



    }


    //if the validation is okk
    //will show the progress bar

    @Override
    public void onClick(View view) {

        if(view==registerBtn)
        {
            // user register button action
            registerUser();
        }

        if(view==signinBtn)
        {
            //for the log in activity
            startActivity(new Intent(this,LogInActivity.class));
        }

    }

    //new ends





}
