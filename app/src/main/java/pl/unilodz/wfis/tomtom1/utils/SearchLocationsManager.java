package pl.unilodz.wfis.tomtom1.utils;

import android.content.Context;

import androidx.annotation.Nullable;

import com.tomtom.online.sdk.common.util.Contextable;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.fragments.models.SearchLocation;
import timber.log.Timber;
/*
public class SearchLocationsManager implements Contextable {

    private Context context;

    public SearchLocationsManager(Context context) {
        this.context = context;
    }
    /*
    public List<SearchLocation> getSearchLocation(){
        Set<SearchLocation> locationSet= deserializeSet(e)
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    private Set<SearchLocation> deserializeSet(Set<String> encoded) {
        HashSet<SearchLocation> decoded = new HashSet<>(encoded.size());
        for(String encode : encoded) {
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
        for(FavouriteLocation location : locations) {
            try {
                encoded.add(ObjectSerializer.toString(location));
            } catch (IOException e) {
                Timber.e(e);
            }
        }
        return encoded;
    }
}
*/