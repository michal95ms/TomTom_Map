package pl.unilodz.wfis.tomtom1.fragments.models;

import com.tomtom.online.sdk.common.location.LatLng;

import java.io.Serializable;
import java.util.Objects;

public class FavouriteLocation implements Serializable {

    private static final long serialVersionUID = -686314766619527677L;

    private String name;
    private LatLng latLng;

    public FavouriteLocation(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavouriteLocation)) return false;
        FavouriteLocation location = (FavouriteLocation) o;
        return Objects.equals(name, location.name) &&
                Objects.equals(latLng, location.latLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, latLng);
    }
}
