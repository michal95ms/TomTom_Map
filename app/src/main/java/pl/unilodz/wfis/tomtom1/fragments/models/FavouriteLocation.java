package pl.unilodz.wfis.tomtom1.fragments.models;

import com.tomtom.online.sdk.common.location.LatLng;

import java.io.Serializable;
import java.util.Objects;

public class FavouriteLocation extends LatLng implements Serializable, INameLocation {

    private static final long serialVersionUID = -686314766619527677L;

    private String name;

    public FavouriteLocation(String name, double lat, double lon) {
        super(lat, lon);
        this.name = name;
    }

    public FavouriteLocation(String name, LatLng latLng) {
        super(latLng.getLatitude(), latLng.getLongitude());
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavouriteLocation)) return false;
        if (!super.equals(o)) return false;
        FavouriteLocation that = (FavouriteLocation) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
