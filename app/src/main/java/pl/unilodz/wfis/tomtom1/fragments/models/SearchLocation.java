package pl.unilodz.wfis.tomtom1.fragments.models;

import com.tomtom.online.sdk.common.location.LatLng;

import java.io.Serializable;
import java.util.Objects;

public class SearchLocation extends LatLng implements Serializable, INameLocation {

    private static final long serialVersionUID = 8730416028381281070L;

    private String name;
    private String address;

    public SearchLocation(String name, String address, double lat, double lon) {
        super(lat, lon);
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchLocation)) return false;
        if (!super.equals(o)) return false;
        SearchLocation that = (SearchLocation) o;
        return name.equals(that.name) &&
                address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, address);
    }
}
