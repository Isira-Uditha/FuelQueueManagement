package com.example.fuelquemanagement_client.vehicle_owner;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fuelquemanagement_client.R;
import com.example.fuelquemanagement_client.constants.Constants;
import com.example.fuelquemanagement_client.models.FuelStation;
import com.example.fuelquemanagement_client.models.User;

/**
 * The StationAdapter class is used for shown Fuel Stations objects in the list view in
 * the SelectStation Activity
 */
public class StationAdapter extends ArrayAdapter<FuelStation>{

    private int resource;
    private Context context;
    private User loggedUser;
    List<FuelStation> stations;

    public StationAdapter(@NonNull Context context, int resource, List<FuelStation> stations, User loggedUser) {
        super(context, resource, stations);
        this.context = context;
        this.resource = resource;
        this.stations = stations;
        this.loggedUser = loggedUser;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(resource,parent,false);

        //Create objects from the elements that are in the single_station xml file and set their id numbers.
        TextView stationName = row.findViewById((R.id.idStationName));
        TextView StationLocation = row.findViewById((R.id.idLocation));

        final FuelStation station = stations.get(position);

        //Set details of that station record into the text views in the single_station xml file
        stationName.setText(station.getStationName());
        StationLocation.setText(station.getLocation());

        stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context,VehicleOwnerDashboard.class);
                myIntent.putExtra(Constants.STATION, station);
                myIntent.putExtra(Constants.LOGGED_USER, loggedUser);
                context.startActivity(myIntent);
            }
        });

        StationLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context,VehicleOwnerDashboard.class);
                myIntent.putExtra(Constants.STATION, station);
                myIntent.putExtra(Constants.LOGGED_USER, loggedUser);
                context.startActivity(myIntent);
            }
        });
        return row;
    }

    public StationAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
