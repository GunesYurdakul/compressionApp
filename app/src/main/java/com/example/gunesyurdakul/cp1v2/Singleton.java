package com.example.gunesyurdakul.cp1v2;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gunesyurdakul on 01/10/2017.
 */

//Singleton class enables that there is only one object is created from this class in the lifetime of the app
public class Singleton {
    private static Singleton singleton = new Singleton();
    private Singleton(){}
    User currentUser;
    TxtFile currentFile;
    List<UserFromServer> userList=new ArrayList<UserFromServer>();

    boolean unsuccesfulLogin;
    public static Singleton getSingleton() {

        return singleton;
    }
    RequestQueue queue;
}
