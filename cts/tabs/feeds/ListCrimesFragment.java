package ae.ac.ud.ceit.cts.tabs.feeds;

/**
 * Created by atalla on 21/02/17.
 */


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ae.ac.ud.ceit.cts.R;

import ae.ac.ud.ceit.cts.SingleCrimeView;
import ae.ac.ud.ceit.cts.model.*;
/**
 * Created by iFocus on 27-10-2015.
 */
public class ListCrimesFragment extends Fragment {

    private static final String TAG = "ListCrimesFragment";
    private boolean list_visibile = false;
    private ListView listview;

    private FeedsListViewAdapter listViewAdapter;
    private List<Crime> crimes = new ArrayList<Crime>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feeds, container, false);
        // Inflate the layout for this fragment
        //creation of the components for the grid/list

        getListAllCrimes();
        //set of layouts for the views
        listview = (ListView) root.findViewById(R.id.feedslist);

        listViewAdapter = new FeedsListViewAdapter(root.getContext(), R.layout.crime_row, crimes);
        listview.setAdapter(listViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a,
                                    View v, int position, long id) {
                Crime crime = (Crime) a.getItemAtPosition(position);
                Intent intent = new Intent(v.getContext(), SingleCrimeView.class);
                intent.putExtra("com.example.cities.City", crime);
                startActivity(intent);
            }
        });

        //     menu = (ContextMenu) findViewById(R.menu.main);

        //inflate the layouts
     //   listview.inflate();

        //set of layouts for the list/grid


        return root;
    }
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ListCrimesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListCrimesFragment fragment = new ListCrimesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    /**
     * Method to create basic components for the example
     */



    void getListAllCrimes(){
        try{
            URI adapterPath = new URI("/adapters/JavaScriptSQl/getListAllCrimes");

            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);

            request.send(new WLResponseListener(){
                public void onSuccess(WLResponse response) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getResponseJSON();
                        Log.d(TAG, jsonObject +"");
                        JSONArray results = jsonObject.getJSONArray("resultSet");
                          for(int i=0; i < results.length(); i++){
                              // if(results.length() > 0) {

                               int reference = results.getJSONObject(i).getInt("reference"); //String.format("%5d", results.getJSONObject(0).getInt("reference"));
                               String date_time = results.getJSONObject(i).getString("created");
                               String created = results.getJSONObject(i).getString("date_time");
                               String evdinces= results.getJSONObject(i).getString("evidences");
                               String location= results.getJSONObject(i).getString("location");
                               String suspects= results.getJSONObject(i).getString("suspects");
                               String victims= results.getJSONObject(i).getString("victims");
                              CrimeBuilder builder = new CrimeBuilderImpl() ;//= ;//.setWheels(4).setColor("Red").build();
                              Crime crime = builder.setDate_time(date_time)
                                            .setReference(String.format("%05d", reference) )
                                            .setVictims(victims)
                                            .setSuspects(suspects)
                                            .setEvidences(evdinces)
                                            .setLocation(location)
                                            .setCreated(created)
                                            .build();
                              crimes.add(crime);
                            Log.d("Success->", crime +"");



                        }
                         if(results.length() == 0){
                            Log.d("Success->", "Wrong username or password");

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //      Log.d("Success", response.getResponseText());
                }
                public void onFailure(WLFailResponse response) {
                    Log.d("Failure", response.getResponseText());

                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}