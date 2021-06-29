package pl.unilodz.wfis.tomtom1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import pl.unilodz.wfis.tomtom1.R;

public class SearchAdapter extends ArrayAdapter {

    Context context;
    String name[];
    String address[];

    public SearchAdapter(Context c, String n[], String a[]) {
        super(c, R.layout.row, R.id.name_search, n);
        this.context = c;
        this.name = n;
        this.address = a;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView tvName = row.findViewById(R.id.name_search);
        TextView tvAddress= row.findViewById(R.id.address_search);

        tvName.setText(name[position]);
        tvAddress.setText(address[position]);

        return row;
    }
}
