package com.maxbradley.dirtydishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Max on 4/25/2016.
 */
public class CodeActivity extends AppCompatActivity {

    private EditText codeEnter;
    private Button joinButton;
    private Button generateButton;
    private String code;

    private String username;
    private String password;

    private final String TAG = "CodeActivity";
    boolean found = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_layout);

        Intent incomingIntent = getIntent();

        username = incomingIntent.getStringExtra(MainActivity.USERNAME);
        password = incomingIntent.getStringExtra(MainActivity.PASSWORD);
        Log.d(TAG,username);
        if (password != null) {
            Log.d(TAG, password);
        }

        codeEnter = (EditText) findViewById(R.id.codeEnter);
        joinButton = (Button) findViewById(R.id.joinButton);
        generateButton = (Button) findViewById(R.id.generateCodeButton);



        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = codeEnter.getText().toString();
                Log.d(TAG,"Entered code: " + enteredCode);

                /*Temporary*/
                if (!enteredCode.equals("")) {
                    //query for code
                    ParseQuery<Apartment> query = Apartment.getQuery();
                    query.whereEqualTo("objectId",enteredCode);
                    query.findInBackground(new FindCallback<Apartment>() {
                        @Override
                        public void done(List<Apartment> objects, ParseException e) {
                            if (e == null) {
                                if (objects == null || objects.size() == 0) {
                                    //no code
                                    Toast.makeText(getApplicationContext(), "Room is not valid", Toast.LENGTH_SHORT).show();
                                } else {

                                    Apartment apt = objects.get(0);
                                    ParseUser.getCurrentUser().put("apartment", apt.getApartmentCode());
                                    ParseUser.getCurrentUser().saveInBackground();

                                    //go to chore list
                                    Intent data = new Intent(CodeActivity.this, MainActivity.class);
                                    data.putExtra(MainActivity.USERNAME, username);
                                    if (null != password) {
                                        data.putExtra(MainActivity.PASSWORD, password);
                                    }
                                    startActivity(data);

                                }
                            }
                        }
                    });

                    Intent intent = new Intent(CodeActivity.this,MainActivity.class);
                    intent.putExtra("signedIn",true);
                    startActivity(intent);
                }
                
            }
        });



        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make a new apartment in Parse and use the objectId as the code
                final Apartment apartment = new Apartment();
                apartment.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser.getCurrentUser().put("apartment", apartment.getApartmentCode());
                            ParseUser.getCurrentUser().saveInBackground();
                        }
                    }
                });
                //Apartment objectId is also the code


                Intent data = new Intent(CodeActivity.this, MainActivity.class);
                data.putExtra(MainActivity.USERNAME, username);
                if (null != password) {
                    data.putExtra(MainActivity.PASSWORD, password);
                }
                startActivity(data);

                /*

                Using the Apartment objectId as the code. Guarantees no duplicates and more complex
                than what was previously being used

                 */


                /* generate a new code. 3 letters (A-F), 3 numbers (0-9).
                Check if it is already in use  */
/*

                    Object[][] codeArray = new Object[2][3];
                    //generate 3 numbers 0-9
                    Random r = new Random();
                    codeArray[0][0] = r.nextInt(10);
                    codeArray[0][1] = r.nextInt(10);
                    codeArray[0][2] = r.nextInt(10);

                    //generate 3 letters
                    String[] letterOptions = {"A", "B", "C", "D", "E", "F"};
                    codeArray[1][0] = letterOptions[r.nextInt(6)];
                    codeArray[1][1] = letterOptions[r.nextInt(6)];
                    codeArray[1][2] = letterOptions[r.nextInt(6)];

                    //combine them randomly
                    //2d array to hold both numbers and letters. 2 rows, 3 columns
                    // First row is numbers, second is letters
                    //have a loop that randomly swaps elements

                    for (int i = 0; i < 10; i++) {
                        int ele1a = r.nextInt(2);
                        int ele1b = r.nextInt(3);

                        int ele2a = r.nextInt(2);
                        int ele2b = r.nextInt(3);

                        Object temp = codeArray[ele1a][ele1b];
                        codeArray[ele1a][ele1b] = codeArray[ele2a][ele2b];
                        codeArray[ele2a][ele2b] = temp;
                    }

                    //put code into a string
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 3; j++) {
                            code += codeArray[i][j];
                        }
                    }
                    */


                //    data.putExtra(MainActivity.CREATE_OR_SIGN_IN, MainActivity.SIGN_IN);


            }
        });
    }

}
