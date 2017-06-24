package in.awarespot.awarespot.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import in.awarespot.awarespot.R;
import io.paperdb.Paper;


/**
 * Created by saiso on 03-02-2017.
 */
public class CitiesAdapter extends ArrayAdapter<String> {

    public int x=1,itemIndex=0;
    private static final String TAG = "CityAdapter";

    private final Activity activity;
    List<String> citylist = new ArrayList<String>();
    public ViewHolder holder;
    public CitiesAdapter(Activity activity,
                         List<String> citylist) {
        super(activity, R.layout.item_city, citylist);
        this.activity = activity;
        this.citylist = citylist;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.item_city, null, true);
            holder = new ViewHolder();

            holder.nameTextView = (TextView) view.findViewById(R.id.addressTextView);
            holder.editImageView = (ImageView) view.findViewById(R.id.editButton);
            Paper.init(getContext());

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.nameTextView.setText(citylist.get(position));

        return view;
    }

    static class ViewHolder {
        TextView nameTextView;
        ImageView editImageView;
    }


}
