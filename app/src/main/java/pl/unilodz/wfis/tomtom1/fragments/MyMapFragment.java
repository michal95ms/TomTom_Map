package pl.unilodz.wfis.tomtom1.fragments;

import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.location.LocationUpdateListener;
import com.tomtom.online.sdk.map.ApiKeyType;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MapConstants;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.MapProperties;
import com.tomtom.online.sdk.map.Marker;
import com.tomtom.online.sdk.map.MarkerAnchor;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import pl.unilodz.wfis.tomtom1.BuildConfig;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.fragments.models.INameLocation;
import pl.unilodz.wfis.tomtom1.utils.FavouriteLocationsManager;

public class MyMapFragment extends MapFragment implements OnMapReadyCallback, TomtomMapCallback.OnMapLongClickListener {

    private TomtomMap tomtomMap;
    private boolean init = true;
    private boolean initLocation = true;
    private final Stack<List<LatLng>> locations = new Stack<>();
    private FavouriteLocationsManager favouriteLocationsManager;

    public MyMapFragment() {
        super();
        initTomTomServices();
    }

    public static MyMapFragment newInstance() {
        return new MyMapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteLocationsManager = new FavouriteLocationsManager(getContext());
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
        this.tomtomMap.addLocationUpdateListener(new LocationUpdateListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (initLocation) {
                    initLocation = false;
                    if (locations.empty()) {
                        tomtomMap.centerOnMyLocationWithNorthUp();
                    } else {
                        for (LatLng latLng : locations.pop()) {
                            prepareLocationMarker(tomtomMap, latLng, ((INameLocation) latLng).getName());
                        }
                    }
                }
            }
        });
        tomtomMap.addOnMarkerClickListener(new TomtomMapCallback.OnMarkerClickListener() {
            @Override
            public void onMarkerClick(@NonNull @NotNull Marker marker) {
            }
        });

        tomtomMap.addOnMarkerClickListener(new TomtomMapCallback.OnMarkerClickListener() {
            @Override
            public void onMarkerClick(@NonNull @NotNull Marker marker) {
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
        createFavouriteLocationPopup(latLng);
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

    private void prepareLocationMarker(TomtomMap tomtomMap, LatLng location, String name) {
        tomtomMap.getMarkers().clear();
        MarkerBuilder markerBuilder = new MarkerBuilder(location)
                .icon(Icon.Factory.fromResources(getContext(), R.drawable.ic_favourites))
                .markerBalloon(new SimpleMarkerBalloon(name))
                .iconAnchor(MarkerAnchor.Bottom);
        tomtomMap.addMarker(markerBuilder);
        tomtomMap.centerOn(CameraPosition.builder()
                .focusPosition(location)
                .zoom(MapConstants.DEFAULT_ZOOM_LEVEL)
                .build());
    }

    private Dialog createFavouriteLocationPopup(LatLng latLng) {
        EditText input = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(input)
                .setTitle(R.string.favourite_location_popup_headline)
                .setMessage(R.string.favourite_location_popup_hint)
                .setPositiveButton(R.string.favourite_location_popup_save_button, (dialog, id) -> {
                    FavouriteLocation favouriteLocation = new FavouriteLocation(input.getText().toString(), latLng);
                    if (!favouriteLocationsManager.save(favouriteLocation)) {
                        Toast.makeText(getContext(), R.string.mymapfragment_save_error, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.favourite_location_popup_save_cancel, (dialog, id) -> dialog.cancel());
        return builder.show();
    }

    public Stack<List<LatLng>> getLocations() {
        return locations;
    }
}