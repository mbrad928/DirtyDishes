package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Cameron on 5/5/2016.
 */
public class ChangePassword extends Activity {

    EditText currentPassword;
    EditText newPassword;
    EditText confirmPassword;
    Button submitButton;
    Button cancelButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.change_password);

        currentPassword = (EditText) findViewById(R.id.currentPasswordText);
        newPassword = (EditText) findViewById(R.id.newPasswordText);
        confirmPassword = (EditText) findViewById(R.id.confirmPasswordText);
        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currPass = currentPassword.getText().toString();
                final String newPass = newPassword.getText().toString();
                String confirmPass = confirmPassword.getText().toString();


                // Check to make sure user entered new password correctly both times
                if (!newPass.equals(confirmPass)) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "New password and confirmed password do not match.",
                            Toast.LENGTH_LONG);
                    t.show();
                    clearFields();



                // Make sure password isn't too short
                }else if(newPass.length() <= 6){
                    Toast t = Toast.makeText(getApplicationContext(),
                            "New password is too short, please use at least 7 characters",
                            Toast.LENGTH_LONG);
                    t.show();
                    clearFields();



                // Make sure new password != old password
                }else if(newPass.equals(currPass)){

                    Toast t = Toast.makeText(getApplicationContext(),
                            "New password cannot equal current password",
                            Toast.LENGTH_LONG);
                    t.show();
                    clearFields();



                // Attempt to reset password
                }else{


                    // Get current Parse user
                    final ParseUser currentUser = ParseUser.getCurrentUser();

                    // Attempt to log in with supplied password - determines if password is correct
                    ParseUser.logInInBackground(currentUser.getUsername(), currPass, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            // Correct password entered - reset password
                            if (user != null){
                                currentUser.setPassword(newPass);

                                currentUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (null == e){
                                            Log.i("RoomMe","Password change success!");
                                            Toast t = Toast.makeText(getApplicationContext(),
                                                    "Password successfully changed!",
                                                    Toast.LENGTH_LONG);
                                            t.show();
                                        }else{
                                            Log.i("RoomMe","Password change error!");
                                        }
                                    }
                                });

                                finish();

                            // Wrong password entered - reset fields
                            }else{
                                Toast t = Toast.makeText(getApplicationContext(),
                                        "Incorrect current password given; please try again",
                                        Toast.LENGTH_LONG);
                                t.show();
                                clearFields();
                            }
                        }
                    });

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

    private void clearFields(){
        currentPassword.setText("");
        newPassword.setText("");
        confirmPassword.setText("");
    }

}
