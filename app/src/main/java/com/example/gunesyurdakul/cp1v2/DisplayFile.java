package com.example.gunesyurdakul.cp1v2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class DisplayFile extends Fragment implements View.OnClickListener{

    // TODO: Rename and change types and number of parameters
//    public static ProjectFragment setArguments(Project p) {
//        ProjectFragment fragment = new ProjectFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
    Singleton singleton =Singleton.getSingleton();
    ListView listView;
    final int ACTIVITY_CHOOSE_FILE = 1;
    String text;
    User receiver;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Log.d("display file", "display");
        View view = inflater.inflate(R.layout.fragment_display_file, container, false);
        TextView name= (TextView)view.findViewById(R.id.fileName);
        name.setText(singleton.currentFile.fileName);
        TextView text= (TextView)view.findViewById(R.id.text);
        text.setText(singleton.currentFile.sourceText);
        return view;
    }

    @Override
    public void onClick(View v) {}

}
