package ae.ac.ud.ceit.cts.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;    //package that prints out log info in real time
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.worklight.jsonstore.api.JSONStoreAddOptions;
import com.worklight.jsonstore.api.JSONStoreChangeOptions;
import com.worklight.jsonstore.api.JSONStoreCollection;
import com.worklight.jsonstore.api.JSONStoreFileInfo;
import com.worklight.jsonstore.api.JSONStoreFindOptions;
import com.worklight.jsonstore.api.JSONStoreInitOptions;
import com.worklight.jsonstore.api.JSONStoreQueryPart;
import com.worklight.jsonstore.api.JSONStoreQueryParts;
import com.worklight.jsonstore.api.JSONStoreRemoveOptions;
import com.worklight.jsonstore.api.JSONStoreReplaceOptions;
import com.worklight.jsonstore.api.WLJSONStore;
import com.worklight.jsonstore.database.SearchFieldType;

import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import com.worklight.wlclient.api.*;
import com.worklight.wlclient.auth.AccessToken;

import java.net.URI;
import java.net.URISyntaxException;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ae.ac.ud.ceit.cts.MainActivity;
import ae.ac.ud.ceit.cts.R;

/**
 * Created by atalla on 24/02/17.
 */

public class LoginActivity extends AppCompatActivity {  //(extends=inherit)AppCombatActivity[Super-class] is part of Android support library
    private static String TAG = "CTS->LoginForm";       //for LOG purposes [CTS->LoginForm is the reference for all LoginActivity LOGS]
    private static final int REQUEST_SIGNUP = 0;
    final LoginActivity loginActivity = this;

    private WLClient client;        //declaring wireless client

    Button      _loginButton;       //declaring login button
    EditText    _userText;          //declaring username textfield
    EditText    _passwordText;      //declaring password textfield

