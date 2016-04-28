package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Max on 4/25/2016.
 */
public class CodeActivity extends Activity {

    private EditText codeEnter;
    private Button joinButton;
    private Button generateButton;
    private TextView generatedCode;
    private String code;

    private String userName;
    private String password;

    private final String TAG = "CodeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_layout);

        Intent incomingIntent = getIntent();

        userName = incomingIntent.getStringExtra(MainActivity.USERNAME);
        password = incomingIntent.getStringExtra(MainActivity.PASSWORD);
        Log.d(TAG,userName);
        Log.d(TAG, password);

        codeEnter = (EditText) findViewById(R.id.codeEnter);
        joinButton = (Button) findViewById(R.id.joinButton);
        generateButton = (Button) findViewById(R.id.generateCodeButton);
        generatedCode = (TextView) findViewById(R.id.generatedCode);



        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredCode = codeEnter.getText().toString();
                Log.d(TAG,"Entered code: " + code);

                /*Temporary*/
                if (!enteredCode.equals("") && enteredCode.equals(code)) {
                    //go to Chore list

                    Intent intent = new Intent(CodeActivity.this,MainActivity.class);
                    intent.putExtra("signedIn",true);
                    startActivity(intent);
                }

                //TODO: Check database for code

                //if code not in database, show Toast
                //Toast.makeText(getApplicationContext(),"Room not found",Toast.LENGTH_LONG).show();

                //else go to the room
            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* generate a new code. 3 letters (A-F), 3 numbers (0-9).
                Check if it is already in use  */
                code = "";
                Object[][] codeArray = new Object[2][3];
                //generate 3 numbers 0-9
                Random r = new Random();
                codeArray[0][0] = r.nextInt(10);
                codeArray[0][1] = r.nextInt(10);
                codeArray[0][2] = r.nextInt(10);

                //generate 3 letters
                String[] letterOptions = {"A","B","C","D","E","F"};
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
                    for(int j = 0; j < 3; j++){
                        code += codeArray[i][j];
                    }
                }

                Log.d(TAG,"Generated code: " + code);
                generatedCode.setText("Code: " + code);



                //TODO: Check if code is already used in database

            }
        });
    }

    public String getCode() {
        return this.code;
    }
}
