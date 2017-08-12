package hussain.com.projectx;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HelpActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference databaseReference;
    ArrayList<LatLong> trackingLocationsList = new ArrayList<>();

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.help_map_fragment);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to handle app links.

        databaseReference = FirebaseDatabase.getInstance().getReference("tracking");
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        id=appLinkData.getQueryParameter("id");


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trackingLocationsList.clear();
                Iterable<DataSnapshot> tracks = dataSnapshot.child(id).getChildren();
                for (DataSnapshot info : tracks) {
                    LatLong latLong = info.getValue(LatLong.class);
                    trackingLocationsList.add(latLong);
                }
                int i=0;
                LatLng previous,next = null;
                for(LatLong value : trackingLocationsList){
                    if(i==0){
                        previous = new LatLng(Double.parseDouble(value.getLatitude()), Double.parseDouble(value.getLongitude()));
                        next = new LatLng(Double.parseDouble(value.getLatitude()), Double.parseDouble(value.getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(previous).title("Marker"+i++));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(previous));
                    }
                    else{
                        previous=next;
                        next = new LatLng(Double.parseDouble(value.getLatitude()), Double.parseDouble(value.getLongitude()));
                        mMap.addMarker(new MarkerOptions().position(next).title("Marker"+i++));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(next));
                    }
                    mMap.addPolyline(new PolylineOptions()
                            .add(previous,next)
                            .width(5)
                            .color(Color.RED));
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
