package pl.unilodz.wfis.tomtom1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import pl.unilodz.wfis.tomtom1.BuildConfig;
import pl.unilodz.wfis.tomtom1.CommonsConstants;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.utils.ObjectSerializer;
import timber.log.Timber;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.ApiKeyType;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.MapProperties;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerAnchor;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.search.SearchApi;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyMapFragment extends MapFragment implements OnMapReadyCallback, TomtomMapCallback.OnMapLongClickListener {

    private TomtomMap tomtomMap;
    private boolean init = true;
    private boolean initLocation = true;

    public MyMapFragment() {
        super();
        initTomTomServices();
    }

    public static MyMapFragment newInstance() {
        return new MyMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getAsyncMap(this);
        initLocation = true;


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        tomtomMap.addOnMarkerClickListener(new TomtomMapCallback.OnMarkerClickListener() {
            @Override
            public void onMarkerClick(@NonNull @NotNull Marker marker) {
                Toast.makeText(getActivity(), "This is my Toast message!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        tomtomMap.getMarkers().clear();
        MarkerBuilder markerBuilder = new MarkerBuilder(latLng)
                .icon(Icon.Factory.fromResources(getContext(), R.drawable.ic_favourites))
                .iconAnchor(MarkerAnchor.Bottom);
        tomtomMap.addMarker(markerBuilder);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> encodedLocations = sharedPref.getStringSet(CommonsConstants.FAVOURITE_LOCATIONS_KEY, new HashSet<>());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(CommonsConstants.FAVOURITE_LOCATIONS_KEY);
        editor.apply();
        try {
            encodedLocations.add(ObjectSerializer.toString(new FavouriteLocation(latLng.toString(), latLng)));
            editor.putStringSet(CommonsConstants.FAVOURITE_LOCATIONS_KEY, encodedLocations);
            boolean success = editor.commit();
            if(!success) {
                Timber.w("Unsuccessful attempt to save Favourite Locations in Shared Preferences: %s", CommonsConstants.FAVOURITE_LOCATIONS_KEY);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        tomtomMap.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initTomTomServices() {
        Map<ApiKeyType, String> mapKeys = new HashMap<>();
        mapKeys.put(ApiKeyType.MAPS_API_KEY, BuildConfig.MAPS_API_KEY);
        MapProperties mapProperties = new MapProperties.Builder()
                .keys(mapKeys)
                .build();
        setArguments(MapFragment.newInstance(mapProperties).getArguments());
    }

    public TomtomMap getTomtomMap() {
        return tomtomMap;
    }


}