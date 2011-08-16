package com.facecook.sassy.watson.myconnect;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.facebook.android.*;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.*;


public class MyConnectActivity extends Activity implements RequestListener {
    Facebook facebook = new Facebook("XXX");
    private static final String LOGTAG = "MyConnect";

    private AsyncFacebookRunner mRunner;
    private UserDataAdapter mAdapter;
    private List<UserData> mUserList;
    private Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog;

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mRunner = new AsyncFacebookRunner(facebook);
        if (!facebook.isSessionValid()) {
           facebook.authorize(this, new DialogListener() {
                @Override
                public void onCancel() {
                }
    
                @Override
                public void onComplete(Bundle values) {
                    mRunner.request("me/friends", MyConnectActivity.this);
                    mProgressDialog = new ProgressDialog(MyConnectActivity.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setMessage("Get Message");
                    mProgressDialog.setCancelable(true);
                    mProgressDialog.show();
                }
    
                @Override
                public void onError(DialogError e) {
                }
    
                @Override
                public void onFacebookError(FacebookError e) {
                }
            });
        }
        mUserList = new ArrayList<UserData>();
        mAdapter = new UserDataAdapter(this, R.layout.list_row, mUserList);
        ListView list = (ListView)findViewById(R.id.listview);
        list.setAdapter(mAdapter);
        
    }
    
    @Override
    public void onResume() {
        super.onResume();
        //TBD update
    }
    
    private String getFriendMessage(JSONObject object) {
        String wall;
        try {
            wall = facebook.request(object.getString("id") + "/feed");
            JSONObject wall_obj = Util.parseJson(wall);
            JSONArray wall_array = wall_obj.getJSONArray("data");
            int j = 0;
            for (j = 0; j < wall_array.length(); j++) {
                JSONObject data = Util.parseJson(wall_array.getJSONObject(j).getString("from"));
                if (data.getString("name").equals(object.getString("name"))) {
                    return wall_array.getJSONObject(j).getString("message");
                }
            }
        } catch (MalformedURLException e) {
            Log.e(LOGTAG, e.toString());
        } catch (IOException e) {
            Log.e(LOGTAG, e.toString());
        } catch (JSONException e) {
            Log.e(LOGTAG, e.toString());
        } catch (FacebookError e) {
            Log.e(LOGTAG, e.toString());
        }
        return null;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        facebook.authorizeCallback(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(String response, Object state) {
        Log.d(LOGTAG, "Complete");
        try {
            JSONObject obj = Util.parseJson(response);
            JSONArray array = obj.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                mUserList.add(new UserData(array.getJSONObject(i).getString("name"), array.getJSONObject(i).getString("id"), getFriendMessage(array.getJSONObject(i))));
            }
            String user = facebook.request(array.getJSONObject(0).getString("id"));
            Log.d(LOGTAG, user);
            String wall = facebook.request(array.getJSONObject(0).getString("id") + "/feed");
            Log.d(LOGTAG, wall);
            mProgressDialog.dismiss();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (MalformedURLException e) {
            Log.e(LOGTAG, e.toString());
        } catch (IOException e) {
            Log.e(LOGTAG, e.toString());
        } catch (JSONException e) {
            Log.e(LOGTAG, e.toString());
        } catch (FacebookError e) {
            Log.e(LOGTAG, e.toString());
        }
    }

    @Override
    public void onFacebookError(FacebookError e, Object state) {
        Log.d(LOGTAG, e.toString());
    }

    @Override
    public void onFileNotFoundException(FileNotFoundException e, Object state) {
        Log.d(LOGTAG, e.toString());
    }

    @Override
    public void onIOException(IOException e, Object state) {
        Log.d(LOGTAG, e.toString());
    }

    @Override
    public void onMalformedURLException(MalformedURLException e, Object state) {
        Log.d(LOGTAG, e.toString());
    }
}