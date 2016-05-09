package com.maxbradley.dirtydishes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Cameron on 4/24/2016.
 */
public class SignIn extends AppCompatActivity {


    private EditText userNameText;
    private EditText passwordText;
    private Button signInButton;
    private Button cancelButton;
    private TextView createAccountText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        userNameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        signInButton = (Button) findViewById(R.id.signInButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        createAccountText = (TextView) findViewById(R.id.createAccountLink);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = userNameText.getText().toString();
                final String password = passwordText.getText().toString();

                final ProgressDialog dialog = new ProgressDialog(SignIn.this);
                dialog.setMessage("Signing in...");
                dialog.show();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        dialog.dismiss();
                        if (e != null) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            /* Create intent, pack data into intent and return */
                            Intent data = new Intent(SignIn.this,MainActivity.class);
                            data.putExtra(MainActivity.USERNAME, username);
                            data.putExtra(MainActivity.PASSWORD, password);
                            //data.putExtra(MainActivity.CREATE_OR_SIGN_IN, MainActivity.SIGN_IN);
                           // setResult(RESULT_OK, data);
                           // finish();
                            startActivity(data);
                            finish();
                        }
                    }
                });

            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this,CreateAccount.class);
                startActivity(intent);
                finish();
              //  startActivityForResult(intent, MainActivity.REQUEST_CREATE_ACCOUNT);

            }
        });

        /*cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });*/

    }


    /* Pass this back to the SignIn Activity */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){

            if( requestCode == MainActivity.REQUEST_CREATE_ACCOUNT ){

                setResult(RESULT_OK, data);
                finish();

            }

        }

    }
}
