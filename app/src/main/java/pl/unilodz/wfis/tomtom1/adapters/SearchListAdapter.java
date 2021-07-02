package pl.unilodz.wfis.tomtom1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.SearchLocation;

public class SearchListAdapter extends BaseAdapter {

    Context context;

    private final List<SearchLocation> searchLocationList;

    public SearchListAdapter(Context context, List<SearchLocation> searchLocationList) {
        this.context = context;
        this.searchLocationList = searchLocationList;
    }

    @Override
    public int getCount() {
        return searchLocationList.size();
    }

    @Override
    public SearchLocation getItem(int i) {
        if (i < 0 || i > (searchLocationList.size() - 1)) {
            return null;
        }
        return searchLocationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (i < 0 || i > (searchLocationList.size() - 1)) {
            return 0;
        }
        return i;
    }

    public List<SearchLocation> getSearchLocationList() {
        return searchLocationList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView tvName = row.findViewById(R.id.name_search);
        TextView tvAddress = row.findViewById(R.id.address_search);

        tvName.setText(searchLocationList.get(position).getName());
        tvAddress.setText(searchLocationList.get(position).getAddress());

        return row;
    }
}
