package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Created by Max on 5/5/2016.
 */
public class StartingActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ParseUser.getCurrentUser() != null){
            //start main activity
            startActivity(new Intent(this,MainActivity.class));
        } else {
            //sign in
            startActivity(new Intent(this,SignIn.class));
        }
    }
}
