package com.maxbradley.dirtydishes;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Max on 5/4/2016.
 */
@ParseClassName("Apartment")
public class Apartment extends ParseObject {

    public Apartment(){
        super();
    }

    /*Returns the user's apartment code from DB*/
    public String getApartmentCode(){
        return getObjectId();
    }

    public static ParseQuery<Apartment> getQuery() {
        return ParseQuery.getQuery(Apartment.class);
    }


}
