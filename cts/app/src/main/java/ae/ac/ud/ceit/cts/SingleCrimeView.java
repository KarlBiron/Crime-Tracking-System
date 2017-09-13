package ae.ac.ud.ceit.cts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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

import ae.ac.ud.ceit.cts.model.Crime;
import ae.ac.ud.ceit.cts.model.CrimeBuilder;
import ae.ac.ud.ceit.cts.model.CrimeBuilderImpl;
import ae.ac.ud.ceit.cts.tabs.feeds.FeedsListViewAdapter;


/**
 * Created by atalla on 10/04/17.
 */

public class SingleCrimeView extends Activity {
    // Declare Variables
    private TextView mReference;
    private TextView mDate_Time;
    private TextView mCreated;
    private TextView mLocation;
    private TextView mVictims;
    private TextView mSuspects;
    private TextView mEvidences;
    private static final String TAG = "SingleCrimeView";
    private ListView listview;

    private FeedsListViewAdapter listViewAdapter;
    private List<Crime> crimes = new ArrayList<Crime>();
    Crime crime = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crime_detail);
   //     View root = inflater.inflate(R.layout.crime_detail, container, false);
        Bundle bundle = getIntent().getExtras();
         crime = (Crime) bundle.getSerializable("com.example.cities.City");

        // Retrieve data from MainActivity on listview item click
        Intent i = getIntent();
        mReference  = (TextView) findViewById(R.id.detailReference);
        mDate_Time  = (TextView) findViewById(R.id.detaildate_time);
        mCreated    = (TextView) findViewById(R.id.detailcreated);
        mLocation   = (TextView) findViewById(R.id.detaillocation);
        mVictims    = (TextView) findViewById(R.id.detailvictims);
        mSuspects   = (TextView) findViewById(R.id.detailsuspects);
        mEvidences  = (TextView) findViewById(R.id.detailevidnce);

        mReference.setText(crime.getReference());
        mDate_Time.setText(crime.getDate_time());
        mCreated.setText(crime.getCreated());
        mLocation.setText(crime.getLocation());
        mVictims.setText(crime.getVictims());
        mSuspects.setText(crime.getSuspects());
        mEvidences.setText(crime.getEvidences());

        getListAllCrimes();
        //set of layouts for the views
        listview = (ListView) findViewById(R.id.feedslist);

        listViewAdapter = new FeedsListViewAdapter(this, R.layout.crime_row, crimes);
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
    }


    void getListAllCrimes(){


        try{
            URI adapterPath = new URI("/adapters/JavaScriptSQl/findSerialCrime");

            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);
            request.setQueryParameter("params","['"+crime.getLocation() + "+"  + crime.getEvidences() +
                                                    crime.getVictims() + "+"  + crime.getSuspects() +
                    "']");

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
                            if((String.format("%05d", reference)).contains( crime.getReference() ))continue;
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
