package hussain.com.projectx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
        import android.webkit.WebChromeClient;
        import android.webkit.WebView;
        import android.widget.Button;
        import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
        import android.annotation.SuppressLint;
        import android.app.Activity;
        import android.app.AlertDialog;

import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LocationPickerActivity extends Activity {

    private WebView locationPickerView;
    private EditText searchText;
    private Button searchButton;

    private float latitude = 0f;
    private float longitude = 0f;
    Double lat;
    Double lon;
    private Integer zoom = 5;
    private Integer radius=10;
    private String locationName;
    @BindView(R.id.rad)
    TextView rad;
    SharedPreferences sharedPreferences;
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        lon =Double.parseDouble(sharedPreferences.getString("lon",""));
        lat = Double.parseDouble(sharedPreferences.getString("lat",""));
        Log.e("data",String.valueOf(lat)+"  "+String.valueOf(lon));
        if (savedInstanceState!=null) {

            zoom = savedInstanceState.getInt("zoom");
            locationName = savedInstanceState.getString("locationName");


        }

        // LOCATION PICKER WEBVIEW SETUP
        locationPickerView = (WebView) findViewById(R.id.locationPickerView);
        locationPickerView.setScrollContainer(false);
        locationPickerView.getSettings().setDomStorageEnabled(true);
        locationPickerView.getSettings().setJavaScriptEnabled(true);
        locationPickerView.addJavascriptInterface(new LocationPickerJSInterface(), "AndroidFunction");
        rad.setText(radius.toString());
        locationPickerView.loadUrl("file:///android_asset/locationPickerPage/index.html");

        locationPickerView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {

                    locationPickerView.loadUrl("javascript:activityInitialize(" + lat+ "," +lon + "," + zoom + ")");
                }
            }
        });
        // ^^^

        // EVENT HANDLER FOR PERFORMING SEARCH IN WEBVIEW
        searchText = (EditText) findViewById(R.id.searchText);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                locationPickerView.loadUrl("javascript:if (typeof activityPerformSearch == 'function') {activityPerformSearch(\"" + searchText.getText().toString() + "\")}");
            }
        });
        // ^^^

        // EVENT HANDLER FOR ZOOM IN WEBVIEW
        Button zoomIncreaseButton = (Button) findViewById(R.id.zoomIncreaseButton);
        zoomIncreaseButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                radius+=20;
                rad.setText(radius.toString());
            }
        });

        Button zoomDecreaseButton = (Button) findViewById(R.id.zoomDecreaseButton);
        zoomDecreaseButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                radius-=20;
                rad.setText(radius.toString());
            }
        });
        // ^^^

        // EVENT HANDLER FOR SAMPLE QUERY BUTTON
        Button sampleQueryButton = (Button) findViewById(R.id.queryButton);
        sampleQueryButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                AlertDialog alertDialog = new AlertDialog.Builder(LocationPickerActivity.this).create();
                alertDialog.setTitle("Data");
                alertDialog.setMessage("lat=" + latitude + ", lng=" + longitude + ", zoom=" + zoom + "\nloc=" + locationName);
                alertDialog.show();
            }
        });
    }

    public class LocationPickerJSInterface {
        @JavascriptInterface
        public void getValues(String latitude, String longitude, String zoom, String locationName){
            LocationPickerActivity.this.latitude = Float.parseFloat(latitude);
            LocationPickerActivity.this.longitude = Float.parseFloat(longitude);
            LocationPickerActivity.this.zoom = Integer.parseInt(zoom);
            LocationPickerActivity.this.locationName = locationName;
        }

        // to ease debugging
        public void showToast(String toast){
            Toast.makeText(LocationPickerActivity.this, toast, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat("latitude", latitude);
        outState.putFloat("longitude", longitude);
        outState.putInt("zoom", zoom);
        outState.putString("locationName", locationName);
    }

}