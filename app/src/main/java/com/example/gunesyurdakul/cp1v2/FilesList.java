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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class FilesList extends Fragment implements View.OnClickListener{

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
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    static List<TxtFromServer> files=new ArrayList<TxtFromServer>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Log.d("Info", "files");
        View view = inflater.inflate(R.layout.fragment_files_list, container, false);
        listView = (ListView) view.findViewById(R.id.filesList);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

        TableLayout table;
        //TxtFile new_File = new TxtFile("katik","yurdakulgu","ABCD\nfghj\nfghjkl\nfghjk","FileFromGullusu.txt");
        int socketTimeout = 10000; // 30 seconds. You can change it
        final RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://cp-h-server.herokuapp.com/receiver/fileList/"+singleton.currentUser.username , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Response",response);
                    Gson rson=new Gson();
                    rson = new GsonBuilder().create();

                    files=rson.fromJson(response,new TypeToken<ArrayList<TxtFromServer>>(){}.getType());

                    listView.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            if(files.size() == 0) {
                                return 0;
                            }else
                                return files.size();

                        }

                        @Override
                        public Object getItem(int i) {
                            return null;
                        }

                        @Override
                        public boolean isEnabled(int position) {
                            return false;
                        }
                        @Override
                        public long getItemId(int i) {
                            return 0;
                        }

                        @Override
                        public View getView(int i, View view, ViewGroup viewGroup) {
                            if(view == null) {
                                view = inflater.inflate(R.layout.view_file_cell,viewGroup,false);
                                User_View mymodel = new User_View();
                                mymodel.sender = (TextView) view.findViewById(R.id.sender);
                                mymodel.date = (TextView) view.findViewById(R.id.date);
                                mymodel.file = (TextView) view.findViewById(R.id.file_name);
                                mymodel.download=(ImageView)view.findViewById(R.id.download);
                                mymodel.pp=(ImageView)view.findViewById(R.id.miniPP);
                                view.setTag(mymodel);

                            }

                            final TxtFromServer current = files.get(i);
                            User_View mymodel = (User_View) view.getTag();
                            UserFromServer user=new UserFromServer();

                            Iterator<UserFromServer> iter = singleton.userList.iterator();
                            while (iter.hasNext()) {
                                UserFromServer user_it = iter.next();

                                if (user_it.username.equals(current.sender))
                                    user=user_it;

                            }
                            Log.d("Sender",user.username);
                            mymodel.sender.setText(user.name+" "+user.surname);
                            mymodel.date.setText(current.date);
                            mymodel.file.setText(current.filename);
                            Log.d("file name",current.filename);
                            Log.d("USer",user.name+" "+user.surname);

                            if (user.profilePicture != null) {
                                // pp.setImageDrawable(null); //this should help
                                //currentEmployee.profilePicture.recycle();
                                try{
                                    Bitmap src = BitmapFactory.decodeByteArray(user.profilePicture, 0, user.profilePicture.length);
                                    RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),src);
                                    dr.setCornerRadius(200);
                                    mymodel.pp.setImageDrawable(dr);
                                    Log.d("image","image");
                                }catch (Exception e){
                                    Log.e("picture","error");
                                }
                   }
                            mymodel.download.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                    try {
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://cp-h-server.herokuapp.com/receiver/"+singleton.currentUser.username+"/"+current.filename, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                Gson rson=new Gson();
                                rson = new GsonBuilder().create();
                                TxtFile download=new TxtFile();
                                download=rson.fromJson(response,new TypeToken<TxtFile>(){}.getType());
                                    Context context = getActivity();
                                    File file = new File(context.getExternalFilesDir(null), download.fileName);
                                    try{

                                        if(download.sourceText.equals("")) {
                                            Decode decode = new Decode(download.zipped, download.bitSize, download.frequencies);
                                            decode.buildTree();
                                            decode.decompressText();
                                            System.out.println(decode.getDecompressedText());
                                            download.sourceText = decode.getDecompressedText();
                                        }
                                        FileWriter out = new FileWriter(file);
                                        String path=file.getAbsolutePath();
                                        out.write(download.sourceText);
                                        Log.d("New File",download.sourceText);
                                        Log.d("Path",path);
                                        out.close();
                                        singleton.currentFile=download;
                                        DisplayFile userFragment = new DisplayFile();
                                        FragmentManager fm = getFragmentManager();
                                        FragmentTransaction ft = fm.beginTransaction();
                                        Bundle args = new Bundle();
                                        userFragment.setArguments(args);
                                        ft.replace(R.id.fragment, userFragment);
                                        ft.addToBackStack("display file");
                                        ft.commit();
                                    } catch (IOException e) {
                                        Log.d( "File Write", e.toString());
                                    }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR",error.toString());

                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return String.format("application/json; charset=utf-8");

                            }
                        };
                        stringRequest.setRetryPolicy(policy);
                        singleton.queue.add(stringRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                                }


                            });
                            return view;
                        }
                    });


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR",error.toString());

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return String.format("application/json; charset=utf-8");

                }
            };
            stringRequest.setRetryPolicy(policy);
            singleton.queue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d("return view", "hey");

        return view;

    }



    public class User_View {
        TextView sender;
        ImageView pp;
        TextView file;
        TextView date;
        ImageView download;
    }
    public class TxtFromServer{
        String sender;
        String filename;
        String date;
    }

    @Override
    public void onClick(View v) {}
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


}
