package com.example.andres.libreria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andres on 12/may/2017.
 */

//tomado de http://stackoverflow.com/questions/18953632/how-to-set-image-from-url-for-imageview
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;

    public ImageLoadTask(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();



            //input.reset();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            //myBitmap=StringToBitMap(BitMapToString(myBitmap));
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected  void onPreExecute(){
        super.onCancelled();
        imageView.setImageResource(R.drawable.load);
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        imageView.setImageBitmap(result);
    }
    //http://stackoverflow.com/questions/13562429/how-many-ways-to-convert-bitmap-to-string-and-vice-versa
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            Log.d("conversion","terminado");
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }
    public String BitMapToString(Bitmap bitmap) {
        Log.d("conversion","comenzando");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("tam",String .valueOf(temp.length()));
        return temp;
    }

}