    @Override           //overrides existing methods in the superclass(AppCompatActivity) [Shortcut = Alt + Insert]
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);        //sets activity_login.xml as content view

        client = WLClient.createInstance(this);
        Log.d(TAG, client.getServerUrl().toString() );      //for LOG purposes [print out client.getServerUrl() states]
        _loginButton    = (Button) findViewById((R.id.btn_login));          //assign button(.java) to activity_login.xml
        _userText       = (EditText) findViewById((R.id.input_user));       //assign user text(.java) to activity_login.xml
        _passwordText   = (EditText) findViewById((R.id.input_password));   //assign pass text(.java) to activity_login.xml

    }

    //public=for all to see, void=no return value,login=method name
    //(x,y)= x is the object from the import, y is the object name;
    public void login(View view){
        Log.d(TAG, "Login");        //for LOG purposes ["Login" is the reference for all login class LOGS]
        connectMobileFirst2();      //attempt to connect to server side
        if (!validate()) {          //[validate() method is boolean]Validating Username and Password Input (check if empty)
            onLoginFailed();        // calls a method that display a toast "Login failed/Try again"
            return;
        }

        _loginButton.setEnabled(false); //Disables the login button to be pressed again if validate() doesn't fail

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,    //setup progress loop display
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);                      // "Whether this ProgressDialog is in indeterminate mode."
        progressDialog.setMessage("Authenticating...");             // setting progress dialog text
        progressDialog.show();                                      // display loading/authentication screen

        String user      = _userText.getText().toString();          //Return the text that TextView(username) is displaying.
        String password = _passwordText.getText().toString();       //Return the text that TextView(password) is displaying.

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(   // TODO: ??Handler to set timer for progress dialog??
                //(postDelayed)"Causes the Runnable r to be added to the message queue, to be run after the specified amount of time elapses."
                new Runnable() {
                    public void run() { //When an object implementing interface Runnable is used to create a thread,
                                        //starting the thread causes the object's run method to be called in that separately executing thread.
                            // On complete call either onLoginSuccess or onLoginFailed
                            //  onLoginSuccess();
                            // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);   // Dismiss progress dialog after 3 seconds

        try{//"try" block is used to enclose the code that might throw an exception
            //Uniform Resource Identifier (URI)
            //For JavaScript adapters, use /adapters/{AdapterName}/{procedureName}
            //"validateInvestigator" is a function from the JS code
            URI adapterPath = new URI("/adapters/JavaScriptSQl/validateInvestigator");

            //MobileFirst applications can access resources using the WLResourceRequest REST API.
            //(WLResourceRequest) class handles resource requests to adapters or external resources)
            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);

            //These are Query Parameters
            //eg: request.setQueryParameter("params","['value1', 'value2']");
            request.setQueryParameter("params","['"+user+"', '"+password+"']");

            //Request the resource by using the .send() method
            request.send(new WLResponseListener(){

                public void onSuccess(WLResponse response) {

                    //JavaScript Object Notation, is a minimal, readable format for structuring data.
                    //It is used primarily to transmit data between a server and web application, as an alternative to XML.
                    //Declare and initialize a JSONObject variable named "jsonObject" as NULL
                    JSONObject jsonObject = null;

                    try {
                        //getResponseJSON() = This method retrieves the response text from the server in JSON format.
                        jsonObject = response.getResponseJSON();

                        //JSONArray = A dense indexed sequence of values.
                        JSONArray results = jsonObject.getJSONArray("resultSet");
                      //  for(int i=0; i < results.length(); i++){
                          if(results.length() > 0) {

                              Log.d("Success->", results.getJSONObject(0).getString("fullname"));

                              progressDialog.dismiss();     //close progress dialog
                      //  }
                              onLoginSuccess();             //runs "MainActivity" intent 
                          }
                        else if(results.length() == 0){
                              Log.d("Success->", "Wrong username or password");

                              progressDialog.dismiss();     //close progress dialog

                              onLoginFailed();              //displays a Toast message "Login failed/Try again"
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

                    progressDialog.dismiss();               //close progress dialog

                    onServerFail();                         //displays a Toast message "Server related issue/Try again"
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

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*/

    @Override
    public void onBackPressed() {       //disable going back to the MainActivity
        moveTaskToBack(true);           //Move the task containing this activity to the back of the activity stack.
                                        //TODO: Test actual code for this behavior
    }

    public void onLoginSuccess() {      //  _loginButton.setEnabled(true);
        finish();                       //activity(LoginActivity) is done and closed

        //An Intent is an object that provides runtime binding between separate components
        Intent intent = new Intent();

        //syntax: setClass(Context packageContext, Class<?> cls)
        //packageContext: A Context of the application package implementing this class.
        intent.setClass(loginActivity, MainActivity.class);
        startActivity(intent);  //starts intent mentioned as class
    }

    public void onLoginFailed() {
        //runOnUiThread(Runnable action)
        //Runs the specified action on the UI thread.
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), "Login failed/Try again", Toast.LENGTH_LONG).show();           // Display a toast
                _loginButton.setEnabled(true);}});          //Enables the login button to be pressed again after Toast message

    }
    public void onServerFail() {
        //runOnUiThread(Runnable action)
        //Runs the specified action on the UI thread.
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), "Server related issue/Try again", Toast.LENGTH_LONG).show();   // Display a toast
                _loginButton.setEnabled(true);              //Enables the login button to be pressed again after Toast message
        }});
    }


    public boolean validate() {         //Validating Username and Password Input
        boolean valid = true;           //Declare and initialize "valid" boolean as true

        String user = _userText.getText().toString();           //retrieving data from username text field
        String password = _passwordText.getText().toString();   //retrieving data from password text field

        if (user.isEmpty() /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()*/) {   //checks for empty username field
            _userText.setError("enter a valid user name");      //displays an error image and message
            valid = false;                                      //set boolean validity to false
        } else {
            _userText.setError(null);                           //no errors if username field is filled up
        }

        if (password.isEmpty() /*|| password.length() < 4 || password.length() > 10*/) {            //checks for empty password field
            _passwordText.setError("enter your password");      //displays an error image and message
            valid = false;                                      //set boolean validity to false
        } else {
            _passwordText.setError(null);                       //no errors if password field is filled up
        }

        return valid;       // returns valid = true if both user and pass fields are not empty
    }

    //TODO: ?? Method is never used ??
    void connectMobileFirst(){
        WLAuthorizationManager.getInstance().obtainAccessToken(null, new WLAccessTokenListener() {

            @Override
            public void onSuccess(AccessToken token) { //AccessToken = IBM mobilefirst
                Log.d(TAG,"Received the following access token value: " + token); //for LOG purposes [displays token value]
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Yay!");
                        Log.d(TAG,"Connected to MobileFirst Server");
                    }
                });
            }

            @Override
            public void onFailure(WLFailResponse wlFailResponse) { //WLFailResponse = IBM mobilefirst
                Log.d(TAG,"Did not receive an access token from server: " + wlFailResponse.getErrorMsg()); //for LOG purposes [displays error message]
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"Bummer...");
                        Log.d(TAG,"Failed to connect to MobileFirst Server");
                    }
                });
            }
        });
    }

    //Several "IBM mobilefirstplatforms" objects used here
    void connectMobileFirst2(){ //This method is ran at "public void login(View view)"
        WLAuthorizationManager.getInstance().obtainAccessToken("", new
                WLAccessTokenListener() {

                    @Override
                    public void onSuccess(AccessToken token) {
                        System.out.println("Received the following access token value: "
                                + token);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Yay!");
                                Log.d(TAG, "Connected to MobileFirst  Server");
                            }
                        });
                        URI adapterPath = null;

                        try {   //try-catch block to handle exceptions
                            adapterPath = new
                                    URI("/adapters/javaAdapter/resource/greet");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }

                        WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);
                        request.setQueryParameter("name","world");
                        request.send(new WLResponseListener() {
                            @Override
                            public void onSuccess(WLResponse wlResponse) {
                                // Will print "Hello world" in LogCat.
                                Log.i("MobileFirst Quick Start", "Success: " +
                                        wlResponse.getResponseText());
                            }
                            @Override
                            public void onFailure(WLFailResponse wlFailResponse) {
                                Log.i("MobileFirst Quick Start", "Failure: " +
                                        wlFailResponse.getErrorMsg());
                            }
                        });
                    }
                    @Override
                    public void onFailure(WLFailResponse wlFailResponse) {
                        System.out.println("Did not receive an access token from server: " + wlFailResponse.getErrorMsg());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Bummer...");
                                Log.d(TAG, "Failed to connect to      MobileFirst Server");
                            }
                        });
                    }
                });

    }
}
