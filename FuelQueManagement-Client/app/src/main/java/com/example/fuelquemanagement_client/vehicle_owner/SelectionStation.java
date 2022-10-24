package com.example.fuelquemanagement_client.vehicle_owner;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.fuelquemanagement_client.LoginScreen;
import com.example.fuelquemanagement_client.R;
import com.android.volley.toolbox.StringRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fuelquemanagement_client.databinding.ActivityMainBinding;
import com.example.fuelquemanagement_client.models.User;
import com.example.fuelquemanagement_client.models.FuelStation;
import com.example.fuelquemanagement_client.constants.Constants;

/**
 * The SelectionStation class is shown all the Fuel Stations in Listview and Give facility to  search them through Location
 */
public class SelectionStation extends AppCompatActivity {

    private User loggedUser;
    private ListView stationView;
    private EditText edSearchStation;
    private ArrayList<FuelStation> stations;
    String api = Constants.BASE_URL+"/FuelStation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_station);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select Nearest Station");

        stationView = (ListView)findViewById(R.id.idStationView);
        stations = new ArrayList<FuelStation>();

        edSearchStation = (EditText)findViewById(R.id.edtTxt_searchStation);

        loadStations();
    }

    //Load the all fuel stations that are registered from the remote database via the api call
    private void loadStations() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest =  new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            int length = array.length();
                            for(int i=0;i<array.length();i++){
                                JSONObject singleObject = array.getJSONObject(i);
                                FuelStation fuelStation = new FuelStation(
                                        singleObject.getString("id"),
                                        singleObject.getString("name"),
                                        singleObject.getString("location"),
                                        singleObject.getString("stationOwner"),
                                        singleObject.getString("lastModified"),
                                        singleObject.getBoolean("dieselStatus"),
                                        singleObject.getBoolean("petrolStatus"),
                                        singleObject.getInt("totalDiesel"),
                                        singleObject.getInt("totalPetrol")
                                );
                                stations.add(fuelStation);
                            }
                            loggedUser = (User) getIntent().getSerializableExtra(Constants.LOGGED_USER);
                            StationAdapter adapter = new StationAdapter(SelectionStation.this , R.layout.single_station, stations,loggedUser);
                            stationView.setAdapter(adapter);

                            edSearchStation.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                   // adapter.getFilter().filter(s);
                                   ArrayList<FuelStation> filteredShapes = new ArrayList<FuelStation>();
                                    String selectedFilter = "all";
                                    for(FuelStation shape: stations)
                                    {
                                        if(shape.getLocation().toLowerCase().contains(String.valueOf(s).toLowerCase()))
                                        {
                                                filteredShapes.add(shape);
                                        }
                                    }

                                    StationAdapter adapter = new StationAdapter(SelectionStation.this , R.layout.single_station, filteredShapes,loggedUser);
                                    stationView.setAdapter(adapter);
                                }

                                @Override
                                public void afterTextChanged(Editable s) {

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work! +" + error.getLocalizedMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //If user clicks on the back button
        if(id == android.R.id.home){
            Intent intent = new Intent(SelectionStation.this, LoginScreen.class);
            startActivity(intent);
        }
        return true;
    }
}