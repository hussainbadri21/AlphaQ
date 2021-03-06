package hussain.com.projectx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import at.markushi.ui.CircleButton;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SOSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SOSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SOSFragment extends Fragment implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Button sosButton;
    TextView sosStatusTextView;
    Boolean isSosActivated = false;    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    String id;
    int i=0;
    ArrayList<Contact> contact = new ArrayList<>();

    private FusedLocationProviderClient mFusedLocationClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SOSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SOSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SOSFragment newInstance(String param1, String param2) {
        SOSFragment fragment = new SOSFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        id=sharedPreferences.getString("id","");
        sosStatusTextView = (TextView) view.findViewById(R.id.sos_status_text_view);
        sosButton = (Button) view.findViewById(R.id.sos_button);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contact.clear();
                Iterable<DataSnapshot> tracks = dataSnapshot.child("users").child(id).getChildren();
                for (DataSnapshot info : tracks) {
                    Contact latLong = info.getValue(Contact.class);
                    contact.add(latLong);
                }
                int i=0;


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        sosButton.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    final Handler handler = new Handler();
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        if (v == sosButton) {
            if (isSosActivated) {
                sosButton.setBackground(getResources().getDrawable(R.drawable.circle));
                sosButton.setTextColor(getResources().getColor(R.color.black));
                sosStatusTextView.setText("SOS Deactivated");
                handler.removeCallbacksAndMessages(null);
                NotificationManager notiman = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                notiman.cancel(0);

                isSosActivated = false;
            } else {
                sosButton.setBackground(getResources().getDrawable(R.drawable.circle_enabled));
                sosButton.setTextColor(getResources().getColor(R.color.white));
                sosStatusTextView.setText("SOS Activated");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
                        if (checkLocationPermission())

                            mFusedLocationClient.getLastLocation()
                                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            // Got last known location. In some rare situations this can be null.
                                            if (location != null) {
                                                Log.e("biswa", String.valueOf(location));
                                                databaseReference.child("tracking").child(id).child("" + i).setValue(new LatLong(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                                                i++;
                                                // ...
                                            }
                                        }
                                    });
                        handler.postDelayed(this, 30000);
                    }
                }, 1500);
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "+919087860604"));
                intent.putExtra("sms_body", "https://narayanasuri08.000webhostapp.com?id=" + id);
                startActivity(intent);*/
                try {
                    for(Contact value : contact){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(value.getNumber(), null, "https://narayanasuri08.000webhostapp.com/map?id=" + id, null, null);
                        Toast.makeText(getApplicationContext(), "Message Sent",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {


                    ex.printStackTrace();
                }
                createNotification();

                isSosActivated = true;
            }
        }

    }



    public void createNotification() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        TaskStackBuilder tsb = TaskStackBuilder.create(getContext());
        tsb.addParentStack(MainActivity.class);
        tsb.addNextIntent(intent);

        PendingIntent pendingIntent = tsb.getPendingIntent(111, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getContext());
        nBuilder.setContentTitle("SOS");
        nBuilder.setContentText("SOS Running");
        nBuilder.setSmallIcon(R.drawable.notif_ic);
        nBuilder.setContentIntent(pendingIntent);
        nBuilder.setOngoing(true);
        Notification notification = nBuilder.build();
        NotificationManager notiman = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notiman.notify(0, notification);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
