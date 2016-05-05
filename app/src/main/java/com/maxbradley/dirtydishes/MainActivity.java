package com.maxbradley.dirtydishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maxbradley.dirtydishes.Chore.Priority;
import com.maxbradley.dirtydishes.Chore.Status;
import com.parse.Parse;
import com.parse.ParseObject;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

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

    private ListView mDrawerList;
    private ArrayAdapter<String> drawerAdapter;


    List_Adapter mAdapter;

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("EJ51eO0rkyN7WdVNflZDoUl4SYXAWJZfinx1IQ9I")
                        .clientKey("V65F9bEcLESrcpmSvxhD6nyiLGjOVAHaXsJROb20")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );
/* How to add a ParseObject
        ParseObject testObject = new ParseObject("NewObject");
        testObject.put("Key",1000);
        testObject.put("Name","Max");
        testObject.saveInBackground();
        Log.d(TAG, "put object");
*/
        setContentView(R.layout.activity_main);
        mAdapter = new List_Adapter(getApplicationContext());

        listView = (ListView) findViewById(R.id.listView);
        mDrawerList = (ListView) findViewById(R.id.nav_list);

        String[] optionsArray = {"Chores",
                "Expenses",
                "Add People",
                "Settings"
        };
        drawerAdapter =
                new ArrayAdapter<String>(this,R.layout.drawer_item,optionsArray);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemSelected(position);

            }
        });



        userSignedIn = getIntent().getBooleanExtra("signedIn",false);

        /* Take user to sign-in activity if not already signed in */
        if (!userSignedIn) {
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
        }


        listView.setFooterDividersEnabled(true);

        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer, null);

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

            if (requestCode == ADD_TODO_ITEM_REQUEST) {
                Chore item = new Chore(data);
                mAdapter.add(item);

            } else if (requestCode == SIGN_IN_REQUEST_CODE) {

                String createOrSignIn = data.getStringExtra(CREATE_OR_SIGN_IN);


                /* User signing into EXISTING ACCOUNT */
                if (createOrSignIn.equals(SIGN_IN)) {

                    String username = data.getStringExtra(USERNAME);
                    String password = data.getStringExtra(PASSWORD);

                    Log.i(TAG, "User wants to sign in to existing account");
                    Log.i(TAG, "Username: " + username + ", Password: " + password);

                    /* TODO - check database for username/password combination */


                /* User CREATING NEW ACCOUNT */
                } else if (createOrSignIn.equals(CREATE_ACCOUNT)) {

                    String username = data.getStringExtra(USERNAME);
                    String password = data.getStringExtra(PASSWORD);

                    Log.i(TAG, "User wants to create account");
                    Log.i(TAG, "Username: " + username + ", Password: " + password);

                    /* TODO - add account information to database */

                    /* Go to code entry/generation */
                    Intent intent = new Intent(MainActivity.this, CodeActivity.class);
                    intent.putExtra(MainActivity.USERNAME, username);
                    intent.putExtra(MainActivity.PASSWORD, password);
                    startActivity(intent);


                }


            }

        }


    }

    public void setSignedIn(boolean b){
        this.userSignedIn = b;
    }

    public boolean itemSelected(int position) {
        if(position == 0){//Chore list
            Log.d(TAG,"First item selected");
        } else if (position == 1){//Expenses
            Log.d(TAG,"Second item selected");
        } else if (position == 2) {//Add people

        } else if (position == 3) {//Settings

        }


        return true;
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}