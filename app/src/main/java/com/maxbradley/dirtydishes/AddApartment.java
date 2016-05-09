package com.maxbradley.dirtydishes;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Christine Schroeder on 4/20/2016.
 */
public class AddApartment extends AppCompatActivity {

    Roommate_Adapter mAdapter;

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_LOGOUT = Menu.FIRST + 1;

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.add_apartment);
        mAdapter = new Roommate_Adapter(getApplicationContext());
        final String apartment_code = (String) ParseUser.getCurrentUser().get("apartment");

        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mAdapter);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {

                    for (ParseUser user : objects) {
                        if (user.get("apartment").equals(apartment_code)) {
                            if (user.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                                mAdapter.add(user.getUsername() + " (self)");
                            } else {
                                mAdapter.add(user.getUsername());
                            }
                        }

                    }
                }
            }
        });

        Button button = (Button) findViewById(R.id.addroom_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.name);
                EditText phone = (EditText) findViewById(R.id.phone);

                // Requesting permission to send SMS
                int permission = ActivityCompat.checkSelfPermission(AddApartment.this,
                        "android.permission.SEND_SMS");

                if (permission == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(AddApartment.this,
                            new String[]{"android.permission.SEND_SMS"},
                            1);
                }

                // Have permission, can now send SMS
                sendSMS(phone.getText().toString(), "hey " + name.getText().toString() + "! You've been added to a room on " +
                        "RoomMe! Download RoomMe, create an account, and enter code: " + apartment_code + " to join!"); //send code here
                mAdapter.add(name.getText().toString());
                name.setText("");
                phone.setText("");
            }
        });

    }


    //http://stackoverflow.com/questions/18828455/android-sms-manager-not-sending-sms
    private void sendSMS(String phoneNumber, String message) {
        String SENT = "Room Request Sent";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                mAdapter.clear();
                return true;
            case MENU_LOGOUT:
                ParseUser.logOut();
                Intent intent = new Intent(AddApartment.this, SignIn.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
