package ae.ac.ud.ceit.cts.tabs.search;

/**
 * Created by atalla on 21/02/17.
 */


import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import ae.ac.ud.ceit.cts.R;
import ae.ac.ud.ceit.cts.tabs.feeds.FeedsComponent;

public class SearchListViewAdaptor extends ArrayAdapter<FeedsComponent> {

    private int[] colors = new int[] { Color.parseColor("#F0F0F0"), Color.parseColor("#FFFFFF") };
    public SearchListViewAdaptor(Context context, int textViewResourceId, List<FeedsComponent> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View curView = convertView;

        if (curView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.search_crime_list_component, null);
        }

        FeedsComponent cp = getItem(position);

        //sets the layouts for the item
        TextView title = (TextView) curView.findViewById (R.id.title);
        //  TextView subtitle = (TextView) curView.findViewById (R.id.subtitle);
        ImageView imageView = (ImageView) curView.findViewById(R.id.icon_list);
        //   TextView lastAccess = (TextView) curView.findViewById (R.id.lastaccess);
        //sets the title and the subtitle for the current component
        title.setText(cp.getTitle());
        //  subtitle.setText(cp.getSubtitle());
        // imageView.setImageResource(R.drawable.ic_alien_cyan);
        //   lastAccess.setText(System.currentTimeMillis() + "");

        int colorPos = position % colors.length;
        curView.setBackgroundColor(colors[colorPos]);
        return curView;

    }


}
