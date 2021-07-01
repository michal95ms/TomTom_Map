package pl.unilodz.wfis.tomtom1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import pl.unilodz.wfis.tomtom1.R;
import pl.unilodz.wfis.tomtom1.fragments.models.SearchLocation;

public class SearchListAdapter extends BaseAdapter {

    Context context;
    //String name[];
    //String address[];

    private final List<SearchLocation> searchLocationList;

    public SearchListAdapter(Context context, List<SearchLocation> searchLocationList) {
        this.context = context;
        this.searchLocationList = searchLocationList;
    }
/*
    public SearchListAdapter(Context c, String n[], String a[]) {
        super(c, R.layout.row, R.id.name_search, n);
        this.context = c;
        this.name = n;
        this.address = a;
    }*/

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public SearchLocation getItem(int i) {
        if(i < 0 || i > (searchLocationList.size()-1)) {
            return null;
        }
        return searchLocationList.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(i < 0 || i > (searchLocationList.size()-1)) {
            return 0;
        };
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView tvName = row.findViewById(R.id.name_search);
        TextView tvAddress= row.findViewById(R.id.address_search);

        tvName.setText(searchLocationList.get(position).getName());
        tvAddress.setText(searchLocationList.get(position).getAddress());
        //tvName.setText(name[position]);
        //tvAddress.setText(address[position]);

        return row;
    }
}
