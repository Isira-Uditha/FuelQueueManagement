package com.example.fuelquemanagement_client.station_owner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelquemanagement_client.R;
import com.example.fuelquemanagement_client.constants.Constants;
import com.example.fuelquemanagement_client.database.InputValidatorHelper;
import com.example.fuelquemanagement_client.models.FuelStation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

/**
 * The FuelUpdateForm class facilitates Station Owner to enter the details fuel arrivals of the fuel station
 */
public class FuelUpdateForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextView tv_arrival_date, tv_arrival_time;
    private int hour, min;
    private EditText et_fuel_amount;
    private Spinner sp_fuel_type;
    private Button btn_update, btn_cancel;
    private String selectedType, stationID;
    private FuelStation fuelStation, updatedFuelStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_update_form);

        // App Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Fuel Arrival");

        // Station ID by the intent
        fuelStation = (FuelStation) getIntent().getSerializableExtra(Constants.STATION);
        stationID = fuelStation.getId();

        et_fuel_amount = findViewById(R.id.editTextFuelAmount);
        tv_arrival_date = findViewById(R.id.tv_ArrivalDate);
        tv_arrival_time = findViewById(R.id.tv_ArrivalTime);
        btn_update = findViewById(R.id.btn_update);
        btn_cancel = findViewById(R.id.btn_cancel);

        // Cancel button navigation
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FuelUpdateForm.this, StationOwnerDashboard.class);
                startActivity(intent);
            }
        });

        // Update button navigation
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checks for validations
                boolean checks = checkValidations();
                if(checks){
                    postFuelUpdate();
                    try {
                        findFuelStationById(stationID);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(FuelUpdateForm.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Setting the drop down menu
        sp_fuel_type = (Spinner) findViewById(R.id.sp_fuel_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.fuel_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_fuel_type.setAdapter(adapter);
        sp_fuel_type.setOnItemSelectedListener(this);

        // Implementing the Time Picker
        tv_arrival_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(FuelUpdateForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        min = minute;
                        Calendar calendar = Calendar.getInstance();

                        // Set hour and minute
                        calendar.set(0, 0, 0, hour, min);

                        // Set selected time on text view
                        tv_arrival_time.setText(DateFormat.format("hh:mm aa", calendar));
                    }
                }, 12, 0, false);
                // Display previous selected time
                timePickerDialog.updateTime(hour, min);
                timePickerDialog.show();
            }
        });

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Implementing the Date Picker
        tv_arrival_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FuelUpdateForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        tv_arrival_date.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    // Function to obtain the selected fuel type from the drop down menu (spinner)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        selectedType = (String) adapterView.getItemAtPosition(pos).toString();
        Toast.makeText(this, selectedType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Function to send fuel update data as post call
    private void postFuelUpdate() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            String URL = Constants.BASE_URL + "/Fuel";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", selectedType);
            jsonObject.put("amount", et_fuel_amount.getText().toString());
            jsonObject.put("date", tv_arrival_date.getText().toString());
            jsonObject.put("time", tv_arrival_time.getText().toString());
            jsonObject.put("stationsId", stationID);
            final String mRequestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response.getClass().getName());

                    // Checking the response to show the toast messages
                    if (response.equals("200")) {
                        Toast.makeText(FuelUpdateForm.this, Constants.UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FuelUpdateForm.this, Constants.ERROR, Toast.LENGTH_SHORT).show();
                    }
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
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding", mRequestBody, "utf-8");
                        return null;
                    }
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
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Back button navigation
        if (id == android.R.id.home) {
            Intent intent = new Intent(FuelUpdateForm.this, StationOwnerDashboard.class);
            intent.putExtra(Constants.STATION, fuelStation);
            startActivity(intent);
        }
        return true;
    }

    // find and retrieve the object of the fuel station which is owned by the logged station owner
    public void findFuelStationById(String stationId) throws InterruptedException {
        Thread.sleep(2500);
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
                            System.out.println("HERE THE RESPONSE OBJECT" + singleObject);
                            Log.e("api", "onResponse: " + singleObject.getString("id"));
                            FuelStation fuelStationUp = new FuelStation(
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
                            Log.e("api", "onResponse: " + fuelStationUp.getStationName() + " has loaded!");
                            System.out.println("TOTAL" + fuelStationUp.getTotalPetrol());
                            updatedFuelStation = fuelStationUp;

                            // Navigation after database operation
                            Intent intent = new Intent(FuelUpdateForm.this, StationOwnerDashboard.class);
                            intent.putExtra(Constants.STATION, updatedFuelStation);
                            startActivity(intent);
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

    // Function to check input field validations
    public boolean checkValidations() {
        InputValidatorHelper inputValidatorHelper = new InputValidatorHelper();

        boolean save = true;

        if(inputValidatorHelper.isNullOrEmpty(et_fuel_amount.getText().toString())) {
            Toast.makeText(this, "Fuel Amount should not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(inputValidatorHelper.isNullOrEmpty(tv_arrival_date.getText().toString())) {
            Toast.makeText(this, "Arrival Date should not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(inputValidatorHelper.isNullOrEmpty(tv_arrival_time.getText().toString())) {
            Toast.makeText(this, "Arrival Time should not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return save;
    }
}