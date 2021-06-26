package pl.unilodz.wfis.tomtom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.ApiKeyType;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.MapProperties;
import com.tomtom.online.sdk.map.OnMapReadyCallback;
import com.tomtom.online.sdk.map.TomtomMap;
import com.tomtom.online.sdk.map.TomtomMapCallback;
import com.tomtom.online.sdk.search.OnlineSearchApi;
import com.tomtom.online.sdk.search.SearchApi;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import pl.unilodz.wfis.tomtom1.fragments.FavouriteFragment;
import pl.unilodz.wfis.tomtom1.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TomtomMapCallback.OnMapLongClickListener {

    private TomtomMap tomtomMap;
    MapFragment mapFragment;
    FavouriteFragment favouriteFragment;
    SearchFragment searchFragment;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapFragment = new MapFragment();
        favouriteFragment = new FavouriteFragment();
        searchFragment = new SearchFragment();
        relativeLayout = (RelativeLayout) findViewById(R.id.fragment_container);

        initTomTomServices();
        initUIViews();
        setupUIViewListeners();

/*
        BottomNavigationView bottomNavigationView= findViewById(R.id.top_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MapFragment()).commit();
        }*/

    }
/*
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.ic_map:
                            relativeLayout.setVisibility(View.INVISIBLE);
                            selectedFragment = mapFragment;
                            break;
                        case R.id.ic_favourite:
                            relativeLayout.setVisibility(View.VISIBLE);
                            selectedFragment = favouriteFragment;
                            break;
                        case R.id.ic_search:
                            relativeLayout.setVisibility(View.VISIBLE);
                            selectedFragment = searchFragment;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
*/

    @Override
    public void onMapReady(@NonNull final TomtomMap tomtomMap) {
        this.tomtomMap = tomtomMap;
        this.tomtomMap.setMyLocationEnabled(true);
        this.tomtomMap.addOnMapLongClickListener(this);
        this.tomtomMap.getMarkerSettings().setMarkersClustering(true);
    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
    }

    private void initTomTomServices() {
        Map<ApiKeyType, String> mapKeys = new HashMap<>();
        mapKeys.put(ApiKeyType.MAPS_API_KEY, "rAT1nOItZYvJEr6XE6TpXQCkre6x4oQA");

        MapProperties mapProperties = new MapProperties.Builder()
                .keys(mapKeys)
                .build();
        com.tomtom.online.sdk.map.MapFragment mapFragment = com.tomtom.online.sdk.map.MapFragment.newInstance(mapProperties);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mapFragment, mapFragment)
                .commit();
        mapFragment.getAsyncMap(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.tomtomMap.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initUIViews() {
    }

    private void setupUIViewListeners() {
    }
}
