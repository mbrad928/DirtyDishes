package com.maxbradley.dirtydishes;

import android.app.ListActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class List_Adapter extends ArrayAdapter<Chore> {



    private static final String TAG = "Lab-UserInterface";

    public List_Adapter(Context context, ArrayList<Chore> chores) {
        super(context, 0, chores);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Chore toDoItem = getItem(position);

        LinearLayout itemLayout;
        if(convertView == null){
            itemLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.todoitem, parent, false);
        }else{
            itemLayout = (LinearLayout) convertView;
        }

        final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
        String title = toDoItem.getTitle();
        titleView.setText(title);

        final TextView person = (TextView) itemLayout.findViewById(R.id.person_task_name);
        //Log.i("Person is", toDoItem.getPerson());
        ParseQuery<ParseUser> query = ParseUser.getQuery(); //sets the chore person to the nickname in the UI
        query.whereEqualTo("username", toDoItem.getPerson());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, com.parse.ParseException e) {
                if (e == null) {

                    for (ParseUser user : objects) {
                        if (user.get("nickname") == null) {
                            person.setText(toDoItem.getPerson());
                        } else {
                            person.setText((String) user.get("nickname"));
                        }

                    }
                }
            }
        });


        final Button statusView = (Button)itemLayout.findViewById(R.id.complete);
        //statusView.setChecked(toDoItem.getStatus().equals(Chore.Status.DONE));
        TextView overdue = (TextView) itemLayout.findViewById(R.id.overdue);

        if(Calendar.getInstance().getTime().before(toDoItem.getDate())) {
            overdue.setVisibility(View.INVISIBLE);
        }

        if(!toDoItem.getPerson().equals(ParseUser.getCurrentUser().getUsername())){
            statusView.setVisibility(View.INVISIBLE);
        }else {
            statusView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (toDoItem.getPerson().equals(ParseUser.getCurrentUser().getUsername())) {
                        remove(toDoItem);
                        notifyDataSetChanged();

                        //remove from db
                        ParseQuery<ChoreItem> query = ChoreItem.getQuery();
                        //query.whereEqualTo("person", ParseUser.getCurrentUser());
                        query.whereEqualTo("title", toDoItem.getTitle());

                        query.findInBackground(new FindCallback<ChoreItem>() {
                            @Override
                            public void done(List<ChoreItem> objects, ParseException e) {
                                if (e == null) {
                                    for (ChoreItem chore : objects) {
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


                    } else {
                        Toast.makeText(getContext(), "This is not your task", Toast.LENGTH_LONG).show();
                    }
                }

            });

        }
        TextView priorityView = (TextView) itemLayout.findViewById (R.id.priorityView);
        priorityView.setText(toDoItem.getPriority().toString());
        //change prioirity text color

        final TextView dateView = (TextView)itemLayout.findViewById(R.id.dateView);


        Calendar cal = Calendar.getInstance();
        cal.setTime(toDoItem.getDate());
        Calendar curr = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_YEAR) == curr.get(Calendar.DAY_OF_YEAR))
            dateView.setText(Chore.DISPLAY_FORMAT_TIME.format(toDoItem.getDate()) + "   Today");
        else if(cal.get(Calendar.DAY_OF_YEAR) == (curr.get(Calendar.DAY_OF_YEAR) + 1))
            dateView.setText(Chore.DISPLAY_FORMAT_TIME.format(toDoItem.getDate()) + "   Tomorrow");
        else if(cal.get(Calendar.DAY_OF_YEAR) == (curr.get(Calendar.DAY_OF_YEAR) - 1))
            dateView.setText(Chore.DISPLAY_FORMAT_TIME.format(toDoItem.getDate()) + "   Yesterday");
        else if(cal.get(Calendar.WEEK_OF_YEAR) == curr.get(Calendar.WEEK_OF_YEAR))
            dateView.setText(Chore.DISPLAY_FORMAT_DAY.format(toDoItem.getDate()));
        else
            dateView.setText(Chore.DISPLAY_FORMAT_DATE_TIME.format(toDoItem.getDate()));

        return itemLayout;

    }
}



