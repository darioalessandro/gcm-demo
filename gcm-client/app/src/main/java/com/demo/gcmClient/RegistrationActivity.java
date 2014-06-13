package com.demo.gcmClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class RegistrationActivity extends ActionBarActivity implements View.OnClickListener {

    private GoogleCloudMessaging gcm;
    private String registrationId;
    private Button submitButton;
    private EditText sessionIdField;
    private TextView errorText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        errorText = (TextView)findViewById(R.id.error_text);
        errorText.setVisibility(View.GONE);
        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);
        submitButton.setEnabled(false);

        sessionIdField = (EditText)findViewById(R.id.sessionId_field);
        sessionIdField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                errorText.setVisibility(View.GONE);
                String text = s.toString();
                if(text==null || text.isEmpty()) {
                    submitButton.setEnabled(false);
                }
                else {
                    submitButton.setEnabled(true);
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void registerInBackground(final Context context) throws IOException {
        //load the sender id and app server url from properties
        final String SENDER_ID = getResources().getString(R.string.gcm_sender_id);
        final String APP_SERVER_URL = getResources().getString(R.string.app_server_url);

        final String sessionId = sessionIdField.getText().toString();
        new AsyncTask<String,Void,Integer>() {
            private final int SUCCESS = 0;
            private final int SESSION_ID_ERR = 1;
            private final int APP_SERVER_REG_ERR = 2;
            private final int ERROR = 3;
            @Override
            protected Integer doInBackground(String... params) {
                try {
                    String sessionId = params[0].trim();
                    if(sessionIdAvailable(sessionId)) {
                        if(gcm == null) {
                            //get a GCM instance
                            gcm = GoogleCloudMessaging.getInstance(context);
                        }
                        //register with GCM
                        registrationId = gcm.register(SENDER_ID);
                        Log.i(RegistrationHelper.TAG, String.format("Device registered with id: %s", registrationId));
                        //send to server
                        String osVersion = Integer.toString(Build.VERSION.SDK_INT);
                        String appVersion = Integer.toString(RegistrationHelper.getAppVersion(context));
                        if(registerWithServer(sessionId,registrationId,osVersion,appVersion)) {
                            RegistrationHelper.storeRegistrationId(registrationId, context);
                            return SUCCESS;
                        }
                        else {
                            return APP_SERVER_REG_ERR;
                        }

                    }
                    else {
                        return SESSION_ID_ERR;
                    }

                }
                catch(IOException err ) {
                    Log.e(RegistrationHelper.TAG,err.getMessage(),err);
                    return ERROR;
                } catch (JSONException e) {
                    Log.e(RegistrationHelper.TAG,e.getMessage(),e);
                    return ERROR;
                }

            }

            @Override
            protected void onPostExecute(Integer s) {
                switch(s) {
                    case SUCCESS:
                        //complete
                        finish();
                        break;
                    case SESSION_ID_ERR:
                        errorText.setText(String.format("Session Id: '%s' isn't available",sessionId));
                        errorText.setVisibility(View.VISIBLE);
                        break;
                    case APP_SERVER_REG_ERR:
                        errorText.setText(String.format("App server registration error (Session Id: '%s')",sessionId));
                        errorText.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        errorText.setText("Unhandled Error");
                        errorText.setVisibility(View.VISIBLE);
                        break;
                }

            }

            private boolean sessionIdAvailable(String id) throws IOException {
                String url = String.format("%s/sessions/%s",APP_SERVER_URL, id.trim());
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet(url);
                return client.execute(request).getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND;
            }

            private boolean registerWithServer(String id, String gcmId, String osVersion, String appVersion) throws IOException, JSONException {
                String url = String.format("%s/sessions/%s",APP_SERVER_URL, id.trim());
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost(url);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gcm_id",gcmId);
                jsonObject.put("os_version",osVersion);
                jsonObject.put("app_version",appVersion);
                final String json = jsonObject.toString();
                request.setEntity(new StringEntity(json));
                request.setHeader("Content-type","application/json");
                final int statusCode = client.execute(request).getStatusLine().getStatusCode();
                if(statusCode == HttpStatus.SC_CREATED) {
                    return true;
                }
                else {
                    Log.e(RegistrationHelper.TAG,String.format("Server registration returned: %d\n%s",statusCode,json));
                    return false;
                }
            }
        }.execute(sessionId);
    }

    @Override
    public void onClick(View v) {
        try {
            registerInBackground(this);
        } catch (IOException e) {
            Log.e(RegistrationHelper.TAG,e.getMessage(),e);
            errorText.setText(String.format("Unhandled registration error: %s",e.getMessage()));
        }
    }
}
