package com.example.fuelquemanagement_client;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import org.json.JSONException;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelquemanagement_client.constants.Constants;
import com.example.fuelquemanagement_client.database.DatabaseHelper;
import com.example.fuelquemanagement_client.database.InputValidatorHelper;
import com.example.fuelquemanagement_client.models.FuelStation;
import com.example.fuelquemanagement_client.models.User;
import com.example.fuelquemanagement_client.station_owner.StationOwnerDashboard;
import com.example.fuelquemanagement_client.vehicle_owner.SelectionStation;

/**
 * The LoginScreen class facilitates the application user to login to the application
 */
public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    final FuelStation[] registeredFuelStation = {new FuelStation()};
    private EditText edUserName, edPassword;
    private DatabaseHelper databaseHelper;
    private TextView txtRegisterLink;
    private Button btnLogin;
    private User user;

    @Override
    public void onClick(View view) {
        boolean allowSave = checkValidations();
        switch (view.getId()) {
            case R.id.txt_linkRegister:
                showAlertDialog();
                break;
            case R.id.btn_logIn:
                if (allowSave) {
                    user = databaseHelper.loginValidate(edUserName.getText().toString(), edPassword.getText().toString());
                    if (user == null) {
                        Toast.makeText(LoginScreen.this, "Invalid Credentials, Please try again", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginScreen.this, "Successfully Logged", Toast.LENGTH_LONG).show();
                        if (user.getRole().equals(Constants.VEHICLE)) {
                            Intent i = new Intent(LoginScreen.this, SelectionStation.class);
                            i.putExtra(Constants.LOGGED_USER, user);
                            startActivity(i);
                        } else {
                            System.out.println("Station Id " + user.getStationId());
                            findFuelStation(user.getStationId());
                        }
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
        setContentView(R.layout.activity_login_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign In");

        edUserName = findViewById(R.id.edtTxt_username);
        edPassword = findViewById(R.id.edtTxt_password);

        btnLogin = findViewById(R.id.btn_logIn);
        btnLogin.setOnClickListener(this);

        txtRegisterLink = findViewById(R.id.txt_linkRegister);
        txtRegisterLink.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(this);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginScreen.this);
        alertDialog.setTitle("Register As ");
        String[] items = {"Vehicle Owner", "Station Owner"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(LoginScreen.this, "You are going to register as a Vehicle Owner", Toast.LENGTH_LONG).show();
                        Intent intentVehicle = new Intent(LoginScreen.this, Registration.class);
                        intentVehicle.putExtra(Constants.ROLE, Constants.VEHICLE);
                        startActivity(intentVehicle);
                        break;
                    case 1:
                        Toast.makeText(LoginScreen.this, "You are going to register as a Station Owner", Toast.LENGTH_LONG).show();
                        Intent intentStation = new Intent(LoginScreen.this, Registration.class);
                        intentStation.putExtra(Constants.ROLE, Constants.STATION);
                        startActivity(intentStation);
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    // Validate the input fields of the form in login
    public boolean checkValidations() {
        InputValidatorHelper inputTaskValidatorHelper = new InputValidatorHelper();
        //Validate and Save
        boolean allowSave = true;
        if (inputTaskValidatorHelper.isNullOrEmpty(edUserName.getText().toString())) {
            Toast.makeText(this, "Username Should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (inputTaskValidatorHelper.isNullOrEmpty(edPassword.getText().toString())) {
            Toast.makeText(this, "Password Should not be Empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        return allowSave;
    }

    // find and retrieve the object of the fuel station which is owned by the logged station owner
    public void findFuelStation(String stationId) {

        ArrayList<FuelStation> stations = new ArrayList<>();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String api = Constants.BASE_URL + "/FuelStation";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, api,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
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
                                for (FuelStation station : stations) {
                                    if (station.getId().equals(stationId)) {
                                        registeredFuelStation[0] = station;
                                        Intent intent = new Intent(LoginScreen.this, StationOwnerDashboard.class);
                                        intent.putExtra(Constants.STATION, registeredFuelStation[0]);
                                        startActivity(intent);
                                    }
                                }
                            }

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
}