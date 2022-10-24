package com.example.fuelquemanagement_client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import org.json.JSONObject;
import org.json.JSONException;
import android.view.View;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fuelquemanagement_client.constants.Constants;
import com.example.fuelquemanagement_client.database.DatabaseHelper;
import com.example.fuelquemanagement_client.database.InputValidatorHelper;
import com.example.fuelquemanagement_client.models.User;

/**
 * The Registration class facilitates the application user to register to the system as Vehicle Owner or a Station Owner
 */
public class Registration extends AppCompatActivity implements View.OnClickListener {

    private EditText edFullName, edUserName, edPassword, edConfirmPassword, edLocation, edStationName;
    final String[] stationId = new String[1];
    private DatabaseHelper databaseHelper;
    private Button btnRegister;
    private String role;
    private User user;

    @Override
    public void onClick(View view) {
        boolean allowSave = checkValidations(role);
        switch (view.getId()) {
            case R.id.btn_register:
                if (role.equals(Constants.VEHICLE)) {
                    if (allowSave) {
                        user = addVehicleUser();
                    }
                } else {
                    if (allowSave) {
                        postDataUsingVolleyObject(edStationName.getText().toString(), edLocation.getText().toString(), "");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registration");

        role = getIntent().getStringExtra(Constants.ROLE);

        edFullName = findViewById(R.id.edtTxt_fname);
        edUserName = findViewById(R.id.edtTxt_username);
        edPassword = findViewById(R.id.edtTxt_password);
        edConfirmPassword = findViewById(R.id.edtTxt_confirmPassword);
        edLocation = findViewById(R.id.edtTxt_location);
        edStationName = findViewById(R.id.edtTxt_stationName);

        if (role.equals(Constants.VEHICLE)) {
            edLocation.setVisibility(View.GONE);
            edStationName.setVisibility(View.GONE);
        } else {
            edLocation.setVisibility(View.VISIBLE);
            edStationName.setVisibility(View.VISIBLE);
        }

        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
    }

    //When user clicks on register button, check for the validations in input fields
    public boolean checkValidations(String role) {

        InputValidatorHelper inputTaskValidatorHelper = new InputValidatorHelper();

        //Validate and Save
        boolean allowSave = true;

        if (inputTaskValidatorHelper.isNullOrEmpty(edFullName.getText().toString())) {
            Toast.makeText(this, "Full Name Should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (inputTaskValidatorHelper.isNullOrEmpty(edUserName.getText().toString())) {
            Toast.makeText(this, "Username Should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (role.equals(Constants.STATION)) {
            if (inputTaskValidatorHelper.isNullOrEmpty(edLocation.getText().toString())) {
                Toast.makeText(this, "Station Location Should not be Empty", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (inputTaskValidatorHelper.isNullOrEmpty(edStationName.getText().toString())) {
                Toast.makeText(this, "Station Location Name Should not be Empty", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (inputTaskValidatorHelper.isNullOrEmpty(edPassword.getText().toString())) {
            Toast.makeText(this, "Password Should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!edPassword.getText().toString().equals(edConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Password and Confirm Password are mismatched", Toast.LENGTH_SHORT).show();
            return false;
        }

        return allowSave;
    }

    /*If user registered as a vehicle Owner this function executes
    and register the user to the system while keeping login credentials in sqllite database
     */
    public User addVehicleUser() {
        user = new User(-1, edFullName.getText().toString(), edUserName.getText().toString(), "", "", edPassword.getText().toString(), "", Constants.VEHICLE);
        boolean success = databaseHelper.addUserRecord(user);
        if (success) {
            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Registration.this, LoginScreen.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return user;
    }

    /*If user registered as a station Owner this function executes
        and register the user to the system while keeping login credentials in sqllite database
    */
    public User addStationUser(String stationId) {

        user = new User(-1, edFullName.getText().toString(), edUserName.getText().toString(), edLocation.getText().toString(), edStationName.getText().toString(), edPassword.getText().toString(), stationId, Constants.STATION);
        boolean success = databaseHelper.addUserRecord(user);
        if (success) {
            String id = databaseHelper.getLastStationOwnerId();
            System.out.println("Last Owner Id:" + id);

            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Registration.this, LoginScreen.class);
            startActivity(i);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return user;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        //If user clicks on the back button
        if (id == android.R.id.home) {
            Intent intent = new Intent(Registration.this, LoginScreen.class);
            startActivity(intent);
        }
        return true;
    }

    //Add station to the remote database through api call along with registered station owner details
    private void postDataUsingVolleyObject(String stationName, String locationName, String stationOwnerId) {
        JSONObject jsonObject = new JSONObject();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        String URL = Constants.BASE_URL + "/FuelStation";

        try {
            jsonObject.put("id", null);
            jsonObject.put("name", stationName);
            jsonObject.put("location", locationName);
            jsonObject.put("stationOwner", stationOwnerId);
            jsonObject.put("dieselStatus", false);
            jsonObject.put("petrolStatus", false);
            jsonObject.put("lastModified", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("Inside new Api" + response);
                        try {
                            System.out.println("StationIdNew" + response.getString("id"));
                            stationId[0] = response.getString("id");
                            addStationUser(stationId[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(jsonObjReq);
    }

}