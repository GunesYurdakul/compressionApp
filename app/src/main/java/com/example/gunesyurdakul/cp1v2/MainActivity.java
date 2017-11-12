package com.example.gunesyurdakul.cp1v2;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    Singleton singleton =Singleton.getSingleton();
    public static final String EXTRA_MESSAGE = "com.example.gunesyurdakul.myapplication.MESSAGE";
    Type type = new TypeToken<Map<String,User>>() {}.getType();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.inbox:
                FilesList user = new FilesList();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                user.setArguments(args);
                ft.replace(R.id.fragment, user);
                ft.addToBackStack("addemployee");
                ft.commit();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String test = "Gunes\n";
        ArrayList<Boolean> code = new ArrayList<>();
        Encode encode = new Encode(test);
        encode.findFrequencies();
        encode.buildTree();
        encode.generateHuffmanCodes(encode.getHuffmanTree(), code);
        encode.compressText();
        byte[] bytes = encode.getCompressedText();
        String s = new String(bytes);
        System.out.println(bytes[0]);
        Decode decode = new Decode(encode.getCompressedText(), encode.getBitSize(), encode.getFrequencies());
        decode.buildTree();
        decode.decompressText();
        System.out.println(decode.getDecompressedText());


        //set view
        setContentView(R.layout.activity_main);
        singleton.queue = Volley.newRequestQueue(this);

        //readFile();//read users from file

        LoginFragment fragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
        transaction.addToBackStack("addemployee");
        transaction.commit();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


}
