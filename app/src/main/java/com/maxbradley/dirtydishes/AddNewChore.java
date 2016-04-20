package com.maxbradley.dirtydishes;

/**
 * Created by Christine Schroeder on 4/14/2016.
 */
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
//import course.labs.todomanager.ToDoItem.Priority;
//import course.labs.todomanager.ToDoItem.Status;

public class AddNewChore extends Activity {

    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;

    private static final String TAG = "Lab-UserInterface";

    private static String timeString;
    private static String dateString;
    private static TextView dateView;
    private static TextView timeView;

    private Date mDate;
    private RadioGroup mPriorityRadioGroup;
    private RadioGroup mStatusRadioGroup;
    private EditText mTitleText;
    private RadioButton mDefaultStatusButton;
    private RadioButton mDefaultPriorityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        mTitleText = (EditText) findViewById(R.id.title);
        mDefaultStatusButton = (RadioButton) findViewById(R.id.statusNotDone);
        mDefaultPriorityButton = (RadioButton) findViewById(R.id.medPriority);
        mPriorityRadioGroup = (RadioGroup) findViewById(R.id.priorityGroup);
        mStatusRadioGroup = (RadioGroup) findViewById(R.id.statusGroup);
        dateView = (TextView) findViewById(R.id.date);
        timeView = (TextView) findViewById(R.id.time);

        // Set the default date and time

        setDefaultDateTime();

        // OnClickListener for the Date button, calls showDatePickerDialog() to
        // show the Date dialog

        final Button datePickerButton = (Button) findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // OnClickListener for the Time button, calls showTimePickerDialog() to
        // show the Time Dialog

        final Button timePickerButton = (Button) findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // OnClickListener for the Cancel Button,

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setResult(RESULT_CANCELED);
                finish();

            }
        });

        final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                mTitleText.setText("");
                mDefaultPriorityButton.setChecked(true);
                mDefaultStatusButton.setChecked(true);

                setDefaultDateTime();

            }
        });

        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Chore.Priority priority = getPriority();
                Chore.Status status = getStatus();


                String titleString =  getToDoTitle();

                String fullDate = dateString + " " + timeString;

                Intent data = new Intent();
                Chore.packageIntent(data, titleString, priority, status,
                        fullDate);

                setResult(RESULT_OK, data);
                finish();

            }
        });
    }

    private void setDefaultDateTime() {

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
    }

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

    private Chore.Status getStatus() {

        switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
            case R.id.statusDone: {
                return Chore.Status.DONE;
            }
            default: {
                return Chore.Status.NOTDONE;
            }
        }
    }

    private String getToDoTitle() {
        return mTitleText.getText().toString();
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

            dateView.setText(dateString);
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

            timeView.setText(timeString);
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
}
