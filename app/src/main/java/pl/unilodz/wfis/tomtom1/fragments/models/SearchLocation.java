package pl.unilodz.wfis.tomtom1.fragments.models;

import com.tomtom.online.sdk.common.location.LatLng;

import java.io.Serializable;
import java.util.Objects;

public class SearchLocation implements Serializable {

    private static final long serialVersionUID = 8730416028381281070L;

    private String name;
    private String address;
    private double lat;
    private double lon;
    private LatLng latLng;

    public SearchLocation(String name, String address, double lat, double lon) {
        this.name = name;
        this.address = address;
        this.lat=lat;
        this.lon=lon;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        LatLng latLng;
        latLng=new LatLng(lat,lon);
        return latLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchLocation)) return false;
        SearchLocation location = (SearchLocation) o;

        return Objects.equals(name, location.name) &&
                Objects.equals(address, location.address) &&
                Objects.equals(latLng, location.latLng);
    }
}
