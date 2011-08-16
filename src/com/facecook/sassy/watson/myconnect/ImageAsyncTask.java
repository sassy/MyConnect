package com.facecook.sassy.watson.myconnect;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageAsyncTask extends AsyncTask<URL, Integer, Drawable> {
    private ImageView mImageView;
    
    public ImageAsyncTask(ImageView view) {
        mImageView = view;
    }

    @Override
    protected Drawable doInBackground(URL... params) {
        if (params.length <= 0 || params[0] == null) {
            return null;
        }
        URL url = params[0];
        Drawable drawable = null;
        try {
            InputStream in = (InputStream)url.getContent();
            Bitmap bmp = BitmapFactory.decodeStream(in);
            drawable = new BitmapDrawable(bmp);
            ImageCache.put(url.toString(), drawable);
        } catch (IOException e) {
            Log.e("Logtag", e.toString());
        }
        
        return drawable;
    }
    
    @Override
    protected void onPostExecute(Drawable result) {
        if (result == null) {
            return;
        }
        mImageView.setImageDrawable(result);
    }
}
