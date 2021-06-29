package pl.unilodz.wfis.tomtom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.Icon;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.Route;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.routing.RoutingApi;
import com.tomtom.online.sdk.search.SearchApi;

import pl.unilodz.wfis.tomtom1.fragments.FavouriteFragment;
import pl.unilodz.wfis.tomtom1.fragments.MyMapFragment;
import pl.unilodz.wfis.tomtom1.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity  {

    MyMapFragment myMapFragment;
    FavouriteFragment favouriteFragment;
    SearchFragment searchFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMyMapFragment();
        initUIViews();
        setupUIViewListeners();




        BottomNavigationView bottomNavigationView= findViewById(R.id.top_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment,
                    new MyMapFragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.ic_map:
                            selectedFragment = myMapFragment;
                            Toast.makeText(getBaseContext(), "Mapa",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case R.id.ic_favourite:
                            selectedFragment = favouriteFragment;
                            Toast.makeText(getBaseContext(), "Favourite",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case R.id.ic_search:
                            selectedFragment = searchFragment;
                            Toast.makeText(getBaseContext(), "Search",
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.map_fragment,
                            selectedFragment).commit();
                    return true;
                }
            };


    private void initMyMapFragment() {
        myMapFragment = MyMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment, myMapFragment)
                .commit();
    }

    private void initUIViews() {
        favouriteFragment = new FavouriteFragment();
        searchFragment = new SearchFragment();
    }

    private void setupUIViewListeners() {
    }


}
