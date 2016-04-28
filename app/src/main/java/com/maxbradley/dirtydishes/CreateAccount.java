package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Cameron on 4/24/2016.
 */
public class CreateAccount extends Activity {

    private EditText userNameText;
    private EditText newPasswordText;
    private EditText confirmPasswordText;
    private Button createAccountButton;
    private Button cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        userNameText = (EditText) findViewById(R.id.newUsernameText);
        newPasswordText = (EditText) findViewById(R.id.newPasswordText);
        confirmPasswordText = (EditText) findViewById(R.id.confirmPasswordText);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        cancelButton = (Button) findViewById(R.id.cancelAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = userNameText.getText().toString();
                String password = newPasswordText.getText().toString();
                String confirmPass = confirmPasswordText.getText().toString();

                if ( password.equals(confirmPass) ){

                    /* TODO - check if username is already used in database */

                    /* Create intent, pack data into intent and return */
                    Intent data = new Intent();
                    data.putExtra(MainActivity.USERNAME,username);
                    data.putExtra(MainActivity.PASSWORD,password);
                    data.putExtra(MainActivity.CREATE_OR_SIGN_IN,MainActivity.CREATE_ACCOUNT);
                    setResult(RESULT_OK, data);
                    finish();

                }else{
                    String message = "Passwords entered do not match.";
                    Toast t = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG);
                    t.show();

                    reset();
                }

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

    protected void reset(){
        userNameText.setText("");
        newPasswordText.setText("");
        confirmPasswordText.setText("");
    }

}
