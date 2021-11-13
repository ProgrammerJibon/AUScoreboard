package bd.com.jibon.AUScoreboard.Internet;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import bd.com.jibon.AUScoreboard.R;

@SuppressLint("StaticFieldLeak")
public class OpenImageFromLink extends AsyncTask<Bitmap, Bitmap, Bitmap> {
    public String url;
    public ImageView imageView;

    public OpenImageFromLink(String url, View imageView) {
        this.imageView = (ImageView) imageView;
        this.url = url;
    }
/*
    @Override
    protected void onPreExecute() {
        imageView.setImageResource(R.drawable.ic_baseline_cloud_download_24);
    }*/

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {
        try{
            URL urlx = new URL(url);
            HttpURLConnection urlxConnection = (HttpURLConnection) urlx.openConnection();
            urlxConnection.setUseCaches(true);
            InputStream urlxInputStream = urlxConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(urlxInputStream);
            return bitmap;
        }catch (Exception e){
            Log.e("errnos_pic_load", "\t"+url+"\t"+e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap!=null){
            try{
                imageView.setImageBitmap(bitmap);
//                Log.e("errnos", "\tImage set success...");
            }catch (Exception error){
//                Log.e("errnos", "\tImage is not set success...");
            }

        }
    }
}
