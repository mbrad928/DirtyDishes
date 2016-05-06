package com.maxbradley.dirtydishes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by Cameron on 5/5/2016.
 */
public class Settings extends AppCompatActivity {

    private static final String TAG = "RoomMe";

    ArrayAdapter<String> mAdapter;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        listView = (ListView) findViewById(R.id.listView);

        String[] optionsArray = {"Change Nickname",
                "Change Password",
                "Change Phone Number",
                "Leave Apartment"
        };

        mAdapter = new ArrayAdapter<String>(this,R.layout.settings_item,optionsArray);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                itemSelected(position);
            }
        });

    }

    public boolean itemSelected(int position) {
        if(position == 0){//Change nickanme
            Log.d(TAG, "'Change nickname' selected");

        } else if (position == 1){//change password
            Log.d(TAG,"'Change password' selected");
            Intent intent = new Intent(Settings.this, ChangePassword.class);
            startActivity(intent);

        } else if (position == 2) {//change phone number
            Log.d(TAG,"'Change phone number' selected");

        } else if (position == 3) {//leave apartment
            Log.d(TAG, "'Leave apartment' selected");

            Boolean removeFromApartment = false;

            ApartmentDialogFragment dialog = new ApartmentDialogFragment();
            dialog.show(getSupportFragmentManager(),"leaving");


        }


        return true;
    }

    public static class ApartmentDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(((Settings)getActivity()));
            builder.setMessage("Are you sure you want to leave the apartment?")

                    // Remove user from apartment group
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "'Yes' button clicked");

                            // Remove user's tasks from apartment task list
                            ParseQuery<ChoreItem> query = ChoreItem.getQuery();
                            query.whereEqualTo("user", ParseUser.getCurrentUser());

                            query.findInBackground(new FindCallback<ChoreItem>() {
                                @Override
                                public void done(List<ChoreItem> objects, ParseException e) {
                                    if(e == null) {
                                        for (ChoreItem chore : objects){
                                            try {
                                                chore.delete();
                                                chore.saveInBackground();
                                            } catch (ParseException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });


                            // Test apartment name: PLuZ0l6UQY
                            // Set user's apartment field to empty string
                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("apartment", "");
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (null == e){
                                        Log.i(TAG,"Apartment field save succeeded");
                                    }else{
                                        Log.i(TAG,"Apartment field save failed");
                                        e.printStackTrace();
                                    }
                                }
                            });

                            getActivity().finish();

                        }
                    })

                    // Don't remove user from apartment group
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.i(TAG, "'No' button clicked");
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}

