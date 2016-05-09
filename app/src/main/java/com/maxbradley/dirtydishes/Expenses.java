package com.maxbradley.dirtydishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by Christine Schroeder on 5/7/2016.
 */
public class Expenses extends AppCompatActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> drawerAdapter;

    // IDs for menu items
    private static final int MENU_LOGOUT = Menu.FIRST;

    private static final String TAG = "Expenses";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses);

        mDrawerList = (ListView) findViewById(R.id.nav_list);

        String[] optionsArray = {"View Your Chores",
                "View All Chores",
                "Expenses",
                "Add People",
                "Settings"
        };
        drawerAdapter =
                new ArrayAdapter<String>(this,R.layout.drawer_item,optionsArray);
        mDrawerList.setAdapter(drawerAdapter);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header_news = (ViewGroup)inflater.inflate(R.layout.drawer_header, mDrawerList, false);
        TextView name = (TextView) header_news.findViewById(R.id.username);
        name.setText(ParseUser.getCurrentUser().getUsername());
        mDrawerList.addHeaderView(header_news, null, false);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);

            }
        });


    }

    public boolean itemSelected(int position) {
        if(position == 1){//View Your Chores
            Log.d(TAG, "'View Your Chores' selected");
            Intent i = new Intent(Expenses.this,MainActivity.class);
            startActivity(i);
        } else if (position == 2){//View all Chores
            Log.d(TAG, "'View all Chores' selected");
            Intent i = new Intent(Expenses.this,MainActivity.class);
            i.putExtra("all",1);
            startActivity(i);
        } else if (position == 3) {// Expenses
            Log.d(TAG, "'Expenses' selected");
            Intent i = new Intent(Expenses.this,Expenses.class);
            startActivity(i);
        } else if (position == 4) {//Add people
            Log.d(TAG, "'Add people' selected");
            Intent i = new Intent(Expenses.this,AddApartment.class);
            startActivity(i);


        }else if(position == 5) { //Settings
            Log.d(TAG,"'Settings' selected");
            Intent intent = new Intent(Expenses.this, Settings.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_LOGOUT:
                ParseUser.logOut();
                Intent intent = new Intent(Expenses.this,SignIn.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }*/
}
