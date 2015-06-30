package com.example.jonathanlaguerre.instagram;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends Activity {

    public static final String CLIENT_ID = "a70218c7f9994df8a1ebdfff244e4734";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter photosAdapter;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //List of photos
        photos= new ArrayList<>();

        //Create the adapter linking it to the source
        photosAdapter = new InstagramPhotosAdapter(this, photos);

        ListView lvphotos = (ListView) findViewById(R.id.lvphotos);

        //Set the adapter binding it to the listview
        lvphotos.setAdapter(photosAdapter);

        //SEND OUT API REQUEST
        fetchPopularPhotos();

        //Swipe Container
        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    public void fetchPopularPhotos(){
     /*
     CLIENT ID: a70218c7f9994df8a1ebdfff244e4734

          -POPULAR: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
    -RESPONSE
      - Type: {"data" => [x] => "type"} ("image" or video)
      - URL: {"data" => [x] => "images" => "standard_resolution" => "username"}
      - Caption: {"data" => [x] => "caption" => "text" }
      - Author Name: {"data" +> [x] => "user" => "username"}

     */
        String url = "https://api.instagram.com/v1/media/popular?client_id="+ CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {

            //OnSuccess (Worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
                //- Type: {"data" => [x] => "type"} ("image" or video)
                //- URL: {"data" => [x] => "images" => "standard_resolution" => "username"}
                //- Caption: {"data" => [x] => "caption" => "text" }
                //- Author Name: {"data" +> [x] => "user" => "username"}
                //Iterate each of the photo item and decode into a java object
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");

                    // iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get JSON object at position i
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        // decode the attributes of the JSON
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.profilePicture = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
                photosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //DO SOMETHING
                swipeContainer.setRefreshing(false);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
