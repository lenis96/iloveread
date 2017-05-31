package com.example.andres.libreria;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CarritoCompras extends AppCompatActivity implements ListadoLibros {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private JSONObject informacion;
    Button comprarCarro;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito_compras);
        setTitle("Carro compras");

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        List items = new ArrayList();


        adapter=new ListaAdapter(items,this);
        recyclerView.setAdapter(adapter);
        ObtenerLibros hconexion=new ObtenerLibros();
        hconexion.execute("");
        comprarCarro=(Button)findViewById(R.id.comprarBtn);
        comprarCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComprarCarrito jconexion=new ComprarCarrito();
                jconexion.execute("");

            }
        });

    }
    public void actualizarInformacion(){
        ObtenerLibros hconexion=new ObtenerLibros();
        hconexion.execute("");
    }



    public class ObtenerLibros extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){
            SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
            String cadena=getString(R.string.host)+"/carritoCompras?usuario="+prefe.getString("usuario","");
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
            renderizarInformacion();
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

    public  void  renderizarInformacion(){
        if(informacion!= null){
            List items = new ArrayList();
            Log.d("l",informacion.toString());
            try{
                JSONArray ansJason = informacion.getJSONArray("carrito");
                if (ansJason.length() > 0) {
                    for(int i=0;i<ansJason.length();i++) {
                        items.add(new Libro(ansJason.getJSONObject(i).getInt("id"), ansJason.getJSONObject(i).getString("titulo"), ansJason.getJSONObject(i).getString("autor"), "", "", ansJason.getJSONObject(i).getInt("precio"), ansJason.getJSONObject(i).getString("imgSrc")));
                    }
                }
                //adapter = new LibroAdapter(items);
                recyclerView.setAdapter(new ListaAdapter(items,this));
                ((TextView)findViewById(R.id.totalTv)).setText("Total de la compra: $"+informacion.getString("total"));

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    };
    public class ComprarCarrito extends AsyncTask<String,Integer,JSONObject> {
        boolean ok=false;
        @Override
        protected JSONObject doInBackground(String... params){
            String cadena=getString(R.string.host)+"/comprarLibros/";
            String devuelve="";
            URL url=null;
            JSONObject resultado=null;
            try{
                url=new URL(cadena);
                HttpURLConnection connection=(HttpsURLConnection) url.openConnection();
                connection.setRequestProperty("user-Agent","Mozilla/5.0.+"+"(Linux: Android 1.5; es-ES) Ejemplo HTTP));");
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");
                JSONObject body=new JSONObject();
                SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
                body.put("usuario",prefe.getString("usuario",""));
                //Log.d("--",informacion.toString());
                //body.put("libroId",informacion.getInt("idLibro"));
                OutputStreamWriter wr= new OutputStreamWriter(connection.getOutputStream());
                wr.write(body.toString());
                wr.flush();
                wr.close();
                Log.d("body",body.toString());
                int respuesta=connection.getResponseCode();
                StringBuilder res1=new StringBuilder();
                if(respuesta== HttpsURLConnection.HTTP_CREATED) {
                    //
                    ok=true;

                }

            }catch(MalformedURLException e){
                e.printStackTrace();
            }catch(JSONException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();

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
            //informacion=aVoid;
            //Log.d("inframacion libro",aVoid.toString());
            if(ok){
                Toast.makeText(getApplicationContext(),"libros comprados",Toast.LENGTH_LONG).show();
                ObtenerLibros hconexio=new ObtenerLibros();
                hconexio.execute();
            }
            Log.d("info",informacion.toString());
            //renderizaInformacion();
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
}
