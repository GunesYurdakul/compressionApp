package com.example.gunesyurdakul.cp1v2;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by gunesyurdakul on 10/11/2017.
 */

public class GsonRequest<T> extends Request<T> {
    private Gson gson = new Gson();
    private final Type type;
    private final TxtFile file;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param type Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int method,String url, TxtFile file, Type type, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.type = type;
        this.headers = headers;
        this.listener = listener;
        this.file=file;
        Log.d("init","init");
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        Log.d("POST","gidiyor");
        return gson.toJson(file).getBytes();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T parseObject = gson.fromJson(json,type);
            return Response.success(parseObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.d("error1","");
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            Log.d("error2","");
            return Response.error(new ParseError(e));
        }
    }
}
