package pl.unilodz.wfis.tomtom1.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.preference.PreferenceManager;
import pl.unilodz.wfis.tomtom1.CommonsConstants;
import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.utils.ObjectSerializer;
import timber.log.Timber;

public class FavouriteListAdapter extends BaseAdapter {

    private final Context context;
    private final List<FavouriteLocation> favourites;

    public FavouriteListAdapter(Context context, List<FavouriteLocation> favourites) {
        this.context = context;
        this.favourites = favourites;
    }

    @Override
    public int getCount() {
        return favourites.size();
    }

    @Override
    public FavouriteLocation getItem(int i) {
        if(i < 0 || i > (favourites.size()-1)) {
            return null;
        }
        return favourites.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(i < 0 || i > (favourites.size()-1)) {
            return 0;
        };
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = View.inflate(context, R.layout.favourite_item, null);
        TextView favouriteItemText = view.findViewById(R.id.favourite_item_text);
        FavouriteLocation favouriteLocation = favourites.get(i);
        String favouriteText = favouriteLocation.getName();
        favouriteItemText.setText(favouriteText);
        Button removeItem = view.findViewById(R.id.favourite_item_remove_button);
        removeItem.setOnClickListener(itemView -> {
            favourites.remove(i);
            try {
                String encoded = ObjectSerializer.toString(favouriteLocation);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                Set<String> encodedLocations = sharedPref.getStringSet(CommonsConstants.FAVOURITE_LOCATIONS_KEY, new HashSet<>());
                encodedLocations.remove(encoded);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(CommonsConstants.FAVOURITE_LOCATIONS_KEY);
                editor.apply();
                editor.putStringSet(CommonsConstants.FAVOURITE_LOCATIONS_KEY, encodedLocations);
                boolean success = editor.commit();
                if(!success) {
                    Timber.w("Unsuccessful attempt to save Favourite Locations in Shared Preferences: %s", CommonsConstants.FAVOURITE_LOCATIONS_KEY);
                }
            } catch (IOException e) {
                Timber.e(e);
            }
            notifyDataSetChanged();
        });
//        convertView.setOnClickListener(view1 -> Toast.makeText(context, "Click!", Toast.LENGTH_SHORT));
        return view;
    }


}
