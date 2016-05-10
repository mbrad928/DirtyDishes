package com.maxbradley.dirtydishes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;

public class Chore {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    public enum Priority {
        LOW, MED, HIGH
    };

    public enum Status {
        NOTDONE, DONE
    };

    public final static String TITLE = "title";
    public final static String PRIORITY = "priority";
    public final static String STATUS = "status";
    public final static String DATE = "date";
    public final static String FILENAME = "filename";

    public final static SimpleDateFormat DISPLAY_FORMAT_TIME = new SimpleDateFormat(
            "h:mm a", Locale.US);

    public final static SimpleDateFormat DISPLAY_FORMAT_DATE = new SimpleDateFormat(
            "MM/dd", Locale.US);

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "HH:mm  MM/dd/yyyy", Locale.US);

    public final static SimpleDateFormat DISPLAY_FORMAT_DATE_TIME = new SimpleDateFormat(
            "h:mm a    M/dd", Locale.US);

    public final static SimpleDateFormat DISPLAY_FORMAT_DAY = new SimpleDateFormat(
            "h:mm a    cccc", Locale.US);


    private String mTitle = new String();
    private String mPerson = new String();
    private Priority mPriority = Priority.LOW;
    private Status mStatus = Status.NOTDONE;
    private Date mDate = new Date();
    private int mID;

    Chore(String title, Priority priority, Status status, Date date, String person) {
        this.mTitle = title;
        this.mPriority = priority;
        this.mStatus = status;
        this.mDate = date;
        this.mPerson = person;
        this.mID = (int) (System.currentTimeMillis()/1000);
    }

    Chore(ChoreItem item){
        this.mTitle = item.getTitle();
        this.mPriority = Priority.values()[item.getPriority()];
        this.mStatus = Status.values()[item.getStatus()];
        this.mPerson = item.getPerson();
        try {
            this.mDate = Chore.FORMAT.parse(item.getDate());
        }catch (ParseException e){
            e.printStackTrace();
        }
        this.mID = item.getID();
    }


    Chore(Intent intent) {

        mTitle = intent.getStringExtra(Chore.TITLE);
        mPriority = Priority.valueOf(intent.getStringExtra(Chore.PRIORITY));
        mStatus = Status.valueOf(intent.getStringExtra(Chore.STATUS));
        mPerson = intent.getStringExtra("Person");
        mID = intent.getIntExtra("notificationID", 0);

        try {
            mDate = Chore.FORMAT.parse(intent.getStringExtra(Chore.DATE));
        } catch (ParseException e) {
            mDate = new Date();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public String getPerson(){return mPerson;}

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public Date getDate() {
        return mDate;
    }

    public void setID(int id){this.mID = id;}

    public int getID(){return mID;}

    public void setDate(Date date) {
        mDate = date;
    }

    public static void packageIntent(Intent intent, String title,
                                     Priority priority, Status status, String date, String person, int id) {

        intent.putExtra(Chore.TITLE, title);
        intent.putExtra(Chore.PRIORITY, priority.toString());
        intent.putExtra(Chore.STATUS, status.toString());
        intent.putExtra(Chore.DATE, date);
        intent.putExtra("Person", person);
        intent.putExtra("notificationID", id);

    }

    public static void packageIntent(Intent intent, Chore chore) {

        intent.putExtra(Chore.TITLE, chore.getTitle());
        intent.putExtra(Chore.PRIORITY, chore.getPriority().toString());
        intent.putExtra(Chore.STATUS, chore.getStatus().toString());
        intent.putExtra(Chore.DATE, chore.getDate());
        intent.putExtra("Person", chore.getPerson());
        intent.putExtra("notificationID", chore.getID());

    }

    public String toString() {
        return mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP
                + FORMAT.format(mDate);
    }

    public String toLog() {
        return "Title:" + mTitle + ITEM_SEP + "Priority:" + mPriority
                + ITEM_SEP + "Status:" + mStatus + ITEM_SEP + "Date:"
                + FORMAT.format(mDate) + "\n";
    }

}
