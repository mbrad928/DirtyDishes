package com.maxbradley.dirtydishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
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
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_LOGOUT = Menu.FIRST + 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses);

        /*mDrawerList = (ListView) findViewById(R.id.nav_list);

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
        });*/

    }

    /*public void itemSelected(int pos){
        MainActivity.itemSelected(pos);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:

                return true;
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
