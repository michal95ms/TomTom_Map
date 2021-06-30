package pl.unilodz.wfis.tomtom1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import pl.unilodz.wfis.tomtom1.CommonsConstants;
import pl.unilodz.wfis.tomtom1.MainActivity;
import pl.unilodz.wfis.tomtom1.adapters.FavouriteListAdapter;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.MarkerAnchor;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.TomtomMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.utils.ObjectSerializer;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    private static final String TAG = FavouriteFragment.class.getSimpleName();
    private ListView favouriteListView;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        favouriteListView = view.findViewById(R.id.favourite_locations_list);
        FavouriteListAdapter adapter = new FavouriteListAdapter(getContext(), getFavouriteLocationFromStorage());
        favouriteListView.setAdapter(adapter);
        favouriteListView.setClickable(true);
        favouriteListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View layout, int position, long arg3) {
                FavouriteListAdapter adapter = (FavouriteListAdapter) listView.getAdapter();
                FavouriteLocation location = adapter.getItem(position);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MainActivity activity = (MainActivity) getActivity();
                MyMapFragment fragment = activity.getMyMapFragment();
                prepareFavouriteLocationMarker(fragment.getTomtomMap(), location.getLatLng());
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private List<FavouriteLocation> getFavouriteLocationFromStorage() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> encodedLocations = sharedPref.getStringSet(CommonsConstants.FAVOURITE_LOCATIONS_KEY, new HashSet<>());
        Timber.d("Found encoded locations in SharedPreferences: %s", encodedLocations.size());
        List<FavouriteLocation> locations = new ArrayList<>(encodedLocations.size());
        for(String encoded : encodedLocations) {
            try {
                locations.add(ObjectSerializer.fromString(encoded));
            } catch (IOException | ClassNotFoundException e) {
                Timber.e(e, e.getMessage());
            }
        }
        return locations;
    }

    private void prepareFavouriteLocationMarker(TomtomMap tomtomMap, LatLng latLng) {
        tomtomMap.getMarkers().clear();
        MarkerBuilder markerBuilder = new MarkerBuilder(latLng)
                .icon(Icon.Factory.fromResources(getContext(), R.drawable.ic_favourites))
                .iconAnchor(MarkerAnchor.Bottom);
        tomtomMap.addMarker(markerBuilder);
        tomtomMap.addMarker(markerBuilder);
    }
}