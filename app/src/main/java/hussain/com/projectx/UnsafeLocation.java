package hussain.com.projectx;

/**
 * Created by Naray on 12-08-2017.
 */

public class UnsafeLocation {

    private String latitude, longitude, radius;

    public UnsafeLocation() {

    }

    public UnsafeLocation(String latitude, String longitude, String radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
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

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }
}
