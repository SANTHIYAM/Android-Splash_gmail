package com.example.googlesignindemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class GoogleLogin extends AppCompatActivity{

    int RC_SIGN_IN=0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initiating views
        signInButton=findViewById(R.id.sign_in_button);

        //configure sign in to request the user's ID,email address and basic
        //profile. ID and basic profile are included in the DEFAULT_SIGN_IN
        GoogleSignInOptions gso =new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

       //ERROR:-
       // signInButton.setOnClickListener({view}-> {signIn();});
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent signInIntent=mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RC_SIGN_IN);
            }

        });

    }
    /*private void signIn()
    {
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Result returned from launching the intent from GoogleSignInClient.getSignInIntent(, , );
        if (requestCode == RC_SIGN_IN) {
            //The Task returned from this call is always completed. no need to attach the listener
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try
        {
            GoogleSignInAccount account=completedTask.getResult(ApiException.class);
            //Signed in successfully, show authenticated UI
            startActivity(new Intent(GoogleLogin.this, UserDetails.class));
        }
        catch (ApiException e)
        {
            // The ApiException status code indicates the detailed failure reason
            //PLease refer the GoogleSignInStatusCode class reference for more information.
            Log.w("Google Sign in Error","signInResult:failed code"+e.getStatusCode());
            Toast.makeText(GoogleLogin.this,"Failed",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onStart()
    {
        //Check for existing Google Sign in account ,if the user is already signed in
        // the GoogleSignInAccount will be non-null
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null)
        {
            startActivity(new Intent(GoogleLogin.this, UserDetails.class));
        }
        super.onStart();
    }

}
