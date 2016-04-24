package com.maxbradley.dirtydishes;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.maxbradley.dirtydishes.Chore.Priority;
import com.maxbradley.dirtydishes.Chore.Status;

public class MainActivity extends AppCompatActivity {

    /* startActivityForResult() request codes */
    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final int SIGN_IN_REQUEST_CODE = 1;

    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";

    /*  For use with sign-in/create account activity */
    static final String USERNAME = "username";
    static final String PASSWORD = "password";

    /* Extra field in result intent to determine if user wants to sign in or create account */
    static final String CREATE_OR_SIGN_IN = "create_or_sign_in";
    static final String SIGN_IN = "sign_in";
    static final String CREATE_ACCOUNT = "create_account";

    static final int REQUEST_CREATE_ACCOUNT = 2;


    // IDs for menu items
    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;


    /* If false, take user to sign-in/create account screen
       If true, go to main screen with chore list
     */
    private boolean userSignedIn = false;



    List_Adapter mAdapter;

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mAdapter = new List_Adapter(getApplicationContext());



        /* Take user to sign-in activity if not already signed in */
        if (!userSignedIn){
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
        }



        mAdapter = new List_Adapter(getApplicationContext());
        listView.setFooterDividersEnabled(true);
        listView = (ListView) findViewById(R.id.listView);
        if(listView == null)
            Log.d(TAG, "null");

        listView.setFooterDividersEnabled(true);

        TextView footerView =  (TextView) getLayoutInflater().inflate(R.layout.footer, null);

        listView.addFooterView(footerView);
        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddNewChore.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });

        listView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Either add a chore, or sign in */
        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_TODO_ITEM_REQUEST){
                Chore item = new Chore(data);
                mAdapter.add(item);

            }else if (requestCode == SIGN_IN_REQUEST_CODE){

                String createOrSignIn = data.getStringExtra(CREATE_OR_SIGN_IN);


                /* User signing into EXISTING ACCOUNT */
                if (createOrSignIn.equals(SIGN_IN)){

                    String username = data.getStringExtra(USERNAME);
                    String password = data.getStringExtra(PASSWORD);

                    Log.i(TAG,"User wants to sign in to existing account");
                    Log.i(TAG,"Username: "+username+", Password: "+password);

                    /* TODO - check database for username/password combination */


                /* User CREATING NEW ACCOUNT */
                }else if (createOrSignIn.equals(CREATE_ACCOUNT)){

                    String username = data.getStringExtra(USERNAME);
                    String password = data.getStringExtra(PASSWORD);

                    Log.i(TAG,"User wants to create account");
                    Log.i(TAG,"Username: "+username+", Password: "+password);

                    /* TODO - add account information to database */



                }


            }

        }


    }



    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();


        saveItems();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                mAdapter.clear();
                return true;
            case MENU_DUMP:
                dump();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((Chore) mAdapter.getItem(i)).toLog();
            Log.i(TAG, "Item " + i + ": " + data.replace(Chore.ITEM_SEP, ","));
        }

    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String priority = null;
            String status = null;
            Date date = null;

            while (null != (title = reader.readLine())) {
                priority = reader.readLine();
                status = reader.readLine();
                date = Chore.FORMAT.parse(reader.readLine());
                mAdapter.add(new Chore(title, Priority.valueOf(priority),
                        Status.valueOf(status), date));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }
}