package com.example.gunesyurdakul.cp1v2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.common.hash.Hashing;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;


public class LoginFragment extends Fragment {
    Bitmap tocheck;

    static final int CAMERA_REQUEST = 1888;
    FaceServiceClient faceServiceClient;

    Singleton singleton = Singleton.getSingleton();
    View view;
    static boolean res;
    static boolean complete;

    public LoginFragment() {
        // Required empty public constructor
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        faceServiceClient = new FaceServiceRestClient(getActivity().getResources().getString(R.string.endpoint), getActivity().getResources().getString(R.string.subscription_key));
        res=false;
        complete=false;
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        //views
        final Button login = (Button) view.findViewById(R.id.sign_in);
        final TextView signUp = (TextView) view.findViewById(R.id.sign_up);
        final EditText idText = (EditText) view.findViewById(R.id.id_edit);
        final TextView warning = (TextView) view.findViewById(R.id.warning_login);
        final EditText pass = (EditText) view.findViewById(R.id.password);

        //If activity starts because of 3 unsuccesful login attempt the below warning will shown
        if(singleton.unsuccesfulLogin)
        {
            singleton.unsuccesfulLogin=false;
            warning.setText("3 unsuccessful login attempts");
        }
        //Sign in button click listener
        login.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View v) {
                                         //sign in constraints
                                         final String id = idText.getText().toString();
                                         final String password = pass.getText().toString();
                                         final String hashed = Hashing.sha256()
                                                 .hashString(password, Charset.forName("UTF-8"))
                                                 .toString();
                                         //if username field is blank
                                         validate(id,hashed);
                                         if (id.length() == 0) {
                                             warning.setText("Username is blank!");
                                         }
                                         else {
                                             if (password.trim().length() == 0) {
                                                 warning.setText("Password is blank!");
                                             }
                                             //If there is no such username in existing users
                                             else{
                                                 int socketTimeout = 10000; // 30 seconds. You can change it
                                                 RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                                                         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                                 complete=false;
                                                 try {
                                                     StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://cp-h-server.herokuapp.com/user/verify/"+id+"/"+hashed , new Response.Listener<String>() {
                                                         @Override
                                                         public void onResponse(String response) {
                                                             Log.d("Response",response);
                                                             if(response.equals("true"))
                                                                 res=true;
                                                             else
                                                                 res=false;
                                                             complete = true;
                                                             //User is found from user map using its username as key of the map
                                                             User logging_in = new User();

                                                             //If password field is blank
                                                             //if all conditions are satisfied
                                                             if (res) {
                                                                 Log.d("username in edittext",id);
                                                                 singleton.currentUser = logging_in;
                                                                 logging_in.username=id;
                                                                 Log.d("current set",logging_in.username);

                                                                 UsersList user = new UsersList();
                                                                 FragmentManager fm = getFragmentManager();
                                                                 FragmentTransaction ft = fm.beginTransaction();
                                                                 Bundle args = new Bundle();
                                                                 user.setArguments(args);
                                                                 ft.replace(R.id.fragment, user);
                                                                 ft.addToBackStack("addemployee");
                                                                 ft.commit();
                                                                 warning.setText("Başarılı:)");
                                                             } else {
                                                                 warning.setText("Password and Id do not match!");

                                                             }
                                                         }
                                                     }, new Response.ErrorListener() {
                                                         @Override
                                                         public void onErrorResponse(VolleyError error) {
                                                             Log.d("ERROR",error.toString());
                                                             complete = true;
                                                             warning.setText("No such username exists!");

                                                         }
                                                     }) {
                                                         @Override
                                                         public String getBodyContentType() {
                                                             complete = true;
                                                             return String.format("application/boolean; charset=utf-8");

                                                         }
                                                     };
                                                     stringRequest.setRetryPolicy(policy);
                                                     singleton.queue.add(stringRequest);
                                                 } catch (Exception e) {
                                                     e.printStackTrace();
                                                 }
                                             }

                                         }

                                     }

                                     ;
                                 }
        );
        //Sign up button click listener, if the user wants to register add new user fragment replaces the existing fragment
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNewUser userFragment = new addNewUser();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Bundle args = new Bundle();
                userFragment.setArguments(args);
                ft.replace(R.id.fragment, userFragment);
                ft.addToBackStack("addUser");
                ft.commit();
            }

            ;
        });
        return view;
    }

public void validate(String username,String password){


}
}