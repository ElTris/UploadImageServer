package com.example.itr.uploadimageserver;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MSingleton {

    private static MSingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private MSingleton(Context con){
        context = con;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }


    public static synchronized MSingleton getmInstance(Context con){
        if (mInstance == null){
            mInstance = new MSingleton(con);
        }
        return mInstance;
    }

    public<T> void addrequerQueue(Request<T> request){
        requestQueue.add(request);
    }


}
