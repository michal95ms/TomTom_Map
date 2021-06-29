package pl.unilodz.wfis.tomtom1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.adapters.SearchAdapter;


public class SearchFragment extends Fragment {

    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    //private String mParam1;
    //private String mParam2;

    private ListView listView;
    private SearchAdapter searchAdapter;
    private String title[] = {"Title1", "Title2", "Title3", "Title4", "Title5"};
    private String address[] = {"Address1", "Address2", "Address3", "Address4", "Address5"};
    private SearchView searchView;
    private Button showAllB;
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
        view=inflater.inflate(R.layout.fragment_search,container,false);



        searchView= view.findViewById(R.id.searchViewSF);
        listView= view.findViewById(R.id.search_list);
        showAllB= view.findViewById(R.id.showAll);

        searchAdapter = new SearchAdapter(getContext(), title, address);
        listView.setAdapter(searchAdapter);

        searchView.setQueryHint("what are you looking for");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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

            }
        });

        showAllB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}