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

//(extends=inherit)ArrayAdapter<FeedsComponent>[Super-class] is part of Android support library
public class SearchListViewAdaptor extends ArrayAdapter<FeedsComponent> {

    //"#F0F0F0"= light gray, "#FFFFFF"= white
    //"parseColor" = Parse the color string, and return the corresponding color-int.
    private int[] colors = new int[] { Color.parseColor("#F0F0F0"), Color.parseColor("#FFFFFF") };

    //this is a (parameterized) constructor:
    //used to provide different values to the distinct objects.
    //Constructor name must be same as its class name
    //Constructor must have no explicit return type
    public SearchListViewAdaptor(Context context, int textViewResourceId, List<FeedsComponent> objects) {
        //super: is used to call the constructor, methods and properties of parent class.
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { //parameter "parent" is never used
        //Adapter uses the (convertView) as a way of recycling old View objects that are no longer being used.
        //In this way, the ListView can send the Adapter old, "recycled" view objects that are no longer being displayed
        //instead of instantiating an entirely new object each time the Adapter wants to display a new list item.
        //Thus, this parameter is used strictly to increase the performance of your Adapter
        View curView = convertView;                                                 //curView = current view

        if (curView == null) {
            //(LayoutInflater): Instantiates a layout XML file into its corresponding View objects.
            // It uses getSystemService(Class) to retrieve a standard LayoutInflater instance
            // that is already hooked up to the current context and correctly configured for the device you are running on.
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            curView = vi.inflate(R.layout.search_crime_list_component, null);
        }

        FeedsComponent cp = getItem(position);                                      //cp = current position

        //sets the layouts for the item
        TextView title = (TextView) curView.findViewById (R.id.title);              //displays the title on the top left hand side(beside logo)

        //TextView subtitle = (TextView) curView.findViewById (R.id.subtitle);

        ImageView imageView = (ImageView) curView.findViewById(R.id.icon_list);     //displays the logo on the far top left hand side

        //TextView lastAccess = (TextView) curView.findViewById (R.id.lastaccess);

        title.setText(cp.getTitle());                                   //sets the title for the current component

        //subtitle.setText(cp.getSubtitle());
        //imageView.setImageResource(R.drawable.ic_alien_cyan);
        //lastAccess.setText(System.currentTimeMillis() + "");

        int colorPos = position % colors.length;        //TODO: What is the (%) symbol for?
        curView.setBackgroundColor(colors[colorPos]);   //sets white and gray as background color in current view
        return curView;

    }


}
