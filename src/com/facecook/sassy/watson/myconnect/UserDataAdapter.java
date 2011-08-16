package com.facecook.sassy.watson.myconnect;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class UserDataAdapter extends ArrayAdapter<UserData> {
    private LayoutInflater mInflater;
    private List<UserData> mItems;
    public UserDataAdapter(Context context, int resourceId, List<UserData> items) {
        super(context, resourceId, items);
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.list_row, null);
        }
        UserData data = (UserData)mItems.get(position);
        ImageView view = (ImageView) v.findViewById(R.id.image);
        String url = "http://graph.facebook.com/" + data.getUserId() + "/picture";
        if (ImageCache.isCache(url)) {
            view.setImageDrawable(ImageCache.get(url));
        } else {
            view.setImageResource(R.drawable.icon);
            ImageAsyncTask task = new ImageAsyncTask(view);
            try {
                task.execute(new URL(url));
            } catch (MalformedURLException e) {
                Log.e("Logtag", e.toString());
            }
        }
        
        TextView text = (TextView) v.findViewById(R.id.text);
        text.setText(data.getUserName());
        TextView message = (TextView) v.findViewById(R.id.message);
        String message_content = data.getUserMessage();
        if (message_content != null) {
            message.setText(message_content);
        } else {
            message.setText("No message");
        }
        return v;
    }

}
