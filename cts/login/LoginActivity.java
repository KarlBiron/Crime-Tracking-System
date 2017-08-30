package ae.ac.ud.ceit.cts.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "CTS->LoginForm";
    private static final int REQUEST_SIGNUP = 0;
    final LoginActivity loginActivity = this;

    private WLClient client;

    Button      _loginButton;
    EditText    _userText;
    EditText    _passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        client = WLClient.createInstance(this);
        Log.d(TAG, client.getServerUrl().toString() );
        _loginButton    = (Button) findViewById((R.id.btn_login));
        _userText       = (EditText) findViewById((R.id.input_user));
        _passwordText   = (EditText) findViewById((R.id.input_password));

    }
    public void login(View view){
        Log.d(TAG, "Login");
        connectMobileFirst2();
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String user      = _userText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                      //  onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);

        try{
            URI adapterPath = new URI("/adapters/JavaScriptSQl/validateInvestigator");

            WLResourceRequest request = new WLResourceRequest(adapterPath, WLResourceRequest.GET);
            request.setQueryParameter("params","['"+user+"', '"+password+"']");
            request.send(new WLResponseListener(){
                public void onSuccess(WLResponse response) {

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getResponseJSON();

                        JSONArray results = jsonObject.getJSONArray("resultSet");
                      //  for(int i=0; i < results.length(); i++){
                          if(results.length() > 0) {

                              Log.d("Success->", results.getJSONObject(0).getString("fullname"));

                              progressDialog.dismiss();
                      //  }
                              onLoginSuccess();
                          }
                        else if(results.length() == 0){
                              Log.d("Success->", "Wrong username or password");
                              progressDialog.dismiss();
                              onLoginFailed();
                          }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

              //      Log.d("Success", response.getResponseText());
                }
                public void onFailure(WLFailResponse response) {
                    Log.d("Failure", response.getResponseText());
                    progressDialog.dismiss();
                    onServerFail();
                }
            });
    } catch (URISyntaxException e) {
        e.printStackTrace();
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
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
      //  _loginButton.setEnabled(true);

        finish();
        Intent intent = new Intent();
        intent.setClass(loginActivity, MainActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), "Login failed/Try again", Toast.LENGTH_LONG).show();
                _loginButton.setEnabled(true);}});

    }
    public void onServerFail() {

        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), "Server related issue/Try again", Toast.LENGTH_LONG).show();
                _loginButton.setEnabled(true);
        }});
    }


    public boolean validate() {
        boolean valid = true;

        String user = _userText.getText().toString();
        String password = _passwordText.getText().toString();

        if (user.isEmpty() /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(user).matches()*/) {
            _userText.setError("enter a valid user name");
            valid = false;
        } else {
            _userText.setError(null);
        }

        if (password.isEmpty() /*|| password.length() < 4 || password.length() > 10*/) {
            _passwordText.setError("enter your password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    void connectMobileFirst(){
        WLAuthorizationManager.getInstance().obtainAccessToken(null, new WLAccessTokenListener() {
            @Override
            public void onSuccess(AccessToken token) {
                Log.d(TAG,"Received the following access token value: " + token);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Yay!");
                        Log.d(TAG,"Connected to MobileFirst Server");
                    }
                });
            }

            @Override
            public void onFailure(WLFailResponse wlFailResponse) {
                Log.d(TAG,"Did not receive an access token from server: " + wlFailResponse.getErrorMsg());
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

    void connectMobileFirst2(){
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
                        try {
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
