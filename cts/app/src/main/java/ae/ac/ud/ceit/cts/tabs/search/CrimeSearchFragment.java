package ae.ac.ud.ceit.cts.tabs.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ae.ac.ud.ceit.cts.SingleCrimeView;
import ae.ac.ud.ceit.cts.model.Crime;
import ae.ac.ud.ceit.cts.model.CrimeBuilder;
import ae.ac.ud.ceit.cts.model.CrimeBuilderImpl;
import ae.ac.ud.ceit.cts.tabs.feeds.FeedsComponent;
import ae.ac.ud.ceit.cts.R;
import ae.ac.ud.ceit.cts.tabs.feeds.FeedsListViewAdapter;

/**
 * Created by atalla on 23/02/17.
 */

public class CrimeSearchFragment extends Fragment{
    private FeedsComponent c1,c2,c3,c4,c5,c6,c7,c8,c9;
    private boolean list_visibile = false;


    private static final String TAG = "SingleCrimeView";
    private ListView listview;

    private FeedsListViewAdapter listViewAdapter;
    private List<Crime> crimes = new ArrayList<Crime>();
    private EditText mSearchField;
    public static final String ARG_PAGE = "ARG_PAGE";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchField = (EditText)root.findViewById(R.id.search_field);
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
                clearResults();
                updateResults();
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d(TAG, "afterTextChanged");






            }
        });
        //set of layouts for the views
        listview = (ListView) root.findViewById(R.id.feedslist);
        listViewAdapter = new FeedsListViewAdapter(getContext(), R.layout.crime_row, crimes);
        listview.setAdapter(listViewAdapter);
        // Inflate the layout for this fragment
        //creation of the components for the grid/list







        return root;
    }
    public static CrimeSearchFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        CrimeSearchFragment fragment = new CrimeSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }
    void getListAllCrimes(){


        try{
            URI adapterPath = new URI("/adapters/JavaScriptSQl/searchBySuspect");

            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);
            String suspect = mSearchField.getText().toString();
            request.setQueryParameter("params","['"+suspect+"']");
            request.send(new WLResponseListener(){
                public void onSuccess(WLResponse response) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getResponseJSON();
                   //     Log.d(TAG, jsonObject +"");
                        JSONArray results = jsonObject.getJSONArray("resultSet");
                        for(int i=0; i < results.length(); i++){
                            // if(results.length() > 0) {

                            int reference = results.getJSONObject(i).getInt("reference"); //String.format("%5d", results.getJSONObject(0).getInt("reference"));
                            String date_time = results.getJSONObject(i).getString("created");
                            String created = results.getJSONObject(i).getString("date_time");
                            String evidences= results.getJSONObject(i).getString("evidences");
                            String location= results.getJSONObject(i).getString("location");
                            String suspects= results.getJSONObject(i).getString("suspects");
                            String victims= results.getJSONObject(i).getString("victims");
                            CrimeBuilder builder = new CrimeBuilderImpl() ;//= ;//.setWheels(4).setColor("Red").build();
                            Crime crime = builder.setDate_time(date_time)
                                    .setReference(String.format("%05d", reference) )
                                    .setVictims(victims)
                                    .setSuspects(suspects)
                                    .setEvidences(evidences)
                                    .setLocation(location)
                                    .setCreated(created)
                                    .build();
                            crimes.add(crime);
                        //    Log.d("Success->", crime +"");



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
    public void clearResults(){
        listview.setOnItemClickListener(null);
        listViewAdapter.clear();
        crimes.clear();
    }
    public void updateResults(){
        getActivity().runOnUiThread(new Runnable() {

            public void run() {
                getListAllCrimes();

                //      listViewAdapter.notifyDataSetChanged();
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
                //Toast.makeText(getContext(), "Server related issue/Try again", Toast.LENGTH_LONG).show();

            }});
    }
}
