package com.maxbradley.dirtydishes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    /* startActivityForResult() request codes */
    private static final int ADD_TODO_ITEM_REQUEST = 0;
    private static final int SIGN_IN_REQUEST_CODE = 1;

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
    private static final int MENU_LOGOUT = Menu.FIRST;


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


        setContentView(R.layout.activity_main);

        mAdapter = new List_Adapter(getApplicationContext());

        listView = (ListView) findViewById(R.id.listView);
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



        //  userSignedIn = getIntent().getBooleanExtra("signedIn",false);

        /* Take user to sign-in activity if not already signed in */
  /*      if (!userSignedIn) {
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
        }

*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewChore.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });

        // TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer, null);

        //  listView.addFooterView(footerView);
        //   footerView.setOnClickListener(new OnClickListener() {
        //      @Override
        //      public void onClick(View v) {

        //     Intent intent = new Intent(MainActivity.this, AddNewChore.class);
        //        startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
        //   }
        //   });


        listView.setAdapter(mAdapter);
        //LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.task_header, mDrawerList, false);
        TextView title = (TextView) header.findViewById(R.id.title);
        Intent i = getIntent();
        int all = i.getIntExtra("all",0);
        if(all == 1){
            title.setText("All Chores");
        }else{
            title.setText("Your Chores");
        }

        listView.addHeaderView(header, null, false);
        listView.setHeaderDividersEnabled(true);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Either add a chore, or sign in */
        if (resultCode == RESULT_OK) {

            if (requestCode == ADD_TODO_ITEM_REQUEST) {
               /* Chore item = new Chore(data);
                if(item.getPerson().equals(ParseUser.getCurrentUser().getUsername())) {
                    mAdapter.add(item);
                }*/


            } else if (requestCode == SIGN_IN_REQUEST_CODE) {

                String createOrSignIn = data.getStringExtra(CREATE_OR_SIGN_IN);


                /* User signing into EXISTING ACCOUNT */
                if (createOrSignIn.equals(SIGN_IN)) {

                    String username = data.getStringExtra(USERNAME);
                    String password = data.getStringExtra(PASSWORD);

                    Log.i(TAG, "User wants to sign in to existing account");
                    Log.i(TAG, "Username: " + username + ", Password: " + password);



                /* User CREATING NEW ACCOUNT */
                } else if (createOrSignIn.equals(CREATE_ACCOUNT)) {

                    String username = data.getStringExtra(USERNAME);
                    String password = data.getStringExtra(PASSWORD);

                    Log.i(TAG, "User wants to create account");
                    Log.i(TAG, "Username: " + username + ", Password: " + password);


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
        if(position == 1){//View Your Chores
            Log.d(TAG, "'View Your Chores' selected");
            Intent i = new Intent(MainActivity.this,MainActivity.class);
            startActivity(i);
        } else if (position == 2){//View all Chores
            Log.d(TAG, "'View all Chores' selected");
            Intent i = new Intent(MainActivity.this,MainActivity.class);
            i.putExtra("all",1);
            startActivity(i);
        } else if (position == 3) {// Expenses
            Log.d(TAG, "'Expenses' selected");
            Intent i = new Intent(MainActivity.this,Expenses.class);
            startActivity(i);
        } else if (position == 4) {//Add people
            Log.d(TAG, "'Add people' selected");
            Intent i = new Intent(MainActivity.this,AddApartment.class);
            startActivity(i);


        }else if(position == 5) { //Settings
            Log.d(TAG,"'Settings' selected");
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

        // if current user is null this screws up big

        if(ParseUser.getCurrentUser().get("apartment")==null){
            Log.i(TAG, "User's apartment is null");
            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
        }else {

            // If user left a previous apartment, their apartment field will be the empty string
            if (ParseUser.getCurrentUser().get("apartment").equals("")) {
                Log.i(TAG, "User has no apartment");
                Intent intent = new Intent(MainActivity.this, CodeActivity.class);
                intent.putExtra(USERNAME, ParseUser.getCurrentUser().getUsername());
                startActivity(intent);
            }

            if (mAdapter.getCount() == 0) {
                Log.d(TAG,"load");
                loadItems();
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();


        saveItems();


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
                Intent intent = new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
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
        //load from Parse
        Intent i = getIntent();
        int all = i.getIntExtra("all",0);
        if(all == 1){ //want tasks for full apartment
            ParseQuery<ChoreItem> query = ChoreItem.getQuery();
            query.whereEqualTo("apartment",ParseUser.getCurrentUser().getString("apartment"));
            query.findInBackground(new FindCallback<ChoreItem>() {
                @Override
                public void done(List<ChoreItem> objects, com.parse.ParseException e) {

                    if(e == null) {
                        for (ChoreItem chore : objects){
                            Chore newC = new Chore(chore);

                            mAdapter.add(newC);

                        }
                    }
                }
            });
        }else{ //want tasks just for current user
            ParseQuery<ChoreItem> query = ChoreItem.getQuery();
            query.whereEqualTo("apartment",ParseUser.getCurrentUser().getString("apartment"));
            query.findInBackground(new FindCallback<ChoreItem>() {
                @Override
                public void done(List<ChoreItem> objects, com.parse.ParseException e) {
                    if(e == null) {
                        for (ChoreItem chore : objects){
                            if(chore.getPerson().equals(ParseUser.getCurrentUser().getUsername())) {
                                Chore newC = new Chore(chore);
                                mAdapter.add(newC);
                            }
                        }
                    }
                }
            });
        }

        /*
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
        }*/
    }

    private void saveItems() {
        //no need to save b/c of Parse

    }
        /*
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
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}