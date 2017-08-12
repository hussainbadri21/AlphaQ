package hussain.com.projectx;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hussain on 12-Aug-17.
 */

public class LatLong {
    String latitude,longitude;
    public LatLong(){

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public LatLong(String latitude, String longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }
}
