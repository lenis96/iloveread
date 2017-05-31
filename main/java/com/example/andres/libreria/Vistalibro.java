package com.example.andres.libreria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class Vistalibro extends AppCompatActivity {
    int idLibro;
    JSONObject informacion;
    TextView tituloTv;
    TextView precioTv;
    TextView descripcionTv;
    Button comprar;
    Button carroBtn;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistalibro);
        Bundle bundle=getIntent().getExtras();
        tituloTv=(TextView)findViewById(R.id.tituloTv);
        tituloTv.setText("cargando");
        precioTv=(TextView)findViewById(R.id.precioTv);
        precioTv.setText("");
        descripcionTv=(TextView)findViewById(R.id.descripcionTv);
        descripcionTv.setText("");
        comprar=(Button)findViewById(R.id.comprarBtn);
        img=(ImageView)findViewById(R.id.imagen);
        carroBtn=(Button)findViewById(R.id.carroBtn);
        idLibro=Integer.parseInt(bundle.getString("id"));
        comprar.setEnabled(false);
        //((TextView)findViewById(R.id.tituloTv)).setText(dato);
        ObtenerInformacion hConexion=new ObtenerInformacion();
        hConexion.execute("","");
        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarCarroCompras jConexion=new AgregarCarroCompras();
                jConexion.execute("");
                comprar.setEnabled(false);
            }
        });
        carroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),CarritoCompras.class);
                startActivity(i);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();


    }
    public class ObtenerInformacion extends AsyncTask<String,Integer,JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params){
            SharedPreferences prefe=getSharedPreferences("datos", Context.MODE_PRIVATE);
            String cadena=getString(R.string.host)+"/Libro/"+String.valueOf(idLibro)+"?user="+prefe.getString("usuario","");
            Log.d("->",String.valueOf(idLibro));
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
            //Log.d("inframacion libro",aVoid.toString());
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
                Log.d("carrito",informacion.getString("carrito")+"9");
                if(informacion.getString("carrito")!="null"){
                    comprar.setEnabled(false);
                    comprar.setText("El libro se encuentra en el carrito de compras");
                }else{
                    comprar.setEnabled(true);
                }
                tituloTv.setText(informacion.getString("titulo")+" de "+informacion.getString("autor"));
                descripcionTv.setText(informacion.getString("descripcion"));
                precioTv.setText("$"+informacion.getString("precio"));
                new ImageLoadTask("https://iloveread-lenis96.c9users.io/"+informacion.getString("imgSrc"),img).execute();

            }catch (Exception e){
                e.printStackTrace();
            }
            //adapter = new LibroAdapter(items);
            //recyclerView.setAdapter(new LibroAdapter(items));

        }
    };
    public class AgregarCarroCompras extends AsyncTask<String,Integer,JSONObject> {
        boolean ok=false;
        @Override
        protected JSONObject doInBackground(String... params){
            String cadena=getString(R.string.host)+"/carritoCompras/";
            Log.d("->",String.valueOf(idLibro));
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
                body.put("libroId",informacion.getInt("idLibro"));
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
                Toast.makeText(getApplicationContext(),"libro agragado al carrito de compras",Toast.LENGTH_LONG).show();
                comprar.setText("El libro se encuentra en el carrito de compras");
            }
            else{
                comprar.setEnabled(true);
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
