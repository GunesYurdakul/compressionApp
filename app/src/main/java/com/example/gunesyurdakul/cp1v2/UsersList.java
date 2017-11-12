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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class UsersList extends Fragment implements View.OnClickListener{

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
    UserFromServer receiver;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Log.d("Info", "hey");
        View view = inflater.inflate(R.layout.fragment_userslist, container, false);
        Log.d("Info", "hey");
        TableLayout table;
        Log.d("Info", "hey");

        listView = (ListView) view.findViewById(R.id.listView);
        Toolbar myToolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        myToolbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);

        int socketTimeout = 10000; /// / 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://cp-h-server.herokuapp.com/user/userList" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response",response);
                Gson rson=new Gson();
                rson = new GsonBuilder().create();
                singleton.userList=rson.fromJson(response,new TypeToken<ArrayList<UserFromServer>>(){}.getType());
                singleton.userList.add(new UserFromServer("Server","","server",null));
                System.out.println(singleton.userList.get(1).name);
                Iterator<UserFromServer> iter = singleton.userList.iterator();
                while (iter.hasNext()) {
                    UserFromServer user = iter.next();

                    if (user.username.equals(singleton.currentUser.username))
                        iter.remove();
                }
                listView.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        if(singleton.userList.size() == 0) {
                            return 0;
                        }else
                            return singleton.userList.size();

                    }

                    @Override
                    public Object getItem(int i) {
                        return null;
                    }

                    @Override
                    public long getItemId(int i) {
                        return 0;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        if(view == null) {
                            view = inflater.inflate(R.layout.view_employee_cell,viewGroup,false);
                            User_View mymodel = new User_View();
        //                    mymodel.id = (TextView) view.findViewById(R.id.id);
                            mymodel.name = (TextView) view.findViewById(R.id.name);

                            mymodel.pp=(ImageView)view.findViewById(R.id.miniPP);
                            view.setTag(mymodel);

                        }

                        User_View mymodel = (User_View) view.getTag();
                        UserFromServer user = singleton.userList.get(i);
                        Log.d("from list",user.username);
        //                mymodel.id.setText(Integer.toString(i+1));
                        mymodel.name.setText(user.name+" "+user.surname);
                        Log.d("USer",user.name+" "+user.surname);


                        if (user.profilePicture != null) {
                            // pp.setImageDrawable(null); //this should help
                            //currentEmployee.profilePicture.recycle();
                            try{
                                Bitmap src = BitmapFactory.decodeByteArray(user.profilePicture, 0, user.profilePicture.length);
                                RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(),src);
                                dr.setCornerRadius(200);
                                mymodel.pp.setImageDrawable(dr);
                            }catch (Exception e){
                                Log.e("picture","error");
                            }
                        }
                        return view;
                    }
                });


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id){
                        Intent chooseFile;
                        Intent intent;
                        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFile.setType("*/*");
                        intent = Intent.createChooser(chooseFile, "Choose a file");
                        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                        receiver=singleton.userList.get(position);

                    }


            });
            Log.d("Info", "hey");

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


        return view;
    }



    public class User_View {
        TextView name;
        ImageView pp;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("wsef","asdfg");
        switch(requestCode) {

            case ACTIVITY_CHOOSE_FILE: {

                if (resultCode == RESULT_OK){
                    Log.d("wsef","asdfgsdfgh");
                    Uri uri = data.getData();
                    String filePath = uri.getPath();
                    Log.d("Path",filePath);
                    String fileName=filePath.substring(filePath.lastIndexOf("/")+1);
                    try {
                    text = readTextFromUri(uri);

                        Log.d("text",text);
                        Gson gson=new Gson();
                        gson = new GsonBuilder().create();
                        ArrayList<Boolean> code = new ArrayList<>();
                        final String str;
                        if(receiver.username.equals("server")) {
                            TxtFile newFile = new TxtFile(receiver.username,singleton.currentUser.username, text, fileName, 0, null);
                            newFile.zipped = null;
                            str = gson.toJson(newFile);
                        }
                        else{
                            Encode encode = new Encode(text);
                            encode.findFrequencies();
                            encode.buildTree();
                            encode.generateHuffmanCodes(encode.getHuffmanTree(), code);
                            encode.compressText();
                            byte[] bytes = encode.getCompressedText();
                            TxtFile newFile = new TxtFile(singleton.currentUser.username, receiver.username, text, fileName, encode.getBitSize(), encode.getFrequencies());
                            newFile.zipped = bytes;
                            newFile.sourceText = "";
                            System.out.println(bytes);
                            str=gson.toJson(newFile);

                        }


                        Log.d("File to send json",str);

                        int socketTimeout = 10000; // 30 seconds. You can change it
                        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);


                        try {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://cp-h-server.herokuapp.com/sender/zipped/", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("Response",response);
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

                                @Override
                                public byte[] getBody() throws AuthFailureError {
                                    try {

                                        Log.d("json",str);
                                        return str == null ? null : str.getBytes("utf-8");
                                    } catch (UnsupportedEncodingException uee) {
                                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                                str, "utf-8");
                                        return null;
                                    }
                                }
                            };
                            stringRequest.setRetryPolicy(policy);
                            singleton.queue.add(stringRequest);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    catch (IOException e) {
                        //You'll need to add proper error handling here
                        Log.d("wsef",e.toString());

                    }

                }
            }
        }

    }
    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");

        }

        inputStream.close();
        reader.close();
        return stringBuilder.toString();
    }

}
