package com.example.gunesyurdakul.cp1v2;


/**
 * Created by gunesyurdakul on 01/10/2017.
 */

//User class
public class User {
    String username;
    String name;
    String surname;
    String password;
    String email;
    byte[] profilePicture;


    //Constructor with inputs
    User(String name_in,String surname_in,String password,String user_name,String email,byte[]pp){//constructorwith inputs
        this.email=email;
        this.username=user_name;
        this.name=name_in;
        this.surname=surname_in;
        this.password=password;
        this.profilePicture=pp;
    }

    User(){
        profilePicture=null;
    }
}
