package com.example.memeshare;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.FoldingCube;
import com.github.ybq.android.spinkit.style.Pulse;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.github.ybq.android.spinkit.style.Wave;
import org.apache.commons.io.FileUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /*
     * first we searched for our desired API on internet then we extract the API link.
     * then we added that link into our Gradle scripts/module: MemeShare.app
     * then we sync the services.so that we can use that API.
     * now we opened the manifest/AndroidMenifest.xml , in which we will add our innternet permission which is required for our API calls.
     * we have to add all our app permission/background process in this AndroidMenifest.xml.
     * */
    ImageView imageView;
    String currentImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.memeImageView);
        FileUtils.deleteQuietly(this.getCacheDir());        //deletes caches

        loadMeme();
    }
    /*
    * //There are many types of requests... GET for getting data, POST for posting data[giving data to backend] ,PUT for updating backend data.
        // Here we are using simple GET type request
        // Request a string response from the provided URL.
    * */
    private void loadMeme() {


        ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit);
        Sprite doubleBounce = new CubeGrid();           //change progressBar type here
        progressBar.setIndeterminateDrawable(doubleBounce);

        progressBar.setVisibility(View.VISIBLE);

//        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://meme-api.herokuapp.com/gimme";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url = response.getString("url");
                            currentImageUrl = url;
                            Glide.with(MainActivity.this).load(url).into(imageView);
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,getString(R.string.error_message),  Toast.LENGTH_LONG).show();
            }
        });
//        mQueue.add(request);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, checkout this cool meme : " + currentImageUrl);
        Intent chooser = Intent.createChooser(intent, "Share this meme using ");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
        else{
            Toast.makeText(MainActivity.this,"No apps can perform this action",  Toast.LENGTH_LONG).show();
        }
//        startActivity(chooser);
    }

    public void nextMeme(View view) {
        FileUtils.deleteQuietly(this.getCacheDir());        //deletes caches
        loadMeme();
    }
}
/*
* FOR WALLPAPER :
*     //    private void loadMeme1(){
//        String url = "https://picsum.photos/600";
//        Glide.with(this)
//                .load(url)
//                .placeholder(R.drawable.ic_launcher_foreground)
//                .into(imageView);
//    }
* */