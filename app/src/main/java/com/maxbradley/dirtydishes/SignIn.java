package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Cameron on 4/24/2016.
 */
public class SignIn extends Activity {


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

                String username = userNameText.getText().toString();
                String password = passwordText.getText().toString();

                /* Create intent, pack data into intent and return */
                Intent data = new Intent();
                data.putExtra(MainActivity.USERNAME, username);
                data.putExtra(MainActivity.PASSWORD, password);
                data.putExtra(MainActivity.CREATE_OR_SIGN_IN,MainActivity.SIGN_IN);
                setResult(RESULT_OK, data);
                finish();

            }
        });

        createAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignIn.this,CreateAccount.class);
                startActivityForResult(intent, MainActivity.REQUEST_CREATE_ACCOUNT);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

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
