package ae.ac.ud.ceit.cts.tabs.manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;

import ae.ac.ud.ceit.cts.R;
import ae.ac.ud.ceit.cts.SingleCrimeView;
import ae.ac.ud.ceit.cts.model.Crime;
import ae.ac.ud.ceit.cts.model.CrimeBuilder;
import ae.ac.ud.ceit.cts.model.CrimeBuilderImpl;

/**
 * Created by atalla on 22/02/17.
 */
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageCrimeFragment extends Fragment  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static  String TAG ="ManageCrimeFragment";
    private EditText editReference;

    private EditText mDate;
    private EditText mTime;
    private EditText mLocation;
    private EditText mVictims;
    private EditText mSuspects;
    private EditText mEvidens;


    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    Button  btn_save, btn_clear;
    int Year, Month, Day ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new, container, false);

       // editReference = (EditText) root.findViewById(R.id.editReference);
        mDate = (EditText) root.findViewById(R.id.editTextDate);
        mTime = (EditText) root.findViewById(R.id.editTextTime);
        mLocation = (EditText)root.findViewById(R.id.editLocation);
        mVictims = (EditText)root.findViewById(R.id.editVictims);
        mSuspects= (EditText)root.findViewById(R.id.editSuspects);
        mEvidens = (EditText)root.findViewById(R.id.editEvidens);

        btn_save = (Button) root.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onSave(v);
            }
        });
        btn_clear= (Button) root.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onClearClicked();
            }
        });
        onClearClicked();

        /*

         */
        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = DatePickerDialog. newInstance(ManageCrimeFragment.this, Year, Month, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(Color.parseColor("#000eee"));

                datePickerDialog.setTitle("Select Date From DatePickerDialog");

                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        ManageCrimeFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),

                        true
                );
                tpd.setThemeDark(true);
                tpd.vibrate(true);
                tpd.dismissOnPause(true);
                tpd.enableSeconds(true);
              //  tpd.setVersion(true);

                    tpd.setAccentColor(Color.parseColor("#000eee"));

                    tpd.setTitle("TimePicker Title");


                    tpd.setTimeInterval(3, 5, 10);

                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });
        return root;
    }

    public void onSave(View view){
        addCrime();
    }

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static ManageCrimeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ManageCrimeFragment fragment = new ManageCrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }




    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = Year + "-" + String.format("%02d", Month)  + "-" + String.format("%02d", Day);

        // 2013-08-05


        mDate.setText(date ) ;
        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

      //  18:19:03

        String time = ""+String.format("%02d", hourOfDay) +":"+String.format("%02d", minute)+":"+String.format("%02d", second);
        mTime.setText(time);
    }

    void addCrime(){
     //   2013-08-05 18:19:03
       // 9-3-2017 0h:5m:50

        String datetime = mDate.getText() + " " + mTime.getText();
        Log.d(TAG, mDate.getText() + " " + mTime.getText()  );

        try{
            //victims, location, suspects
            URI adapterPath = new URI("/adapters/JavaScriptSQl/addCrime");
            String params = "[  " +
                    "'" + datetime + "', " +
                    "'" + mLocation.getText() + "', " +
                    "'" + mVictims.getText() + "', " +
                    "'" + mSuspects.getText() + "', " +
                    "'" + mEvidens.getText() + "', " +
                    "'" + "value1" + "' " +
                    " ]";
            Log.d("AA", params);
            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);
            request.setQueryParameter("params", params);

            request.send(new WLResponseListener(){
                public void onSuccess(WLResponse response) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getContext(), "New Crime added Successfully ...", Toast.LENGTH_LONG).show();
                            onClearClicked();
                           openNewCrimeDetails();
                          //  return;
                        }});
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getResponseJSON();

                        JSONArray results = jsonObject.getJSONArray("resultSet");
                        //  for(int i=0; i < results.length(); i++){
                        if(results.length() > 0) {

                            Log.d("Success->", results.getJSONObject(0).getString("fullname"));



                        }
                        else if(results.length() == 0){
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

    private void openNewCrimeDetails() {
        try{
            URI adapterPath = new URI("/adapters/JavaScriptSQl/getLastCrime");

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
                            Intent intent = new Intent(getContext(), SingleCrimeView.class);
                            intent.putExtra("com.example.cities.City", crime);
                            startActivity(intent);
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

    void onClearClicked(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = df1.format(c.getTime());

// textView is the TextView view that should display it
        mDate.setText(formattedDate);

        mTime.setText(formattedTime);
        mLocation.setText(null);
        mVictims.setText(null);
        mSuspects.setText(null);
        mEvidens.setText(null);
    }
}
