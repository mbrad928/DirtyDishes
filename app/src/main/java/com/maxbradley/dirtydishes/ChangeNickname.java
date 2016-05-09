package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by Cameron on 5/8/2016.
 */
public class ChangeNickname extends Activity {

    private TextView currNicknameText;
    private EditText newNicknameText;
    private Button submitButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_nickname);

        currNicknameText = (TextView) findViewById(R.id.currNicknameText);
        newNicknameText = (EditText) findViewById(R.id.newNicknameText);
        submitButton = (Button) findViewById(R.id.submitButton);
        cancelButton =(Button) findViewById(R.id.cancelButton);

        // Check for user nickname, and display
        ParseUser user = ParseUser.getCurrentUser();
        String nickname = (String) user.get("nickname");

        if(null == nickname || nickname.equals("")){
            currNicknameText.setText("<no current nickname>");
        }else{
            currNicknameText.setText(nickname);
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currNickname = currNicknameText.getText().toString();
                String newNickname = newNicknameText.getText().toString();

                if (newNickname.equals("")){
                    Toast t = Toast.makeText(getApplicationContext(),
                            "You must enter a valid nickname",
                            Toast.LENGTH_LONG);
                    t.show();

                }else if (newNickname.equals(currNickname)){
                    Toast t = Toast.makeText(getApplicationContext(),
                            "New nickname cannot be the same as your current one",
                            Toast.LENGTH_LONG);
                    t.show();

                }else{

                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("nickname",newNickname);
                    user.saveInBackground();
                    finish();

                }

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
