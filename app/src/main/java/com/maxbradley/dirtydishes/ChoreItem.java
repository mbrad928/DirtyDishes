package com.maxbradley.dirtydishes;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Max on 5/4/2016.
 */
@ParseClassName("Chore")
public class ChoreItem extends ParseObject {

    public ChoreItem(){
        super();
    }


    public void setUser(ParseUser user){
        put("user", user);
    }

    public void setTitle(String title) {
        put("title", title);
    }
    public void setPriority(int priority){
        put("priority", priority);
    }

    public void setStatus(int status) {
        put("status",status);
    }

    public void setDate(String date) {
        put("date",date);
    }

    public void setPerson(String person) {
        put("person",person);
    }

    public String getPerson(){return getString("person");}

    public String getTitle() {
        return getString("title");
    }

    public int getPriority(){
        return getInt("priority");
    }

    public int getStatus(){
        return getInt("status");
    }

    public int getID(){return getInt("notificationID");}

    public void setID(int id){put("notificationID", id);}

    public String getDate(){
        return getString("date");
    }

    public void setApartment(String apartment){
        put("apartment",apartment);
    }

    public static ParseQuery<ChoreItem> getQuery() {
        return ParseQuery.getQuery(ChoreItem.class);
    }


}
