package com.maxbradley.dirtydishes;

/**
 * Created by Christine Schroeder on 4/14/2016.
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//import course.labs.todomanager.ToDoItem.Priority;
//import course.labs.todomanager.ToDoItem.Status;

public class AddNewChore extends AppCompatActivity {

    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;

    private static final String TAG = "Lab-UserInterface";

    private static String timeString;
    private static String dateString;
    private Spinner chores_spinner;
    private Spinner roommate_spinner;

    private Date mDate;
    private RadioGroup mPriorityRadioGroup;
    private RadioGroup mStatusRadioGroup;
    private EditText mTitleText;
    private RadioButton mDefaultStatusButton;
    private RadioButton mDefaultPriorityButton;
    private static Button timePickerButton;
    private static Button datePickerButton;

    RadioGroup roomates_radio;

    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_LOGOUT = Menu.FIRST + 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        mTitleText = (EditText) findViewById(R.id.title);
        mDefaultPriorityButton = (RadioButton) findViewById(R.id.medPriority);
        mPriorityRadioGroup = (RadioGroup) findViewById(R.id.priorityGroup);
        chores_spinner = (Spinner) findViewById(R.id.chores_spinner);
        //roommate_spinner = (Spinner) findViewById(R.id.roommate_spinner);
        roomates_radio = (RadioGroup) findViewById(R.id.roomates_radio);

        datePickerButton = (Button) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        timePickerButton = (Button) findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });


        final ArrayAdapter<CharSequence> chores_adapter = ArrayAdapter.createFromResource(this,
                R.array.chores_array, android.R.layout.simple_spinner_item);
        chores_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chores_spinner.setAdapter(chores_adapter);

        final String apartment_code = (String) ParseUser.getCurrentUser().get("apartment");
        final ArrayList<String> roommate_names = new ArrayList<String>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {
                    int i = 1;
                    for (ParseUser user : objects) {
                        if (user.get("apartment").equals(apartment_code)) {
                            //if (user.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                            //roommate_names.add(user.getUsername() + " (self)");
                            RadioButton rdbtn = new RadioButton(getApplicationContext());
                            roommate_names.add(user.getUsername());
                            rdbtn.setId(i);
                            i += 1;
                            rdbtn.setText(user.getUsername().toString());
                            roomates_radio.addView(rdbtn);
                            //} else {
                            //roommate_names.add(user.getUsername());
                            //}
                        }

                    }
                }
            }
        });

        //String [] str = (String) roommate_names.toArray();
        //String[] mStringArray = new String[roommate_names.size()];
        //mStringArray = roommate_names.toArray(mStringArray);

        /*final ArrayAdapter<String> roommate_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,roommate_names);
        roommate_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roommate_spinner.setAdapter(roommate_adapter);


        roommate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //roommate_adapter.notifyDataSetChanged();
                //person = (roommate_names.get(position));
                //Log.i("spinner listener ","person is "+roommate_names.get(position));
                Toast.makeText(getApplicationContext(),"Spinner listener person is "+roommate_adapter.getItem(position).toString(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/



        // OnClickListener for the Cancel Button,

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();

            }
        });

        /*final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitleText.setText("");
                mDefaultPriorityButton.setChecked(true);

                //setDefaultDateTime();

            }
        });*/

        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Chore.Priority priority = getPriority();
                Chore.Status status = Chore.Status.NOTDONE;

                String person = getRoommate(roommate_names);
                Log.i("person is ",person);
                String titleString =  getToDoTitle();


                String fullDate = dateString + " " + timeString;
                ChoreItem newChore = new ChoreItem();
                newChore.setUser(ParseUser.getCurrentUser());
                newChore.setApartment(ParseUser.getCurrentUser().getString("apartment"));
                newChore.setTitle(titleString);
                newChore.setPriority(priority.ordinal());
                newChore.setStatus(status.ordinal());
                newChore.setDate(fullDate);
                newChore.setPerson(person);
                newChore.saveInBackground();

                Intent data = new Intent();
                Chore.packageIntent(data, titleString, priority, status,
                        fullDate,person);

                setResult(RESULT_OK, data);
                finish();

            }
        });
    }

    /*private void setDefaultDateTime() {

        mDate = new Date();
        mDate = new Date(mDate.getTime() + SEVEN_DAYS);

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));

        timeView.setText(timeString);
    }*/

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = year + "-" + mon + "-" + day;
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";
    }

    private Chore.Priority getPriority() {

        switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
            case R.id.lowPriority: {
                return Chore.Priority.LOW;
            }
            case R.id.highPriority: {
                return Chore.Priority.HIGH;
            }
            default: {
                return Chore.Priority.MED;
            }
        }
    }

    private String getRoommate(ArrayList<String> roommates){
        int id = roomates_radio.getCheckedRadioButtonId();
        //for(String roomie : roommates){
        Log.i("id checked is",""+id);
        return roommates.get(id-1);
        //}

    }

    /*private Chore.Status getStatus() {

        switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
            case R.id.statusDone: {
                return Chore.Status.DONE;
            }
            default: {
                return Chore.Status.NOTDONE;
            }
        }
    }*/

    private String getToDoTitle() {
        String spinner_item = chores_spinner.getSelectedItem().toString();
        if(spinner_item.equals("-Please Select-")){
            return mTitleText.getText().toString();
        }
        return spinner_item.toString();
    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);

            datePickerButton.setText(dateString);
        }

    }


    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);

            timePickerButton.setText(timeString);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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
                //mAdapter.clear();
                return true;
            case MENU_LOGOUT:
                ParseUser.logOut();
                Intent intent = new Intent(AddNewChore.this,SignIn.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



