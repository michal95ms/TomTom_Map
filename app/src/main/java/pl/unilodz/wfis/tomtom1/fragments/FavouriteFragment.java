package pl.unilodz.wfis.tomtom1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import pl.unilodz.wfis.tomtom1.MainActivity;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.adapters.FavouriteListAdapter;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.utils.FavouriteLocationsManager;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    private static final String TAG = FavouriteFragment.class.getSimpleName();
    private ListView favouriteListView;
    private FavouriteLocationsManager favouriteLocationsManager;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteLocationsManager = new FavouriteLocationsManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        favouriteListView = view.findViewById(R.id.favourite_locations_list);
        FavouriteListAdapter adapter = new FavouriteListAdapter(getContext(), favouriteLocationsManager.getFavouriteLocations());
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
                fragment.getLocations().addElement(Arrays.asList(location));
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        Button showAll = view.findViewById(R.id.favourite_show_all_button);
        showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List locations = adapter.getFavourites();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MainActivity activity = (MainActivity) getActivity();
                MyMapFragment fragment = activity.getMyMapFragment();
                fragment.getLocations().addElement(locations);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

}