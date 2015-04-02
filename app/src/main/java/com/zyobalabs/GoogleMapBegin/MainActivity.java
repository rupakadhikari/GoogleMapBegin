package com.zyobalabs.GoogleMapBegin;

import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zyobalabs.GoogleMapBegin.AppController.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends FragmentActivity {
    private GoogleMap googleMap;
    private static String TAG = MainActivity.class.getSimpleName();
    private String urlJsonArry = "http://api.androidhive.info/volley/person_array.json";
    private String jsonResponse;
    public static String name;
    LatLng latLng1;
    String tag_json_obj = "json_obj_req";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize googlemap object to mapfragment
        googleMap=((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        //new latitude longitude position
         latLng1 = new LatLng(25.77,85.77);
        //camera position seeting up
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng1).tilt(4).zoom(4).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //adding point to given latlng
        googleMap.addMarker(new MarkerOptions().position(latLng1).title("start"));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latLng1=latLng;
                System.out.println(latLng);
              goToThere();
             
            }
        });

    }

    private void goToThere(){

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlJsonArry,
        new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d(TAG, response.toString());
                try {
                    jsonResponse = "";
                    JSONObject person = (JSONObject) response
                            .get(0);
                     name = person.getString("name");
                    googleMap.addMarker(new MarkerOptions().position(latLng1).title(name));
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){

                    VolleyLog.d(TAG,"Error: " + error.getMessage());
                }


        });


        AppController.getInstance().addToRequestQueue(jsonArrayRequest);



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
