package com.example.fuelquemanagement_client.station_owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelquemanagement_client.MainActivity;
import com.example.fuelquemanagement_client.R;
import com.example.fuelquemanagement_client.constants.Constants;
import com.example.fuelquemanagement_client.models.FuelStation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The StationOwnerDashboard class facilitates the Station Owner see a dashboard view for the Fuel Station including fuel status
 */
public class StationOwnerDashboard extends AppCompatActivity {
    private Button btn_update_fuel;
    private RadioGroup radioGroupPetrol, radioGroupDiesel;
    private RadioButton rbPetrol, rbPetrolAvailable, rbPetrolFinish;
    private RadioButton rbDiesel, rbDieselAvailable, rbDieselFinish;
    private String stationID;
    private boolean status_petrol, status_diesel;
    private FuelStation fuelStation, loaded_fuel_station;
    private TextView textView, petrol, diesel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // App Bar
        setContentView(R.layout.activity_station_owner_dashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Station Owner Dashboard");

        fuelStation = (FuelStation) getIntent().getSerializableExtra(Constants.STATION);
        stationID = fuelStation.getId();

        loaded_fuel_station = findFuelStationById(stationID);

        btn_update_fuel = findViewById(R.id.btn_update_fuel);
        radioGroupPetrol = findViewById(R.id.rad_group_petrol);
        radioGroupDiesel = findViewById(R.id.rad_group_diesel);
        rbDieselFinish = findViewById(R.id.rad_diesel_finish);
        rbDieselAvailable = findViewById(R.id.rad_diesel_available);
        rbPetrolFinish = findViewById(R.id.rad_petrol_finish);
        rbPetrolAvailable = findViewById(R.id.rad_petrol_available);
        textView = findViewById(R.id.txt_station_name);
        petrol = findViewById(R.id.txt_current_petrol);
        diesel = findViewById(R.id.txt_current_diesel);

        // Setting the currently available petrol and diesel amounts
        petrol.setText(" Available Petrol Amount: " + loaded_fuel_station.getTotalPetrol() + " Liters");
        System.out.println("PETROLLLL" + loaded_fuel_station.getTotalPetrol());
        diesel.setText(" Available Diesel Amount: " + loaded_fuel_station.getTotalDiesel() + "Liters");

        // Setting station name and location on the interface
        textView.setText(loaded_fuel_station.getStationName() + " - " + loaded_fuel_station.getLocation());

        status_petrol = loaded_fuel_station.isPetrolStatus();
        status_diesel = loaded_fuel_station.isDieselStatus();

        // Setting the current Diesel Status on the interface
        if (status_diesel == false) {
            rbDieselFinish.setChecked(true);
        } else {
            rbDieselAvailable.setChecked(true);
        }

        // Setting the current Petrol Status on the interface
        if (status_petrol == false) {
            rbPetrolFinish.setChecked(true);
        } else {
            rbPetrolAvailable.setChecked(true);
        }

        // Onclick function for Petrol Status Radio Button
        radioGroupPetrol.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String URL_petrol, status;
                rbPetrol = findViewById(i);
                status = rbPetrol.getText().toString();

                String URL_to_send;
                System.out.println("Radio Result " + rbPetrol.getText().toString());

                if (rbPetrol.getText().toString().equals("Available")) {
                    System.out.println("Send Petrol as available");
                    URL_petrol = Constants.BASE_URL + "/FuelStation/UpdatePetrolStatus?status=true&id=";
                } else {
                    System.out.println("Send Petrol as finished");
                    URL_petrol = Constants.BASE_URL + "/FuelStation/UpdatePetrolStatus?status=false&id=";
                }

                URL_to_send = URL_petrol.concat(stationID);
                showAlertDialog(URL_to_send, Constants.PETROL, status);
            }
        });

        // Onclick function for Diesel Status Radio Button
        radioGroupDiesel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String URL_diesel;
                rbDiesel = findViewById(i);

                String URL_to_send, status;
                status = rbDiesel.getText().toString();
                System.out.println("Radio Result " + rbDiesel.getText().toString());

                if (rbDiesel.getText().toString().equals("Available")) {
                    System.out.println("Send Diesel as available");
                    URL_diesel = Constants.BASE_URL + "/FuelStation/UpdateDieselStatus?status=true&id=";
                } else {
                    System.out.println("Send Diesel as finished");
                    URL_diesel = Constants.BASE_URL + "/FuelStation/UpdateDieselStatus?status=false&id=";
                }

                URL_to_send = URL_diesel.concat(stationID);
                showAlertDialog(URL_to_send, Constants.DIESEL, status);
            }
        });

        // Update Fuel arrival form button navigation
        btn_update_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StationOwnerDashboard.this, FuelUpdateForm.class);
                intent.putExtra(Constants.STATION, loaded_fuel_station);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //If user clicks on the back button
        if (id == android.R.id.home) {
            Intent intent = new Intent(StationOwnerDashboard.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }

    // Function to handle the radio select of fuel availability with alert dialog
    private void showAlertDialog(String URL_to_send, String fuelType, String status) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StationOwnerDashboard.this);
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage(
                        "" +
                                "Are you sure to change " + fuelType +
                                " status into " + status + "?"
                )
                .setCancelable(true)
                // Cancel button action
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(StationOwnerDashboard.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                })
                // Submit button action
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("Send this " + URL_to_send);
                        StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL_to_send, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_VOLLEY", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_VOLLEY", error.toString());
                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";
                                if (response != null) {
                                    responseString = String.valueOf(response.statusCode);
                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };
                        // sending the POST Request
                        requestQueue.add(stringRequest);

                        if(fuelType.equals("Petrol")) {
                            if(status.equals("Available")) {
                                loaded_fuel_station.setPetrolStatus(true);
                            } else {
                                loaded_fuel_station.setPetrolStatus(false);
                            }
                        } else {
                            if(status.equals("Available")) {
                                loaded_fuel_station.setDieselStatus(true);
                            } else {
                                loaded_fuel_station.setDieselStatus(false);
                            }
                        }

                        Intent intent = new Intent(StationOwnerDashboard.this, StationOwnerDashboard.class);
                        intent.putExtra(Constants.STATION, loaded_fuel_station);
                        startActivity(intent);
                    }
                })
                .setIcon(R.drawable.warning)
        ;
        AlertDialog alert = alertDialog.create();

        alert.show();
    }

    // find and retrieve the object of the fuel station which is owned by the logged station owner
    public FuelStation findFuelStationById(String stationId) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.BASE_URL + "/FuelStation/";
        String get_url = url.concat(stationId);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, get_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject singleObject = new JSONObject(response);
                            Log.e("api", "onResponse: " + singleObject.getString("id"));
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
                            Log.e("api", "onResponse: " + fuelStation.getStationName() + " has loaded!");
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
        return fuelStation;
    }
}