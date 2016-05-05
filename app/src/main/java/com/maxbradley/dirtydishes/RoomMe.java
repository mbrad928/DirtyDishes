package com.maxbradley.dirtydishes;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Max on 5/5/2016.
 */
public class RoomMe extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();

        //place any custom ParseObject classes here
        ParseObject.registerSubclass(Apartment.class);
        ParseObject.registerSubclass(ChoreItem.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("EJ51eO0rkyN7WdVNflZDoUl4SYXAWJZfinx1IQ9I")
                        .clientKey("V65F9bEcLESrcpmSvxhD6nyiLGjOVAHaXsJROb20")
                        .server("https://parseapi.back4app.com/")
                        .build()
        );
    }
}
