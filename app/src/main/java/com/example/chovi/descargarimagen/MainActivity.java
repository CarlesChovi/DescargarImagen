package com.example.chovi.descargarimagen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {

    private ImageView imagen;
    private ProgressBar progressBar;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagen = (ImageView) findViewById(R.id.imagenDescarga);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        textView = (TextView)findViewById(R.id.textView);

        DescargaImagen descargaImagen = new DescargaImagen();
        descargaImagen.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DescargaImagen extends AsyncTask<Void,Integer,Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... params) {

            InputStream inputStream = null;
            Bitmap bitmap = null;
            HttpURLConnection conexion = null;
            ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            //Recoge información de red
            NetworkInfo nInfo = cManager.getActiveNetworkInfo();

            if(nInfo!=null && nInfo.isConnected()) try {
                Uri uri = Uri.parse("http://photo.rukes.com/shrine8/shrine8_031.jpg");
                URL url = new URL(uri.toString());
                conexion = (HttpURLConnection) url.openConnection();

                conexion.connect();

                inputStream = conexion.getInputStream();

                for (int i = 0; i <= 100; i++){
                    Thread.sleep(100);
                    publishProgress(i);
                }

                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            textView.setText((values[0])+"%");
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            //Ocultamos el progressBar una vez se muestre la imagen
            progressBar.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            imagen.setImageBitmap(bitmap);
        }
    }
}
