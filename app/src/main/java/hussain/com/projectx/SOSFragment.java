package hussain.com.projectx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import at.markushi.ui.CircleButton;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SOSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SOSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SOSFragment extends Fragment implements View.OnClickListener {

    Button sosButton;
    TextView sosStatusTextView;
    Boolean isSosActivated = false;

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
        sosStatusTextView = (TextView) view.findViewById(R.id.sos_status_text_view);
        sosButton = (Button) view.findViewById(R.id.sos_button);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        if(v == sosButton){
            if(isSosActivated) {
                sosButton.setBackground(getResources().getDrawable(R.drawable.circle));
                sosButton.setTextColor(getResources().getColor(R.color.black));
                sosStatusTextView.setText("SOS Deactivated");

                NotificationManager notiman = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                notiman.cancel(0);

                isSosActivated = false;
            }
            else{
                sosButton.setBackground(getResources().getDrawable(R.drawable.circle_enabled));
                sosButton.setTextColor(getResources().getColor(R.color.white));
                sosStatusTextView.setText("SOS Activated");

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
