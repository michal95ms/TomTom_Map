package pl.unilodz.wfis.tomtom1.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tomtom.online.sdk.common.util.Contextable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import pl.unilodz.wfis.tomtom1.CommonsConstants;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import timber.log.Timber;

public class FavouriteLocationsManager implements Contextable {

    private Context context;
    SharedPreferences sharedPreferences;

    public FavouriteLocationsManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public List<FavouriteLocation> getFavouriteLocations() {
        Set<String> encodedLocations = sharedPreferences.getStringSet(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY, new HashSet<>());
        Set<FavouriteLocation> locationSet = deserializeSet(encodedLocations);
        return new ArrayList<>(locationSet);
    }

    public boolean save(FavouriteLocation location) {
        Set<String> encodedLocations = sharedPreferences.getStringSet(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY, new HashSet<>());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY);
        editor.apply();
        try {
            encodedLocations.add(ObjectSerializer.toString(new FavouriteLocation(location.getName(), location)));
            editor.putStringSet(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY, encodedLocations);
            boolean success = editor.commit();
            if (!success) {
                Timber.w("Unsuccessful attempt to save Favourite Locations in Shared Preferences: %s", CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY);
            }
            return success;
        } catch (IOException e) {
            Timber.e(e);
        }
        return false;
    }

    public boolean delete(FavouriteLocation location) {
        Set<String> encodedLocations = sharedPreferences.getStringSet(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY, new HashSet<>());
        Set<FavouriteLocation> locationSet = deserializeSet(encodedLocations);
        if (locationSet.contains(location)) {
            locationSet.remove(location);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY);
            editor.apply();
            encodedLocations = serializeSet(locationSet);
            editor.putStringSet(CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY, encodedLocations);
            boolean success = editor.commit();
            if (!success) {
                Timber.w("Unsuccessful attempt to save Favourite Locations in Shared Preferences: %s", CommonsConstants.FAVOURITE_LOCATIONS_SHARED_PREFERENCES_KEY);
            }
            return success;
        }
        return false;
    }

    private Set<FavouriteLocation> deserializeSet(Set<String> encoded) {
        HashSet<FavouriteLocation> decoded = new HashSet<>(encoded.size());
        for (String encode : encoded) {
            try {
                decoded.add(ObjectSerializer.fromString(encode));
            } catch (IOException | ClassNotFoundException e) {
                Timber.e(e);
            }
        }
        return decoded;
    }

    private Set<String> serializeSet(Set<FavouriteLocation> locations) {
        HashSet<String> encoded = new HashSet<>(locations.size());
        for (FavouriteLocation location : locations) {
            try {
                encoded.add(ObjectSerializer.toString(location));
            } catch (IOException e) {
                Timber.e(e);
            }
        }
        return encoded;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }
}
