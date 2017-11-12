package com.example.gunesyurdakul.cp1v2;

/**
 * Created by gunesyurdakul on 11/11/2017.
 */

public class UserFromServer {
    String username;
    String name;
    String surname;
    byte[] profilePicture;


    //Constructor with inputs
    UserFromServer(String name_in,String surname_in,String user_name,byte[]pp){//constructorwith inputs
        this.username=user_name;
        this.name=name_in;
        this.surname=surname_in;
        this.profilePicture=pp;
    }

    UserFromServer(){
        profilePicture=null;
    }
}
