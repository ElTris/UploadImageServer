package com.example.itr.uploadimageserver;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView pictureUpload;
    EditText namepicture;
    Button btnUpload,btnchooser;
    String ip="http://192.168.0.105/publication/imageup.php";

    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureUpload=findViewById(R.id.picture);
        namepicture=findViewById(R.id.txtname);
        btnUpload=findViewById(R.id.uploag_img);
        btnchooser=findViewById(R.id.chooseimg);

        btnUpload.setOnClickListener(this);
        btnchooser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.chooseimg:
                selectImage();
                break;


            case R.id.uploag_img:
                uploadImage();
                break;
        }
    }



    private void selectImage(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                pictureUpload.setImageBitmap(bitmap);
                pictureUpload.setVisibility(View.VISIBLE);
                namepicture.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void uploadImage(){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, ip, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=  new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toasty.info(MainActivity.this,Response,Toast.LENGTH_LONG).show();
                    pictureUpload.setImageResource(0);
                    pictureUpload.setVisibility(View.GONE);
                    namepicture.setText("");
                    namepicture.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",namepicture.getText().toString());
                params.put("image",imageToString(bitmap));

                return params;
            }
        };

        MSingleton.getmInstance(MainActivity.this).addrequerQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imgbytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgbytes,Base64.DEFAULT);

    }





}
