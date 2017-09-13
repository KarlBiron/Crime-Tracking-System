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
//This class extends the fragment class. A fragment is an entity that can run inside an activity in android studio.
//You can run multiple fragments at the same time inside one activity.
public class ManageCrimeFragment extends Fragment  implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    //this variable is just declared to identify this file.
    private static  String TAG ="ManageCrimeFragment";

    //These variables serve the same purpse as the ones in the crime.java file.
    private EditText editReference;     //there is no usage of this EditText

    private EditText mDate;
    private EditText mTime;
    private EditText mLocation;
    private EditText mVictims;
    private EditText mSuspects;
    private EditText mEvidens;

    //This is a new Calendar object being invoked because we included a functionality
    //that allows a calendar to pop up when wanting to input a date to make selection easier for the user.
    //This is achieved using the "datePickerDialog" and "Calendar" object.
    //The year, month and day variables have been initialized here as well.
    Calendar calendar ;
    DatePickerDialog datePickerDialog ;
    Button  btn_save, btn_clear;        //btn_save = submits EditText values, btn_clear = set null to all EditText
    int Year, Month, Day ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new, container, false);

        // editReference = (EditText) root.findViewById(R.id.editReference);

        //We are grabbing the input from the XML file and storing them into the declared variables.
        //This is also true for the button btn_save.
        mDate = (EditText) root.findViewById(R.id.editTextDate);
        mTime = (EditText) root.findViewById(R.id.editTextTime);
        mLocation = (EditText)root.findViewById(R.id.editLocation);
        mVictims = (EditText)root.findViewById(R.id.editVictims);
        mSuspects= (EditText)root.findViewById(R.id.editSuspects);
        mEvidens = (EditText)root.findViewById(R.id.editEvidens);

        btn_save = (Button) root.findViewById(R.id.btn_save);

        //We are connecting the button to a default click function. In this function we can have any action
        //done after we click the button.
        btn_save.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onSave(v);
            }
        });

        //The same thing is done here to the clear button.
        btn_clear= (Button) root.findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onClearClicked();
            }
        });
        onClearClicked();

        /*
         */

        //The calendar instance is being created here.
        calendar = Calendar.getInstance();

        //The values for the year, month and day are being store in their respective variables.
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        //This is a click listener for the Date textbox. When it is tapped, the calendar instance then runs
        //and below are the properties applied when the textbox is clicked.
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

        // A similar action is being applied here to the Time textbox, where properties are being applied on creation
        // and a small clock appears to enable the user to easily input the time.
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();      //Time is also being pulled using the "Calendar" library
                TimePickerDialog tpd = TimePickerDialog.newInstance(    //creating "TimePickerDialog" object
                        ManageCrimeFragment.this,
                        now.get(Calendar.HOUR_OF_DAY),          //Syncing current hour to the tpd
                        now.get(Calendar.MINUTE),               //Syncing current minute to th tpd

                        true
                );

                //These are T"imePickerDialog" configurations
                tpd.setThemeDark(true);
                tpd.vibrate(true);
                tpd.dismissOnPause(true);
                tpd.enableSeconds(true);
                //tpd.setVersion(true);
                tpd.setAccentColor(Color.parseColor("#000eee"));
                tpd.setTitle("TimePicker Title");
                tpd.setTimeInterval(3, 5, 10);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //Send a log message, seen in logcat.
                        //"TimePicker" param: used to identify the source of a log message.
                        // It usually identifies the class or activity where the log call occurs.
                        //"Dialog was cancelled" param: The message you would like logged.
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });
        return root;
    }

    public void onSave(View view){
        addCrime();     //This is the function that adds a new crime to the database.
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
    //To override the implemented "DatePickerDialog.OnDateSetListener"
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //create a String called "date" and store date in YYYY-MM-DD format
        String date = Year + "-" + String.format("%02d", Month)  + "-" + String.format("%02d", Day);

        //set the text in the EditText called "mDate" as the String called "date" (above)
        mDate.setText(date ) ;

        //Displays standard toast that just contains a text view of the date set.
        //"LENGTH_LONG": Show the view or text notification for a long period of time
        Toast.makeText(getContext(), date, Toast.LENGTH_LONG).show();
    }

    @Override
    //To override the implemented "TimePickerDialog.OnTimeSetListener"
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        //create a String called "time" and store time in HH:MM:SS format
        String time = ""+String.format("%02d", hourOfDay) +":"+String.format("%02d", minute)+":"+String.format("%02d", second);

        //set the text in the EditText called "mTime" as the String called "time" (above)
        mTime.setText(time);
    }


    //This is the function that adds a new crime to the database.
    void addCrime(){

        //retrieving combined date and time from the EditText fields
        String datetime = mDate.getText() + " " + mTime.getText();
        Log.d(TAG, mDate.getText() + " " + mTime.getText()  );

        try{//"try" block is used to enclose the code that might throw an exception
            //Uniform Resource Identifier (URI)
            //For JavaScript adapters, use /adapters/{AdapterName}/{procedureName}
            //"addCrime" is a function from the JS code
            URI adapterPath = new URI("/adapters/JavaScriptSQl/addCrime");

            //victims, location, suspects, evidence
            String params = "[  " +
                    "'" + datetime + "', " +
                    "'" + mLocation.getText() + "', " +
                    "'" + mVictims.getText() + "', " +
                    "'" + mSuspects.getText() + "', " +
                    "'" + mEvidens.getText() + "', " +
                    "'" + "value1" + "' " +
                    " ]";
            Log.d("AA", params);

            //MobileFirst applications can access resources using the WLResourceRequest REST API.
            //(WLResourceRequest) class handles resource requests to adapters or external resources)
            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);

            //These are Query Parameters
            //eg: request.setQueryParameter("params","['value1']");
            request.setQueryParameter("params", params);

            //Request the resource by using the .send() method
            request.send(new WLResponseListener(){

                public void onSuccess(WLResponse response) {

                    //runOnUiThread(Runnable action)
                    //Runs the specified action on the UI thread.
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {

                            // Display a toast stating "New Crime added Successfully ..."
                            //"LENGTH_LONG": Show the view or text notification for a long period of time
                            Toast.makeText(getContext(), "New Crime added Successfully ...", Toast.LENGTH_LONG).show();
                            onClearClicked();
                            openNewCrimeDetails();
                            //  return;
                        }});

                    //JavaScript Object Notation, is a minimal, readable format for structuring data.
                    //It is used primarily to transmit data between a server and web application, as an alternative to XML.
                    //Declare and initialize a JSONObject variable named "jsonObject" as NULL
                    JSONObject jsonObject = null;

                    try {//"try" block is used to enclose the code that might throw an exception

                        //getResponseJSON() = This method retrieves the response text from the server in JSON format.
                        jsonObject = response.getResponseJSON();

                        //JSONArray = A dense indexed sequence of values.
                        JSONArray results = jsonObject.getJSONArray("resultSet");
                        //  for(int i=0; i < results.length(); i++){
                        if(results.length() > 0) {
                            Log.d("Success->", results.getJSONObject(0).getString("fullname"));
                        }
                        else if(results.length() == 0){
                            Log.d("Success->", "Wrong username or password");
                        }
                        //"catch": used to handle the Exception
                        //If not used, the code below will not run(Only error will be displayed)
                        //JSONException: set to look for these type of exceptions
                        //store specified exception to "e"
                    } catch (JSONException e) {
                        e.printStackTrace();    //more details than System.out.println(e);
                    }

                    //      Log.d("Success", response.getResponseText());
                }
                public void onFailure(WLFailResponse response) {
                    Log.d("Failure", response.getResponseText());
                }
            });
            //"catch": used to handle the Exception
            //If not used, the code below will not run(Only error will be displayed)
            //URISyntaxException: set to look for these type of exceptions
            //store specified exception to "e"
        } catch (URISyntaxException e) {
            e.printStackTrace();        //more details than System.out.println(e);
        }
    }

    //This bit of code is responsible for running when the user wants to display the details of a crime record.
    private void openNewCrimeDetails() {


        try{//"try" block is used to enclose the code that might throw an exception
            //Uniform Resource Identifier (URI)
            //For JavaScript adapters, use /adapters/{AdapterName}/{procedureName}
            //"getLastCrime" is a function from the JS code
            URI adapterPath = new URI("/adapters/JavaScriptSQl/getLastCrime");

            //MobileFirst applications can access resources using the WLResourceRequest REST API.
            //(WLResourceRequest) class handles resource requests to adapters or external resources)
            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);

            //Request the resource by using the .send() method
            request.send(new WLResponseListener(){
                public void onSuccess(WLResponse response) {

                    //JavaScript Object Notation, is a minimal, readable format for structuring data.
                    //It is used primarily to transmit data between a server and web application, as an alternative to XML.
                    //Declare and initialize a JSONObject variable named "jsonObject" as NULL
                    JSONObject jsonObject = null;

                    try {//"try" block is used to enclose the code that might throw an exception

                        //getResponseJSON() = This method retrieves the response text from the server in JSON format.
                        jsonObject = response.getResponseJSON();

                        Log.d(TAG, jsonObject +"");

                        //JSONArray = A dense indexed sequence of values.
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

                            //Sets a new crime with all the values from the EditText field
                            Crime crime = builder
                                    .setDate_time(date_time)
                                    .setReference(String.format("%05d", reference) )
                                    .setVictims(victims)
                                    .setSuspects(suspects)
                                    .setEvidences(evdinces)
                                    .setLocation(location)
                                    .setCreated(created)
                                    .build();

                            //An Intent is an object that provides runtime binding between separate components
                            //moves to the "SingleCrimeView" page
                            // where all inputted values of the new crime are displayed
                            Intent intent = new Intent(getContext(), SingleCrimeView.class);

                            intent.putExtra("com.example.cities.City", crime);
                            startActivity(intent);      //starts intent mentioned as class
                            Log.d("Success->", crime +"");



                        }
                        if(results.length() == 0){
                            Log.d("Success->", "Wrong username or password");

                        }
                        //"catch": used to handle the Exception
                        //If not used, the code below will not run(Only error will be displayed)
                        //JSONException: set to look for these type of exceptions
                        //store specified exception to "e"
                    } catch (JSONException e) {
                        e.printStackTrace();    //more details than System.out.println(e);
                    }

                    //      Log.d("Success", response.getResponseText());
                }
                public void onFailure(WLFailResponse response) {
                    Log.d("Failure", response.getResponseText());

                }
            });
            //"catch": used to handle the Exception
            //If not used, the code below will not run(Only error will be displayed)
            //URISyntaxException: set to look for these type of exceptions
            //store specified exception to "e"
        } catch (URISyntaxException e) {
            e.printStackTrace();        //more details than System.out.println(e);
        }
    }

    void onClearClicked(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = df1.format(c.getTime());

        // This code sets the values of the fields to null again after all the other code runs.
        mDate.setText(formattedDate);
        mTime.setText(formattedTime);
        mLocation.setText(null);
        mVictims.setText(null);
        mSuspects.setText(null);
        mEvidens.setText(null);
    }
}