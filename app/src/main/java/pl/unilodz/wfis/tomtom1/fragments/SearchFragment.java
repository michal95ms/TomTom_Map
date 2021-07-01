package pl.unilodz.wfis.tomtom1.fragments;


import pl.unilodz.wfis.tomtom1.adapters.LocationProvider;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import java.util.List;

import pl.unilodz.wfis.tomtom1.BuildConfig;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.adapters.SearchListAdapter;
import pl.unilodz.wfis.tomtom1.fragments.models.SearchLocation;


public class SearchFragment extends Fragment {

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private String mParam1;
    //private String mParam2;

    private ListView listView;
    private SearchListAdapter searchListAdapter;
    private SearchView searchView;
    private Button showAllB;
    private SearchApi searchApi;
    private LocationProvider locationProvider;
    View view;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
      /*  Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);

        searchApi = OnlineSearchApi.create(getContext(), BuildConfig.SEARCH_API_KEY);
        locationProvider = new LocationProvider(getContext());
        locationProvider.activateLocationSource();


        searchView = view.findViewById(R.id.searchViewSF);
        listView = view.findViewById(R.id.search_list);
        showAllB = view.findViewById(R.id.showAll);

        searchView.setQueryHint("what are you looking for");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // FuzzySearchSpecification fuzzySearchSpecification = new FuzzySearchSpecification.Builder(query).build();
                FuzzySearchSpecification fuzzySearchSpecification = getSearchSpecificationForNonFuzzySearch(query, locationProvider.getLastKnownPosition());
                searchApi.search(fuzzySearchSpecification, new FuzzyOutcomeCallback() {
                    @Override
                    public void onSuccess(@NotNull FuzzyOutcome fuzzyOutcome) {
                        //String name[] = new String[fuzzyOutcome.getFuzzyDetailsList().size()];
                        //String address[] = new String[fuzzyOutcome.getFuzzyDetailsList().size()];
                        SearchLocation searchLocation;
                        List<SearchLocation> searchLocationList = new ArrayList<SearchLocation>();
                        int i = 0;
                        for (FuzzySearchDetails details : fuzzyOutcome.getFuzzyDetailsList()) {
                            if (details.getPoi().getName().isEmpty()) {
                                searchLocation = new SearchLocation(details.getAddress().getMunicipality(), details.getAddress().getStreetName() + " " + details.getAddress().getStreetNumber(), details.getPosition().getLatitude(), details.getPosition().getLongitude());
                                searchLocationList.add(searchLocation);
                                //name[i] = details.getAddress().getMunicipality();
                                //address[i] = details.getAddress().getStreetName() + " " + details.getAddress().getStreetNumber();
                            } else {
                                //name[i] = details.getPoi().getName();
                                //address[i] = details.getAddress().getMunicipality() + " " + details.getAddress().getStreetName() + " " + details.getAddress().getStreetNumber();
                                searchLocation = new SearchLocation(details.getPoi().getName(), details.getAddress().getMunicipality() + " " + details.getAddress().getStreetName() + " " + details.getAddress().getStreetNumber(), details.getPosition().getLatitude(), details.getPosition().getLongitude());
                                searchLocationList.add(searchLocation);
                            }
                            //address[i] = details.getAddress().getMunicipality()+" "+details.getAddress().getStreetName()+" "+details.getAddress().getStreetNumber();
                            i++;
                        }
                        //searchListAdapter = new SearchListAdapter(getContext(), name, address);
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


                Toast.makeText(getContext(), "lista" + searchListAdapter.getItem(position), Toast.LENGTH_LONG).show();

            }
        });

        showAllB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "przycisk", Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

    FuzzySearchSpecification getSearchSpecificationForNonFuzzySearch(String query, LatLng position) {
        FuzzyLocationDescriptor fuzzyLocationDescriptor = new FuzzyLocationDescriptor.Builder()
                .positionBias(new LatLngBias(position))
                .build();
        return
                //tag::doc_create_standard_search_query[]
                new FuzzySearchSpecification.Builder(query)
                        .locationDescriptor(fuzzyLocationDescriptor)
                        .build();
        //end::doc_create_standard_search_query[]
    }
}