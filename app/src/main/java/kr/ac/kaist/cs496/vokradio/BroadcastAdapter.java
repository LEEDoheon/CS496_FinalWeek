package kr.ac.kaist.cs496.vokradio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by q on 2017-07-31.
 */

public class BroadcastAdapter extends BaseAdapter{
    ArrayList<BroadcastItem> items = new ArrayList<BroadcastItem>();

    @Override
    public int getCount(){
        return items.size();
    }

    public void addItem(BroadcastItem item) {
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.broadcast_list, parent, false);
        }

        TextView titleTextView = (TextView) convertView.findViewById(R.id.titleText);
        TextView ANNTextView = (TextView) convertView.findViewById(R.id.annText);
        ImageView backgroundImage = (ImageView) convertView.findViewById(R.id.backgroundImage);

        final BroadcastItem broadcastItem = items.get(position);

        titleTextView.setText(broadcastItem.getTitle());
        ANNTextView.setText("ANN:" +broadcastItem.getId());

        return convertView;

    }

}
