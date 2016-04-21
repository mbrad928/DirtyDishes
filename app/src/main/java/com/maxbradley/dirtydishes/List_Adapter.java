package com.maxbradley.dirtydishes;

import android.app.ListActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class List_Adapter extends BaseAdapter {

    private final List<Chore> mItems = new ArrayList<Chore>();
    private final Context mContext;

    private static final String TAG = "Lab-UserInterface";

    public List_Adapter(Context context) {

        mContext = context;

    }

    public void add(Chore item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {

        return mItems.size();

    }

    @Override
    public Chore getItem(int pos) {

        return mItems.get(pos);

    }

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Chore toDoItem = (Chore)getItem(position);

        RelativeLayout itemLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.todoitem, parent, false);
        if(convertView == null){

        }

        final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleView);
        String title = toDoItem.getTitle();
        titleView.setText(title);


        final CheckBox statusView = (CheckBox)itemLayout.findViewById(R.id.statusCheckBox);
        statusView.setChecked(toDoItem.getStatus().equals(Chore.Status.DONE));

        statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){
                    toDoItem.setStatus(Chore.Status.DONE);
                } else {
                    toDoItem.setStatus(Chore.Status.NOTDONE);
                }

            }
        });

        TextView priorityView = (TextView) itemLayout.findViewById (R.id.priorityView);
        priorityView.setText(toDoItem.getPriority().toString());


        String string = Chore.FORMAT.format(toDoItem.getDate());
        final TextView dateView = (TextView)itemLayout.findViewById(R.id.dateView);
        dateView.setText(string);

        return itemLayout;

    }
}

