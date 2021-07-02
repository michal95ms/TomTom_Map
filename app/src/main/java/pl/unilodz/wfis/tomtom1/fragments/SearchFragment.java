package pl.unilodz.wfis.tomtom1.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.common.location.LatLngBias;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;
import com.tomtom.online.sdk.search.SearchException;
import com.tomtom.online.sdk.search.fuzzy.FuzzyLocationDescriptor;
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcome;
import com.tomtom.online.sdk.search.fuzzy.FuzzyOutcomeCallback;
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchDetails;
import com.tomtom.online.sdk.search.fuzzy.FuzzySearchSpecification;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import pl.unilodz.wfis.tomtom1.BuildConfig;
import pl.unilodz.wfis.tomtom1.CommonsConstants;
import pl.unilodz.wfis.tomtom1.MainActivity;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.adapters.LocationProvider;
import pl.unilodz.wfis.tomtom1.adapters.SearchListAdapter;
import pl.unilodz.wfis.tomtom1.fragments.models.SearchLocation;

public class SearchFragment extends Fragment {

    private ListView listView;
    private SearchListAdapter searchListAdapter;
    private SearchView searchView;
    private Button showAllB;
    private SearchApi searchApi;
    private LocationProvider locationProvider;
    View view;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        searchApi = OnlineSearchApi.create(getContext(), BuildConfig.SEARCH_API_KEY);
        locationProvider = new LocationProvider(getContext());
        locationProvider.activateLocationSource();
        searchListAdapter = new SearchListAdapter(getContext(), new ArrayList<>());

        searchView = view.findViewById(R.id.searchViewSF);
        listView = view.findViewById(R.id.search_list);
        showAllB = view.findViewById(R.id.showAll);

        searchView.setQueryHint(CommonsConstants.SEARCH_HINT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FuzzySearchSpecification fuzzySearchSpecification = getSearchSpecificationForNonFuzzySearch(query, locationProvider.getLastKnownPosition());
                searchApi.search(fuzzySearchSpecification, new FuzzyOutcomeCallback() {
                    @Override
                    public void onSuccess(@NotNull FuzzyOutcome fuzzyOutcome) {
                        SearchLocation searchLocation;
                        List<SearchLocation> searchLocationList = new ArrayList<SearchLocation>();
                        int i = 0;
                        for (FuzzySearchDetails details : fuzzyOutcome.getFuzzyDetailsList()) {
                            if (details.getPoi().getName().isEmpty()) {
                                searchLocation = new SearchLocation(details.getAddress().getMunicipality(), details.getAddress().getStreetName() + " " + details.getAddress().getStreetNumber(), details.getPosition().getLatitude(), details.getPosition().getLongitude());
                                searchLocationList.add(searchLocation);
                            } else {
                                searchLocation = new SearchLocation(details.getPoi().getName(), details.getAddress().getMunicipality() + " " + details.getAddress().getStreetName() + " " + details.getAddress().getStreetNumber(), details.getPosition().getLatitude(), details.getPosition().getLongitude());
                                searchLocationList.add(searchLocation);
                            }
                            i++;
                        }
                        searchListAdapter = new SearchListAdapter(getContext(), searchLocationList);
                        listView.setAdapter(searchListAdapter);
                    }

                    @Override
                    public void onError(@NotNull SearchException e) {
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchListAdapter = (SearchListAdapter) listView.getAdapter();
                SearchLocation location = searchListAdapter.getItem(position);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                MainActivity activity = (MainActivity) getActivity();
                MyMapFragment fragment = activity.getMyMapFragment();
                fragment.getLocations().addElement(Arrays.asList(location));
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        showAllB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List locations = searchListAdapter.getSearchLocationList();
                if (!searchListAdapter.isEmpty()) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    MainActivity activity = (MainActivity) getActivity();
                    MyMapFragment fragment = activity.getMyMapFragment();
                    fragment.getLocations().addElement(locations);
                    transaction.replace(R.id.fragment_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), R.string.search_location_nothing_to_show, Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    FuzzySearchSpecification getSearchSpecificationForNonFuzzySearch(String query, LatLng position) {
        FuzzyLocationDescriptor fuzzyLocationDescriptor = new FuzzyLocationDescriptor.Builder()
                .positionBias(new LatLngBias(position))
                .build();
        return
                new FuzzySearchSpecification.Builder(query)
                        .locationDescriptor(fuzzyLocationDescriptor)
                        .build();
    }
}