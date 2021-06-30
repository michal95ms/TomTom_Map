package pl.unilodz.wfis.tomtom1.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.FavouriteLocation;
import pl.unilodz.wfis.tomtom1.utils.FavouriteLocationsManager;

public class FavouriteListAdapter extends BaseAdapter {

    private final Context context;
    private final List<FavouriteLocation> favourites;
    private final FavouriteLocationsManager favouriteLocationsManager;

    public FavouriteListAdapter(Context context, List<FavouriteLocation> favourites) {
        this.context = context;
        this.favourites = favourites;
        this.favouriteLocationsManager = new FavouriteLocationsManager(context);
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
            favouriteLocationsManager.delete(favouriteLocation);
            notifyDataSetChanged();
        });
//        convertView.setOnClickListener(view1 -> Toast.makeText(context, "Click!", Toast.LENGTH_SHORT));
        return view;
    }


}
