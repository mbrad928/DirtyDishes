package com.maxbradley.dirtydishes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Christine Schroeder on 4/20/2016.
 */
public class Roommate_Adapter extends BaseAdapter{
    private final List<List> mItems = new ArrayList<>();
    private final Context mContext;


    public Roommate_Adapter(Context context) {

        mContext = context;

    }

    public void add(String name, String phonenumber) {

        List temp = new ArrayList<>();
        temp.add(name);
        temp.add(phonenumber);
        mItems.add(temp);
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
    public List getItem(int pos) {

       return mItems.get(pos);

    }

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final List roomie = getItem(position);
        String name = (String)roomie.get(0);
        String phone = (String)roomie.get(1);

        LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.roommate, parent, false);
        TextView name_text = (TextView) itemLayout.findViewById(R.id.name);
        name_text.setText(name);
        TextView phone_text = (TextView) itemLayout.findViewById(R.id.number);
        phone_text.setText(phone);
        Button remove = (Button) itemLayout.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItems.remove(position);
                notifyDataSetChanged();
            }
        });

        return itemLayout;

    }
}
