package com.example.andres.libreria;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CategoriaVista extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private JSONObject informacion;
    private int idCat;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        Bundle bundle=getIntent().getExtras();
        setTitle(bundle.getString("categoria"));
        idCat=Integer.parseInt(bundle.getString("id"));

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        List items = new ArrayList();
        adapter=new LibroAdapter(items);
        recyclerView.setAdapter(adapter);
        ObtenerLibros hConexion=new ObtenerLibros();
        hConexion.execute("","");
    }
    public void abrirLibro(View v){
        Intent i =new Intent(this,Vistalibro.class);
        startActivity(i);
        //finish();
    }

    public class ObtenerLibros extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){
            String cadena=getString(R.string.host)+"/Libros/"+String.valueOf(idCat);
            String devuelve="";
            URL url=null;
            JSONObject resultado=null;
            try{
                url=new URL(cadena);
                HttpURLConnection connection=(HttpsURLConnection) url.openConnection();
                connection.setRequestProperty("user-Agent","Mozilla/5.0.+"+"(Linux: Android 1.5; es-ES) Ejemplo HTTP));");
                int respuesta=connection.getResponseCode();
                StringBuilder res1=new StringBuilder();
                if(respuesta== HttpsURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader leer = new BufferedReader(new InputStreamReader(in));

                    String linea=leer.readLine();

                    while (linea!= null) {
                        res1.append(linea);
                        Log.d(">",linea);
                        devuelve+=linea;
                        linea=leer.readLine();
                    }
                    resultado=new JSONObject(res1.toString());

                }
            }catch(MalformedURLException e){
                e.printStackTrace();
                devuelve="oo";
            }catch(JSONException e){
                e.printStackTrace();
                //devuelve="ool";
            }catch(IOException e){
                e.printStackTrace();
                devuelve="ooll";
            }
            if(resultado!=null) {
                Log.d("antes", resultado.toString());
            }
            return resultado;

        }
        @Override
        protected void onCancelled(JSONObject aVoid){
            super.onCancelled();
        }
        @Override
        protected  void onPostExecute(JSONObject aVoid){
            informacion=aVoid;
            renderizaInformacion();
        }
        @Override
        protected  void onPreExecute(){
            super.onCancelled();
            //tv1.setText("pidiendo");
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            Log.d("lll",String.valueOf(values));
        }


    }
    public void renderizaInformacion(){
        if(informacion!= null){
            List items = new ArrayList();
            try{
                JSONArray ansJason = informacion.getJSONArray("nuevos");
                if (ansJason.length() > 0) {
                    for(int i=0;i<ansJason.length();i++) {
                        items.add(new Libro(ansJason.getJSONObject(i).getInt("isbn"), ansJason.getJSONObject(i).getString("titulo"), ansJason.getJSONObject(i).getString("autor"), "LOL", "lorem0", 50000, ansJason.getJSONObject(i).getString("imgSrc")));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            //adapter = new LibroAdapter(items);
            recyclerView.setAdapter(new LibroAdapter(items));

        }
    };
}
