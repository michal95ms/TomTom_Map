package pl.unilodz.wfis.tomtom1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pl.unilodz.wfis.tomtom1.R;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.ApiKeyType;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.MapProperties;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.search.SearchApi;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMapFragment extends MapFragment implements OnMapReadyCallback, TomtomMapCallback.OnMapLongClickListener {

    private TomtomMap tomtomMap;
    private SearchApi searchApi;
    private RoutingApi routingApi;
    private Route route;
    private LatLng departurePosition;
    private LatLng destinationPosition;
    private LatLng wayPointPosition;
    private Icon departureIcon;
    private Icon destinationIcon;
    private boolean init = true;
    private boolean initLocation = true;

    public MyMapFragment() {
        super();
    }

    public static MyMapFragment newInstance() {
        return new MyMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(init) {
            initTomTomServices(getContext());
            getAsyncMap(this);
            init = false;
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

    }

    @Override
    public void onMapReady(@NonNull final TomtomMap tomtomMap) {
        this.tomtomMap = tomtomMap;
        this.tomtomMap.setMyLocationEnabled(true);
        this.tomtomMap.addOnMapLongClickListener(this);
        this.tomtomMap.getMarkerSettings().setMarkersClustering(true);
        this.tomtomMap.addLocationUpdateListener(location -> {
           if(initLocation) {
               initLocation = false;
               tomtomMap.centerOnMyLocationWithNorthUp();
           }
        });

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

    }


    private void initTomTomServices(Context context) {
        if(context == null) {
            throw new IllegalStateException("Context cannot be null during MyMapFragment initialization!");
        }
        Map<ApiKeyType, String> mapKeys = new HashMap<>();
        mapKeys.put(ApiKeyType.MAPS_API_KEY, context.getResources().getString(R.string.MAPS_API_KEY));
        MapProperties mapProperties = new MapProperties.Builder()
                .keys(mapKeys)
                .build();
        setArguments(MapFragment.newInstance(mapProperties).getArguments());
    }
    private void clearMap() {
        tomtomMap.clear();
        departurePosition = null;
        destinationPosition = null;
        route = null;
    }


}