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
import java.util.Map;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import pl.unilodz.wfis.tomtom1.BuildConfig;
import pl.unilodz.wfis.tomtom1.CommonsConstants;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.utils.FavouriteLocationsManager;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MyMapFragment extends MapFragment implements OnMapReadyCallback, TomtomMapCallback.OnMapLongClickListener {

    private TomtomMap tomtomMap;
    private boolean init = true;
    private boolean initLocation = true;
    private final Stack<FavouriteLocation> favouriteLocation = new Stack<>();
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
        if(getArguments().containsKey(CommonsConstants.FAVOURITE_LOCATION_BUNDLE_ATTRIBUTE)) {
            favouriteLocation.push((FavouriteLocation) getArguments().get(CommonsConstants.FAVOURITE_LOCATION_BUNDLE_ATTRIBUTE));
            getArguments().remove(CommonsConstants.FAVOURITE_LOCATION_BUNDLE_ATTRIBUTE);
        }
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
                Toast.makeText(getContext(),"marker",Toast.LENGTH_LONG).show();
            }
        });
        /*this.tomtomMap.addLocationUpdateListener(location -> {
           if(initLocation) {
               initLocation = false;
               if(favouriteLocation.empty()) {
                   tomtomMap.centerOnMyLocationWithNorthUp();
               } else {
                   prepareFavouriteLocationMarker(tomtomMap, favouriteLocation.pop());
               }
           }
        });*/

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

    private void prepareFavouriteLocationMarker(TomtomMap tomtomMap, FavouriteLocation location) {
        tomtomMap.getMarkers().clear();
        MarkerBuilder markerBuilder = new MarkerBuilder(location.getLatLng())
                .icon(Icon.Factory.fromResources(getContext(), R.drawable.ic_favourites))
                .markerBalloon(new SimpleMarkerBalloon(location.getName()))
                .iconAnchor(MarkerAnchor.Bottom);
        tomtomMap.addMarker(markerBuilder);
        tomtomMap.centerOn(CameraPosition.builder()
                .focusPosition(location.getLatLng())
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
                        Toast.makeText(getContext(), "Nie udalo sie zapisac lokalizacji", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.favourite_location_popup_save_cancel, (dialog, id) -> dialog.cancel());
        return builder.show();
    }


}