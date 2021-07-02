package pl.unilodz.wfis.tomtom1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.unilodz.wfis.tomtom1.adapters.LocationProvider;
import pl.unilodz.wfis.tomtom1.fragments.FavouriteFragment;
import pl.unilodz.wfis.tomtom1.fragments.MyMapFragment;
import pl.unilodz.wfis.tomtom1.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private MyMapFragment myMapFragment;
    private FavouriteFragment favouriteFragment;
    private SearchFragment searchFragment;
    private LocationProvider locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationProvider = new LocationProvider(getApplicationContext());
        locationProvider.activateLocationSource();

        initMyMapFragment();
        initUIViews();
        setupUIViewListeners();

        BottomNavigationView bottomNavigationView = findViewById(R.id.top_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
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
                            break;
                        case R.id.ic_favourite:
                            selectedFragment = favouriteFragment;
                            break;
                        case R.id.ic_search:
                            selectedFragment = searchFragment;
                            break;
                        case R.id.ic_share:
                            selectedFragment = myMapFragment;
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            String location = CommonsConstants.SHARE_BODY + " Latitude: " + locationProvider.getLastKnownPosition().getLatitude() +
                                    " Longitude: " + locationProvider.getLastKnownPosition().getLongitude();
                            String shareSubject = CommonsConstants.SHARE_SUBJECT;
                            String shareBody = location;
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(shareIntent, "Share my location"));
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };

    private void initMyMapFragment() {
        myMapFragment = MyMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, myMapFragment)
                .commit();
    }

    private void initUIViews() {
        favouriteFragment = new FavouriteFragment();
        searchFragment = new SearchFragment();
    }

    private void setupUIViewListeners() {
    }

    public MyMapFragment getMyMapFragment() {
        return myMapFragment;
    }

    public void setMyMapFragment(MyMapFragment myMapFragment) {
        this.myMapFragment = myMapFragment;
    }

    public FavouriteFragment getFavouriteFragment() {
        return favouriteFragment;
    }

    public void setFavouriteFragment(FavouriteFragment favouriteFragment) {
        this.favouriteFragment = favouriteFragment;
    }

    public SearchFragment getSearchFragment() {
        return searchFragment;
    }

    public void setSearchFragment(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }
}
